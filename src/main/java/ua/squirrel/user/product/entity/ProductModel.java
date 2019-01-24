package ua.squirrel.user.product.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductModel {

	private long id;
	private String name;
	private String description;
	private String group;
	private PropertiesProduct propertiesProduct;
	private MeasureProduct measureProduct;

}
