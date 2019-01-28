package ua.squirrel.user.entity.store;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StoreModel {
	private long id;

	private String address;

	private String description;
	
	
}
