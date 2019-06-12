package ua.squirrel.user.service.store.invoice;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.invoice.Invoice;

@Service
public class InvoiceServiceImpl implements InvoiceService {
	
	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Override
	public Optional<Invoice> findOneByStoreAndDate(Store store, LocalDate date) {
		return invoiceRepository.findOneByStoreAndDate(store, date);
	}

	@Override
	public Optional<Invoice> findOneByIdAndStore(Long id, Store store) {
		return invoiceRepository.findOneByIdAndStore(id, store);
	}

	@Override
	public List<Invoice> findAllByStoreAndDateBetween(Store store, LocalDate start, LocalDate finish) {
		return invoiceRepository.findAllByStoreAndDateBetween(store, start, finish);
	}
	
	public Invoice save(Invoice invoice) {
		return invoiceRepository.save(invoice);
	}
	public List<Invoice> saveAll(Iterable<Invoice> invoices) {
		return invoiceRepository.saveAll(invoices);
	}
}
