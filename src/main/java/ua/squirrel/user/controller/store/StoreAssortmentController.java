package ua.squirrel.user.controller.store;

import java.time.LocalDate;
import java.util.ArrayList;
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
import ua.squirrel.user.entity.product.ProductModel;
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
	private ProductServiceImpl productServiceImpl;
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
	
	@GetMapping("/leftovers")
	public List<ProductModel> getLeftOvers(@PathVariable("store_id")Long storeId, Authentication authentication) throws NotFoundException {

		log.info("LOGGER: get leftovers for current store");
		User user = userServiceImpl.findOneByLogin("test1").get();
		// делаем мапу ид количество
		Map<Long, Integer> idsQuantity = storeUtil.spliteIdsValue(getCurrentStore(user ,storeId).getProductLeftovers(), "quantity");
		
		List<ProductModel> productModel = new ArrayList<>();
		  productServiceImpl.findAllByUserAndIdIn(user, idsQuantity.keySet()).forEach(product->{
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
		 return productModel;
		
	}
	
	
	
	
	
	/**
	 * Метод добавляет продукт на магазин 
	 * и создает прихродную накладную
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
		
		
		Set<Long> idsIngridient = new HashSet<>();//получаем ид ингридиентов для создания накладной
		compositeProductServiceImpl.findAllByUserAndIdIn(user, cleanProductPrice.keySet()).forEach(compositeProduct->{
			idsIngridient.addAll(storeUtil.spliteIds(compositeProduct.getProductExpend(), "rate"));
		});
		
		//создаем прихоную накладную при добавлении нового товара на магазин
		LocalDate calendar =  LocalDate.now() ;
		//находи накладную сегодняшнего числа, текущего магазина со статусом поступления и не проведенную
		Optional<Consignment> consignmentOptional = consignmentServiceImpl.findOneByDateAndStoreAndConsignmentStatusAndIsApprovedAndMetaIgnoreCaseContaining
				(calendar, store, consignmentStatusServiceImpl.findOneByName("ARRIVAL").get(), false, "user:%:");
		
		Consignment consignment = consignmentOptional.isPresent() ?consignmentOptional.get() : null ;
		
		storeUtil.createOrUpdateConsigment(store, idsIngridient, consignment, calendar);
		store.getConsignment().add(consignment);//
		
	
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
