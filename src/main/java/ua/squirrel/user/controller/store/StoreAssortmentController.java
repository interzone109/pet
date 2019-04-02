package ua.squirrel.user.controller.store;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.product.composite.CompositeProductModel;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.util.StoreUtil;
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@RestController
@RequestMapping("/user/stores/assortment/{store_id}")
@Slf4j
public class StoreAssortmentController {
	
	@Autowired
	private StoreServiceImpl storeServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private StoreUtil storeUtil;


	/**
	 * Метод возращает список композитных продуктов и их цену для данной ТТ
	 */
	@GetMapping
	public List<CompositeProductModel> getAssortment(@PathVariable("store_id")Long storeId, Authentication authentication) throws NotFoundException {

		log.info("LOGGER: get assortment for current user");
		User user = userServiceImpl.findOneByLogin("test1").get();
		
		return storeUtil.getCompositeProductPrice( user ,  getCurrentStore(user ,storeId).getProductPrice());
		
	}
	
	
	
	private Store getCurrentStore(User user ,Long id) throws NotFoundException {
		return storeServiceImpl.findOneByIdAndUser(id, user)
				.orElseThrow(() -> new NotFoundException("Store not found"));
	}
}
