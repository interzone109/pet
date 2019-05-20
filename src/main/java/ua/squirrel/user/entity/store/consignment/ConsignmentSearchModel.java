package ua.squirrel.user.entity.store.consignment;


import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class ConsignmentSearchModel {
	
	private long storeId;

	private String dateStart;
	
	private String dateFinish;

	private String consignmentStatus;
}
