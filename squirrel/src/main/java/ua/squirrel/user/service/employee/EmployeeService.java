package ua.squirrel.user.service.employee;

import java.util.List;
import java.util.Optional;

import ua.squirrel.user.entity.employee.Employee;
import ua.squirrel.web.entity.account.AccountApp;
import ua.squirrel.web.entity.user.User;

public interface EmployeeService {

	List<Employee> findAllByUser(User user);

	Employee save(Employee employee);

	Optional<Employee> findOneByIdAndUser(Long id, User user);
	
	Optional<Employee> findOneByAccountApp(AccountApp accountApp);
	
	
}
