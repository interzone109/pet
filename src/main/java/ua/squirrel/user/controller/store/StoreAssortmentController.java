package ua.squirrel.user.controller.store;

import java.time.LocalDate;
import java.util.ArrayList; 
import java.util.List;
import java.util.Map;
import java.util.Optional; 
import java.util.stream.Collectors;

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
import ua.squirrel.user.entity.product.Product;
import ua.squirrel.user.entity.product.ProductModel; 
import ua.squirrel.user.entity.product.composite.CompositeProductModel;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.compositeproduct.node.StoreCompositeProductNode;
import ua.squirrel.user.entity.store.consignment.Consignment; 
import ua.squirrel.user.service.product.CompositeProductServiceImpl; 
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.user.service.store.compositeproduct.node.StoreCompositeProductServiceImpl;
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
	private UserServiceImpl userServiceImpl;
	@Autowired
	private ConsignmentServiceImpl consignmentServiceImpl;
	@Autowired
	private ConsignmentStatusServiceImpl consignmentStatusServiceImpl;
	@Autowired
	private StoreCompositeProductServiceImpl storeCompositeProductServiceImpl;
	@Autowired
	private CompositeProductServiceImpl compositeProductServiceImpl;
	@Autowired
	private StoreServiceImpl storeServiceImpl;
	@Autowired
	private StoreUtil storeUtil;
	

	
	
	@GetMapping("/leftovers")
	public List<ProductModel> getLeftOvers(@PathVariable("store_id")Long storeId, Authentication authentication) throws NotFoundException {

		log.info("LOGGER: get leftovers for current store");
		User user = userServiceImpl.findOneByLogin("test1").get();
		Store store = getCurrentStore(user ,storeId);
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

	/**
	 * Метод возращает список композитных продуктов и их цену для данной ТТ
	 */
	@GetMapping
	public List<CompositeProductModel> getAssortment(@PathVariable("store_id")Long storeId,
			Authentication authentication) throws NotFoundException {

		log.info("LOGGER: get assortment for current user");
		User user = userServiceImpl.findOneByLogin("test1").get();
		return storeUtil.getProductPriceModel( getCurrentStore(user ,storeId).getStoreCompositeProductNode());
		
	}

	
	
	
	
	
	/**
	 * Метод добавляет продукт на магазин 
	 * и создает прихродную накладную с ингридиентами продукта
	*/
	@PostMapping
	public List<CompositeProductModel> addToStoreProduct(@PathVariable("store_id") Long storeId ,
			@RequestBody Map<Long, Integer> newProductPrice, Authentication authentication) throws NotFoundException {
		log.info("LOGGER: add new product price to store");
		User user = userServiceImpl.findOneByLogin("test1").get();
		Store store = getCurrentStore(user ,storeId) ;
		
		List<Product> newIngridients = new ArrayList<>();
		List<StoreCompositeProductNode> compositesPriceNode = new ArrayList<>();
		//находим композитный продукт
		compositeProductServiceImpl.findAllByUserAndIdIn(user, newProductPrice.keySet()).forEach(compProd->{
			boolean isProductExist = false ;//проверяем есть ли уже такой продукт на магазине
			for(StoreCompositeProductNode node : store.getStoreCompositeProductNode()){
				if(node.getCompositeProduct().getId() == compProd.getId()) {
					isProductExist = true;
				}
			};
			if(!isProductExist) {
			// создаем узел для хранения продукта цены и магазина
			StoreCompositeProductNode storeCompositeProductNode = new StoreCompositeProductNode();
			storeCompositeProductNode.setCompositeProduct(compProd);
			storeCompositeProductNode.setPrice(newProductPrice.get(compProd.getId()));
			storeCompositeProductNode.setStore(store);
			compositesPriceNode.add(storeCompositeProductNode);
			//получаем ингридиенты композитного продукта		
			compProd.getProductMap().forEach(productMap->{
				Product product = productMap.getProduct() ;
				if(!newIngridients.contains(product)) {
				newIngridients.add(product);
				}
			});
			}
		});
		storeCompositeProductServiceImpl.saveAll(compositesPriceNode);
		
		//создаем прихоную накладную при добавлении нового товара на магазин
		LocalDate calendar =  LocalDate.now() ;
		//находи накладную сегодняшнего числа, текущего магазина со статусом поступления и не проведенную
		Optional<Consignment> consignmentOptional = consignmentServiceImpl.findOneByDateAndStoreAndConsignmentStatusAndIsApprovedAndMetaIgnoreCaseContaining
				(calendar, store, consignmentStatusServiceImpl.findOneByName("ARRIVAL").get(), false, "user:%:");
		
		Consignment consignment ;
		if(consignmentOptional.isPresent()) {
			  consignment = consignmentOptional.get();
			  if(!consignment.isApproved()) {
				consignment = storeUtil.uniqueConsigment( consignment , newIngridients);
			  }
		}else {
			consignment = new Consignment();
			consignment.setDate(calendar);
			consignment.setApproved(false);
			consignment.setMeta("user:%:Поступление новых ингридиентов на *" + store.getAddress() + "*");
			consignment.setStore(store);
			consignment.setConsignmentStatus(consignmentStatusServiceImpl.findOneByName("ARRIVAL").get());
			consignment.setConsignmentNode(new ArrayList<>());
			consignment = storeUtil.fillConsigment( consignment , newIngridients);
			
		}
		consignmentServiceImpl.save(consignment);
	return storeUtil.getProductPriceModel(compositesPriceNode);	
	}
	
	
	
	/**
	 * Метод обноляет цену на продукт
	 */
	@PutMapping
	public List<CompositeProductModel> updateToStoreProduct(@PathVariable("store_id") Long storeId ,
			@RequestBody Map<Long, Integer> productPrice, Authentication authentication) throws NotFoundException {
		log.info("LOGGER: update product price to store");
		User user = userServiceImpl.findOneByLogin("test1").get(); 
		 //получаем колекцию узлов которые нунжо обновить
		List<StoreCompositeProductNode> newPriceNode =  getCurrentStore(user ,storeId)
		.getStoreCompositeProductNode().stream()
		.filter( productNode-> productPrice.containsKey(productNode.getCompositeProduct().getId()))
		.collect(Collectors.toList());
		
		newPriceNode.forEach(updateNode->{
			updateNode.setPrice(productPrice.get(updateNode.getCompositeProduct().getId()));
		});
		
		return storeUtil.getProductPriceModel(newPriceNode) ;
	}
	
	
	
	
	private Store getCurrentStore(User user ,Long id) throws NotFoundException {
		return storeServiceImpl.findOneByIdAndUser(id, user)
				.orElseThrow(() -> new NotFoundException("Store not found"));
	}
}
