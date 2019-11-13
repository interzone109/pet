package ua.squirrel.user.entity.product;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductModel {

	private long id;
	private String name;
	private String description;
	private String group;
	private String partner ;
	private String propertiesProduct;
	private String measureProduct;
	private int rate;
	private int quantity;
}
