package ua.squirrel.user.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import ua.squirrel.user.entity.product.Product;
import ua.squirrel.user.entity.product.ProductModel;

@Component
public class CompositeProductUtil extends SmallOneUtil{
	
	//метод получает список CompositeProduct и конвертирует его в CompositeProductModel
	//а значения из mapValue    записываются в описание объекта
	public List<ProductModel> convertToProductModelDescription(List<Product> products , Map<Long, Integer> mapValue){
		List<ProductModel> result = new ArrayList<>();
		
		products.forEach(product -> {
			ProductModel prodModel = ProductModel.builder()
					.id(product.getId())
					.name(product.getName())
					.description( mapValue.get(product.getId()).toString())
					.group(product.getGroup())
					.measureProduct(product.getMeasureProduct().getMeasure())
					.build();
			result.add(prodModel);

		});
		
		return result ;
	}
	
	

}
