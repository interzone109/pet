package ua.squirrel.user.service.store.spending;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.spending.Spend;
import ua.squirrel.web.entity.user.User;

public interface SpendService {

	List<Spend> findAllByUserOrderByDateAsc(User user);

	List<Spend> findAllByStoreOrderByDateAsc(Store store);

	Optional<Spend> findOneByUserAndIdOrderByDateAsc(User user, Long id);

	Spend save(Spend spend);
	
	List<Spend> findByUserAndDateBetween(User user, Date from, Date to);
}

