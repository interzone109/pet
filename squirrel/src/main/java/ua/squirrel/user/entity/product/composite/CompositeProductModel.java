package ua.squirrel.user.entity.product.composite;



import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CompositeProductModel {

	private long id;
	
	private String name;
	@NotNull
	private String group;
	
	@NotNull
	private String measureProduct;
	
	private int totalSumm ;
	
	private int sellQuantite ;
	
}
