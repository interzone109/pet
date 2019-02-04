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

import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.product.composite.CompositeProduct;
import ua.squirrel.user.entity.product.composite.CompositeProductModel;
import ua.squirrel.user.service.product.CompositeProductServiceImpl;
import ua.squirrel.user.service.product.properties.PropertiesProductServiceImpl;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@RestController
@RequestMapping("/partners/composites" )
@Slf4j
//@Secured("USER")
public class AllCompositeProductController {

	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private  PropertiesProductServiceImpl propertiesProductServiceImpl;
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
		compositeProduct.setName(newCompositeProductModels.getName());
		compositeProduct.setGroup(newCompositeProductModels.getGroup());
		compositeProduct.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("PRODUCT_FINAL"));
		compositeProduct.setProductExpend(newCompositeProductModels.getProductExpend());
		
		compositeProduct.setUser(user);
		compositeProductServiceImpl.save(compositeProduct);
	

		return getAllCompositProduct(user);
	}

	private List<CompositeProductModel> getAllCompositProduct(User user) {
		List<CompositeProductModel> compositeProductModels = new ArrayList<>();

		compositeProductServiceImpl.findAllByUser(user).stream().forEach(obj -> {
			compositeProductModels.add(CompositeProductModel.builder()
					.id(obj.getId())
					.name(obj.getName())
					.propertiesProduct(obj.getPropertiesProduct().toString())
					.group(obj.getGroup())
					.build());
		});

		return compositeProductModels;
	}

	/*
	  тестовый джейсон Post method
	 
	 
	 */

}
