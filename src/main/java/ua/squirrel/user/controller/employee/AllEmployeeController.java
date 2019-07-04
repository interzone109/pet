package ua.squirrel.user.controller.employee;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.employee.Employee;
import ua.squirrel.user.entity.employee.EmployeeModel;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.service.employee.EmployeeServiceImpl;
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.user.utils.SmallOneUtil;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.registration.RoleService;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@RestController
@RequestMapping("user/rest/employee")
@Slf4j
public class AllEmployeeController {
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private EmployeeServiceImpl employeeServiceImpl;
	@Autowired
	private StoreServiceImpl storeServiceImpl;
	@Autowired
	private RoleService roleService;
	@Autowired
	private SmallOneUtil smallOneUtil;

	/**
	 * метод возращает лист моделей всех работников
	 * 
	 */
	@GetMapping
	public List<EmployeeModel> getAllEmployee(Authentication authentication) {
		log.info("LOGGER: show all employees ");

		User user = userServiceImpl.findOneByLogin("test1").get();
		return buildEmployeeModel(user);
	}
	/**
	 * метод сохраняет данные о новом работнике
	 * @throws NotFoundException 
	 */
	@PostMapping
	public EmployeeModel addEmployee(@RequestBody EmployeeModel newEmployeeModel, Authentication authentication) throws NotFoundException {
		log.info("LOGGER: add new employees ");
		Employee employee = new Employee();
		User user = userServiceImpl.findOneByLogin("test1").get();

		employeeServiceImpl.save(putOrPostEmployee(employee, newEmployeeModel, user));
		return employeeBuild(employee);
	}
	
	
	@PutMapping
	@RequestMapping("/{id}/edit")
	public EmployeeModel updateEmployee(@RequestBody EmployeeModel newEmployeeModel,
			@PathVariable Long id ,Authentication authentication) throws NotFoundException {
		log.info("LOGGER: update current employees ");

		User user = userServiceImpl.findOneByLogin("test1").get();
		Employee employee = getEmployee(user, id);
	
		employeeServiceImpl.save(putOrPostEmployee(employee, newEmployeeModel, user));
		return employeeBuild(employee);
	}

	private Employee putOrPostEmployee(Employee employee ,EmployeeModel newEmployeeModel, User user ) throws NotFoundException {
		String login = newEmployeeModel.getLogin();
		String pass = newEmployeeModel.getPassword();
		String role = !(login == null & login.isEmpty()) && !( pass == null & pass.isEmpty()) 
				?"EMPLOYEE"
				:"EMPLOYEE_WITH_ACCESS";
		if(role=="EMPLOYEE") {
			login = "%autogenerate%"+newEmployeeModel.getStoreId()+ new Long(System.currentTimeMillis()).toString();
			pass = login;
		}
		
		employee.setFirstName(newEmployeeModel.getFirstName());
		employee.setLastName(newEmployeeModel.getLastName());
		employee.setSalary(newEmployeeModel.getSalary());
		employee.setCashBoxType(0);
		employee.setHairingDate(smallOneUtil.convertDate(newEmployeeModel.getHairingDate()));
		employee.setUser(user);
		employee.setLogin(login);
		employee.setPassword(pass);
		employee.setRole(roleService.findOneByName(role));
		employee.setStore(getStore(user , newEmployeeModel.getStoreId()));
		return employee;
	}
	
	private List<EmployeeModel> buildEmployeeModel(User user) {
		List<EmployeeModel> employeeModel = new ArrayList<>();

		employeeServiceImpl.findAllByUser(user).forEach(employee -> {

			employeeModel.add(employeeBuild(employee));
		});

		return employeeModel;
	}
	
	private EmployeeModel employeeBuild(Employee employee) {
		return EmployeeModel.builder()
				.id(employee.getId())
				.firstName(employee.getFirstName())
				.lastName(employee.getLastName())
				.cashBoxType(employee.getCashBoxType())
				.hairingDate(employee.getHairingDate().toString())
				.salary(employee.getSalary())
				.storeId( employee.getStore().getId())
				.storeName( employee.getStore().getAddress())
				.build();
	}
	
	private Store getStore(User user, Long storeId) throws NotFoundException {
		return	storeServiceImpl.findOneByIdAndUser(storeId, user)
				.orElseThrow(() -> new NotFoundException("Store not found"));
	}
	
	private Employee getEmployee (User user, Long id) throws NotFoundException {
		return employeeServiceImpl.findOneByIdAndUser(id, user)
				.orElseThrow(() -> new NotFoundException("Store not found"));
	}
}
