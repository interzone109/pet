package ua.squirrel.user.controller.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.product.Product;
import ua.squirrel.user.entity.product.ProductModel;
import ua.squirrel.user.service.product.ProductServiceImpl;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@RestController
@RequestMapping("/user/products_list" )
@Slf4j
public class AllProductController {
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private ProductServiceImpl productServiceImpl;
	
	@GetMapping
	public List<ProductModel> getUserProduct(Authentication authentication) throws NotFoundException {
		
		log.info("LOGGER: get all user product : /user/products_list");
		User user = userServiceImpl.findOneByLogin("test1").get();
		
		List<ProductModel> productList = new ArrayList<>();
		
		productServiceImpl.findAllByUser(user).forEach(product->{
			productList.add(createProductModel(product) );
		});
		
		return productList ;
	}
	
	@PostMapping
	public List<ProductModel> getUserProductFitler(@RequestBody List<Long> filter,
			Authentication authentication) throws NotFoundException {
		
		log.info("LOGGER: get all user product : /user/products_list");
		User user = userServiceImpl.findOneByLogin("test1").get();
		
		List<ProductModel> productList = new ArrayList<>();
		
		productServiceImpl.findAllByUser(user).forEach(product->{
			if(!filter.contains(product.getId())){
				productList.add(createProductModel(product) );
			}
		});

		return productList ;
	}
	
	
	private ProductModel createProductModel(Product product) {
		return ProductModel.builder()
				.id(product.getId())
				.name(product.getName())
				.measureProduct(product.getMeasureProduct().getMeasure())
				.group(product.getGroup())
				.build();
	}
	
}
