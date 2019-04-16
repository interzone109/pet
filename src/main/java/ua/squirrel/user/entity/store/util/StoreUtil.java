package ua.squirrel.user.entity.store.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.squirrel.user.entity.product.composite.CompositeProductModel;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.user.service.product.CompositeProductServiceImpl;
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.user.service.store.consignment.ConsignmentServiceImpl;
import ua.squirrel.user.service.store.consignment.status.ConsignmentStatusServiceImpl;
import ua.squirrel.web.entity.user.User;

@Component
public class StoreUtil {
	@Autowired
	private CompositeProductServiceImpl compositeProductServiceImpl;
	@Autowired
	private StoreServiceImpl storeServiceImpl;
	@Autowired
	private ConsignmentServiceImpl consignmentServiceImpl;
	@Autowired
	private ConsignmentStatusServiceImpl consignmentStatusServiceImpl;

	// метод получает пользоветаеля и строку с ид и ценой
	// разбивает ее и формирует список моделей CompositeProductModel
	public List<CompositeProductModel> getCompositeProductPrice(User user, String compositeProductPrice) {

		List<CompositeProductModel> compositeProducts = new ArrayList<>();
		if (compositeProductPrice != null) {
			String[] productPrice = compositeProductPrice.split("price");
			Map<Long, Integer> idsPrice = new HashMap<>();

			for (int i = 0; i < productPrice.length; i++) {
				String[] parse = productPrice[i].split(":");
				idsPrice.put(Long.parseLong(parse[0]), Integer.parseInt(parse[1]));
			}
			compositeProductServiceImpl.findAllByUserAndIdIn(user, idsPrice.keySet()).forEach(compositeProd -> {
				compositeProducts.add(CompositeProductModel.builder().id(compositeProd.getId())
						.name(compositeProd.getName()).group(compositeProd.getGroup())
						.propertiesProduct(idsPrice.get(compositeProd.getId()).toString())
						.measureProduct(compositeProd.getMeasureProduct().getMeasure()).build());
			});

		}
		return compositeProducts;

	}

	// метод добавляет новые продукты с ценой к текущему магазину
	public List<CompositeProductModel> addCompositeProductPrice(Map<Long, Integer> newProductPrice, Store store) {
		List<CompositeProductModel> compositeProducts = new ArrayList<>();

		Map<Long, Integer> idsPrice = new HashMap<>();
		boolean isStoreEmpty = (store.getProductPrice() == null || store.getProductPrice().isEmpty());
		if (!isStoreEmpty) {
			String[] productPrice = store.getProductPrice().split("price");
			for (int i = 0; i < productPrice.length; i++) {
				String[] parse = productPrice[i].split(":");
				idsPrice.put(Long.parseLong(parse[0]), Integer.parseInt(parse[1]));
			}

		}

		// удаляем дублиат из входных данных
		Set<Long> dublicate = new HashSet<>();
		newProductPrice.keySet().forEach(id -> {
			if (idsPrice.containsKey(id)) {// если в магазине есть продукт с таким же айди
				dublicate.add(id);// то удаляем его из нового списка
			}
		});
		dublicate.forEach(id->{
			newProductPrice.remove(id);
		});
		
		// формируем строку с новыми композитными продуктами и их ценой
		StringBuilder str = new StringBuilder();
		if (newProductPrice.keySet().size() > 0) {
			newProductPrice.keySet().forEach(id -> {
				str.append(id + ":" + newProductPrice.get(id) + "price");
			});
		}
		// добавляем сформировоную строку в магазин
		if (!isStoreEmpty) {
			str.append(store.getProductPrice());
		}

		store.setProductPrice(str.toString());

		Set<Long> idsSet = new HashSet<>();// список все ингридиентов которые используются в композитных продуктах
		compositeProductServiceImpl.findAllByUserAndIdIn(store.getUser(), newProductPrice.keySet())
				.forEach(compositeProd -> {
					String[] ids = compositeProd.getProductExpend().split(":[0-9]+rate|rate*");
					for (String id : ids) {
						idsSet.add(Long.parseLong(id));
					}
				});
		// метод добавляет ингридиенты продукта к остаткам магазина
		String storeRes = addLeftoversToStore(idsSet, store.getProductLeftovers());
		store.setProductLeftovers(storeRes);

		
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		Optional<Consignment> optionalCons = consignmentServiceImpl.findOneByDateAndStore(cal, store);

		
		Consignment consignment;
		StringBuilder consignmentData = new StringBuilder();
		if (optionalCons.isPresent()) {
			consignment = optionalCons.get();
			consignmentData.append(consignment.getConsignmentData());
		} else {
			consignment = new Consignment();
			consignment.setDate(cal);
			consignment.setApproved(false);
			consignment.setMeta("add new ingridient");
			consignment.setStore(store);
			consignment.setConsignmentStatus(consignmentStatusServiceImpl.findOneByName("ARRIVAL").get());
		}
		
		idsSet.forEach(id->{
			consignmentData.append(id + ":0quantity");
		});
		consignment.setConsignmentData(consignmentData.toString());
		
		

		 store.getConsignment().add(consignment);

		storeServiceImpl.save(store);

		compositeProductServiceImpl.findAllByUserAndIdIn(store.getUser(), newProductPrice.keySet())
				.forEach(compositeProd -> {
					compositeProducts.add(CompositeProductModel.builder().id(compositeProd.getId())
							.name(compositeProd.getName()).group(compositeProd.getGroup())
							.propertiesProduct(newProductPrice.get(compositeProd.getId()).toString())
							.measureProduct(compositeProd.getMeasureProduct().getMeasure()).build());
				});

		return compositeProducts;
	}

	// метод обновляет цену для выбраного продукта
	public List<CompositeProductModel> updateCompositeProductPrice(Map<Long, Integer> updateProductPrice, Store store) {
		List<CompositeProductModel> compositeProducts = new ArrayList<>();

		Map<Long, Integer> idsPrice = new HashMap<>();
		String[] productPrice = store.getProductPrice().split("price");
		for (int i = 0; i < productPrice.length; i++) {
			String[] parse = productPrice[i].split(":");
			idsPrice.put(Long.parseLong(parse[0]), Integer.parseInt(parse[1]));
		}

		StringBuilder updatePrice = new StringBuilder();
		Date currentDate = new Date();
		// проходимся по ид продуктов на обновление
		updateProductPrice.keySet().forEach(id -> {
			if (idsPrice.containsKey(id)) { // находим совпадение и записываем старую цену и время обновления
				updatePrice.append(id + ":" + currentDate.getTime() + "date" + idsPrice.get(id) + "price");// format
																											// 9:1231332324date12500price
				idsPrice.remove(id);
				idsPrice.put(id, updateProductPrice.get(id));// добавляем новую цену

				compositeProducts.add(CompositeProductModel.builder().id(id)
						.propertiesProduct(updateProductPrice.get(id).toString()).build());
			}
		});

		// записываем обратно цены и продукты
		StringBuilder updatePriceProduct = new StringBuilder();
		idsPrice.keySet().forEach(id -> {
			updatePriceProduct.append(id + ":" + idsPrice.get(id) + "price");
		});

		store.setProductPrice(updatePriceProduct.toString());

		if (store.getPriceUpdate() == null || store.getPriceUpdate().isEmpty()) {
			store.setPriceUpdate(updatePrice.toString());
		} else {
			updatePrice.append(store.getPriceUpdate());
			store.setPriceUpdate(updatePrice.toString());
		}

		storeServiceImpl.save(store);

		return compositeProducts;
	}

	// метод принимает новые айти и строку с имеющимеся остатками
	// и добавляет новые ингридиенты к старым (остатки уже имеющихся ингридиентов не
	// меняются)
	public String addLeftoversToStore(Set<Long> productId, String sotreLeftovers) {

		Map<Long, Integer> currentIdsQuantity = new HashMap<>();

		if (sotreLeftovers != null && !sotreLeftovers.isEmpty()) {// если на магазине имеются остатки то преобразуем их
																	// в значения
			String[] leftovers = sotreLeftovers.split("quantity");// рвзбиваем строку на подстроки - 1:12

			for (int i = 0; i < leftovers.length; i++) {// проходимся по каждой подстроке
				String[] elem = leftovers[i].split(":");// разбиваем строку на масив из двух еслементон elem[0] - id
														// elem[1] - quantity
				currentIdsQuantity.put(Long.parseLong(elem[0]), Integer.parseInt(elem[1]));// ложим все данные в мапу
			}

		}
		productId.stream().forEach(id -> {
			if (!currentIdsQuantity.containsKey(id)) {
				currentIdsQuantity.put(id, 0);
			}
		});

		StringBuilder productRes = new StringBuilder();
		currentIdsQuantity.keySet().forEach(id -> {
			productRes.append(id + ":" + currentIdsQuantity.get(id) + "quantity");
		});

		return productRes.toString();
	}

}
