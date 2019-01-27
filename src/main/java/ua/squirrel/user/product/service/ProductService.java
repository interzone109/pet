package ua.squirrel.user.product.service;

import java.util.List;
import java.util.Optional;

import ua.squirrel.user.partner.entity.Partner;
import ua.squirrel.user.product.entity.Product;
import ua.squirrel.web.user.entity.User;

public interface ProductService {
	void save(Product productToSave);

	public  List<Product> findAllById(Iterable<Long> ids);

	Optional<Product> findOneByIdAndPartner(Long id, Partner partner);

	List<Product> findAllByIdAndUser(Iterable<Long> ids, User user);
}
