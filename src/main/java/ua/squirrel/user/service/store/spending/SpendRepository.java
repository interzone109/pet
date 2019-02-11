package ua.squirrel.user.service.store.spending;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.spending.Spend;
import ua.squirrel.web.entity.user.User;

public interface SpendRepository extends JpaRepository<Spend, Long> {

	List<Spend> findAllByUserOrderByDateAsc(User user);

	List<Spend> findAllByStoreOrderByDateAsc(Store store);

	Optional<Spend> findOneByUserAndIdOrderByDateAsc(User user, Long id);

	List<Spend> findByUserAndDateBetween(User user, Calendar from, Calendar to);

}
