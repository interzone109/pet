package ua.squirrel.user.controller.store.invoice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.invoice.Invoice;
import ua.squirrel.user.entity.store.invoice.InvoiceModel;
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.user.service.store.invoice.InvoiceServiceImpl;
import ua.squirrel.user.utils.InvoiceUtil;
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
	private InvoiceUtil invoiceUtil;

	
	
	@PostMapping("/find") 
	public List<InvoiceModel> createСonsignment( Authentication authentication,
			@RequestBody InvoiceModel invoiceModel) throws NotFoundException {
		log.info("LOGGER: find invoice be date and store");
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
	
	
	@PutMapping("{storeId}")
	public ResponseEntity<String> saleProduct( Authentication authentication,
			@RequestBody Map<Long, Integer> productQuantitySales , @PathVariable("storeId")Long storeId) throws NotFoundException {
		log.info("LOGGER: add sale to current date and store invoice");
		User user = userServiceImpl.findOneByLogin("test1").get();
		Store store = getCurrentStore(user ,storeId) ;
		LocalDate date = LocalDate.now();
		
		Optional<Invoice> invoiceOption = invoiceServiceImpl.findOneByStoreAndDate(store, date);
		Invoice invoice = null ;
		if(invoiceOption.isPresent()) {
			invoice = invoiceOption.get();
			Map<Long, Integer> invoiceData = invoiceUtil.spliteIdsValue(invoice.getInvoiceData(), "sale");
			productQuantitySales.keySet().forEach(id->{
				if(invoiceData.containsKey(id)) {
					int currentSale = invoiceData.get(id);
					currentSale += productQuantitySales.get(id);
					invoiceData.put(id, currentSale);
				}else {
					invoiceData.put(id,productQuantitySales.get(id));
				}
			});
		}else {
			invoice = new Invoice();
			invoice.setDate(date);
			invoice.setStore(store);
			invoice.setInvoiceData(invoiceUtil.concatIdsValueToString(productQuantitySales, "sale"));
		}
		invoiceServiceImpl.save(invoice);
		
		return new ResponseEntity<String>("Sale done", HttpStatus.OK) ;
	}
	
	
	
	
	
	
	
	
	private Store getCurrentStore(User user ,Long id) throws NotFoundException {
		return storeServiceImpl.findOneByIdAndUser(id, user)
				.orElseThrow(() -> new NotFoundException("Store not found"));
	}
}
