package ua.squirrel.user.service.product;

import java.util.List;
import java.util.Optional;

import ua.squirrel.user.entity.partner.Partner;
import ua.squirrel.user.entity.product.Product;
import ua.squirrel.web.entity.user.User;

public interface ProductService {
	void save(Product productToSave);

	public  List<Product> findAllById(Iterable<Long> ids);

	Optional<Product> findOneByIdAndPartner(Long id, Partner partner);

	 List<Product> findAllByUserAndIdIn(User user,Iterable<Long> id );
	
	List<Product> saveAll(Iterable<Product> productToSave);
}
