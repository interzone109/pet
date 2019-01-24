package ua.squirrel.user.product.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.product.entity.CompositeProduct;
import ua.squirrel.user.product.entity.CompositeProductModel;
import ua.squirrel.user.product.entity.ProductModel;
import ua.squirrel.user.product.service.CompositeProductServiceImpl;
import ua.squirrel.user.product.service.ProductServiceImpl;
import ua.squirrel.web.registration.user.service.UserServiceImpl;
import ua.squirrel.web.user.entity.User;

@RestController
@RequestMapping("/partners/composites/{id}/edit")
@Slf4j
//@Secured("USER")
public class CompositeProductController {
	@Autowired
	private CompositeProductServiceImpl compositeProductServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private ProductServiceImpl productServiceImpl;
	

	/**
	 * метод находит по id и  User CompositeProduct и возращает информацию о нем и о
	 * его состовляющих
	 */
	
	@GetMapping
	public CompositeProductModel getCompositeProductInfo(Authentication authentication, @PathVariable("id") Long id)
			throws NotFoundException {
		log.info("LOGGER: return curent composite product");

		User userCurrentSesion = userServiceImpl.findOneByLogin("test1").get();

		CompositeProduct compositeProduct = getCompositeProduct(id, userCurrentSesion);
		
		Map<ProductModel, Integer> composites = new HashMap<>();
		//делаем выборку всех Product у обьекта CompositeProduct
		// записываем их в Map<ProductModel, Integer> 
		// значемием служит количество расходуемое каждым Product на один CompositeProduct
		 productServiceImpl.findAllById(compositeProduct.getProductsConsumption().keySet()).stream()
		 .forEach(product->{
			 ProductModel prodModel = ProductModel.builder()
					 .id(product.getId())
					 .name(product.getName())
					 .description(product.getDescription())
					 .group(product.getGroup())
					 .measureProduct(product.getMeasureProduct())
					 .propertiesProduct(product.getPropertiesProduct()).build();
			 composites.put(prodModel,compositeProduct.getProductsConsumption().get(product.getId()) );

		 });

		return CompositeProductModel.builder()
				.id(compositeProduct.getId())
				.name(compositeProduct.getName())
				.productsComposite(composites)
				.build();
	}

	@PutMapping
	public CompositeProductModel updateCompositeProduct(@PathVariable("id") Long id,
			@RequestBody Map<Long, Integer> composites ,Authentication authentication) throws NotFoundException {
		log.info("LOGGER: update curent composite product");
		User userCurrentSesion = userServiceImpl.findOneByLogin("test1").get();
		CompositeProduct compositeProduct = getCompositeProduct(id, userCurrentSesion);
		productServiceImpl.findAllById(composites.keySet());

		return null;
	}

	@DeleteMapping
	public void getDeleteCompositeProduct(@PathVariable("id") Long id, Authentication authentication)
			throws NotFoundException {
		log.info("LOGGER: delete curent partners");

	}

	private CompositeProduct getCompositeProduct(Long id , User currentUser) throws NotFoundException {
		return compositeProductServiceImpl.findByIdAndUser(id, currentUser)
				.orElseThrow(() -> new NotFoundException("Composite product not found"));
	}
	
	/*
	тестовый джейсон 
	{
   "id": 4,
   "name": "картошка фри 500",
   "productsConsumption": null,
   "productsComposite": {
      "ProductModel(id=6, name=Картошка, description=катошка фри мороженая, group=овощи, propertiesProduct=PropertiesProduct(id=3, name=COMPLETE_COMPOSITE), measureProduct=MeasureProduct(id=2, measure=KILOGRAM))": 500,
      "ProductModel(id=7, name=Масло Стожор, description=Масло подсолнечное стожор банка 0.9л , group=, propertiesProduct=PropertiesProduct(id=1, name=COMPOSITE), measureProduct=MeasureProduct(id=1, measure=LITER))": 100,
      "ProductModel(id=8, name=Соль столовая, description=Сель соленая, group=, propertiesProduct=PropertiesProduct(id=1, name=COMPOSITE), measureProduct=MeasureProduct(id=2, measure=KILOGRAM))": 10
   }
}
	 */
	
}
