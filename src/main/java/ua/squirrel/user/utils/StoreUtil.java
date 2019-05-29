package ua.squirrel.user.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.squirrel.user.entity.product.composite.CompositeProduct;
import ua.squirrel.user.entity.product.composite.CompositeProductModel;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.user.service.store.consignment.status.ConsignmentStatusServiceImpl;

@Component
public class StoreUtil extends SmallOneUtil {
	@Autowired
	private StoreServiceImpl storeServiceImpl;
	@Autowired
	private ConsignmentStatusServiceImpl consignmentStatusServiceImpl;

	// метод создает модель продукт - цена
	public List<CompositeProductModel> createProductPriceModel(List<CompositeProduct> products,
			Map<Long, Integer> mapValue) {

		List<CompositeProductModel> compositeProducts = new ArrayList<>();
		products.forEach(compositeProd -> {
			compositeProducts.add(CompositeProductModel.builder().id(compositeProd.getId())
					.name(compositeProd.getName()).group(compositeProd.getGroup())
					.propertiesProduct(mapValue.get(compositeProd.getId()).toString())
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

	public Consignment createOrUpdateConsigment(Store store, Set<Long> newIdsSet, Consignment consignment,
			LocalDate calendar) {

		if (consignment != null && consignment.isApproved()) {// если накладная проведена/подтверждена она не изменяемая
			consignment = null;
		}
		if (consignment == null) {
			consignment = new Consignment();
			consignment.setDate(calendar);
			consignment.setApproved(false);
			consignment.setMeta("user:%:Поступление новых ингридиентов на " + store.getAddress());
			consignment.setStore(store);
			consignment.setConsignmentStatus(consignmentStatusServiceImpl.findOneByName("ARRIVAL").get());
		}
		String data = consignment.getConsignmentData();
		StringBuilder consignmentData = (data != null) ? new StringBuilder(consignment.getConsignmentData())
				: new StringBuilder();

		// удаляем дубликаты ид ингридиентов
		super.spliteIds(consignmentData.toString(), "[price]*:*quantity[0-9]*price").forEach(id -> {
			newIdsSet.remove(id);
		});

		newIdsSet.forEach(id -> {
			consignmentData.append(id + ":0quantity0price");
		});

		consignment.setConsignmentData(consignmentData.toString());
		store.getConsignment().add(consignment);
		return consignment;
	}
	// [price]*:[0-9]*quantity[0-9]*price

	// метод принимает id ингридиента и его количество и добавляет к остаткам на
	// магазине
	public void addStoreLeftovers(Store store, String consignmentData) {
		//получаем ид и количество ингридиентов из накладной
		Map<Long, Integer> consignmentIdsQuantity = super.spliteIdsValue(consignmentData,
				"[price]*:*quantity[0-9]*price");
		String leftovers = store.getProductLeftovers();
		//получаем текущие остатки на магазине
		Map<Long, Integer> storeIdsQuantity = (leftovers == null || leftovers.isEmpty()) 
				? new HashMap<>()
				: super.spliteIdsValue(leftovers, "quantity");
			
		consignmentIdsQuantity.keySet().forEach(id->{
			int newtQuantity = (storeIdsQuantity.containsKey(id))
					? storeIdsQuantity.get(id) + consignmentIdsQuantity.get(id)
					: consignmentIdsQuantity.get(id);
			storeIdsQuantity.put(id, newtQuantity);
			
		});
		StringBuilder storeLeftovers = new StringBuilder();
		storeIdsQuantity.keySet().forEach(id->{
			storeLeftovers.append(id+":"+storeIdsQuantity.get(id)+"quantity");
		});
		
		store.setProductLeftovers(storeLeftovers.toString());
	}

}
