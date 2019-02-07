package ua.squirrel.user.entity.employee;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmployeeModel {
	private long id;

	private String firstName;

	private String lastName;

	private long salary;

	private Long storeId;

	private String storeName;
}
