package ua.squirrel.user.entity.product.composite;



import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CompositeProductModel {

	private long id;
	
	private String name;
	
	private String group;
	
	private String propertiesProduct;
	
	private String measureProduct;
	
	private int totalSumm ;
	
	private int sellQuantite ;
	
}
