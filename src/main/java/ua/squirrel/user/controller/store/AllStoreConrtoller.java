package ua.squirrel.user.controller.store;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/stores")
@Slf4j
public class AllStoreConrtoller {

	@Autowired
	private StoreServiceImpl storeServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;

	/**
	 * Метод возращает список всех торговых точек
	 * */
	@GetMapping
	public List<StoreModel> showAllStores(Authentication authentication) throws NotFoundException {

		log.info("LOGGER: return all stores current user");
		User userCurrentSesion = userServiceImpl.findOneByLogin("test1").get();
		return getAllStore(userCurrentSesion);
	}
	/**
	 * Метод добавляет новую торговую точку
	 * */
	@PostMapping
	public List<StoreModel> addNewStore(@RequestBody StoreModel storeModel, Authentication authentication)
			throws NotFoundException {
		log.info("LOGGER: create new store");
		User userCurrentSesion = userServiceImpl.findOneByLogin("test1").get();

		Store newStore = new Store();
		newStore.setAddress(storeModel.getAddress());
		newStore.setDescription(storeModel.getDescription());
		newStore.setUser(userCurrentSesion);
		storeServiceImpl.save(newStore);
		
		return getAllStore(userCurrentSesion);
	}

	

	private List<StoreModel> getAllStore(User user) {
		List<StoreModel> storeModels = new ArrayList<>();
		storeServiceImpl.findAllByUser(user).stream().forEach(store -> {
			storeModels.add(StoreModel.builder()
					.id(store.getId())
					.address(store.getAddress())
					.description(store.getDescription()).build());
		});
		return storeModels;
	}

}
