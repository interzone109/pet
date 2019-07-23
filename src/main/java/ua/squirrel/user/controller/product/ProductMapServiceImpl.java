package ua.squirrel.user.controller.product;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.user.entity.product.map.ProductMap;

@Service
public class ProductMapServiceImpl   {
@Autowired
private ProductMapRepository productMapRepository;

public  List<ProductMap> saveAll(Set<ProductMap> productMap) {
	return productMapRepository.saveAll(productMap);
}
}
