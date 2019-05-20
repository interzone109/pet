package ua.squirrel.user.entity.store.consignment;


import java.util.List;

import lombok.Builder;
import lombok.Data;
import ua.squirrel.user.entity.product.ProductModel;

@Builder
@Data
public class ConsignmentModel {
	private long id;
	
	private String date;
	
	private List<ProductModel> productPrice;

	private boolean isApproved;

	private String consignmentStatus;
	
	private String meta;
}
