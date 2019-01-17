package ua.squirrel.user.assortment.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.squirrel.user.assortment.product.Product;
import ua.squirrel.web.entity.user.User;

@Service
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	@Autowired
	public  ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	public void save(Product productToSave) {
		productRepository.save(productToSave);
	}
	public List<Product> findByUser(User user){
		return productRepository.findByUser(user);
	}
}
