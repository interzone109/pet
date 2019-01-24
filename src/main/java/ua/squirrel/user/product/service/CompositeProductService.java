package ua.squirrel.user.product.service;

import java.util.List;
import java.util.Optional;

import ua.squirrel.user.product.entity.CompositeProduct;
import ua.squirrel.web.user.entity.User;

public interface CompositeProductService {
	CompositeProduct save(CompositeProduct compositeProduct);

	Optional<CompositeProduct> findByIdAndUser(Long id, User user);
}
