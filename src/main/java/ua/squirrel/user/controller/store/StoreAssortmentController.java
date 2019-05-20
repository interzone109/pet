package ua.squirrel.user.controller.store;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
import ua.squirrel.user.entity.product.composite.CompositeProductModel;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.user.service.product.CompositeProductServiceImpl;
import ua.squirrel.user.service.product.ProductServiceImpl;
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.user.service.store.consignment.ConsignmentServiceImpl;
import ua.squirrel.user.service.store.consignment.status.ConsignmentStatusServiceImpl;
import ua.squirrel.user.utils.StoreUtil;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@RestController
@RequestMapping("/user/stores/assortment/{store_id}")
@Slf4j
public class StoreAssortmentController {
	@Autowired
	private CompositeProductServiceImpl compositeProductServiceImpl;
	@Autowired
	private StoreServiceImpl storeServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private StoreUtil storeUtil;
	@Autowired
	private ConsignmentServiceImpl consignmentServiceImpl;
	@Autowired
	private ConsignmentStatusServiceImpl consignmentStatusServiceImpl;


	/**
	 * Метод возращает список композитных продуктов и их цену для данной ТТ
	 */
	@GetMapping
	public List<CompositeProductModel> getAssortment(@PathVariable("store_id")Long storeId, Authentication authentication) throws NotFoundException {

		log.info("LOGGER: get assortment for current user");
		User user = userServiceImpl.findOneByLogin("test1").get();
		// делаем мапу ид цена
		Map<Long, Integer> idsPrice = storeUtil.spliteIdsValue(getCurrentStore(user ,storeId).getProductPrice(), "price");
		
		return storeUtil.createProductPriceModel( compositeProductServiceImpl.findAllByUserAndIdIn(user, idsPrice.keySet()) ,idsPrice);
		
	}
	
	
	
	
	
	
	
	/**
	 * Метод добавляет продукт на магазин 
	 * также добавляет к магазину ингридиенты из которых состоит продукт
	 */
	@PostMapping
	public List<CompositeProductModel> addToStoreProduct(@PathVariable("store_id") Long storeId ,
			@RequestBody Map<Long, Integer> newProductPrice, Authentication authentication) throws NotFoundException {
		log.info("LOGGER: add new product price to store");
		User user = userServiceImpl.findOneByLogin("test1").get();
		
		Store store = getCurrentStore(user ,storeId) ;
		// делаем мапу ид цена
		Map<Long, Integer> idsPrice = storeUtil.spliteIdsValue(store.getProductPrice(), "price");
		//мапа без дубликатов уже добаленых продуктов
		Map<Long, Integer> cleanProductPrice = storeUtil.removeDublicateMap(idsPrice, newProductPrice);
		idsPrice.putAll( cleanProductPrice);
		store.setProductPrice(storeUtil.concatIdsValueToString(idsPrice, "price"));
		
		
		Set<Long> idsIngridient = new HashSet<>();
		compositeProductServiceImpl.findAllByUserAndIdIn(user, cleanProductPrice.keySet()).forEach(compositeProduct->{
			idsIngridient.addAll(storeUtil.spliteIds(compositeProduct.getProductExpend(), "rate"));
		});
		
		String productLeftovers = storeUtil.addDefaultLeftoverValue(idsIngridient, store.getProductLeftovers(), "quantity");
		store.setProductLeftovers(productLeftovers);
		
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		Optional<Consignment> consignmentOptional = consignmentServiceImpl.findOneByDateAndStoreAndConsignmentStatus(calendar, store, 
				consignmentStatusServiceImpl.findOneByName("ARRIVAL").get());
		
		Consignment consignment = consignmentOptional.isPresent() ?consignmentOptional.get() : null ;
		
		storeUtil.createOrUpdateConsigment(store, idsIngridient, consignment, calendar);
		store.getConsignment().add(consignment);
		
	
		storeServiceImpl.save(store);
		return storeUtil.createProductPriceModel( compositeProductServiceImpl.findAllByUserAndIdIn(user, cleanProductPrice.keySet())
				,cleanProductPrice);
	}
	
	
	
	
	
	
	/**
	 * Метод обноляет цену на продукт
	 */
	@PutMapping
	public List<CompositeProductModel> updateToStoreProduct(@PathVariable("store_id") Long storeId ,
			@RequestBody Map<Long, Integer> productPrice, Authentication authentication) throws NotFoundException {
		log.info("LOGGER: update product price to store");
		User user = userServiceImpl.findOneByLogin("test1").get(); 
		 
		
		return storeUtil.updateCompositeProductPrice( productPrice , getCurrentStore(user ,storeId)) ;
	}
	
	
	
	
	private Store getCurrentStore(User user ,Long id) throws NotFoundException {
		return storeServiceImpl.findOneByIdAndUser(id, user)
				.orElseThrow(() -> new NotFoundException("Store not found"));
	}
}
