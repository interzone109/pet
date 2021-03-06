package ua.squirrel.user.controller.updates;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList; 
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j; 
import ua.squirrel.user.entity.product.Product;
import ua.squirrel.user.entity.product.composite.CompositeProduct;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.user.entity.store.consignment.ConsignmentStatus;
import ua.squirrel.user.entity.store.consignment.node.ConsignmentNode;
import ua.squirrel.user.entity.store.invoice.Invoice;
import ua.squirrel.user.entity.store.invoice.InvoiceModel;
import ua.squirrel.user.entity.store.invoice.node.InvoiceNode;
import ua.squirrel.user.service.product.CompositeProductServiceImpl;
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.user.service.store.consignment.ConsignmentServiceImpl;
import ua.squirrel.user.service.store.consignment.status.ConsignmentStatusServiceImpl;
import ua.squirrel.user.service.store.invoice.InvoiceServiceImpl;
import ua.squirrel.user.service.store.invoice.node.InvoiceNodeServiceImpl;
import ua.squirrel.user.utils.ConsignmentUtil;
import ua.squirrel.user.utils.StoreUtil;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@Controller
@Slf4j
public class AutoUpdateController {
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private StoreServiceImpl storeServiceImpl;
	@Autowired
	private InvoiceServiceImpl invoiceServiceImpl;
	@Autowired
	private ConsignmentServiceImpl consignmentServiceImpl;
	@Autowired
	private ConsignmentStatusServiceImpl consignmentStatusServiceImpl;
	@Autowired
	private CompositeProductServiceImpl compositeProductServiceImpl;
	@Autowired
	private InvoiceNodeServiceImpl invoiceNodeServiceImpl;
	@Autowired
	private ConsignmentUtil consignmentUtil;
	@Autowired
	private StoreUtil storeUtil;

	
	
	@Scheduled(fixedRate = 500000)
    public void reportCurrentTime() {
        log.info("System controller");
    }
	
	@Scheduled(fixedRate = 500000)
    public void checkPayment() {
        log.info("check payment");
    }
	
	
	
	
	/**
	 * контроллер добавляет данные о продаже в инвойс обновляет остатки на магазине
	 * и формирует расходную накладную по ингридиентам по FIFO 
	 * 1. Получение данных и базы (магазин, инавойс, список композитных продуктов) 
	 * 2. Создаем узел с  продуктом, количеством продаж и временем покупки 
	 * 3. Получаем список ингридиентов из композитных продуктов (нужно для поиска их входной цены и
	 * обновления остатков на магазине), так же умножаем расход ингридиентов н аколичество продаж копмозитного продукта
	 * 4. Обновляем остатки на магазине
	 * 5. Делаем выборку по накладным, для нахождение входной цены продукта
	 * 6. Обновляем данные в накладной
	 */
	@PutMapping("{storeId}/end")
	public InvoiceModel saleProduct(Authentication authentication, @RequestBody InvoiceModel invoiceModel,
			@PathVariable("storeId") Long storeId) throws NotFoundException {
		log.info("LOGGER: add sale to current date and store invoice");
		User user = userServiceImpl.findOneByLogin(authentication.getName()).get();
		/**
		 *		1 пункт
		 */
		Store store = getCurrentStore(user, storeId);
		LocalDate date = LocalDate.now();
		Optional<Invoice> invoiceOption = invoiceServiceImpl.findOneByStoreAndDate(store, date);// ннаходим инвойс за текущую дату
		Map<Long, Integer> invoiceData = invoiceModel.getInvoiceData();
		List<CompositeProduct> compositeProductList = new ArrayList<>();
		if (invoiceOption.isPresent()) {
			Invoice invoice = invoiceOption.get();
			Set<CompositeProduct> existProduct = new HashSet<>();
			invoice.getInvoiceNode().forEach(node -> {
				existProduct.add(node.getCompositeProduct());
			});
			List<InvoiceNode> invoiceNodeList = new ArrayList<>();
			// получаем все проданыe продукты
			compositeProductList = compositeProductServiceImpl.findAllByUserAndIdIn(user, invoiceData.keySet());
			/**
			 *		2 пункт
			 */
			compositeProductList.forEach(compositeProduct -> {// создаем запись в инвойсе с продуктом, количеством его продажей и временем
				InvoiceNode invoiceNode = new InvoiceNode();
				invoiceNode.setCompositeProduct(compositeProduct);
				invoiceNode.setInvoice(invoice);
				invoiceNode.setSaleQuantity(invoiceData.get(compositeProduct.getId()));
				invoiceNode.setTime(LocalDateTime.now());
				invoiceNodeList.add(invoiceNode);
			});// сохраняем новые продажи для инвойса
			invoiceNodeServiceImpl.saveAll(invoiceNodeList);

		} else {// если инвойс не найден значит что он небыл заранее создан в контроллере
				// createOrFindInvoice и работать с ним невыйдет
			return InvoiceModel.builder().build();
		}
		/**
		 * 		3 пункт
		 */
		// делаем мапу ингридиента и его общего расхода из инвойса
		Map<Product, Integer> productRateMap = new HashMap<>();
		Map<Product, Integer> productRateMapCopy = new HashMap<>();
		compositeProductList.forEach(compositeProduct -> {
			compositeProduct.getProductMap().forEach(productNode -> {
				//если ингридиент уже есть в мапе то увеличиваем его общий расход
				if(productRateMap.containsKey(productNode.getProduct())) {
					int totalRate = productRateMap.get(productNode.getProduct());//текущий расход ингридиентов
					//прибавляем к текущему расходу, расход ингридиентов от продаж
					totalRate += productNode.getRate()* invoiceData.get(compositeProduct.getId()); 
					//сохраняем в мапу
					productRateMap.put(productNode.getProduct(), totalRate);
					productRateMapCopy.put(productNode.getProduct(), totalRate);
				}else {
					productRateMap.put(productNode.getProduct(), productNode.getRate()* invoiceData.get(compositeProduct.getId()));
					productRateMapCopy.put(productNode.getProduct(), productNode.getRate()* invoiceData.get(compositeProduct.getId()));
				}
			});
		});
		/**
		 * 		4 пункт
		 */
		storeUtil.updateStoreLeftoversForSale(store, productRateMap, "-");
		/**
		 * 		5 пункт
		 */
		// получаем список накладных для реализации расчетов отпускаемых ингридиентов с
		// по методу ФИФО
		List<Consignment> consignmentFIFOList = consignmentServiceImpl.getConsigmentFIFO(store,
				productRateMap.keySet());
		Map<Product, Integer> productPriceMap = consignmentUtil.formFIFOIngridientPrices(consignmentFIFOList , productRateMap);
		/**
		 * 		6 пункт
		 */
		ConsignmentStatus consStatus = consignmentStatusServiceImpl.findOneByName("CONSAMPTION").get();
		// находим расходную накладную за сегодняшний день
		// эта накладная создаеться автоматически и отвечаетза расход ингридиентов
		// относительно количества продаж
		Optional<Consignment> consOptional = consignmentServiceImpl
				.findOneByDateAndStoreAndConsignmentStatusAndIsApprovedAndMetaIgnoreCaseContaining(date, store,
						consStatus, true, "auto:%:");
		Consignment consignment  = null;
		if (consOptional.isPresent()) {
			consignment = consOptional.get();
			Set <Product> existProduct = new HashSet<>();
			consignment.getConsignmentNode().forEach(node->{//обновляем среднуюю цену и количество в расходной накладной
				if(productRateMapCopy.containsKey(node.getProduct())) {
					int totalSumm = node.getQuantity()*node.getUnitPrice() + productPriceMap.get(node.getProduct());//старое кол * на старую цену + новую сумму
					int totalQuantity = node.getQuantity() + productRateMapCopy.get(node.getProduct());//получаем общее количество расхода по ингридиенту
					node.setQuantity(totalQuantity);
					node.setUnitPrice(totalSumm/totalQuantity);//получаем среднуюю цену деля общюю стоимость на количество ингридиента
					existProduct.add(node.getProduct());
				}
			});
			productRateMap.keySet().removeAll(existProduct);
			//добавляем расход новых ингридиентов 
			for(Product product : productRateMap.keySet())  {
				ConsignmentNode consignmentNode = new ConsignmentNode();
				consignmentNode.setConsignment(consignment);
				consignmentNode.setProduct(product);
				consignmentNode.setQuantity(productRateMapCopy.get(product));
				consignmentNode.setUnitPrice(productPriceMap.get(product));
				consignment.getConsignmentNode().add(consignmentNode);
			}
			

		} else {
			consignment  = new Consignment();
			consignment.setDate(date);
			consignment.setApproved(true);
			consignment.setMeta("auto:%:Продажи магазина "+store.getAddress());
			consignment.setStore(store);
			consignment.setConsignmentStatus(consStatus);

			List<ConsignmentNode> consignmentsNode = new ArrayList<>();
			for(Product product : productRateMap.keySet())  {
				ConsignmentNode consignmentNode = new ConsignmentNode();
				consignmentNode.setConsignment(consignment);
				consignmentNode.setProduct(product);
				consignmentNode.setQuantity(productRateMapCopy.get(product));
				consignmentNode.setUnitPrice(productPriceMap.get(product)/productRateMapCopy.get(product));
				consignmentsNode.add(consignmentNode);
			}
			consignment.setConsignmentNode(consignmentsNode);
		}
		consignmentFIFOList.add(consignment);
		consignmentServiceImpl.saveAll(consignmentFIFOList);
		return  null;//invoiceUtil.createInvoiceMetaModel(invoiceOption.get());
	}
	
	
	private Store getCurrentStore(User user, Long id) throws NotFoundException {
		return storeServiceImpl.findOneByIdAndUser(id, user)
				.orElseThrow(() -> new NotFoundException("Store not found"));
	}

}
