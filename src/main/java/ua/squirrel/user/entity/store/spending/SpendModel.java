package ua.squirrel.user.entity.store.spending;


import java.util.Calendar;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SpendModel {
	
	private long id;
	
	private String name ;
	
	private String description;
	
	private int cost ;
	
	private int interval ;
	
	private boolean isRegular ;

	private Calendar date;
	
	private long storId;

}
