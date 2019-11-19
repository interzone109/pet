package ua.squirrel.user.service.store.spending;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.spending.Spend;
import ua.squirrel.web.entity.user.User;

public interface SpendService {
				
	List<Spend> findAllByUserAndLasteDateBetweenOrderByLasteDateAsc(User user , LocalDate from, LocalDate to);

	List<Spend> findAllByStoreAndLasteDateBetweenOrderByLasteDateAsc(Store store , LocalDate from, LocalDate to);

	Optional<Spend> findOneByUserAndId(User user, Long id);

	List<Spend> findAllByUserAndStoreAndLasteDateBetweenOrderByLasteDateAsc(User user,Store store, LocalDate from, LocalDate to);
	
	List<Spend> findAllByIsOpen(boolean isOpen);
	
	List<Spend> findAllByUser(User user);
}

