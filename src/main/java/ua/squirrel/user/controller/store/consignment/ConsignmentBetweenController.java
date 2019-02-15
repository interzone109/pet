package ua.squirrel.user.controller.store.consignment;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.consignment.ConsignmentModel;
import ua.squirrel.user.entity.store.consignment.util.ConsignmentUtil;
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.user.service.store.consignment.ConsignmentServiceImpl;
import ua.squirrel.user.service.store.consignment.status.ConsignmentStatusServiceImpl;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@RestController
@RequestMapping("/stores/{id}/consignment/{startDate}/{endDate}/{status}")
@Slf4j
public class ConsignmentBetweenController {
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private ConsignmentStatusServiceImpl consignmentStatusServiceImpl;
	@Autowired
	private ConsignmentServiceImpl consignmentServiceImpl;
	@Autowired
	private StoreServiceImpl storeServiceImpl;
	@Autowired
	private ConsignmentUtil consignmentUtil;
	/**
	 * Метод возращает список всех поставок
	 * за период либо за конкретную дату
	 */
	@GetMapping
	public List<ConsignmentModel> showAllStoreStorage(@PathVariable Long id
			,@PathVariable String status,@PathVariable Calendar startDate,@PathVariable Calendar endDate, Authentication authentication)
			throws NotFoundException {

		log.info("LOGGER: return consignment peeriod");
		User user = userServiceImpl.findOneByLogin("test1").get();
		
		return consignmentUtil.createListModel(
				consignmentServiceImpl.findByStorageAndConsignmentStatusAndDateBetween(
						getStore(user, id).getStorage()
						, consignmentStatusServiceImpl.findOneByName(status).get()
						, startDate, endDate));
	}
	
	
	
	
	
	private Store getStore(User user, Long id) throws NotFoundException {
		return storeServiceImpl.findOneByIdAndUser(id, user)
				.orElseThrow(() -> new NotFoundException("Store not found"));
	}

}
