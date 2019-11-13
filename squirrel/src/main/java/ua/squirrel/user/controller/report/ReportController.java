package ua.squirrel.user.controller.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.product.ProductModel;
import ua.squirrel.user.entity.report.ReportModel;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.user.entity.store.consignment.ConsignmentStatus;
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.user.service.store.consignment.ConsignmentServiceImpl;
import ua.squirrel.user.service.store.consignment.status.ConsignmentStatusServiceImpl;
import ua.squirrel.user.utils.ConsignmentUtil;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@RestController
@Slf4j
@RequestMapping("/user/report/data")
public class ReportController {
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	
	
	/**
	 * контроллер возращает отчет о оборачеваемых средствах расчитаным на основе
	 * приходных накладных
	 * */
	@Autowired
	private ConsignmentStatusServiceImpl consignmentStatusServiceImpl;
	@Autowired
	private ConsignmentServiceImpl consignmentServiceImpl;
	@Autowired
	private StoreServiceImpl storeServiceImpl;
	@Autowired
	private ConsignmentUtil consignmentUtil;
	@PostMapping("1")
	public  ResponseEntity<ReportModel> getFirstReport(Authentication authentication, @RequestBody ReportModel reportModel){
		log.info("LOGGER: get first report");
		User user = userServiceImpl.findOneByLogin(authentication.getName()).get();
		ConsignmentStatus status = consignmentStatusServiceImpl.findOneByName("ARRIVAL").get();
		List<Store> stores = storeServiceImpl.findAllByUser(user);
		List<Consignment> consignmentList = consignmentServiceImpl.findByStoreInAndIsApprovedAndConsignmentStatusAndDateBetween(
				stores , true, status,consignmentUtil.convertDate(reportModel.getDateStart()),  consignmentUtil.convertDate(reportModel.getDateEnd()));
		
		
		
		Map<Long,Map<Long,ProductModel> > storesMap = new HashMap<>();
		stores.forEach(store->{
			storesMap.put(store.getId(), new HashMap<Long,ProductModel>());
		});
		
		consignmentList.forEach(consigment->{
			consigment.getConsignmentNode().forEach(productNode->{
				Map <Long,ProductModel> map = storesMap.get(consigment.getStore().getId());
				ProductModel productModel;
				
				if(map.containsKey(productNode.getProduct().getId())) {
					productModel = map.get(productNode.getProduct().getId());
				}else {
					productModel = ProductModel.builder()
							.id(productNode.getProduct().getId())
							.name(productNode.getProduct().getName())
							.measureProduct(productNode.getProduct().getMeasureProduct().getMeasure())
							.description(consigment.getStore().getAddress())
							.rate(0)
							.quantity(0)
							.build() ;	
				}

				int val = (!productNode.getProduct().getMeasureProduct().getMeasure().equals("UNIT")) ?1000 :1;

				int totalSumm =productModel.getRate()+( productNode.getUnitPrice()*productNode.getQuantity()/val);
				int totalQuan = productModel.getQuantity()+(productModel.getQuantity()+productNode.getQuantity());
				
				productModel.setRate(totalSumm);
				productModel.setQuantity(totalQuan);
				
				map.put(productModel.getId(),productModel);
				
				storesMap.put(consigment.getStore().getId(), map);
			});
		});
		List<ProductModel> modelList = new ArrayList<>();
		storesMap.keySet().forEach(key->{
			modelList.addAll(storesMap.get(key).values());
		});
		
		reportModel.setProductReportData(modelList);
		
		return new ResponseEntity<>(reportModel, HttpStatus.OK);
	}
	
	@PostMapping("4")
	public  ResponseEntity<ReportModel> getFourReport(Authentication authentication, @RequestBody ReportModel reportModel){
		log.info("LOGGER: get first report");
		User user = userServiceImpl.findOneByLogin(authentication.getName()).get();
		ConsignmentStatus status = consignmentStatusServiceImpl.findOneByName("ARRIVAL").get();
		List<Store> stores = storeServiceImpl.findAllByUser(user);
		
		
	
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
}






