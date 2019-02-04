package ua.squirrel.user.controller.store;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import ua.squirrel.user.entity.product.composite.CompositeProductModel;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.storage.UpdateDeleteStorageModel;
import ua.squirrel.user.service.product.CompositeProductServiceImpl;
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@RestController
@RequestMapping("/stores/{id}/storage")
@Slf4j
public class StoreAssortmentController {

	@Autowired
	private StoreServiceImpl storeServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private CompositeProductServiceImpl compositeService;


	/**
	 * Метод возращает список всех продуктов на тт
	 */
	@GetMapping
	public  Map<CompositeProductModel, Integer> showAllStoreStorage
	(@PathVariable Long id, Authentication authentication)throws NotFoundException {

		log.info("LOGGER: return all product and price for current store");
		User user = userServiceImpl.findOneByLogin("test1").get();
		return getStorageModel(user, getStore(user, id).getStorage().getProductPrice());
	}

	/**
	 * Метод добавляет продукты и цену к текущей тт
	 */
	@PostMapping
	public Map<CompositeProductModel, Integer> addProductToStorage(@PathVariable Long id, Authentication authentication,
			@RequestBody Map<Long, Integer> newIdsPrice) throws NotFoundException {

		log.info("LOGGER:add new product and price to current store");
		User user = userServiceImpl.findOneByLogin("test1").get();

		StringBuilder strBuilder = new StringBuilder();
		compositeService.findAllByUserAndIdIn(user, newIdsPrice.keySet()).stream().forEach(product -> {
			strBuilder.append(product.getId() + ":" + newIdsPrice.get(product.getId()) + "price");
		});
		Store store = getStore(user, id);
		String productPrice = store.getStorage().getProductPrice();

		if (productPrice == null) {
			store.getStorage().setProductPrice(strBuilder.toString());
		} else {
			store.getStorage().getProductPrice().concat(strBuilder.toString());
		}
		storeServiceImpl.save(store);

		return getStorageModel(user, getStore(user, id).getStorage().getProductPrice());
	}

	/**
	 * Метод возращает список всех торговых точек
	 */
	@PutMapping
	public  Map<CompositeProductModel, Integer> updateDeleteStorageProduct(@PathVariable Long id,
			Authentication authentication, @RequestBody UpdateDeleteStorageModel storageModel) throws NotFoundException {

		log.info("LOGGER: update or delete product-price for current store");
		User user = userServiceImpl.findOneByLogin("test1").get();

		if (storageModel.getIdsPrice() != null) {
			updateStorage(user, id, storageModel.getIdsPrice());
		}
		if (storageModel.getRemoveProduct() != null) {
			removeProduct(user, id, storageModel.getRemoveProduct());
		}

		return getStorageModel(user, getStore(user, id).getStorage().getProductPrice());
	}
	
	/**
	 * метод удаляет указаные продукты
	 * 
	 */
	private void removeProduct(User user, Long id, List<Long> removeProduct) throws NotFoundException {
		Store store = getStore(user, id);

		Map<Long, Integer> currentIdsPrice = new HashMap<>();
		for (String str : store.getStorage().getProductPrice().split("price")) {
			String[] args = str.split(":");
			currentIdsPrice.put(Long.parseLong(args[0]), Integer.parseInt(args[1]));
		}
		
		// удаляем записи
				StringBuilder remove = new StringBuilder();
				Date curentDate = new Date();
				compositeService.findAllByUserAndIdIn(user, removeProduct).stream().forEach(prod -> {
					remove.append(prod.getId() + ":" + currentIdsPrice.get(prod.getId()) + "price" + curentDate.getTime() + "date");
					currentIdsPrice.remove(prod.getId());
				});
				
				StringBuilder newPrice = new StringBuilder();
				currentIdsPrice.forEach((key, value) -> {
					newPrice.append(key + ":" + value + "price");
				});
				store.getStorage().setProductPrice(newPrice.toString());
				
				if(store.getStorage().getProductDelete() != null) {
					remove.append(store.getStorage().getProductDelete());
				}
				store.getStorage().setProductDelete(remove.toString());

				storeServiceImpl.save(store);		
	}

	/**
	 * метод обновляем цену у указаных продуктов
	 * 
	 * @throws NotFoundException
	 */
	private void updateStorage(User user, Long id, Map<Long, Integer> idsPrice) throws NotFoundException {
		Store store = getStore(user, id);

		Map<Long, Integer> currentIdsPrice = new HashMap<>();
		for (String str : store.getStorage().getProductPrice().split("price")) {
			String[] args = str.split(":");
			currentIdsPrice.put(Long.parseLong(args[0]), Integer.parseInt(args[1]));
		}
		// обновляем записи
		StringBuilder oldPrice = new StringBuilder();
		Date curentDate = new Date();
		compositeService.findAllByUserAndIdIn(user, idsPrice.keySet()).stream().forEach(prod -> {
			oldPrice.append(
					prod.getId() + ":" + currentIdsPrice.get(prod.getId()) + "price" + curentDate.getTime() + "date");
			currentIdsPrice.put(prod.getId(), idsPrice.get(prod.getId()));
		});
		
		StringBuilder newPrice = new StringBuilder();
		currentIdsPrice.forEach((key, value) -> {
			newPrice.append(key + ":" + value + "price");
		});
		store.getStorage().setProductPrice(newPrice.toString());
		
		if(store.getStorage().getPriceUpdate() != null) {
			oldPrice.append(store.getStorage().getPriceUpdate());
		}
		store.getStorage().setPriceUpdate(oldPrice.toString());
		storeServiceImpl.save(store);
	}

	/**
	 * метод создает модель продукт-цена для текущего склада
	 */
	private Map<CompositeProductModel, Integer> getStorageModel(User user, String productPrice) {
		// получаю ид - цена из текущего склада и записываю их в Мар
		Map<Long, Integer> idsPrice = new HashMap<>();
		for (String str : productPrice.split("price")) {
			String[] args = str.split(":");
			idsPrice.put(Long.parseLong(args[0]), Integer.parseInt(args[1]));
		}
		// создаю Мар по продукту и его цене и по ид создаю модель продукта
		Map<CompositeProductModel, Integer> productPriceMap = new HashMap<>();
		compositeService.findAllByUserAndIdIn(user, idsPrice.keySet()).stream().forEach(prod -> {
			productPriceMap.put(
					CompositeProductModel.builder().id(prod.getId()).name(prod.getName()).group(prod.getGroup())
							.propertiesProduct(prod.getPropertiesProduct().toString()).build(),
					idsPrice.get(prod.getId()));

		});

		return productPriceMap;
	}

	private Store getStore(User user, Long id) throws NotFoundException {
		return storeServiceImpl.findOneByIdAndUser(id, user)
				.orElseThrow(() -> new NotFoundException("Store not found"));
	}

	/*test POST
	 {
   "1": 2
	}

test PUT
	{
   "idsPrice": {
      "1": 1
   },
   "removeProduct": [
      1
   ]
}
	 */
}
