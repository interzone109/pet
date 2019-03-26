package ua.squirrel.user.controller.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.product.composite.CompositeProduct;
import ua.squirrel.user.entity.product.composite.CompositeProductModel;
import ua.squirrel.user.service.product.CompositeProductServiceImpl;
import ua.squirrel.user.service.product.properties.MeasureProductServiceImpl;
import ua.squirrel.user.service.product.properties.PropertiesProductServiceImpl;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@RestController
@RequestMapping("/user/composites" )
@Slf4j
public class AllCompositeProductController {

	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private  PropertiesProductServiceImpl propertiesProductServiceImpl;
	@Autowired
	private MeasureProductServiceImpl measureProductServiceImpl;
	@Autowired
	private CompositeProductServiceImpl compositeProductServiceImpl;
	
	/**
	 * метод получает зарегисрированого пользователя берет у него список композитных
	 * продуктов и перезаписывает их в List<CompositeProductModel>, после чего
	 * возращает данные
	 * 
	 */
	@GetMapping
	public List<CompositeProductModel> getAllCompositeProduct(Authentication authentication) {
		log.info("LOGGER: show all Composite ProductModel");

		User user = userServiceImpl.findOneByLogin("test1").get();

		return getAllCompositProduct(user);
	}

	/**
	 * метод получает новый коmпозитный продукт CompositeProductModel
	 * newCompositeProductModel, после чего переписывает данные из поделей в
	 * сущности и сохраняет их в базу
	 */
	@PostMapping
	public CompositeProductModel addNewCompositeProduct(Authentication authentication,
			@RequestBody CompositeProductModel newCompositeProductModels) {
		log.info("LOGGER: save new Composite ProductModel from model ");

		User user = userServiceImpl.findOneByLogin("test1").get();
		CompositeProduct compositeProduct = new CompositeProduct();
		compositeProduct.setName(newCompositeProductModels.getName());
		compositeProduct.setGroup(newCompositeProductModels.getGroup());
		compositeProduct.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("PRODUCT_FINAL"));
		compositeProduct.setMeasureProduct(measureProductServiceImpl.findOneByMeasure(newCompositeProductModels.getMeasureProduct()));
		
		compositeProduct.setUser(user);
		compositeProductServiceImpl.save(compositeProduct);
		
		newCompositeProductModels.setId(compositeProduct.getId());

		return newCompositeProductModels;
	}
	
	
	/**
	 * метод обновляет данные в полях CompositeProduct
	 */
	@PutMapping
	public CompositeProductModel updateCompositeProduct(Authentication authentication,
			@RequestBody CompositeProductModel newCompositeProductModels) throws NotFoundException {
		log.info("LOGGER: update Composite Product Model from model ");

		User user = userServiceImpl.findOneByLogin("test1").get();
		
		
		
		
		CompositeProduct compositeProduct = getCompositeProduct(newCompositeProductModels.getId(), user);
		compositeProduct.setName(newCompositeProductModels.getName());
		compositeProduct.setGroup(newCompositeProductModels.getGroup());
		compositeProduct.setMeasureProduct(measureProductServiceImpl.findOneByMeasure(newCompositeProductModels.getMeasureProduct()));
		
		compositeProductServiceImpl.save(compositeProduct);
		

		return newCompositeProductModels;
	}
	
	
	
	private List<CompositeProductModel> getAllCompositProduct(User user) {
		List<CompositeProductModel> compositeProductModels = new ArrayList<>();

		compositeProductServiceImpl.findAllByUser(user).stream().forEach(obj -> {
			compositeProductModels.add(CompositeProductModel.builder()
					.id(obj.getId())
					.name(obj.getName())
					.propertiesProduct(obj.getPropertiesProduct().getName())
					.measureProduct(obj.getMeasureProduct().getMeasure())
					.group(obj.getGroup())
					.build());
		});

		return compositeProductModels;
	}


	private CompositeProduct getCompositeProduct(Long id, User currentUser) throws NotFoundException {
		return compositeProductServiceImpl.findByIdAndUser(id, currentUser)
				.orElseThrow(() -> new NotFoundException("Composite product not found"));
	}
}
