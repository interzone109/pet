package ua.squirrel.user.entity.employee;
import lombok.Builder;
import lombok.Data;
import ua.squirrel.user.entity.store.Store;

@Builder
@Data
public class EmployeeModel {
	private long id;
	
	private String firstName;
	
	private String lastName;
	
	private long salary;
	
	private Store store;
}
