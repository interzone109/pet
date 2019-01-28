package ua.squirrel.user.service.store;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.user.entity.store.Store;
import ua.squirrel.web.entity.user.User;

public interface StoreRepository extends JpaRepository<Store, Long> {
	
	Optional<Store> findOneByIdAndUser(Long id, User user);
	
	List<Store> findAllByUser(User user);
}
