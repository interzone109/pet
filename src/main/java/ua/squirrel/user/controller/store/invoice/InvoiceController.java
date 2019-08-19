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
import ua.squirrel.user.utils.InvoiceUtil;
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
	@Autowired
	private InvoiceUtil invoiceUtil;

	/**
	 * Метод принимает модель InvoiceModel и возращает один или список инвойсов
	 * 
	 */
	@PostMapping("/find")
	public List<InvoiceModel> createСonsignment(Authentication authentication, @RequestBody InvoiceModel invoiceModel)
			throws NotFoundException {
		log.info("LOGGER: find invoice by date and store");
		User user = userServiceImpl.findOneByLogin("test1").get();
		Store store = getCurrentStore(user, invoiceModel.getStoreId());

		List<Invoice> invoices = new ArrayList<>();
		if (invoiceModel.isBetween()) {
			invoices = invoiceServiceImpl.findAllByStoreAndDateBetween(store,
					invoiceUtil.convertDate(invoiceModel.getDateStart()),
					invoiceUtil.convertDate(invoiceModel.getDateEnd()));
		} else {
			Optional<Invoice> invoice = invoiceServiceImpl.findOneByStoreAndDate(store,
					invoiceUtil.convertDate(invoiceModel.getDateStart()));
			if (invoice.isPresent()) {
				invoices.add(invoice.get());
			}
		}

		return invoiceUtil.createInvoiceModel(invoices);
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
	@PutMapping("{storeId}")
	public InvoiceModel saleProduct(Authentication authentication, @RequestBody InvoiceModel invoiceModel,
			@PathVariable("storeId") Long storeId) throws NotFoundException {
		log.info("LOGGER: add sale to current date and store invoice");
		User user = userServiceImpl.findOneByLogin("test1").get();
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
			compositeProductList.forEach(compositeProduct -> {// создаем запись в инвойсе с продуктом, количеством его
																// продажей и временем
				InvoiceNode invoiceNode = new InvoiceNode();
				invoiceNode.setCompositeProduct(compositeProduct);
				invoiceNode.setInvoice(invoice);
				invoiceNode.setSaleQuantity(invoiceData.get(compositeProduct.getId()));
				invoiceNode.setTime(LocalDateTime.now());
				invoiceNodeList.add(invoiceNode);
			});// сохраняем новые продажи для инвойса
			invoiceNodeServiceImpl.saveAll(invoiceNodeList);

		} else {// если инвойс не найден значит что он небыл заранее создан в контроллере
				// createOrFindInvoice
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
		Map<Product, Integer> productPriceMap = consignmentUtil.formFIFOIngridientPrice(consignmentFIFOList , productRateMap);
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
			consignment.getConsignmentNode().forEach(node->{
				if(productRateMapCopy.containsKey(node.getProduct())) {
					int quantity = node.getQuantity();
					node.setQuantity(quantity + productRateMapCopy.get(node.getProduct()));
					int price = node.getUnitPrice();
					node.setUnitPrice((price+productPriceMap.get(node.getProduct()))/2);
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
				consignmentNode.setUnitPrice(productPriceMap.get(product));
				consignmentsNode.add(consignmentNode);
			}
			consignment.setConsignmentNode(consignmentsNode);
		}
		consignmentFIFOList.add(consignment);
		consignmentServiceImpl.saveAll(consignmentFIFOList);
		return null;
	}

	/**
	 * метод возращает инвойс на текущю дату либо создает новый и заполняет данные
	 */
	@PostMapping("cashBox/{storeId}")
	public ResponseEntity<InvoiceModel> createOrFindInvoice(Authentication authentication,
			@RequestBody InvoiceModel invoiceModel, @PathVariable("storeId") Long storeId) throws NotFoundException {
		log.info("LOGGER: get current invoice or create new");
		User user = userServiceImpl.findOneByLogin("test1").get();
		Store store = getCurrentStore(user, storeId);
		LocalDate date = LocalDate.now();
		// находим инвойс за сегодня
		Optional<Invoice> invoiceOption = invoiceServiceImpl.findOneByStoreAndDate(store, date);
		Invoice invoice = null;
		if (invoiceOption.isPresent()) {// если инвойс представлен то возращаем его модель
			return new ResponseEntity<InvoiceModel>(invoiceUtil.createInvoiceMetaModel(invoiceOption.get()),
					HttpStatus.OK);
		}
		invoice = invoiceUtil.createNewInvoice(invoiceModel, store);
		invoiceServiceImpl.save(invoice);

		return new ResponseEntity<InvoiceModel>(invoiceUtil.createInvoiceMetaModel(invoice), HttpStatus.OK);
	}

	private Store getCurrentStore(User user, Long id) throws NotFoundException {
		return storeServiceImpl.findOneByIdAndUser(id, user)
				.orElseThrow(() -> new NotFoundException("Store not found"));
	}
}
