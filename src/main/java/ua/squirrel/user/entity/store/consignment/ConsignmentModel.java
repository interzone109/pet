package ua.squirrel.user.entity.store.consignment;

import java.util.Calendar;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ConsignmentModel {
	private long id;

	private Calendar date;

	private List<ConsignmentProductModel> consignmentProductModel;

	private boolean isApproved;

	private boolean isConsignmentEmpty;

	private String consignmentStatus;
	
	private String meta;
}
