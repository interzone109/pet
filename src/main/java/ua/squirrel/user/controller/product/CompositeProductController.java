package ua.squirrel.user.controller.product;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import ua.squirrel.user.entity.product.composite.CompositeProduct;
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
	 */
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
	}

	
	
	
	
	/**
	 * метод обновляет расход ингридиентов и удаляет продукты
	 */
	@PutMapping("{ingridientId}")
	public ProductModel updateProduct(@PathVariable("id") Long copositeId, @PathVariable("ingridientId") Long ingridientId, Authentication authentication,
			@RequestBody  Integer updateRate) throws NotFoundException {
		
		log.info("LOGGER: update  product expends");
		User userCurrentSesion = userServiceImpl.findOneByLogin("test1").get(); // получаем текущий композитный продукт по Id и пользователю
		
		CompositeProduct compositeProduct = getCompositeProduct(copositeId, userCurrentSesion); // проверяем входные данные на наличие обновлений
		 											
			String[] productExpends = compositeProduct.getProductExpend().split("rate");
			Map<Long, Integer> idsExpends = new HashMap<>();// разбиваем строку с Id и Расходами и сохраняем данные в Мар idsExpends
			for (int i = 0; i < productExpends.length; i++) {
				String[] parse = productExpends[i].split(":");
				idsExpends.put(Long.parseLong(parse[0]), Integer.parseInt(parse[1]));
			}
			
			//создаем новую строку с обновлениями
			StringBuilder update = compositeProduct.getExpendUpdate() != null
					//если старая строка пустая то создаеться новая если нет то строки конкатинируються
					? new StringBuilder(compositeProduct.getExpendUpdate())
					: new StringBuilder();
			
			
			ProductModel result = null ;
			if (idsExpends.containsKey(ingridientId)) {
				//записываем Id старый расход и время когда произошло обновление
				// получаем такой формат "5:10rate1548925801498date6:2rate1548925801498date"
				update.append(ingridientId + ":" + idsExpends.get(ingridientId) + "rate" + new Date().getTime() + "date");
				
				//обновляем значение по ключю для композитного продукта
				
				idsExpends.put(ingridientId, updateRate);
				result = ProductModel.builder().id(ingridientId).description(updateRate.toString()).build();
			}
			
			// сохраняем данные об изменении в еxpendUpdate
			compositeProduct.setExpendUpdate(update.toString());
			//преобразуем все ключи и значение в строку
			StringBuilder productExpend = new StringBuilder();
			idsExpends.forEach((key, value) -> {
				productExpend.append((key + ":" + value + "rate"));
					});
			
			//записываем в обьект compositeProduct и сохраняем в базу
			compositeProduct.setProductExpend(productExpend.toString());
			compositeProductServiceImpl.save(compositeProduct);
			
		
		
		return  result ;
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
