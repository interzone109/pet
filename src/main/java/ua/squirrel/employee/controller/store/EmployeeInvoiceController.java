package ua.squirrel.employee.controller.store;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import ua.squirrel.user.entity.employee.Employee;
import ua.squirrel.user.entity.product.composite.CompositeProduct;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.compositeproduct.node.StoreCompositeProductNode;
import ua.squirrel.user.entity.store.invoice.Invoice;
import ua.squirrel.user.entity.store.invoice.InvoiceModel;
import ua.squirrel.user.entity.store.invoice.node.InvoiceNode;
import ua.squirrel.user.service.employee.EmployeeServiceImpl;
import ua.squirrel.user.service.product.CompositeProductServiceImpl;
import ua.squirrel.user.service.store.invoice.InvoiceServiceImpl;
import ua.squirrel.user.utils.StoreUtil;

@RestController
@RequestMapping("/employee/stores/invoice")
@Slf4j
public class EmployeeInvoiceController {
	@Autowired
	private EmployeeServiceImpl employeeServiceImpl;
	@Autowired
	private CompositeProductServiceImpl compositeProductServiceImpl;
	@Autowired
	private InvoiceServiceImpl invoiceServiceImpl;
	@Autowired
	private StoreUtil storeUtil;


	/**
	 * Метод принимает модель InvoiceModel и возращает один или список инвойсов
	 * 
	 */
	@PostMapping("/find")
	public List<InvoiceModel> createСonsignment(Authentication authentication, @RequestBody InvoiceModel invoiceModel)
			throws NotFoundException {
		log.info("LOGGER: get cuurent day sell");
		Employee employee = employeeServiceImpl.findOneById(1l).get();
		Store store = employee.getStore();

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
	@PostMapping("/cashBox")
	public ResponseEntity<InvoiceModel> createOrFindInvoice(Authentication authentication,
			@RequestBody InvoiceModel invoiceModel, @PathVariable("storeId") Long storeId) throws NotFoundException {
		log.info("LOGGER: get current invoice or create new");
		Employee employee = employeeServiceImpl.findOneById(1l).get();
		Store store = employee.getStore();
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
	 * */
	@PutMapping()
	public InvoiceModel saleCompositeProduct(Authentication authentication, @RequestBody InvoiceModel invoiceModel,
			@PathVariable("storeId") Long storeId) throws NotFoundException {
		log.info("LOGGER: add sale to current date and store invoice");
		Employee employee = employeeServiceImpl.findOneById(1l).get();
		/**
		 *		1 пункт
		 */
		Store store = employee.getStore();
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
			
			LocalDateTime time = LocalDateTime.now();
			int currentHour = time.getHour();
			// получаем все проданыe продукты
			compositeProductList = compositeProductServiceImpl.findAllByUserAndIdIn(employee.getUser(), invoiceData.keySet());
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
			return storeUtil.createInvoiceModel(invoiceOption.get());
		} 
		// если инвойс не найден значит что он небыл заранее создан в контроллере
		// createOrFindInvoice и работать с ним невыйдет
		return InvoiceModel.builder().build();

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
