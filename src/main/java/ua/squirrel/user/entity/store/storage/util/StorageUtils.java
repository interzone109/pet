package ua.squirrel.user.entity.store.storage.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import ua.squirrel.user.entity.product.composite.CompositeProduct;
import ua.squirrel.user.entity.product.composite.CompositeProductModel;
import ua.squirrel.user.entity.store.Store;

@Component
public class StorageUtils {

	/**
	 * метод разбивает строку на мапу id-price
	 */
	public Map<Long, Integer> getIdPrice(String productPrice) {
		Map<Long, Integer> idsPrice = new HashMap<>();
		for (String str : productPrice.split("price")) {
			String[] args = str.split(":");
			idsPrice.put(Long.parseLong(args[0]), Integer.parseInt(args[1]));
		}
		return idsPrice;
	}

	/**
	 * метод форминует мапу из копмозитного продукта и цены
	 * 
	 * @param List<CompositeProduct> compositeProductList список сущностей у которых
	 *        берутся данные для модели
	 * @param Map<Long, Integer> idsPrice мапа используеться для пpисвоения цены по
	 *        id
	 */
	public Map<CompositeProductModel, Integer> getCompositeProductModel(List<CompositeProduct> compositeProductList,
			Store store) {

		Map<Long, Integer> idsPrice = this.getIdPrice(store.getStorage().getProductPrice());

		Map<CompositeProductModel, Integer> productPriceMap = new HashMap<>();
		compositeProductList.stream().forEach(prod -> {
			productPriceMap.put(
					CompositeProductModel.builder().id(prod.getId()).name(prod.getName()).group(prod.getGroup())
							.propertiesProduct(prod.getPropertiesProduct().toString()).build(),
					idsPrice.get(prod.getId()));
		});
		return productPriceMap;
	}

	/**
	 * метод форминует мапу из копмозитного продукта и цены
	 * 
	 * @param Store store обьект магазина в котором будет происходить обновления
	 * @param       Map<Long, Integer> updateIdsPrice мапа с новыми ценами: id-новая
	 *              цена
	 * @param       List<CompositeProduct> compositeProductList список совпадений
	 */
	public Store updateIdsPrice(Store store, Map<Long, Integer> updateIdsPrice,
			List<CompositeProduct> compositeProductList) {
		// текущие продукты на ТТ
		Map<Long, Integer> currentIdsPrice = this.getIdPrice(store.getStorage().getProductPrice());

		// переменная хранит старые записи
		StringBuilder oldPrice = new StringBuilder();
		// дата изменения цены
		// Date curentDate = new Date();
		Calendar curentDate = new GregorianCalendar();
		// для каждого совпадения из compositeProductList
		compositeProductList.stream().forEach(prod -> {
			// проверяем если продукт с новой ценой числиться за текущей торговой точкой
			// то обновляем его цену
			if (currentIdsPrice.containsKey(prod.getId())) {
				// записываем id продукта его старую
				// цену и дату изменения
				oldPrice.append(prod.getId() + ":" + currentIdsPrice.get(prod.getId()) + "price"
						+ curentDate.getTimeInMillis() + "date");
				// записываем новую цену продукта мапу отображающую асортемент-цена текущей ТТ
				currentIdsPrice.put(prod.getId(), updateIdsPrice.get(prod.getId()));
			}
		});
		// преобразуем мапу с обновленной ценой в строку
		StringBuilder newPrice = new StringBuilder();
		currentIdsPrice.forEach((key, value) -> {
			newPrice.append(key + ":" + value + "price");
		});
		// и сохраняем ее в ТТ
		store.getStorage().setProductPrice(newPrice.toString());
		// проверяем не пустая ли строка с изменениями в цене и записываем в нее новые
		// данные об изменениях
		if (store.getStorage().getPriceUpdate() != null) {
			oldPrice.append(store.getStorage().getPriceUpdate());
		}
		store.getStorage().setPriceUpdate(oldPrice.toString());

		return store;
	}

	/**
	 * метод форминует мапу из копмозитного продукта и цены
	 * 
	 * @param Store store обьект магазина в котором будет происходить удаления
	 * @param       List<Long> delete список id на удаление
	 * @param       List<CompositeProduct> compositeProductList список совпадений
	 */
	public Store deleteIdsPrice(Store store, List<Long> delete, List<CompositeProduct> compositeProductList) {

		Map<Long, Integer> currentIdsPrice = this.getIdPrice(store.getStorage().getProductPrice());

		// обьект будех хранить id-цена удаленного товара
		StringBuilder remove = new StringBuilder();
		// Date curentDate = new Date();
		Calendar curentDate = new GregorianCalendar();
		compositeProductList.stream().forEach(prod -> {
			// записываем данные по удаленному продукту
			remove.append(prod.getId() + ":" + currentIdsPrice.get(prod.getId()) + "price"
					+ curentDate.getTimeInMillis() + "date");
			// по ключу удаляем продукт из текущей ТТ
			currentIdsPrice.remove(prod.getId());
		});
		// форматируем в строку асортимент с уже удаленными записями
		StringBuilder newPrice = new StringBuilder();
		currentIdsPrice.forEach((key, value) -> {
			newPrice.append(key + ":" + value + "price");
		});
		// сохраняем для текущего магазина
		store.getStorage().setProductPrice(newPrice.toString());
		// проверяем на наличие ранее удаленных прдуктов и добавляем к ним новые
		// удаления
		if (store.getStorage().getProductDelete() != null) {
			remove.append(store.getStorage().getProductDelete());
		}
		store.getStorage().setProductDelete(remove.toString());

		return store;
	}

	/**
	 * метод принимает список композитных продуктов
	 *  парсит и добавляет id продуктов в Set ids
	 */
	public Set<Long> parseCompositeProductIds(List<CompositeProduct> compositeProducts) {
		// productServiceImpl
		Set<Long> ids = new HashSet<>();
		compositeProducts.stream().forEach(compositeProduct -> {
			for (String str : compositeProduct.getProductExpend().split(":[0-9]+rate|rate*")) {
				ids.add(Long.parseLong(str));
			}
		});
		return ids;
	}

}
