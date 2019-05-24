package ua.squirrel.user.controller.store.consignment;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.product.ProductModel;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.user.entity.store.consignment.ConsignmentModel;
import ua.squirrel.user.entity.store.consignment.ConsignmentSearchModel;
import ua.squirrel.user.entity.store.consignment.ConsignmentStatus;
import ua.squirrel.user.service.product.ProductServiceImpl;
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.user.service.store.consignment.ConsignmentServiceImpl;
import ua.squirrel.user.service.store.consignment.status.ConsignmentStatusServiceImpl;
import ua.squirrel.user.utils.ConsignmentUtil;
import ua.squirrel.user.utils.ProductUtil;
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
	private ProductServiceImpl productServiceImpl;
	@Autowired
	private ConsignmentServiceImpl consignmentServiceImpl;
	@Autowired
	private ConsignmentStatusServiceImpl consignmentStatusServiceImpl;
	@Autowired
	private ConsignmentUtil consignmentUtil;
	@Autowired
	private ProductUtil productUtil;
	
	
	@GetMapping("{storeId}/{consignmentId}")
	public List<ProductModel> getСonsignmentData(Authentication authentication, @PathVariable("storeId") Long storeId ,
			@PathVariable("consignmentId") Long consignmentId)  throws NotFoundException {
		
			log.info("LOGGER: get Consignment for search value");
			User user = userServiceImpl.findOneByLogin("test1").get();
			Store store = getCurrentStore(user, storeId);
			
			Consignment consignment = consignmentServiceImpl.findOneByIdAndStore(consignmentId, store)
					.orElseThrow(() -> new NotFoundException("Status not found"));
			// select * from Consignments where join user.id = store.id
			Set<Long> prodIds = productUtil.spliteIds(consignment.getConsignmentData(), "[price]*:*quantity[0-9]*price");
				 
			return productUtil.createProductPriceQuantityModelList(
						productServiceImpl.findAllByUserAndIdIn(user, prodIds) , consignment.getConsignmentData());
		
	}
	
	/**
	 * Метод отдает результаты поиска согласно данным запроса
	 * из обьекта ConsignmentSearchModel
	 * */
	@PostMapping 
	public List<ConsignmentModel> getСonsignmentSearch( Authentication authentication,
			@RequestBody ConsignmentSearchModel consignmentSearchModel) throws NotFoundException {
		log.info("LOGGER: get Consignment for search value");
		User user = userServiceImpl.findOneByLogin("test1").get();
		Store store = getCurrentStore(user ,consignmentSearchModel.getStoreId()) ;
		
		List<Consignment> resultList = null;
		if(consignmentSearchModel.getMeta() == null || consignmentSearchModel.getMeta().isEmpty()) {
		resultList = consignmentServiceImpl.findByStoreAndConsignmentStatusAndDateBetween(store,
				getConsignmentStatus(consignmentSearchModel.getConsignmentStatus()),
				consignmentUtil.convertDate(consignmentSearchModel.getDateStart()),
				consignmentUtil.convertDate(consignmentSearchModel.getDateFinish()));
		}else {
		 resultList = consignmentServiceImpl.findByStoreAndConsignmentStatusAndMetaIgnoreCaseContainingAndDateBetween(
				 	store,
					getConsignmentStatus(consignmentSearchModel.getConsignmentStatus()),
					consignmentSearchModel.getMeta(),
					consignmentUtil.convertDate(consignmentSearchModel.getDateStart()),
					consignmentUtil.convertDate(consignmentSearchModel.getDateFinish()));
		}
		
	 
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