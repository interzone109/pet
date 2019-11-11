package ua.squirrel.user.controller.store.invoice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.product.Product;
import ua.squirrel.user.entity.product.composite.CompositeProduct;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.compositeproduct.node.StoreCompositeProductNode;
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
import ua.squirrel.user.utils.ConsignmentUtil;
import ua.squirrel.user.utils.StoreUtil;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@RestController
@RequestMapping("/user/stores/invoice")
@Slf4j
public class InvoiceController {
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private StoreServiceImpl storeServiceImpl;
	@Autowired
	private InvoiceServiceImpl invoiceServiceImpl;
	@Autowired
	private CompositeProductServiceImpl compositeProductServiceImpl;
	@Autowired
	private StoreUtil storeUtil;
	@Autowired
	private ConsignmentServiceImpl consignmentServiceImpl;
	@Autowired
	private ConsignmentStatusServiceImpl consignmentStatusServiceImpl;
	@Autowired
	private ConsignmentUtil consignmentUtil;

	/**
	 * Метод принимает модель InvoiceModel и возращает один или список инвойсов
	 * 
	 */
	@PostMapping("/find")
	public List<InvoiceModel> createСonsignment(Authentication authentication, @RequestBody InvoiceModel invoiceModel)
			throws NotFoundException {
		log.info("LOGGER: find invoice by date and store");
		User user = userServiceImpl.findOneByLogin(authentication.getName()).get();
		Store store = getCurrentStore(user, invoiceModel.getStoreId());

		List<Invoice> invoices = new ArrayList<>();
		if (invoiceModel.isBetween()) {
			invoices = invoiceServiceImpl.findAllByStoreAndDateBetween(store,
					storeUtil.convertDate(invoiceModel.getDateStart()),
					storeUtil.convertDate(invoiceModel.getDateEnd()));
		} else {
			Optional<Invoice> invoice = invoiceServiceImpl.findOneByStoreAndDate(store,
					storeUtil.convertDate(invoiceModel.getDateStart()));
			if (invoice.isPresent()) {
				invoices.add(invoice.get());
			}
		}

		return  storeUtil.createSaleProductViev(invoices);
	}



	/**
	 * метод возращает инвойс на текущю дату либо создает новый и заполняет данные
	 */
	@PostMapping("cashBox/{storeId}")
	public ResponseEntity<InvoiceModel> createOrFindInvoice(Authentication authentication,
			@RequestBody InvoiceModel invoiceModel, @PathVariable("storeId") Long storeId) throws NotFoundException {
		log.info("LOGGER: get current invoice or create new");
		User user = userServiceImpl.findOneByLogin(authentication.getName()).get();
		Store store = getCurrentStore(user, storeId);
		LocalDate date = LocalDate.now();
		// находим инвойс за сегодня
		Optional<Invoice> invoiceOption = invoiceServiceImpl.findOneByStoreAndDate(store, date);
		Invoice invoice = null;
		if (invoiceOption.isPresent()) {// если инвойс представлен то возращаем его модель
			return new ResponseEntity<InvoiceModel>(storeUtil.createInvoiceModel(invoiceOption.get()),
					HttpStatus.OK);
		}
		invoice = createNewInvoice(invoiceModel, store);
		invoiceServiceImpl.save(invoice);

		return new ResponseEntity<InvoiceModel>(storeUtil.createInvoiceModel(invoice), HttpStatus.OK);
	}

	/**
	 * контроллер добавляет данные о продаже в инвойс обновляет остатки на магазине
	 * и формирует расходную накладную по ингридиентам по FIFO 
	 * 1. Получение данных и базы (магазин, инавойс, список композитных продуктов) и обновляем продажи в рамках одного часа
	 * 2. Создаем узел с  продуктом который еще не был продан в рамках часа, количеством продаж и временем покупки 
	 * 3. Получаем список ингридиентов из композитных продуктов (нужно для поиска их входной цены и
	 * обновления остатков на магазине), так же умножаем расход ингридиентов н аколичество продаж копмозитного продукта
	 * 4. Обновляем остатки на магазине
	 * 5. Делаем выборку по накладным, для нахождение входной цены продукта
	 * 6. Обновляем данные в накладной
	 * */
	@PutMapping("{storeId}")
	public ResponseEntity<InvoiceModel> saleCompositeProduct(Authentication authentication, @RequestBody InvoiceModel invoiceModel,
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
			//обновляем данные в инвойсе
			int cash = invoice.getCashBox();
			int order = invoice.getOrderQuantity();
			int sell =  invoice.getSellQuantity();
			invoice.setCashBox(invoiceModel.getCashBox()+ cash);
			invoice.setOrderQuantity(invoiceModel.getOrderQuantity() + order);
			invoice.setSellQuantity(invoiceModel.getSellQuantity() + sell);
			//получаем текущий час
			LocalDateTime time = LocalDateTime.now();
			int currentHour = time.getHour();
			// получаем все проданыe продукты
			compositeProductList = compositeProductServiceImpl.findAllByUserAndIdIn(user, invoiceData.keySet());
			List<Long> dublicateProductId  = new ArrayList<>();
			List<InvoiceNode> invoiceNodeUpdate = invoice.getInvoiceNode().stream().filter
			(node->node.getTime().getHour()>=currentHour && node.getTime().getHour()<currentHour+1 ).collect(Collectors.toList());
			invoiceNodeUpdate.forEach(node->{//обновляем количество продаж за текущий час
				if(invoiceData.containsKey(node.getCompositeProduct().getId())) {
				int saleQuantity = node.getSaleQuantity() + invoiceData.get(node.getCompositeProduct().getId());
				node.setSaleQuantity(saleQuantity);
				dublicateProductId.add(node.getCompositeProduct().getId());//сохраняем обновленные айди
				}
			});
			
			/**
			 *		2 пункт
			 */
			Map<Long, Integer> prodPriceMap = new HashMap<>();
			for(StoreCompositeProductNode node : store.getStoreCompositeProductNode()){
				if(compositeProductList.contains(node.getCompositeProduct())) {
					prodPriceMap.put(node.getCompositeProduct().getId(), node.getPrice());
					}
				}
			
			compositeProductList.forEach(compositeProduct -> {// создаем запись в инвойсе с продуктом, количеством его продажей и временем
				if(!dublicateProductId.contains(compositeProduct.getId())) {//если позиция не обновлялась то делаем новую запись
				InvoiceNode invoiceNode = new InvoiceNode();
				invoiceNode.setCompositeProduct(compositeProduct);
				invoiceNode.setInvoice(invoice);
				invoiceNode.setSaleQuantity(invoiceData.get(compositeProduct.getId()));
				invoiceNode.setTime(time);
				invoiceNode.setPrice(prodPriceMap.get(compositeProduct.getId()));
				invoice.getInvoiceNode().add(invoiceNode);
				}
			});// сохраняем новые продажи для инвойса
			invoiceServiceImpl.save(invoice);
			//обновить остатки на магазине
			// списать остатки с накладной приходной и создать накладную расходную
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
			storeServiceImpl.save(store);
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
			
			return new ResponseEntity<InvoiceModel>(storeUtil.createInvoiceModel(invoiceOption.get()), HttpStatus.OK);
		} 
		// если инвойс не найден значит что он небыл заранее создан в контроллере
		// createOrFindInvoice и работать с ним невыйдет
		return  new ResponseEntity<InvoiceModel>(InvoiceModel.builder().dateStart("create invoice errore").build(), HttpStatus.MULTI_STATUS);

	}
	


	private Store getCurrentStore(User user, Long id) throws NotFoundException {
		return storeServiceImpl.findOneByIdAndUser(id, user)
				.orElseThrow(() -> new NotFoundException("Store not found"));
	}
	
	
	private Invoice createNewInvoice(InvoiceModel invoiceModel, Store store) {
		Invoice invoice = new Invoice ();
		invoice.setCashBox(0);
		invoice.setCashBoxStartDay(invoiceModel.getCashBoxStartDay());
		invoice.setDate(LocalDate.now());
		invoice.setOrderQuantity(0);
		invoice.setSellQuantity(0);
		invoice.setStore(store);
		return invoice;
	}
}
