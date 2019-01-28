package ua.squirrel.user.service.product;

import java.util.List;
import java.util.Optional;

import ua.squirrel.user.entity.product.CompositeProduct;
import ua.squirrel.web.entity.user.User;

public interface CompositeProductService {
	CompositeProduct save(CompositeProduct compositeProduct);

	Optional<CompositeProduct> findByIdAndUser(Long id, User user);
	
	List<CompositeProduct> findAllByUser(User user);
	
	List<CompositeProduct> saveAll(List<CompositeProduct> compositeProduct) ;
}
