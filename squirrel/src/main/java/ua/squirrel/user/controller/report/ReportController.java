package ua.squirrel.user.controller.report;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.employee.Employee;
import ua.squirrel.user.entity.product.ProductModel;
import ua.squirrel.user.entity.product.composite.CompositeProductModel;
import ua.squirrel.user.entity.product.node.ProductMap;
import ua.squirrel.user.entity.report.ReportModel;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.user.entity.store.consignment.ConsignmentStatus;
import ua.squirrel.user.entity.store.invoice.Invoice;
import ua.squirrel.user.entity.store.invoice.InvoiceModel;
import ua.squirrel.user.entity.store.spending.Spend;
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.user.service.store.consignment.ConsignmentServiceImpl;
import ua.squirrel.user.service.store.consignment.status.ConsignmentStatusServiceImpl;
import ua.squirrel.user.service.store.spending.SpendServiceImpl;
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
	
	
	@Autowired
	private SpendServiceImpl spendServiceImpl;
	@PostMapping("2")
	public  ResponseEntity<ReportModel> getSecondReport(Authentication authentication, @RequestBody ReportModel reportModel){
		log.info("LOGGER: get second report");
		User user = userServiceImpl.findOneByLogin(authentication.getName()).get();
		List<Store> stores = storeServiceImpl.findAllByUser(user);
		LocalDate dateStart = consignmentUtil.convertDate(reportModel.getDateStart());
		LocalDate dateEnd = consignmentUtil.convertDate(reportModel.getDateEnd());
		
		ConsignmentStatus status = consignmentStatusServiceImpl.findOneByName("CONSAMPTION").get();
		List<Consignment> consignments = consignmentServiceImpl.findByStoreInAndIsApprovedAndConsignmentStatusAndMetaIgnoreCaseContainingAndDateBetween
		(stores , true, status,"auto:%:", dateStart,  dateEnd);
		int daysInterval = dayInterval(dateStart, dateEnd);
		Map <Long, Integer> spends = spendCount( spendServiceImpl.findAllByUser(user), daysInterval);
		
		 List<InvoiceModel> invoiceModelList = new ArrayList<>();

		 stores.forEach(store->{
			int employeeCost = employeeCount(store.getEmployee() ,daysInterval );

			List<Invoice> storeInvoics = store.getInvoice().stream().filter(invoice->
			(invoice.getDate().isAfter(dateStart) || invoice.getDate().equals(dateStart))
			&& (invoice.getDate().isBefore(dateEnd) || invoice.getDate().equals(dateEnd))
			).collect(Collectors.toList());
			
			storeInvoics.forEach(invoice->{
				int consingmentSumm = totalSumOfConsingment(consignments.stream().filter(cons -> cons.getDate().equals(invoice.getDate()) 
									&& cons.getStore().getId() ==  invoice.getStore().getId() ).findFirst());
				
				int spend =0;
				if(spends.containsKey(store.getId())) {
					spend = spends.get(store.getId())/daysInterval;
				}
				
				 invoiceModelList.add(InvoiceModel.builder()
						 .dateStart(invoice.getDate().toString())
						 .cashBox(invoice.getCashBox())
						 .orderQuantity(spend)
						 .sellQuantity(consingmentSumm)
						 .dateEnd(store.getAddress())
						 .cashBoxStartDay(employeeCost)
						 .storeId(store.getId())
						 .build());
			 });
			});
		
		if(spends.containsKey(0l)) {
			 invoiceModelList.add(InvoiceModel.builder()
					 .dateStart("total spend")
					 .dateEnd("spends")
					 .cashBox( spends.get(0l))
					 .storeId(0l)
					 .build());
		}

		reportModel.setInvoiceData(invoiceModelList);
		return new ResponseEntity<>(reportModel, HttpStatus.OK);
	}
	
	private int dayInterval( LocalDate from, LocalDate to) {
		if(ChronoUnit.DAYS.between(from,to) == 0 ) {
			return 1;
			} 
		return (int)ChronoUnit.DAYS.between(from,to);
		
	}
	// считаем расходы на  сотрудников за период
	private int employeeCount(List<Employee> employee, int interval) {
		int weekEndDays = 8;
		int[] summ= new int[] {0};
		employee.forEach(empl->{
			 summ[0] += (empl.getSalary()/ (LocalDate.now().getDayOfMonth())-weekEndDays)*interval;
		});
		return summ[0];
	}
	//считаем стороние расходы за период
	private Map<Long, Integer> spendCount(List<Spend> spendList , int interval) {
		Map<Long, Integer> spends = new HashMap<>();
		
		spendList.forEach(spend->{
			int payInterval = 0;
			if(spend.getInterval() <0 ) {
				int monthCount = Math.abs(spend.getInterval()) ;
				payInterval = dayInterval(spend.getLasteDate().minusMonths(monthCount) , spend.getLasteDate());
			}else if (spend.getInterval() ==0 ) {
				payInterval = 1 ;
			}else {
				payInterval = spend.getInterval();
			}
			
			int oneDayCost = (spend.getStep() / payInterval);
			int days = spend.getInterval() == 0 ? 1 : interval;
			
			int summ = oneDayCost *  days;
			long id = (spend.getStore() == null) ?0l : spend.getStore().getId();
			
			 if(spends.containsKey(id)) {
				summ +=  spends.get(id);
				 spends.put(id, summ);
			}else {
				spends.put(id, summ);
			}
		});
		
		return spends;
	}

	private int totalSumOfConsingment(Optional<Consignment> consignmentOption) {
		int []result = new int[1];
		result[0] = 0;
 		if(consignmentOption.isPresent()) {
			consignmentOption.get().getConsignmentNode().forEach(node->{
			result[0] += node.getProduct().getMeasureProduct().getMeasure().equals("UNIT" ) 
					? node.getQuantity() * node.getUnitPrice() 
					:(node.getQuantity() * (node.getUnitPrice())/1000) ; 
		});
		}
 		
		return result[0];
	}
	
	
	@PostMapping("3")
	public  ResponseEntity<ReportModel> getThreeReport(Authentication authentication, @RequestBody ReportModel reportModel){
		log.info("LOGGER: get third report");
		User user = userServiceImpl.findOneByLogin(authentication.getName()).get();
		List<Store> stores = storeServiceImpl.findAllByUser(user);
		LocalDate dateStart = consignmentUtil.convertDate(reportModel.getDateStart());
		LocalDate dateEnd = consignmentUtil.convertDate(reportModel.getDateEnd());
		List<InvoiceModel> invoiceModelList = new ArrayList<>();

		 stores.forEach(store->{

			List<Invoice> storeInvoics = store.getInvoice().stream().filter(invoice->
			(invoice.getDate().isAfter(dateStart) || invoice.getDate().equals(dateStart))
			&& (invoice.getDate().isBefore(dateEnd) || invoice.getDate().equals(dateEnd))
			).collect(Collectors.toList());
			
			int totalSellumm[] = new int[] {0};
			storeInvoics.forEach(invoice->{
				totalSellumm[0]+=invoice.getCashBox();
			});
			invoiceModelList.add(InvoiceModel.builder()
			 .dateStart(dateStart.toString()+":"+dateEnd.toString())
			 .cashBox(totalSellumm[0])
			 .dateEnd(store.getAddress())
			 .build());
		 });
		 reportModel.setInvoiceData(invoiceModelList);
		return new ResponseEntity<>(reportModel, HttpStatus.OK);
	}
	
	
	@PostMapping("4")
	public  ResponseEntity<ReportModel> getFourReport(Authentication authentication, @RequestBody ReportModel reportModel){
		log.info("LOGGER: get four report");
		User user = userServiceImpl.findOneByLogin(authentication.getName()).get();
		List<Store> stores = storeServiceImpl.findAllByUser(user);
		
		 List<ProductModel> reportProductlList = getFirstReport(authentication, reportModel).getBody().getProductReportData();
		 Map<Long,Map<Long,CompositeProductModel> > storesMap = new HashMap<>();

			stores.forEach(store->{
				Map<Long,CompositeProductModel> mapProduct = new HashMap<>();
				store.getStoreCompositeProductNode().forEach(node->{
					int []totalSummOfIngridient = new int[1];
					totalSummOfIngridient[0]=0;
					List<ProductModel> igridientList = reportProductlList.stream().filter(
							reportProduct->node.getCompositeProduct().getProductMap()
							.stream().anyMatch(compositeIngridient->
							reportProduct.getId() == compositeIngridient.getProduct().getId() 
							)).collect(Collectors.toList());
					
					igridientList.forEach(model->{
					if(model.getDescription().equals(store.getAddress())) {
						ProductMap prod = node.getCompositeProduct().getProductMap().stream()
								.filter(prodNode->prodNode.getProduct().getId() == model.getId()).findFirst().get();
								
								totalSummOfIngridient[0] += prod.getRate() * (model.getRate() / model.getQuantity() )  ;
								System.err.println( prod.getRate() +"*"+"("+model.getRate() +"/"+ model.getQuantity() +")"+"="+ totalSummOfIngridient[0] );
					}});
					
					
					//делаем мапу композитного кродукта для каждого магазина
					mapProduct.put(node.getCompositeProduct().getId(),
							CompositeProductModel.builder()
							.id(node.getCompositeProduct().getId())
							.name(node.getCompositeProduct().getName())
							.group(store.getAddress())
							.totalSumm(node.getPrice())
							.sellQuantite(totalSummOfIngridient[0])
							.measureProduct(node.getCompositeProduct().getMeasureProduct().getMeasure())
							.build());
					
				});
				storesMap.put(store.getId(), mapProduct);
			});
			
			List<CompositeProductModel> modelList = new ArrayList<>();
			storesMap.keySet().forEach(key->{
				modelList.addAll(storesMap.get(key).values());
			});
			//удаляекм колекцию ингридиентов за ненадобностью
			reportModel.setProductReportData(null);
			reportModel.setCompositeProductReportData(modelList);
			
			
	
		return new ResponseEntity<>(reportModel, HttpStatus.OK);
	}

}






