package ua.squirrel.user.entity.employee;


import lombok.Builder;
import lombok.Data;

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
	
	private String login;
	
	private String password;
	
	private String hairingDate;
	
	private String status;
}
