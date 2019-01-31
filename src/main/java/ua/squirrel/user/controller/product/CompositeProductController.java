package ua.squirrel.user.controller.product;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import ua.squirrel.user.entity.product.composite.CompositeProduct;
import ua.squirrel.user.entity.product.composite.CompositeProductModel;
import ua.squirrel.user.entity.product.composite.CompositeProductUpdateDeteleModel;
import ua.squirrel.user.service.product.CompositeProductServiceImpl;
import ua.squirrel.user.service.product.ProductServiceImpl;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@RestController
@RequestMapping("/partners/composites/{id}/info")
@Slf4j
//@Secured("USER")
public class CompositeProductController {
	@Autowired
	private CompositeProductServiceImpl compositeProductServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private ProductServiceImpl productServiceImpl;

	/**
	 * метод находит по id и User CompositeProduct и возращает информацию о нем и о
	 * его ингридиентах
	 */
	@GetMapping
	public CompositeProductModel getCompositeProductInfo(Authentication authentication, @PathVariable("id") Long id)
			throws NotFoundException {
		log.info("LOGGER: return curent composite product");
		User userCurrentSesion = userServiceImpl.findOneByLogin("test1").get();
		// вызывается приветный метод возращающий модель коспозитного продукта
		return getCompositeProductModel(id, userCurrentSesion);

	}

	/**
	 * метод добавляет новые ингридиенты и их расход к продукту
	 */
	@PostMapping
	public CompositeProductModel addToCompositeProduct(@PathVariable("id") Long id,
			@RequestBody Map<Long, Integer> composites, Authentication authentication) throws NotFoundException {
		log.info("LOGGER: update curent composite product");
		User userCurrentSesion = userServiceImpl.findOneByLogin("test1").get();
		// Сет хранящий значения Id продукта и его расход на 1 единицу
		Set<String> ids = new HashSet<>();
		// вызываеться приватный метод возращающий композитный продукт по Id и
		// проверяющий принадлежит ли он данному пользователю
		CompositeProduct compositeProduct = getCompositeProduct(id, userCurrentSesion);
		// заполнение данными из композитного продуктами -"1:20rate2:1rate"
		for (String idsExpends : compositeProduct.getProductExpend().split("rate")) {
			ids.add(idsExpends + "rate");
		}
		// находим все продукты по вхождению ключей и текущему пользователю
		// далее добавляем каждое найденое знеачение в Сет преобразуя в строку
		// id продукта получаем из запроса к бд а его расход из входных данных обьекта
		// composites
		productServiceImpl.findAllByUserAndIdIn(userCurrentSesion, composites.keySet()).stream().forEach(prod -> {
			ids.add(prod.getId() + ":" + composites.get(prod.getId()) + "rate");
		});
		// преобразуем наш сет в обну строку
		StringBuilder productExpend = new StringBuilder();
		ids.stream().forEach(obj -> {
			productExpend.append(obj);
		});
		// записываем строку в композитный продукт и сохраняем его в базу
		compositeProduct.setProductExpend(productExpend.toString());
		compositeProductServiceImpl.save(compositeProduct);
		return getCompositeProductModel(id, userCurrentSesion);
	}

	/**
	 * метод обновляет расход ингридиентов и удаляет продукты
	 */
	@PutMapping
	public CompositeProductModel updateDeleteProduct(@PathVariable("id") Long id, Authentication authentication,
			@RequestBody CompositeProductUpdateDeteleModel updateDeteleModel) throws NotFoundException {
		log.info("LOGGER: update  product expends");
		User userCurrentSesion = userServiceImpl.findOneByLogin("test1").get();
		// получаем текущий композитный продукт по Id и пользователю
		CompositeProduct compositeProduct = getCompositeProduct(id, userCurrentSesion);
		// проверяем входные данные на наличие обновлений
		if (updateDeteleModel.getComposites() != null) {
			// разбиваем строку с Id и Расходами и сохраняем данные в Мар idsExpends
			String[] productExpends = compositeProduct.getProductExpend().split("rate");
			Map<Long, Integer> idsExpends = new HashMap<>();
			for (int i = 0; i < productExpends.length; i++) {
				String[] parse = productExpends[i].split(":");
				idsExpends.put(Long.parseLong(parse[0]), Integer.parseInt(parse[1]));
			}
			//создаем новую строку с обновлениями
			StringBuilder update = compositeProduct.getExpendUpdate() != null
					//если старая строка пустая то создаеться новая если нет то строки конкатинируються
					? new StringBuilder(compositeProduct.getExpendUpdate())
					: new StringBuilder();
			//получаем дату изменения количества расхода
			Date curentDate = new Date();
			//проходимся по входным данным Мар 
			updateDeteleModel.getComposites().forEach((key, value) -> {
				//если входной ключ соответствует уже имеющимуся ключю то
				if (idsExpends.containsKey(key)) {
					//записываем Id старый расход и время когда произошло обновление
					// получаем такой формат "5:10rate1548925801498date6:2rate1548925801498date"
					update.append(key + ":" + idsExpends.get(key) + "rate" + curentDate.getTime() + "date");
					//обновляем значение по ключю для композитного продукта
					idsExpends.put(key, updateDeteleModel.getComposites().get(key));
				}
			});
			// сохраняем данные об изменении в еxpendUpdate
			compositeProduct.setExpendUpdate(update.toString());
			//преобразуем все ключи и значение в строку
			StringBuilder str = new StringBuilder();
			idsExpends.forEach((key, value) -> {
				str.append((key + ":" + value + "rate"));
			});
			//записываем в обьект compositeProduct и сохраняем в базу
			compositeProduct.setProductExpend(str.toString());
			compositeProductServiceImpl.save(compositeProduct);
		}
		//проверяем на наличие id  для удаления из композитного продукта
		if (updateDeteleModel.getDeleteIds() != null) {
			deleteCompositeProduct(id, updateDeteleModel.getDeleteIds(), compositeProduct);
		}
		return getCompositeProductModel(id, userCurrentSesion);
	}

	/**
	 * метод удаляет ингридиенты
	 */
	private void deleteCompositeProduct(Long id, List<Long> composites, CompositeProduct compositeProduct)
			throws NotFoundException {
		log.info("LOGGER: delete  product from current composite product");
		//получаем масив id и Расхода и преобразуем их в Мар
		String[] productExpends = compositeProduct.getProductExpend().split("rate");
		Map<Long, Integer> idsExpends = new HashMap<>();
		for (int i = 0; i < productExpends.length; i++) {
			String[] parse = productExpends[i].split(":");
			idsExpends.put(Long.parseLong(parse[0]), Integer.parseInt(parse[1]));
		}
		// удаляем данные по дубликатам
		composites.stream().forEach(key -> {
			if (idsExpends.containsKey(key)) {
				idsExpends.remove(key);
			}
		});
		//преобразовываем оставшиеся данные обратно в строку и сохраняем все в базу
		StringBuilder str = new StringBuilder();
		idsExpends.forEach((key, value) -> {
			str.append((key + ":" + value + "rate"));
		});
		compositeProduct.setProductExpend(str.toString());
		compositeProductServiceImpl.save(compositeProduct);
	}

	private CompositeProduct getCompositeProduct(Long id, User currentUser) throws NotFoundException {
		return compositeProductServiceImpl.findByIdAndUser(id, currentUser)
				.orElseThrow(() -> new NotFoundException("Composite product not found"));
	}

	private CompositeProductModel getCompositeProductModel(Long id, User currentUser) throws NotFoundException {

		CompositeProduct compositeProduct = getCompositeProduct(id, currentUser);

		String[] productExpends = compositeProduct.getProductExpend().split("rate");

		Map<Long, Integer> idsExpends = new HashMap<>();

		for (int i = 0; i < productExpends.length; i++) {
			String[] parse = productExpends[i].split(":");
			idsExpends.put(Long.parseLong(parse[0]), Integer.parseInt(parse[1]));
		}

		Map<ProductModel, Integer> composites = new HashMap<>();

		productServiceImpl.findAllById(idsExpends.keySet()).stream().forEach(product -> {
			ProductModel prodModel = ProductModel.builder().id(product.getId()).name(product.getName())
					.description(product.getDescription()).group(product.getGroup())
					.measureProduct(product.getMeasureProduct()).propertiesProduct(product.getPropertiesProduct())
					.build();
			composites.put(prodModel, idsExpends.get(product.getId()));

		});

		return CompositeProductModel.builder().id(compositeProduct.getId()).name(compositeProduct.getName())
				.products(composites).group(compositeProduct.getGroup())
				.propertiesProduct(compositeProduct.getPropertiesProduct().toString()).build();
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
