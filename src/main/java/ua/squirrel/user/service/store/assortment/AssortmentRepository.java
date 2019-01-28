package ua.squirrel.user.service.store.assortment;

import java.util.Optional;

import org.apache.catalina.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.user.entity.store.StoreAssortment;

public interface AssortmentRepository extends JpaRepository<StoreAssortment, Long> {
	Optional<StoreAssortment> findOneByStore(Store store);
	
}
