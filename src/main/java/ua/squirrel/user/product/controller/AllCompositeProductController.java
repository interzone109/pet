package ua.squirrel.user.product.controller;

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
import ua.squirrel.user.product.entity.CompositeProduct;
import ua.squirrel.user.product.entity.CompositeProductModel;
import ua.squirrel.web.registration.user.service.UserServiceImpl;
import ua.squirrel.web.user.entity.User;

@RestController
@RequestMapping("/partners/composites")
@Slf4j
//@Secured("USER")
public class AllCompositeProductController {
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	/**
	 * метод получает зарегисрированого пользователя 
	 * берет у него список композитных продуктов и перезаписывает их
	 * в List<CompositeProductModel>, после чего возращает данные
	 * 
	 * */
	@GetMapping
	public List<CompositeProductModel> getAllCompositeProduct(Authentication authentication) {
		log.info("LOGGER: show all Composite ProductModel");
		
		User user = userServiceImpl.findOneByLogin("test1").get();
		
		 List<CompositeProductModel>compositeProductModels = new ArrayList<>();
		
		user.getCompositeProducts().stream().forEach(obj->{
			compositeProductModels.add(CompositeProductModel.builder()
					.id(obj.getId())
					.name(obj.getName())
					.productsConsumption(obj.getProductsConsumption())
					.build());});
		
		
		return compositeProductModels ;
	}
	
	/**
	 * метод получает список новых коспозитных продуктов List <CompositeProductModel> newCompositeProductModel,
	 * после чего переписывает данные из поделей в сущности и сохраняет их в базу
	 * */
	@PostMapping
	public List <CompositeProductModel> addNewCompositeProducе(Authentication authentication,
			@RequestBody List <CompositeProductModel> newCompositeProductModels ) {
		log.info("LOGGER: save new Composite ProductModel from model " );
		
		
		User user = userServiceImpl.findOneByLogin("test1").get();
		List<CompositeProduct> newList = new ArrayList<>();
		newCompositeProductModels.stream().forEach(obj->{
			CompositeProduct newCompositeProduct = new CompositeProduct();
			newCompositeProduct.setName(obj.getName());
			newCompositeProduct.setProductsConsumption(obj.getProductsConsumption());
			newCompositeProduct.setUser(user);
			newList.add(newCompositeProduct);
		});
		user.getCompositeProducts().addAll(newList);
		userServiceImpl.save(user);
	return newCompositeProductModels ;	
	}
	
	
	
	
/*	
   тестовый джейсон
	[
	   {
	      "name": "Американо большое",
	      "productsConsumption": {
	         "1": 30,
	         "2": 10,
	         "3": 2,
	         "9": 1,
	         "13": 1
	      }
	   },
	   {
	      "name": "чай",
	      "productsConsumption": {
	         "1": 50,
	         "5": 5,
	         "11": 1,
	         "13": 1
	      }
	   }
	 ]
	     */
	
}
