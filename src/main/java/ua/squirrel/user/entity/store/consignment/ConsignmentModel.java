package ua.squirrel.user.entity.store.consignment;

import java.util.Calendar;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ConsignmentModel {
	private long id;

	private Calendar date;

	private boolean isApproved;

	private String consignmentStatus;
	
	private String meta;
}
