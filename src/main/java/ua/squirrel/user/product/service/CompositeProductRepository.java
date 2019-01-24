package ua.squirrel.user.product.service;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.user.product.entity.CompositeProduct;
import ua.squirrel.web.user.entity.User;

public interface CompositeProductRepository extends JpaRepository<CompositeProduct, Long> {
	Optional<CompositeProduct> findByIdAndUser(Long id, User user);
}
