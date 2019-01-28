package ua.squirrel.user.controller.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.product.CompositeProduct;
import ua.squirrel.user.entity.product.CompositeProductModel;
import ua.squirrel.user.entity.product.Product;
import ua.squirrel.user.service.product.CompositeProductServiceImpl;
import ua.squirrel.user.service.product.ProductServiceImpl;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@RestController
@RequestMapping("/partners/composites")
@Slf4j
//@Secured("USER")
public class AllCompositeProductController {

	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private ProductServiceImpl productServiceImpl;
	@Autowired
	private CompositeProductServiceImpl compositeProductServiceImpl;
	/**
	 * метод получает зарегисрированого пользователя берет у него список композитных
	 * продуктов и перезаписывает их в List<CompositeProductModel>, после чего
	 * возращает данные
	 * 
	 */
	@GetMapping
	public List<CompositeProductModel> getAllCompositeProduct(Authentication authentication) {
		log.info("LOGGER: show all Composite ProductModel");

		User user = userServiceImpl.findOneByLogin("test1").get();

		return getAllCompositProduct(user);
	}

	/**
	 * метод получает новый коmпозитный продукт CompositeProductModel
	 * newCompositeProductModel, после чего переписывает данные из поделей в
	 * сущности и сохраняет их в базу
	 */
	@PostMapping
	public List<CompositeProductModel> addNewCompositeProduct(Authentication authentication,
			@RequestBody CompositeProductModel newCompositeProductModels) {
		log.info("LOGGER: save new Composite ProductModel from model ");

		User user = userServiceImpl.findOneByLogin("test1").get();
		CompositeProduct compositeProduct = new CompositeProduct();

		Map<Long, Integer> consumption = new HashMap<>();
		List<Product> pList = productServiceImpl
				.findAllByIdAndUser(newCompositeProductModels.getProductsConsumption().keySet(), user);
		pList.stream().forEach(product -> {
			consumption.put(product.getId(), newCompositeProductModels.getProductsConsumption().get(product.getId()));
		});
		compositeProduct.setName(newCompositeProductModels.getName());
		compositeProduct.setUser(user);
		compositeProduct.setProductsConsumption(consumption);

		compositeProductServiceImpl.save(compositeProduct);

		return getAllCompositProduct(user);
	}

	private List<CompositeProductModel> getAllCompositProduct(User user) {
		List<CompositeProductModel> compositeProductModels = new ArrayList<>();

		compositeProductServiceImpl.findAllByUser(user).stream().forEach(obj -> {
			compositeProductModels.add(CompositeProductModel.builder().id(obj.getId()).name(obj.getName())
					.productsConsumption(obj.getProductsConsumption()).build());
		});

		return compositeProductModels;
	}

	/*
	 * тестовый джейсон
	  
	  { 
	  "name": "test 2",
	   "productsConsumption": {
	    "1": 1,
	    "2": 1 
	     } 
	  }

	 
	  [ { "name": "Американо большое test", "productsConsumption": { "1": 30, "2":
	  10, "3": 2, "9": 1, "13": 1 } }, { "name": "чай test", "productsConsumption":
	  { "1": 50, "5": 5, "11": 1, "13": 1 } } ]
	 */

}
