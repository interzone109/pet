package ua.squirrel.user.controller.store;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.product.CompositeProductModel;
import ua.squirrel.user.entity.product.ProductModel;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.StoreAssortment;
import ua.squirrel.user.entity.store.StoreAssortmentModel;
import ua.squirrel.user.service.product.CompositeProductServiceImpl;
import ua.squirrel.user.service.product.ProductServiceImpl;
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

/**
 * Контроллер формирует ассортимент на тт
 * 
 */

@RestController
@RequestMapping("/stores/{store_id}/assortment")
@Slf4j
public class AssortmentFormationController {
	@Autowired
	private StoreServiceImpl storeServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private ProductServiceImpl productServiceImpl;
	@Autowired
	private CompositeProductServiceImpl compositeProductServiceImpl;

	
	/**
	 * Метод возращяет все тавары на тт
	 * */
	@GetMapping
	public StoreAssortmentModel showAllStoresAssortment(@PathVariable("store_id") Long id,
			Authentication authentication) throws NotFoundException {

		log.info("LOGGER: return all stores assortment");
		User userCurrentSesion = userServiceImpl.findOneByLogin("test1").get();

		return getStoreAsssortment(id, userCurrentSesion);
	}
	
	
	

	private Store getStore(Long id, User user) throws NotFoundException {
		return storeServiceImpl.findOneByIdAndUser(id, user)
				.orElseThrow(() -> new NotFoundException("Store not found"));
	}
	
	/**
	 * Метод формирует все тавары на тт
	 * */
	private StoreAssortmentModel getStoreAsssortment(Long id, User user) throws NotFoundException {

		StoreAssortment store = getStore(id, user).getStoreAssortment();
		  
		List<ProductModel> productModels = new ArrayList<>();
		
		store.getProduct().stream().forEach(prod->{
			productModels.add(ProductModel.builder()
					.name(prod.getName())
					.description(prod.getDescription())
					.group(prod.getGroup())
					.build());});

		
		List<CompositeProductModel> compositeProductModels = new ArrayList<>();
		store.getCompositeProduct().stream().forEach(composite->{
			compositeProductModels.add(CompositeProductModel.builder()
					.name(composite.getName())
					.productsConsumption(composite.getProductsConsumption())
					.build()
					);});
		

		return StoreAssortmentModel.builder()
				.compositeProductModels(compositeProductModels)
				.productModels(productModels).build();
	}
}
