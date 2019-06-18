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
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@RestController
@RequestMapping("user/employee")
@Slf4j
public class AllEmployeeController {
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private EmployeeServiceImpl employeeServiceImpl;
	@Autowired
	private StoreServiceImpl storeServiceImpl;
	

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
	public List<EmployeeModel> addEmployee(@RequestBody EmployeeModel newEmployeeModel, Authentication authentication) throws NotFoundException {
		log.info("LOGGER: add new employees ");

		User user = userServiceImpl.findOneByLogin("test1").get();
		Employee employee = new Employee();
		employee.setFirstName(newEmployeeModel.getFirstName());
		employee.setLastName(newEmployeeModel.getLastName());
		employee.setSalary(newEmployeeModel.getSalary());
		employee.setCashBoxType(0);
		employee.setUser(user);
		employee.setStore(getStore(user , newEmployeeModel.getStoreId()));

		employeeServiceImpl.save(employee);
		return buildEmployeeModel(user);
	}
	
	
	@PutMapping
	@RequestMapping("/employee/{id}/edit")
	public List<EmployeeModel> updateEmployee(@RequestBody EmployeeModel newEmployeeModel,
			@PathVariable Long id ,Authentication authentication) throws NotFoundException {
		log.info("LOGGER: add new employees ");

		User user = userServiceImpl.findOneByLogin("test1").get();
		Employee employee = getEmployee(user, id);
		
		employee.setFirstName(newEmployeeModel.getFirstName());
		employee.setLastName(newEmployeeModel.getLastName());
		employee.setSalary(newEmployeeModel.getSalary());
		employee.setCashBoxType(newEmployeeModel.getCashBoxType());
		employee.setWorkPeriod(newEmployeeModel.getWorkPeriod());
		employee.setWorkTime(newEmployeeModel.getWorkTime());
		employee.setUser(user);
		employee.setStore(getStore(user , newEmployeeModel.getStoreId()));

		employeeServiceImpl.save(employee);
		return buildEmployeeModel(user);
	}

	
	private List<EmployeeModel> buildEmployeeModel(User user) {
		List<EmployeeModel> employeeModel = new ArrayList<>();

		employeeServiceImpl.findAllByUser(user).forEach(employee -> {
			Store store = employee.getStore();
			Long storeId = new Long(0l);
			String storeAddres = new String("");
			if(store != null) {
			 storeId = employee.getStore().getId();
			 storeAddres = employee.getStore().getAddress();
			}

			employeeModel.add(EmployeeModel.builder()
					.id(employee.getId())
					.firstName(employee.getFirstName())
					.lastName(employee.getLastName())
					.cashBoxType(employee.getCashBoxType())
					.salary(employee.getSalary())
					.storeId( storeId)
					.storeName( storeAddres)
					.build());
		});

		return employeeModel;
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
