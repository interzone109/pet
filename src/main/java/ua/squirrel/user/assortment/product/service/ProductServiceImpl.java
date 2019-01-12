package ua.squirrel.user.assortment.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.squirrel.user.assortment.product.Product;

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
}
