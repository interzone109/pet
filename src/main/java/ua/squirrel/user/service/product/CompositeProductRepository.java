package ua.squirrel.user.service.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.user.entity.product.Product;
import ua.squirrel.user.entity.product.composite.CompositeProduct;
import ua.squirrel.web.entity.user.User;

public interface CompositeProductRepository extends JpaRepository<CompositeProduct, Long> {
	Optional<CompositeProduct> findByIdAndUser(Long id, User user);

	List<CompositeProduct> findAllByUser(User user);

	List<CompositeProduct> findAllByUserAndIdIn(User user, Iterable<Long> id);
}
