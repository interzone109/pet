package ua.squirrel.user.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import ua.squirrel.user.entity.product.Product;
import ua.squirrel.user.entity.product.ProductModel;
import ua.squirrel.user.entity.product.node.ProductMap;

@Component
public class CompositeProductUtil extends SmallOneUtil{
	
	@Deprecated
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
	
	// new convector
	public List<ProductModel> convertToProductModelFromMap(List<ProductMap> productsMap ){
		List<ProductModel> productsModel = new ArrayList<>();
		productsMap.forEach(productMap->{
			Product product = productMap.getProduct();
			productsModel.add(ProductModel.builder()
					.id(productMap.getId())
					.name(product.getName())
					.description( Integer.toString(productMap.getRate()))
					.group(product.getGroup())
					.measureProduct(product.getMeasureProduct().getMeasure())
					.build()
					);
		});
		return productsModel;
	}
	// new convector
	public ProductModel convertToProductPriceModel(ProductMap productMap ){	
		Product product = productMap.getProduct();
		return ProductModel.builder()
				.id(productMap.getId())
				.name(product.getName())
				.description( Integer.toString(productMap.getRate()))
				.group(product.getGroup())
				.measureProduct(product.getMeasureProduct().getMeasure())
				.build();
	}
	
	
	@Deprecated
	public ProductModel convertToProductModel(Product product ,  Integer value){	
		
		return   ProductModel.builder()
					.id(product.getId())
					.name(product.getName())
					.description(value.toString())
					.group(product.getGroup())
					.measureProduct(product.getMeasureProduct().getMeasure())
					.build();
	}

}
