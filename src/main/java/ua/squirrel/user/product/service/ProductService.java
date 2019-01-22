package ua.squirrel.user.product.service;

import java.util.Optional;
import ua.squirrel.user.partner.Partner;
import ua.squirrel.user.product.Product;

public interface ProductService {
	 void save(Product productToSave);
	 
	 Optional<Product> findOneByIdAndPartner(Long id ,Partner partner);
}
