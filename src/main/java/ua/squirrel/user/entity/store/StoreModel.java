package ua.squirrel.user.entity.store;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import ua.squirrel.user.entity.employee.EmployeeModel;

@Builder
@Data
public class StoreModel {
	private long id;

	private String address;

	private String description;
	
	private List<EmployeeModel> employeeModel;
	

}
