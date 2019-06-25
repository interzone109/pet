package ua.squirrel.user.controller.store.invoice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.user.entity.store.consignment.ConsignmentStatus;
import ua.squirrel.user.entity.store.invoice.Invoice;
import ua.squirrel.user.entity.store.invoice.InvoiceModel;
import ua.squirrel.user.service.product.CompositeProductServiceImpl;
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.user.service.store.consignment.ConsignmentServiceImpl;
import ua.squirrel.user.service.store.consignment.status.ConsignmentStatusServiceImpl;
import ua.squirrel.user.service.store.invoice.InvoiceServiceImpl;
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
	private ConsignmentUtil consignmentUtil;
	@Autowired 
	private StoreUtil storeUtil;
	@Autowired
	private InvoiceUtil invoiceUtil;

	
	/**
	 * Метод принимает модель InvoiceModel
	 * и возращает одинт или список инвойсов
	 * 
	 * */
	@PostMapping("/find") 
	public List<InvoiceModel> createСonsignment( Authentication authentication,
			@RequestBody InvoiceModel invoiceModel) throws NotFoundException {
		log.info("LOGGER: find invoice by date and store");
		User user = userServiceImpl.findOneByLogin("test1").get();
		Store store = getCurrentStore(user ,invoiceModel.getStoreId()) ;
		
		List<Invoice> invoices = new ArrayList<>();
		if(invoiceModel.isBetween()) {
			invoices = invoiceServiceImpl.findAllByStoreAndDateBetween(store, 
					invoiceUtil.convertDate(invoiceModel.getDateStart()), 
					invoiceUtil.convertDate(invoiceModel.getDateEnd()));
		}else {
			 Optional<Invoice> invoice = invoiceServiceImpl.findOneByStoreAndDate(store,
					 invoiceUtil.convertDate(invoiceModel.getDateStart()));
			 if(invoice.isPresent()) {
				 invoices.add(invoice.get());
			 }
		}		
		
		return invoiceUtil.createInvoiceModel(invoices);
		}
	
	/**
	 * метод добавляет данные в ивойс
	 * */
	@PutMapping("{storeId}")
	public InvoiceModel saleProduct( Authentication authentication,
			@RequestBody InvoiceModel invoiceModel , @PathVariable("storeId")Long storeId) throws NotFoundException {
		log.info("LOGGER: add sale to current date and store invoice");
		User user = userServiceImpl.findOneByLogin("test1").get();
		Store store = getCurrentStore(user ,storeId) ;
		
		Map<Long, Integer> productQuantitySales = invoiceModel.getInvoiceData();
		// ннаходим инвойс за текущую дату
		LocalDate date = LocalDate.now();
		Optional<Invoice> invoiceOption = invoiceServiceImpl.findOneByStoreAndDate(store, date);
		Invoice invoice = null ;
		if(invoiceOption.isPresent()) {
			invoice = invoiceOption.get();
			Map<Long, Integer> invoiceData = invoiceUtil.spliteIdsValue(invoice.getInvoiceData(), "sale");
			productQuantitySales.keySet().forEach(id->{
				if(invoiceData.containsKey(id)) {// если  в инвойсе уже есть продукт с таким айди то суммируем его количество
					int currentSale = invoiceData.get(id);
					currentSale += productQuantitySales.get(id);
					invoiceData.put(id, currentSale);
				}else {
					invoiceData.put(id,productQuantitySales.get(id));
				}
			});
			invoice.setInvoiceData(invoiceUtil.concatIdsValueToString(invoiceData, "sale"));
		}else {
			return  InvoiceModel.builder().build() ;
		}
		
		//обновляем мета данные 
		String [] invoiceMeta = invoice.getMeta().split(":%:");
		StringBuilder meta = new StringBuilder();
		meta.append(invoiceMeta[0]
				+":%:"+(Integer.parseInt(invoiceMeta[1])+invoiceModel.getCurrentSell())
				+":%:"+(Integer.parseInt(invoiceMeta[2])+invoiceModel.getSellQuantity()));
		invoice.setMeta(meta.toString());
		
		invoiceServiceImpl.save(invoice);
		
		ConsignmentStatus consStatus = consignmentStatusServiceImpl.findOneByName("CONSAMPTION").get();
		// находим расходную накладную за сегодняшний день
		// эта накладная создаеться автоматически и отвечаетза расход ингридиентов
		// относительно количества продаж
		 Optional<Consignment> consOptional = consignmentServiceImpl.findOneByDateAndStoreAndConsignmentStatusAndIsApprovedAndMetaIgnoreCaseContaining
				 (date, store, consStatus, true, "auto:%:");
		 
		 Consignment consignment = null ;
		 if(consOptional.isPresent()) {
			 consignment = consOptional.get();
		 }else {
			consignment = new Consignment();
			consignment.setDate(date);
			consignment.setConsignmentData("");
			consignment.setApproved(true);
			consignment.setMeta("auto:%:");
			consignment.setStore(store);
			consignment.setConsignmentStatus(consStatus);
		 }
		 //мапа содержит ид ингридиента и его расход
		 Map<Long, Integer> ingridientQuantity = new HashMap<>(); 
		 // достаем список продуктов из входящего инвойска
		 compositeProductServiceImpl.findAllByUserAndIdIn(user, productQuantitySales.keySet()).forEach(product->{
			 // получаем количество проданых продуктов
			 int multiply = productQuantitySales.get(product.getId());
			 //получаем мапу с Ид ингридиента и его расходом на 1 единицу продукта
			Map<Long, Integer> ingridientRate = consignmentUtil.spliteIdsValue(product.getProductExpend(), "rate");
			//проходимся посписку ингридиентов
			ingridientRate.keySet().forEach(ingridietnId->{
				//если в  ingridientQuantity содержиться Ид ингридиента то увеличиваем его раход
				if(ingridientQuantity.containsKey(ingridietnId)) {
					//  расход ингридиента для текущего продукта
					int rate = ingridientRate.get(ingridietnId);
					//текущее расход ингридиента
					int currentRate = ingridientQuantity.get(ingridietnId);
					// сограняем Ид ингридиента и его количество ( количество расхода * на кол продукта и добавляем старое количество)
					ingridientQuantity.put(ingridietnId, (rate*multiply)+currentRate);
				}else {
				//  расход ингридиента для текущего продукта
					int rate = ingridientRate.get(ingridietnId);
					ingridientQuantity.put(ingridietnId, (rate*multiply));
				}
			});
		 });
		 
		 
		 consignmentUtil.addData(consignment, ingridientQuantity);
		 consignmentServiceImpl.save(consignment);
		 
		 storeUtil.removeStoreLeftovers(store, ingridientQuantity);
		 storeServiceImpl.save(store);
		 
		return invoiceUtil.createInvoiceMetaModel(invoice) ;
	}
	
	@GetMapping()
	public InvoiceModel get() {
		HashMap<Long, Integer> t = new HashMap<>();
		t.put(1l, 2);
		t.put(2l, 4);
		t.put(3l, 5);
		return InvoiceModel.builder()
				.id(22)
				.invoiceData(t)
				.storeId(22l)
				.cashBoxStartDay(22)
				.currentSell(22)
				.sellQuantity(22)
				.build();
	}
	
	/**
	 * метод возращает инвойс на текущю дату 
	 * либо создает новый и заполняет данные
	 * */
	@PostMapping("cashBox/{storeId}")
	public ResponseEntity<InvoiceModel> createOrFindInvoice( Authentication authentication,
			@RequestBody InvoiceModel invoiceModel , @PathVariable("storeId")Long storeId) throws NotFoundException {
		log.info("LOGGER: get current invoice or create new");
		User user = userServiceImpl.findOneByLogin("test1").get();
		Store store = getCurrentStore(user ,storeId) ;
		LocalDate date = LocalDate.now();
		//находим инвойс за сегодня
		Optional<Invoice> invoiceOption = invoiceServiceImpl.findOneByStoreAndDate(store, date);
		Invoice invoice = null ;
		if(invoiceOption.isPresent()) {// если инвойс представлен то возращаем его модель
			return new ResponseEntity<InvoiceModel> (invoiceUtil.createInvoiceMetaModel(invoiceOption.get()), HttpStatus.OK);
		}
		invoice = invoiceUtil.createNewInvoice(invoiceModel, store);
		invoiceServiceImpl.save(invoice);
		
		return new ResponseEntity<InvoiceModel> (invoiceUtil.createInvoiceMetaModel(invoice), HttpStatus.OK);
		}
	
	
	
	
	
	private Store getCurrentStore(User user ,Long id) throws NotFoundException {
		return storeServiceImpl.findOneByIdAndUser(id, user)
				.orElseThrow(() -> new NotFoundException("Store not found"));
	}
}
