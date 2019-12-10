package ua.squirrel.user.service.employee;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.user.entity.employee.Employee;
import ua.squirrel.web.entity.account.AccountApp;
import ua.squirrel.web.entity.user.User;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private EmployeeRepository employeeRepository  ;
	
	@Autowired
	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@Override
	public List<Employee> findAllByUser(User user) {
		return employeeRepository.findAllByUser(user);
	}

	@Override
	public Employee save(Employee employee) {
		return employeeRepository.save(employee);
	}
	
	public Optional<Employee> findOneByIdAndUser(Long id, User user){
		return employeeRepository.findOneByIdAndUser(id, user);
	}
	public Optional<Employee> findOneById(Long id){
		return employeeRepository.findById(id);
	}

	public  Optional<Employee> findOneByAccountApp( AccountApp accountApp) {
		return employeeRepository.findOneByAccountApp( accountApp);
	}

}
