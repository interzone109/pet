package ua.squirrel.user.controller.store;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.product.composite.CompositeProduct;
import ua.squirrel.user.entity.product.composite.CompositeProductModel;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.StoreModel;
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
public class StoreAssortmentController {
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
	public StoreModel showAllStoresAssortment(@PathVariable("store_id") Long id,
			Authentication authentication) throws NotFoundException {

		log.info("LOGGER: return all stores assortment");
		User user = userServiceImpl.findOneByLogin("test1").get();
		
		return getStoreModel(id, user);
	}
	
	@PostMapping
	public StoreModel addNewStore(@PathVariable("store_id") Long id, 
			@RequestBody List<CompositeProduct> addCompositeProduct, Authentication authentication)
			throws NotFoundException {
		log.info("LOGGER: create new store");
		User user = userServiceImpl.findOneByLogin("test1").get();
		
		
		return getStoreModel(id, user);
	}
	

	
	/**
	 * Метод формирует модель ТТ и заполняет его композитными продуктами (выставлеными на продажу)
	 * */
	private StoreModel getStoreModel(Long id, User user) throws NotFoundException {
		Store store = getStore(id, user);
		List<CompositeProductModel> compositeProductModel = new ArrayList<>();
		
		store.getCompositeProduct().forEach(product->{
			compositeProductModel.add(CompositeProductModel.builder()
					.id(product.getId())
					.name(product.getName())
					.group(product.getGroup())
					.propertiesProduct(product.getPropertiesProduct().toString())
					.build());});
		
		
		return StoreModel.builder().address(store.getAddress())
				.description(store.getDescription())
				.compositeProduct(compositeProductModel)
				.build();
	}
	private Store getStore(Long id, User user) throws NotFoundException {
		return storeServiceImpl.findOneByIdAndUser(id, user)
				.orElseThrow(() -> new NotFoundException("Store not found"));
	}
	
}
