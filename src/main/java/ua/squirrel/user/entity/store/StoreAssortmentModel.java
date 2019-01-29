package ua.squirrel.user.entity.store;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import ua.squirrel.user.entity.product.ProductModel;
import ua.squirrel.user.entity.product.composite.CompositeProductModel;

@Builder
@Data
public class StoreAssortmentModel {
	private List<ProductModel> productModels;
	private List<CompositeProductModel> compositeProductModels;
}
