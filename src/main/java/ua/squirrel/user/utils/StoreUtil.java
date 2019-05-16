package ua.squirrel.user.utils;

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

import ua.squirrel.user.entity.product.composite.CompositeProduct;
import ua.squirrel.user.entity.product.composite.CompositeProductModel;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.user.service.product.CompositeProductServiceImpl;
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.user.service.store.consignment.ConsignmentServiceImpl;
import ua.squirrel.user.service.store.consignment.status.ConsignmentStatusServiceImpl;
import ua.squirrel.web.entity.user.User;

@Component
public class StoreUtil extends SmallOneUtil{
	@Autowired
	private CompositeProductServiceImpl compositeProductServiceImpl;
	@Autowired
	private StoreServiceImpl storeServiceImpl;
	@Autowired
	private ConsignmentServiceImpl consignmentServiceImpl;
	@Autowired
	private ConsignmentStatusServiceImpl consignmentStatusServiceImpl;

	// метод создает модель продукт - цена
	public List<CompositeProductModel> createProductPriceModel(List<CompositeProduct> products,  Map<Long, Integer> mapValue) {

		List<CompositeProductModel> compositeProducts = new ArrayList<>();
		products.forEach(compositeProd -> {
			compositeProducts.add(CompositeProductModel.builder()
					.id(compositeProd.getId())
					.name(compositeProd.getName())
					.group(compositeProd.getGroup())
					.propertiesProduct(mapValue.get(compositeProd.getId()).toString())
					.measureProduct(compositeProd.getMeasureProduct().getMeasure())
					.build());
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

	
	 public Consignment createOrUpdateConsigment(Store store ,Set<Long>newIdsSet , Consignment consignment, Calendar calendar) {

			if (consignment != null && consignment.isApproved()) {//если накладная проведена/подтверждена она не изменяемая
					return consignment;
				}
			else if(consignment == null){
				consignment = new Consignment();
				consignment.setDate(calendar);
				consignment.setApproved(false);
				consignment.setMeta("накладная создана - "+ new Date());
				consignment.setStore(store);
				consignment.setConsignmentStatus(consignmentStatusServiceImpl.findOneByName("ARRIVAL").get());
			}
			String data =  consignment.getConsignmentData();
			StringBuilder consignmentData = (data!= null)
					?new StringBuilder(consignment.getConsignmentData())
					:new StringBuilder();
					
			// удаляем дубликаты ид ингридиентов
			super.spliteIds(consignmentData.toString(), "[price]*:*quantity[0-9]price").forEach(id->{
				newIdsSet.remove(id);
			});
			
			newIdsSet.forEach(id->{
			consignmentData.append(id+":0quantity0price");
			});
			
			consignment.setConsignmentData(consignmentData.toString());
			store.getConsignment().add(consignment);
			return consignment;
	 }
	 //[price]*:[0-9]*quantity[0-9]price
	 
	 
	 
	 

	// метод принимает новые айти и строку с имеющимеся остатками
		// и добавляет новые ингридиенты к старым (остатки уже имеющихся ингридиентов не
		// меняются)
	public String addDefaultValue(Set<Long> ids, String strValue ,String regex) {
		 Map<Long, Integer>  currentIdsQuantity = super.spliteIdsValue(strValue, regex);
		
		//удаляем дубликаты
		currentIdsQuantity.keySet().forEach(id->{
			ids.remove(id);
		});
		//устанавливаем значение по умолчанию 0
		for (Long id : ids) {
			currentIdsQuantity.put(id, 0);
		}
		return super.concatIdsValueToString(currentIdsQuantity, regex);
	}
	

}
