package ua.squirrel.user.service.store.invoice;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.invoice.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long>{
		
	Optional<Invoice> findOneByStoreAndDate(Store store, LocalDate date);
	
	Optional<Invoice> findOneByIdAndStore(Long id ,Store store);
	
	List<Invoice> findAllByStoreAndDateBetween(Store store, LocalDate start, LocalDate finish);
}
