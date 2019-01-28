package ua.squirrel.user.service.store.assortment;

import java.util.Optional;

import org.apache.catalina.Store;

import ua.squirrel.user.entity.store.StoreAssortment;

public interface AssortmentService {
	StoreAssortment save(StoreAssortment storeAssortment);
	Optional<StoreAssortment> findOneByStore(Store store);
}
