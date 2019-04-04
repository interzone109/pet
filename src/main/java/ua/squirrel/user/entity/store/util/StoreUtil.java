package ua.squirrel.user.entity.store.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.squirrel.user.entity.product.composite.CompositeProductModel;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.service.product.CompositeProductServiceImpl;
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.web.entity.user.User;

@Component
public class StoreUtil {
	@Autowired
	private CompositeProductServiceImpl compositeProductServiceImpl;
	@Autowired
	private StoreServiceImpl storeServiceImpl;
	
	// метод получает пользоветаеля и строку с ид и ценой
	// разбивает ее и формирует список моделей CompositeProductModel
	public List<CompositeProductModel> getCompositeProductPrice(User user ,String compositeProductPrice){
		
		List<CompositeProductModel> compositeProducts = new ArrayList<>();
		if(compositeProductPrice!= null) {
		String[] productPrice = compositeProductPrice.split("price");
		Map<Long, Integer> idsPrice = new HashMap<>();

		for (int i = 0; i < productPrice.length; i++) {
			String[] parse = productPrice[i].split(":");
			idsPrice.put(Long.parseLong(parse[0]), Integer.parseInt(parse[1]));
		}
		compositeProductServiceImpl.findAllByUserAndIdIn(user, idsPrice.keySet()).forEach(compositeProd->{
			compositeProducts.add(CompositeProductModel.builder()
					.id(compositeProd.getId())
					.name(compositeProd.getName())
					.group(compositeProd.getGroup())
					.propertiesProduct(idsPrice.get(compositeProd.getId()).toString())
					.measureProduct(compositeProd.getMeasureProduct().getMeasure())
					.build()
					);
			});

		}
		return compositeProducts ;
		
	}
	
	// метод добавляет новые продукты с ценой к текущему магазину
	public List<CompositeProductModel> addCompositeProductPrice(User user,Map<Long, Integer> newProductPrice, Store store){
		List<CompositeProductModel> compositeProducts = new ArrayList<>();
		
		
		Map<Long, Integer> idsPrice = new HashMap<>();
		String[] productPrice = store.getProductPrice().split("price");
		for (int i = 0; i < productPrice.length; i++) {
			String[] parse = productPrice[i].split(":");
			idsPrice.put(Long.parseLong(parse[0]), Integer.parseInt(parse[1]));
		}
		//удаляем дублиат из входных данных 
		newProductPrice.keySet().forEach(id->{
			if(idsPrice.containsKey(id)) {
				newProductPrice.remove(id);
			}
		});
		
		StringBuilder str = new StringBuilder();
		if(newProductPrice.keySet().size() > 0) {
			newProductPrice.keySet().forEach(id->{
				str.append(id +":"+newProductPrice.get(id)+"price");
			});
		}
		
		str.append( store.getProductPrice());
		store.setProductPrice(str.toString());
		
		storeServiceImpl.save(store);
		
		compositeProductServiceImpl.findAllByUserAndIdIn(user, newProductPrice.keySet()).forEach(compositeProd->{
			compositeProducts.add(CompositeProductModel.builder()
					.id(compositeProd.getId())
					.name(compositeProd.getName())
					.group(compositeProd.getGroup())
					.propertiesProduct(newProductPrice.get(compositeProd.getId()).toString())
					.measureProduct(compositeProd.getMeasureProduct().getMeasure())
					.build()
					);
			});

		return compositeProducts;
	}
	

	

	public List<CompositeProductModel> updateCompositeProductPrice(User user, Map<Long, Integer> updateProductPrice,
			Store store) {
	List<CompositeProductModel> compositeProducts = new ArrayList<>();
		
		
		Map<Long, Integer> idsPrice = new HashMap<>();
		String[] productPrice = store.getProductPrice().split("price");
		for (int i = 0; i < productPrice.length; i++) {
			String[] parse = productPrice[i].split(":");
			idsPrice.put(Long.parseLong(parse[0]), Integer.parseInt(parse[1]));
		}
		
		StringBuilder updatePrice = new StringBuilder();
		Date currentDate = new Date();
		//проходимся по ид продуктов на обновление
		updateProductPrice.keySet().forEach(id->{
			if(idsPrice.containsKey(id)) {	//находим совпадение и записываем старую цену и время обновления
				updatePrice.append(id+":"+currentDate.getTime()+"date"+idsPrice.get(id)+"price");//format 9:1231332324date12500price
				idsPrice.remove(id);
				idsPrice.put(id, updateProductPrice.get(id));//добавляем новую цену
				
				compositeProducts.add(CompositeProductModel.builder()
						.id(id)
						.propertiesProduct(updateProductPrice.get(id).toString())
						.build()
						);
			}
		});
		
		// записываем обратно цены и продукты
		StringBuilder updatePriceProduct = new StringBuilder();
		idsPrice.keySet().forEach(id->{
			updatePriceProduct.append(id +":"+idsPrice.get(id)+"price");
		});
		
		store.setProductPrice(updatePriceProduct.toString());
	
		if(store.getPriceUpdate() == null || store.getPriceUpdate().isEmpty()) {
			store.setPriceUpdate(updatePrice.toString());
		}else {
			updatePrice.append(store.getPriceUpdate());
			store.setPriceUpdate(updatePrice.toString());
		}
		
		storeServiceImpl.save(store);
		
		return compositeProducts;
	}
	
	
	
	
	
	
	
	
	
	
}
