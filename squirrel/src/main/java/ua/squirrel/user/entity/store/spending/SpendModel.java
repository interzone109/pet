package ua.squirrel.user.entity.store.spending;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SpendModel {
	
	private long id;
	
	private String name ;
	
	private int cost ;
	
	private int step;
	
	private int interval ;
	
	private boolean isOpen ;

	private String dateStart;
	
	private String dateEnd;
	
	private String storeName;
	
	private long storeId;

}
