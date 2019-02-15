package ua.squirrel.user.entity.store.consignment;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ConsignmentProductModel {
	
	private long id ;
	
	private String name;
	
	private int quantity; 
	
	private int totalPrice;
	
	private int unitPrice;
}
