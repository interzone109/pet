package ua.squirrel.user.controller.product;

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
		return getCompositeProductModel(id, userCurrentSesion);
		
	}

	/**
	 * метод добавляет новые ингридиенты и их расход к продукту
	 * */
	@PostMapping
	public CompositeProductModel addToCompositeProduct(@PathVariable("id") Long id,
			@RequestBody Map<Long, Integer> composites, Authentication authentication) throws NotFoundException {
		log.info("LOGGER: update curent composite product");
		User userCurrentSesion = userServiceImpl.findOneByLogin("test1").get();

		Set<String> ids = new HashSet<>();

		// сохраняю все ид и затраты в сет
		CompositeProduct compositeProduct = getCompositeProduct(id, userCurrentSesion);
		for (String idsExpends : compositeProduct.getProductExpend().split("rate")) {
			ids.add(idsExpends + "rate");
		}

		productServiceImpl.findAllByUserAndIdIn(userCurrentSesion, composites.keySet()).stream().forEach(prod -> {
			ids.add(prod.getId() + ":" + composites.get(prod.getId()) + "rate");
		});

		StringBuilder productExpend = new StringBuilder();
		ids.stream().forEach(obj -> {
			productExpend.append(obj);
		});

		compositeProduct.setProductExpend(productExpend.toString());

		compositeProductServiceImpl.save(compositeProduct);
		return getCompositeProductModel(id, userCurrentSesion);
	}

	/**
	 * метод обновляет расход ингридиентов и удаляет продукты
	 * */
	@PutMapping
	public CompositeProductModel updateDeleteProduct(@PathVariable("id") Long id, Authentication authentication,
			@RequestBody CompositeProductUpdateDeteleModel updateDeteleModel) throws NotFoundException {
		log.info("LOGGER: update  product expends");
		User userCurrentSesion = userServiceImpl.findOneByLogin("test1").get();
		CompositeProduct compositeProduct = getCompositeProduct(id, userCurrentSesion);
		if (updateDeteleModel.getComposites() != null) {
			
			String[] productExpends = compositeProduct.getProductExpend().split("rate");

			Map<Long, Integer> idsExpends = new HashMap<>();
			for (int i = 0; i < productExpends.length; i++) {
				String[] parse = productExpends[i].split(":");
				idsExpends.put(Long.parseLong(parse[0]), Integer.parseInt(parse[1]));
			}
			
			updateDeteleModel.getComposites().forEach((key, value) -> {
				if (idsExpends.containsKey(key)) {
					idsExpends.put(key, updateDeteleModel.getComposites().get(key));
				}
			});

			StringBuilder str = new StringBuilder();

			idsExpends.forEach((key, value) -> {
				str.append((key + ":" + value + "rate"));
			});

			compositeProduct.setProductExpend(str.toString());
			compositeProductServiceImpl.save(compositeProduct);
		}
		if (updateDeteleModel.getDeleteIds() != null) {
			deleteCompositeProduct(id, updateDeteleModel.getDeleteIds(), compositeProduct);
		}
		return getCompositeProductModel(id, userCurrentSesion);
	}

	

	private void deleteCompositeProduct(Long id, List<Long> composites, CompositeProduct compositeProduct) throws NotFoundException {
		log.info("LOGGER: delete  product");

		String[] productExpends = compositeProduct.getProductExpend().split("rate");

		Map<Long, Integer> idsExpends = new HashMap<>();

		for (int i = 0; i < productExpends.length; i++) {
			String[] parse = productExpends[i].split(":");
			idsExpends.put(Long.parseLong(parse[0]), Integer.parseInt(parse[1]));
		}

		composites.stream().forEach(key -> {
			if (idsExpends.containsKey(key)) {
				idsExpends.remove(key);
			}
		});

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
	   тестовый джейсон Put/delete product {[1,2]}
{
   "deleteIds": [
      1,
      2
   ]
}

	String[] result = product.getProductExpend().split(":[0-9]+rate");
	 */

}
