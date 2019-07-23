package ua.squirrel.user.controller.product;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
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
import ua.squirrel.user.entity.product.composite.CompositeProduct;
import ua.squirrel.user.entity.product.map.ProductMap;
import ua.squirrel.user.service.product.CompositeProductServiceImpl;
import ua.squirrel.user.service.product.ProductServiceImpl;
import ua.squirrel.user.utils.CompositeProductUtil;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@RestController
@RequestMapping("user/composites/{id}/edit")
@Slf4j
public class CompositeProductController {
	@Autowired
	private CompositeProductServiceImpl compositeProductServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private ProductServiceImpl productServiceImpl;
	@Autowired
	private CompositeProductUtil  compositeProductUtil;
	
	@Autowired
	private ProductMapServiceImpl  productMapRepository;
	
	/**
	 * метод находит по id и User CompositeProduct и возращает информацию о нем и о
	 * его ингридиентах
	 */
	@GetMapping
	public List<ProductModel> getCompositeProductInfo(Authentication authentication, @PathVariable("id") Long id)
			throws NotFoundException {
		log.info("LOGGER: return curent composite product");
		User userCurrentSesion = userServiceImpl.findOneByLogin("test1").get();
		// вызывается привaтный метод возращающий модель коспозитного продукта
		return getProductExpendsModel(id, userCurrentSesion);

	}

	/**
	 * метод добавляет новые ингридиенты и их расход к продукту
	 
	@PostMapping
	public List<ProductModel> addToCompositeProduct(@PathVariable("id") Long compositeId ,
			@RequestBody Map<Long, Integer> composites, Authentication authentication) throws NotFoundException {
		log.info("LOGGER:  product add new ingridient in  curent composite product");
		User userCurrentSesion = userServiceImpl.findOneByLogin("test1").get();
		
		//получаем композитный продукт в который будем добавлять новые ингридиенты и их расход
		CompositeProduct compositeProduct = getCompositeProduct(compositeId, userCurrentSesion);
		Map<Long, Integer> idsExpends = compositeProductUtil.spliteIdsValue(compositeProduct.getProductExpend(), "rate");
		
		//пнроверяем входные данные на дубликаты
		idsExpends.putAll( compositeProductUtil.removeDublicateMap(idsExpends, composites));
		// добавляем новые ингридиенты и расход к продукту и сохраняем в базу
		compositeProduct.setProductExpend(compositeProductUtil.concatIdsValueToString(idsExpends, "rate"));
		compositeProductServiceImpl.save(compositeProduct);
		
		return compositeProductUtil.convertToProductModelDescription(productServiceImpl.findAllByUserAndIdIn(userCurrentSesion, composites.keySet()), idsExpends);
	}*/
	// New controller
	@PostMapping
	public List<ProductModel> addToCompositeProduct(@PathVariable("id") Long compositeId ,
			@RequestBody Map<Long, Integer> composites, Authentication authentication) throws NotFoundException {
		log.info("LOGGER:  product add new ingridient in  curent composite product");
		User user = userServiceImpl.findOneByLogin("test1").get();
		
		//получаем композитный продукт в который будем добавлять новые ингридиенты и их расход
		CompositeProduct compositeProduct = getCompositeProduct(compositeId, user);
		List<Product> prducts = productServiceImpl.findAllByUserAndIdIn(user, composites.keySet());
		Set<ProductMap> productMaps = new HashSet<>();
		prducts.forEach(product->{
			ProductMap productMap = new ProductMap();
			productMap.setCompositeProduct(compositeProduct);
			productMap.setProduct(product);
			productMap.setRate(composites.get(product.getId()));
			productMaps.add(productMap);
		});
		productMapRepository.saveAll(productMaps);
		return null;
	}
	
	
	
	/**
	 * метод обновляет расход ингридиентa 
	 */
	@PutMapping("{ingridientId}")
	public ProductModel updateProduct(@PathVariable("id") Long copositeId, @PathVariable("ingridientId") Long ingridientId, Authentication authentication,
			@RequestBody  Integer updateRate) throws NotFoundException {
		
		log.info("LOGGER: update  product expends");
		User user = userServiceImpl.findOneByLogin("test1").get();
		
		// получаем текущий композитный продукт по Id и пользователю
		CompositeProduct compositeProduct = getCompositeProduct(copositeId, user); 
		Map<Long, Integer> idsExpends = compositeProductUtil.spliteIdsValue(compositeProduct.getProductExpend(), "rate");
		
		if(idsExpends.containsKey(ingridientId)) {
			String updateDate = compositeProductUtil.concatIdsValueDateToString(ingridientId, idsExpends.get(ingridientId), "rate");
			idsExpends.put(ingridientId, updateRate);
			
			
			compositeProduct.setProductExpend(compositeProductUtil.concatIdsValueToString(idsExpends, "rate"));
			
			//создаем новую строку с обновлениями
			StringBuilder update = compositeProduct.getExpendUpdate() != null
					//если старая строка пустая то создаеться новая если нет то строки конкатинируються
					? new StringBuilder(compositeProduct.getExpendUpdate())
					: new StringBuilder();
					update.append(updateDate);
					compositeProduct.setExpendUpdate(update.toString());
					
					compositeProductServiceImpl.save(compositeProduct);
		}
		
		return  compositeProductUtil.convertToProductModel(productServiceImpl.findOneByIdAndUser(ingridientId, user).get(), idsExpends.get(ingridientId)) ;
	}
	
	


	private CompositeProduct getCompositeProduct(Long id, User currentUser) throws NotFoundException {
		return compositeProductServiceImpl.findByIdAndUser(id, currentUser)
				.orElseThrow(() -> new NotFoundException("Composite product not found"));
	}

	// метод возращает описание продукта с его расходом
	private List<ProductModel> getProductExpendsModel(Long id, User currentUser) throws NotFoundException {

		CompositeProduct compositeProduct = getCompositeProduct(id, currentUser);
		Map<Long, Integer> idsExpends = compositeProductUtil.spliteIdsValue(compositeProduct.getProductExpend(), "rate");	
		
		return compositeProductUtil.convertToProductModelDescription(productServiceImpl.findAllById(idsExpends.keySet()), idsExpends);
	}

	/*
	  тестовый джейсон Post 
	  { 
	  "composites": { 
	  "4":4444, 
	  "5":55555 
	  }, 
	  "deleteIds": [] 
	  } 
	  тестовый джейсон Put/delete
	   
	   {
	    "composites": 
	   	{ 
	      "1":4444,
	     "2":55555 
	     },
	      "deleteIds": [ 1, 2] 
	      }
	 
	  ":[0-9]+rate|rate*"
	 
	  [date]*[0-9]*:[0-9]*rate|[date]*
	 */

}
