package ua.squirrel.user.service.store;

import java.util.List;
import java.util.Optional;

import ua.squirrel.user.entity.store.Store;
import ua.squirrel.web.entity.user.User;

public interface StoreService {

	Store save(Store store);

	Optional<Store> findOneByIdAndUser(Long id, User user);

	List<Store> findAllByUser(User user);
}
