package ua.squirrel.employee.controller.store;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.employee.Employee;
import ua.squirrel.user.entity.product.Product;
import ua.squirrel.user.entity.product.ProductModel;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.service.employee.EmployeeServiceImpl;


@RestController
@RequestMapping("/employee/stores/leftover")
@Slf4j
public class EmployeLeftoverController {

	@Autowired
	private EmployeeServiceImpl employeeServiceImpl;

	/**
	 * метод возращает список ингридиентов магазина на котором закреплен сотрудник
	 * который авторизирован в системе
	 * */
	@GetMapping
	public List<ProductModel> getLeftOvers( Authentication authentication) throws NotFoundException {

		log.info("LOGGER: get leftovers for  store");
		Employee employee = employeeServiceImpl.findOneById(1l).get();

		
		Store store = employee.getStore();
		List<ProductModel> productModel = new ArrayList<>();
		store.getStoreIngridientNode().forEach(productNode->{
			Product product = productNode.getProduct();
			  productModel.add(ProductModel.builder()
					  .id(product.getId())
					  .name(product.getName())
					  .description(Integer.toString(productNode.getLeftOvers()))
					  .measureProduct(product.getMeasureProduct().getMeasure())
					  .propertiesProduct(product.getPropertiesProduct().getName())
					  .group(product.getGroup())
					  .build()
					  );
		});
		 return productModel;
		
	}
	
	
}
