package ua.squirrel.user.controller.store;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.StoreAssortmentModel;
import ua.squirrel.user.entity.store.StoreModel;
import ua.squirrel.user.service.product.CompositeProductServiceImpl;
import ua.squirrel.user.service.product.ProductServiceImpl;
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.registration.user.UserServiceImpl;
/**
 *Контроллер формирует ассортимент на тт
 * 
 * */

@RestController
@RequestMapping("/stores/{store_id}/edit")
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
	
	
	@GetMapping
	public StoreAssortmentModel showAllStoresAssortment(Authentication authentication) throws NotFoundException {

		log.info("LOGGER: return all stores current user");
		User userCurrentSesion = userServiceImpl.findOneByLogin("test1").get();
		
		return null;
		}
	
	
	
	private Store getStore(Long id, User user) throws NotFoundException {
		return storeServiceImpl.findOneByIdAndUser(id, user)
				.orElseThrow(() -> new NotFoundException("Store not found"));
	}
}
