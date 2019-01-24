package ua.squirrel.user.product.entity;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CompositeProductModel {

	private long id;

	private String name;

	private Map<Long, Integer> productsConsumption;
	
	private Map<ProductModel, Integer> productsComposite;
}
