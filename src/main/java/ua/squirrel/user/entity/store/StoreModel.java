package ua.squirrel.user.entity.store;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import ua.squirrel.user.entity.product.composite.CompositeProductModel;

@Builder
@Data
public class StoreModel {
	private long id;

	private String address;

	private String description;
	
	private List<CompositeProductModel> compositeProduct;
}
