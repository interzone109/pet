package ua.squirrel.user.service.product;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.user.entity.partner.Partner;
import ua.squirrel.user.entity.product.Product;
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
	public Optional<Product> findOneByIdAndPartner(Long id ,Partner partner){
		return productRepository.findOneByIdAndPartner(id, partner);
	}
	
	public  List<Product> findAllById(Iterable<Long> ids){
		return productRepository.findAllById(ids);
	}
	
	
	public  List<Product> findAllByIdAndUser(Iterable<Long> ids ,User user ){
		return productRepository.findAllByIdAndUser(ids ,user);
	}
	
	public List<Product> saveAll(Iterable<Product> productToSave) {
		return productRepository.saveAll(productToSave);
	}
}