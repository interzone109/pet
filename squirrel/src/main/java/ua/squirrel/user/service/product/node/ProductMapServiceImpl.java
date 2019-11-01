package ua.squirrel.user.service.product.node;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.user.entity.product.composite.CompositeProduct;
import ua.squirrel.user.entity.product.node.ProductMap;

@Service
public class ProductMapServiceImpl implements ProductMapService {
	@Autowired
	private ProductMapRepository productMapRepository;

	public List<ProductMap> saveAll(Iterable<ProductMap> productMap) {
		return productMapRepository.saveAll(productMap);
	}

	public ProductMap save(ProductMap productMapUpdate) {
		return productMapRepository.save(productMapUpdate);

	}
	public ProductMap findOneByIdAndCompositeProduct(Long id, CompositeProduct compositeProduct) {
		return productMapRepository.findOneByIdAndCompositeProduct(id, compositeProduct);
	}
	public void deleteById(Long id) {
		productMapRepository.deleteById(id);
	}
}
