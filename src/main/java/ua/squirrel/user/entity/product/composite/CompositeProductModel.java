package ua.squirrel.user.entity.product.composite;

import java.util.Map;

import lombok.Builder;
import lombok.Data;
import ua.squirrel.user.entity.product.ProductModel;

@Builder
@Data
public class CompositeProductModel {

	private long id;

	private String name;
	
	private Map<ProductModel, Integer> products;
	
	private String group;
	
	private String propertiesProduct;
	
}
