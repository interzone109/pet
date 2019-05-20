package ua.squirrel.user.controller.store.consignment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.user.entity.store.consignment.ConsignmentModel;
import ua.squirrel.user.entity.store.consignment.ConsignmentSearchModel;
import ua.squirrel.user.entity.store.consignment.ConsignmentStatus;
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.user.service.store.consignment.ConsignmentServiceImpl;
import ua.squirrel.user.service.store.consignment.status.ConsignmentStatusServiceImpl;
import ua.squirrel.user.utils.ConsignmentUtil;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@RestController
@RequestMapping("/user/stores/сonsignment")
@Slf4j
public class ConsignmentController {
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private StoreServiceImpl storeServiceImpl;
	@Autowired
	private ConsignmentServiceImpl consignmentServiceImpl;
	@Autowired
	private ConsignmentStatusServiceImpl consignmentStatusServiceImpl;
	@Autowired
	private ConsignmentUtil consignmentUtil;

	@PostMapping 
	public List<ConsignmentModel> getAssortment( Authentication authentication,
			@RequestBody ConsignmentSearchModel consignmentSearchModel) throws NotFoundException {
		log.info("LOGGER: get Consignment for search value");
		User user = userServiceImpl.findOneByLogin("test1").get();
		Store store = getCurrentStore(user ,consignmentSearchModel.getStoreId()) ;
		
		
		List<Consignment> resultList = consignmentServiceImpl.findByStoreAndConsignmentStatusAndDateBetween(store,
				getConsignmentStatus(consignmentSearchModel.getConsignmentStatus()),
				consignmentUtil.convertDate(consignmentSearchModel.getDateStart()),
				consignmentUtil.convertDate(consignmentSearchModel.getDateFinish()));
		
	 
		return consignmentUtil.createConsignmentModelList(resultList) ;
		}
	
	
	
	private Store getCurrentStore(User user ,Long id) throws NotFoundException {
		return storeServiceImpl.findOneByIdAndUser(id, user)
				.orElseThrow(() -> new NotFoundException("Store not found"));
	}
	private ConsignmentStatus getConsignmentStatus(String name) throws NotFoundException {
		return consignmentStatusServiceImpl.findOneByName(name)
				.orElseThrow(() -> new NotFoundException("Status not found"));
	}
	
	
}