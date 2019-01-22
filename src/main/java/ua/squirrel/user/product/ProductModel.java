package ua.squirrel.user.product;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductModel {

	private long id;
	private String name;
	private String description;
	private long price;
	private GroupProduct groupProduct;
	private MeasureProduct measureProduct;

}
