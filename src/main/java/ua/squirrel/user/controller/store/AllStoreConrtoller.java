package ua.squirrel.user.controller.store;

import java.util.ArrayList;
import java.util.List;

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
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.StoreModel;
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@RestController
@RequestMapping("/user/stores")
@Slf4j
public class AllStoreConrtoller {

	@Autowired
	private StoreServiceImpl storeServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;


	/**
	 * Метод возращает список всех торговых точек
	 */
	@GetMapping
	public List<StoreModel> showAllStores(Authentication authentication) throws NotFoundException {

		log.info("LOGGER: return all stores current user");
		User userCurrentSesion = userServiceImpl.findOneByLogin(authentication.getName()).get();
		return getAllStore(userCurrentSesion);
		
	}

	/**
	 * Метод добавляет новую торговую точку
	 */
	@PostMapping
	public StoreModel addNewStore(@RequestBody StoreModel storeModel, Authentication authentication)
			throws NotFoundException {
		log.info("LOGGER: create new store with composite product");
		User user = userServiceImpl.findOneByLogin(authentication.getName()).get();


		
		Store newStore = new Store();
		newStore.setAddress(storeModel.getAddress());
		newStore.setMail(storeModel.getMail());
		newStore.setPhone(storeModel.getPhone());
		newStore.setUser(user);
		storeServiceImpl.save(newStore);
		
		storeModel.setId(newStore.getId());
		
		return storeModel;
	}
	
	
	/**
	 * Метод обновляет двнные о ТТ
	 */
	@PutMapping("{id}")
	public StoreModel updateStore(@PathVariable("id") Long storeId, @RequestBody StoreModel storeModel, Authentication authentication)
			throws NotFoundException {
		log.info("LOGGER: create new store with composite product");
		User user = userServiceImpl.findOneByLogin(authentication.getName()).get();

		Store updateStore =  storeServiceImpl.findOneByIdAndUser(storeId, user)
				.orElseThrow(() -> new NotFoundException("Store not found"));
		
		updateStore.setAddress(storeModel.getAddress());
		updateStore.setMail(storeModel.getMail());
		updateStore.setPhone(storeModel.getPhone());	
				
		storeServiceImpl.save(updateStore);
		
		storeModel.setId(updateStore.getId());
		
		return storeModel;
	}
	
	
	

	private List<StoreModel> getAllStore(User user) {
		List<StoreModel> storeModels = new ArrayList<>();
		storeServiceImpl.findAllByUser(user).stream().forEach(store -> {
			
			storeModels.add(StoreModel.builder().id(store.getId())
					.address(store.getAddress())
					.mail(store.getMail())
					.phone(store.getPhone())
					.build());
		});
		return storeModels;
	}
	
}
