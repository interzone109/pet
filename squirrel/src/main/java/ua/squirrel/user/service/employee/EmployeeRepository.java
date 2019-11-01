package ua.squirrel.user.service.employee;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.user.entity.employee.Employee;
import ua.squirrel.web.entity.user.User;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	List<Employee> findAllByUser(User user);
	
	Optional<Employee> findOneByIdAndUser(Long id, User user);
}
