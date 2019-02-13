package ua.squirrel.user.controller.store;

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
import ua.squirrel.user.entity.store.consignment.util.ConsignmentUtil;
import ua.squirrel.user.entity.store.storage.UpdateDeleteStorageModel;
import ua.squirrel.user.entity.store.storage.util.StorageUtils;
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
	@Autowired
	private StorageUtils storageUtils;
	@Autowired
	private ConsignmentUtil consignmentUtil;

	/**
	 * Метод возращает список всех продуктов на тт
	 */
	@GetMapping
	public Map<CompositeProductModel, Integer> showAllStoreStorage(@PathVariable Long id, Authentication authentication)
			throws NotFoundException {

		log.info("LOGGER: return all product and price for current store");
		User user = userServiceImpl.findOneByLogin("test1").get();
		return getStorageProcut(user, getStore(user, id));
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

		if (productPrice != null) {
			strBuilder.append(productPrice);
		}

		store.getStorage().setProductPrice(strBuilder.toString());
		
		consignmentUtil.updateConsignment(store);// обновляем данные по партии
		
		storeServiceImpl.save(store);
		

		return getStorageProcut(user, getStore(user, id));
	}

	/**
	 * Метод обновляет или удаляет товары с ТТ
	 */
	@PutMapping
	public Map<CompositeProductModel, Integer> updateDeleteStorageProduct(@PathVariable Long id,
			Authentication authentication, @RequestBody UpdateDeleteStorageModel storageModel)
			throws NotFoundException {

		log.info("LOGGER: update or delete product-price for current store");
		User user = userServiceImpl.findOneByLogin("test1").get();

		if (storageModel.getIdsPrice() != null) {
			updateStorage(user, id, storageModel.getIdsPrice());
		}
		if (storageModel.getRemoveProduct() != null) {
			removeProduct(user, id, storageModel.getRemoveProduct());
		}
		

		return getStorageProcut(user, getStore(user, id));
	}

	/**
	 * метод удаляет указаные продукты
	 * 
	 */
	private void removeProduct(User user, Long id, List<Long> removeProduct) throws NotFoundException {
		Store store = getStore(user, id);
		storeServiceImpl.save(storageUtils.deleteIdsPrice(store, removeProduct,
				compositeService.findAllByUserAndIdIn(user, removeProduct)));
	}

	/**
	 * метод обновляет цену у указаных продуктов
	 */
	private void updateStorage(User user, Long id, Map<Long, Integer> newIdsPrice) throws NotFoundException {
		Store store = getStore(user, id);
		// вызываем метод по обновлению цены на композитный продукт в магазине
		// метод обновляет цену на новую, старую цену и дату обновления сохраняет
		// результат сохраняем в бд
		storeServiceImpl.save(storageUtils.updateIdsPrice(store, newIdsPrice,
				compositeService.findAllByUserAndIdIn(user, newIdsPrice.keySet())));

	}

	/**
	 * метод создает модель продукт-цена для текущего склада
	 */
	private Map<CompositeProductModel, Integer> getStorageProcut(User user, Store store) {
		// получаю ид - цена из текущего склада и записываю их в Мар
		Map<Long, Integer> idsPrice = storageUtils.getIdPrice(store.getStorage().getProductPrice());
		// создаю Мар по продукту и его цене и по ид создаю модель продукта
		return storageUtils.getCompositeProductModel(compositeService.findAllByUserAndIdIn(user, idsPrice.keySet()),
				store);
	}

	private Store getStore(User user, Long id) throws NotFoundException {
		return storeServiceImpl.findOneByIdAndUser(id, user)
				.orElseThrow(() -> new NotFoundException("Store not found"));
	}

	/*
	 * test POST { "1": 2 }
	 * 
	 * test PUT { "idsPrice": { "1": 1 }, "removeProduct": [ 1 ] }
	 */
}
