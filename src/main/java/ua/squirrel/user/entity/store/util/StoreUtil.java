package ua.squirrel.user.entity.store.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.squirrel.user.entity.product.composite.CompositeProductModel;
import ua.squirrel.user.service.product.CompositeProductServiceImpl;
import ua.squirrel.web.entity.user.User;

@Component
public class StoreUtil {
	@Autowired
	private CompositeProductServiceImpl compositeProductServiceImpl;
	
	
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
}
