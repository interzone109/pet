package ua.squirrel.user.controller.product;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.product.Product;
import ua.squirrel.user.entity.product.ProductModel;
import ua.squirrel.user.entity.product.composite.CompositeProduct;
import ua.squirrel.user.entity.product.node.ProductMap;
import ua.squirrel.user.service.product.CompositeProductServiceImpl;
import ua.squirrel.user.service.product.ProductServiceImpl;
import ua.squirrel.user.service.product.node.ProductMapServiceImpl;
import ua.squirrel.user.utils.CompositeProductUtil;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@RestController
@RequestMapping("user/composites/{id}/edit")
@Slf4j
public class CompositeProductController {
	@Autowired
	private CompositeProductServiceImpl compositeProductServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private ProductServiceImpl productServiceImpl;
	@Autowired
	private CompositeProductUtil  compositeProductUtil;
	
	@Autowired
	private ProductMapServiceImpl  productMapServiceImpl;
	
	/**
	 * метод находит по id и User CompositeProduct и возращает информацию о нем и о
	 * его ингридиентах
	 */
	@GetMapping
	public List<ProductModel> getCompositeProductInfo(Authentication authentication, @PathVariable("id") Long id)
			throws NotFoundException {
		log.info("LOGGER: return curent composite product");
		User user = userServiceImpl.findOneByLogin("test1").get();
		
		CompositeProduct compositeProduct = getCompositeProduct(id, user);
		// вызывается привaтный метод возращающий модель коспозитного продукта
		return compositeProductUtil.convertToProductModelFromMap(compositeProduct.getProductMap());

	}
	
	// New controller
	@PostMapping
	public List<ProductModel> addToCompositeProduct(@PathVariable("id") Long compositeId ,
			@RequestBody Map<Long, Integer> composites, Authentication authentication) throws NotFoundException {
		log.info("LOGGER:  product add new ingridient in  curent composite product");
		User user = userServiceImpl.findOneByLogin("test1").get();
		
		//получаем композитный продукт в который будем добавлять новые ингридиенты и их расход
		CompositeProduct compositeProduct = getCompositeProduct(compositeId, user);
		List<Product> prducts = productServiceImpl.findAllByUserAndIdIn(user, composites.keySet());
		List<ProductMap> productMaps = new ArrayList<>();
		prducts.forEach(product->{
			ProductMap productMap = new ProductMap();
			productMap.setCompositeProduct(compositeProduct);
			productMap.setProduct(product);
			productMap.setRate(composites.get(product.getId()));
			productMaps.add(productMap);
		});
		productMapServiceImpl.saveAll(productMaps);
		
		
		return compositeProductUtil.convertToProductModelFromMap(productMaps);
	}
	
	
	@PutMapping("{productMapId}")
	public ProductModel updateProduct(@PathVariable("id") Long copositeId, @PathVariable("productMapId") Long productMapId, Authentication authentication,
			@RequestBody  Integer updateRate) throws NotFoundException {
		
		log.info("LOGGER: update  product expends");
		User user = userServiceImpl.findOneByLogin("test1").get();
		
		// получаем текущий композитный продукт по Id и пользователю
		ProductMap node = productMapServiceImpl.findOneByIdAndCompositeProduct(productMapId, getCompositeProduct(copositeId, user));
		node.setRate(updateRate);
		productMapServiceImpl.save(node);
		return  compositeProductUtil.convertToProductPriceModel(node);
	}


	private CompositeProduct getCompositeProduct(Long id, User currentUser) throws NotFoundException {
		return compositeProductServiceImpl.findByIdAndUser(id, currentUser)
				.orElseThrow(() -> new NotFoundException("Composite product not found"));
	}


	/*
	  тестовый джейсон Post 
	  { 
	  "composites": { 
	  "4":4444, 
	  "5":55555 
	  }, 
	  "deleteIds": [] 
	  } 
	  тестовый джейсон Put/delete
	   
	   {
	    "composites": 
	   	{ 
	      "1":4444,
	     "2":55555 
	     },
	      "deleteIds": [ 1, 2] 
	      }
	 
	  ":[0-9]+rate|rate*"
	 
	  [date]*[0-9]*:[0-9]*rate|[date]*
	 */

}
