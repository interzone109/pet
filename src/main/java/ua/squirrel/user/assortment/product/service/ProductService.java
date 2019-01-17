package ua.squirrel.user.assortment.product.service;

import java.util.List;

import ua.squirrel.user.assortment.product.Product;
import ua.squirrel.web.entity.user.User;

public interface ProductService {
	public void save(Product productToSave);
	List<Product> findByUser(User user);
}
