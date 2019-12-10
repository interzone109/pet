package ua.squirrel.user.entity.employee;


import lombok.Builder;
import lombok.Data;
import ua.squirrel.web.entity.account.AccountAppModel;

@Builder
@Data
public class EmployeeModel {
	private long id;

	private String firstName;

	private String lastName;

	private Integer salary;

	private Long storeId;

	private Integer cashBoxType; 
	
	private String storeName;
	
	private AccountAppModel accountAppModel;
	
	private String hairingDate;
	
	private String status;
}
