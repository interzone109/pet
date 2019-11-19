package ua.squirrel.user.service.store.invoice;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.invoice.Invoice;

public interface InvoiceService {
	Optional<Invoice> findOneByStoreAndDate(Store store, LocalDate date);
	
	Optional<Invoice> findOneByIdAndStore(Long id ,Store store);
	
	List<Invoice> findAllByStoreAndDateBetween(Store store, LocalDate start, LocalDate finish);
	
	List<Invoice> findAllByStoreInAndDateBetween(Collection<Store> store, LocalDate start, LocalDate finish);
}
