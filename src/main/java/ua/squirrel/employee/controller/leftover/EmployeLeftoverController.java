package ua.squirrel.employee.controller.leftover;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.employee.Employee;
import ua.squirrel.user.entity.product.ProductModel;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.service.employee.EmployeeServiceImpl;
import ua.squirrel.user.service.product.ProductServiceImpl;
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.user.utils.StoreUtil;
import ua.squirrel.web.entity.user.User;


@RestController
@RequestMapping("/employee/stores/leftover")
@Slf4j
public class EmployeLeftoverController {
	 
	@Autowired
	private StoreServiceImpl storeServiceImpl;
	@Autowired
	private EmployeeServiceImpl employeeServiceImpl;
	@Autowired
	private ProductServiceImpl productServiceImpl;
	@Autowired
	private StoreUtil storeUtil;

	/**
	 * метод возращает список ингридиентов магазина на котором закреплен сотрудник
	 * который авторизирован в системе
	 * */
	@GetMapping
	public List<ProductModel> getLeftOvers( Authentication authentication) throws NotFoundException {

		log.info("LOGGER: get leftovers for  store");
		//Employee employee = employeeServiceImpl.findOneById(authentication.getId()).get();
		Employee employee = employeeServiceImpl.findOneById(1l).get();
		// делаем мапу ид количество
		/*Map<Long, Integer> idsQuantity = storeUtil.spliteIdsValue(getCurrentStore(employee.getUser() ,employee.getStore().getId()).getProductLeftovers(), "quantity[0-9]*price");
		
		List<ProductModel> productModel = new ArrayList<>();
		  productServiceImpl.findAllByUserAndIdIn(employee.getUser(), idsQuantity.keySet()).forEach(product->{
			  productModel.add(ProductModel.builder()
					  .id(product.getId())
					  .name(product.getName())
					  .description(idsQuantity.get(product.getId()).toString())
					  .measureProduct(product.getMeasureProduct().getMeasure())
					  .propertiesProduct(product.getPropertiesProduct().getName())
					  .group(product.getGroup())
					  .build()
					  );
		});
		 return productModel;*/
		return null;
		
	}
	
	private Store getCurrentStore(User user ,Long id) throws NotFoundException {
		return storeServiceImpl.findOneByIdAndUser(id, user)
				.orElseThrow(() -> new NotFoundException("Store not found"));
	}
}
