package ua.squirrel.user.entity.store.spending;


import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SpendModel {
	
	private long id;
	
	private String name ;
	
	private int cost ;
	
	private int interval ;
	
	private boolean isRegular ;

	private Date date;
	
	private long storId;

}
