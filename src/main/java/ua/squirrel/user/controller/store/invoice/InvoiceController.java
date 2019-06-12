package ua.squirrel.user.controller.store.invoice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	
	
	private Store getCurrentStore(User user ,Long id) throws NotFoundException {
		return storeServiceImpl.findOneByIdAndUser(id, user)
				.orElseThrow(() -> new NotFoundException("Store not found"));
	}
}
