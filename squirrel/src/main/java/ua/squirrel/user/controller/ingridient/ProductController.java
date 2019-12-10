package ua.squirrel.user.controller.ingridient;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.partner.Partner;
import ua.squirrel.user.entity.product.Product;
import ua.squirrel.user.entity.product.ProductModel;
import ua.squirrel.user.entity.product.composite.CompositeProduct;
import ua.squirrel.user.entity.product.node.ProductMap;
import ua.squirrel.user.service.partner.PartnerServiceImpl;
import ua.squirrel.user.service.product.ProductServiceImpl;
import ua.squirrel.user.service.product.node.ProductMapServiceImpl;
import ua.squirrel.user.service.product.properties.MeasureProductServiceImpl;
import ua.squirrel.user.service.product.properties.PropertiesProductServiceImpl;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.account.AccountAppServiceImpl;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

/**
 * Controller :
 * POST create new product, save in db  and return new product model
 * PUT update product info by product id in current partner
 * */


@RestController
@RequestMapping("/user/partners/{partner_id}/edit")
@Slf4j
public class ProductController {
	@Autowired
	private ProductServiceImpl productServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private PartnerServiceImpl partnerServiceImpl;
	@Autowired
	private PropertiesProductServiceImpl propertiesProductServiceImpl;
	@Autowired
	private MeasureProductServiceImpl measureProductServiceImpl;
	@Autowired
	private ProductMapServiceImpl productMapServiceImpl;
	@Autowired
	private AccountAppServiceImpl accountAppServiceImpl;
	
	/**
	 * мeтод получает  новый продукт и добавляет его
	 * к текущему поставщику.
	 * */
	
	@PostMapping
	public ProductModel dddProductToPartner(Authentication authentication, @PathVariable("partner_id") Long id,
			@RequestBody ProductModel productsModel) throws NotFoundException {
		log.info("LOGGER: add product to current partner");

		User user = userServiceImpl.findOneByAccount(accountAppServiceImpl.findOneByLogin(authentication.getName()).get()).get();

		Partner partner = getCurrentPartner(id, user);

			Product addProduct = new Product();
			addProduct.setDescription(productsModel.getDescription());
			addProduct.setName(productsModel.getName());
			addProduct.setPropertiesProduct( 
					propertiesProductServiceImpl.findOneByName(productsModel.getPropertiesProduct()));
			addProduct.setMeasureProduct(
					measureProductServiceImpl.findOneByMeasure(productsModel.getMeasureProduct()));
			addProduct.setGroup(productsModel.getGroup());
			addProduct.setPartner(partner);

			addProduct.setUser(user);

		productServiceImpl.save(addProduct);
		
		
		productsModel.setId(addProduct.getId());

		return productsModel;
	}
	
	/**
	 * мeтод получает  новый продукт и добавляет его
	 * к текущему поставщику.
	 * */
	
	@PostMapping("/coposite")
	public ResponseEntity<ProductModel> dddProductToPartnerCreateCompProd(Authentication authentication, @PathVariable("partner_id") Long idPartner,
			@RequestBody ProductModel productsModel) throws NotFoundException {
		log.info("LOGGER: add product to current partner and create composite product");

		User user = userServiceImpl.findOneByAccount(accountAppServiceImpl.findOneByLogin(authentication.getName()).get()).get();

		Partner partner = getCurrentPartner(idPartner, user);

			Product addProduct = new Product();
			addProduct.setDescription(productsModel.getDescription());
			addProduct.setName(productsModel.getName());
			addProduct.setPropertiesProduct( propertiesProductServiceImpl.findOneByName(productsModel.getPropertiesProduct()));
			addProduct.setMeasureProduct( measureProductServiceImpl.findOneByMeasure(productsModel.getMeasureProduct()));
			addProduct.setGroup(productsModel.getGroup());
			addProduct.setPartner(partner);

			addProduct.setUser(user );

		productServiceImpl.save(addProduct);
		int productCurrent = user.getUserSubscription().getProductCurrentQuantity();
		int productrLimit = user.getUserSubscription().getProductQuantity();
		if(productCurrent < productrLimit) {
		CompositeProduct compositeProduct = new CompositeProduct();
		compositeProduct.setName(productsModel.getName());
		compositeProduct.setGroup(productsModel.getGroup());
		compositeProduct.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("PRODUCT_FINAL"));
		compositeProduct.setMeasureProduct(measureProductServiceImpl.findOneByMeasure(productsModel.getMeasureProduct()));
		
		compositeProduct.setUser(user );
		
		ProductMap productMap = new ProductMap();
		productMap.setCompositeProduct(compositeProduct);
		productMap.setProduct(addProduct);
		productMap.setRate(productsModel.getRate());
		
		productMapServiceImpl.save(productMap);
		
		user.getUserSubscription().setProductCurrentQuantity(++productCurrent);
		userServiceImpl.save(user);
		}else {
			return  new ResponseEntity<>(ProductModel.builder().name("excess_of_limit").build()
					, HttpStatus.FAILED_DEPENDENCY);
		}
		
		productsModel.setId(addProduct.getId());

		return new ResponseEntity<>(productsModel, HttpStatus.OK);
	}
	
	/**
	 * мeтод обновляет выбранный продукт
	 * */
	@PutMapping
	@RequestMapping("/{product_id}")
	public ProductModel updateProduct(Authentication authentication, @PathVariable("partner_id") Long id,
			@PathVariable("product_id") Long productId ,@RequestBody ProductModel productModel) throws NotFoundException {
		log.info("LOGGER: update current product");
		
		User user = userServiceImpl.findOneByAccount(accountAppServiceImpl.findOneByLogin(authentication.getName()).get()).get();

		Partner partner = getCurrentPartner(id, user);
		
		Product updateProduct = productServiceImpl.findOneByIdAndPartner(productId, partner)
				.orElseThrow(() -> new NotFoundException("Product not found"));
		
		updateProduct.setName(productModel.getName());
		updateProduct.setDescription(productModel.getDescription());
		updateProduct.setPropertiesProduct( 	
				propertiesProductServiceImpl.findOneByName(productModel.getPropertiesProduct()));
		updateProduct.setMeasureProduct( 
				measureProductServiceImpl.findOneByMeasure(productModel.getMeasureProduct()));
		updateProduct.setGroup(productModel.getGroup());
		updateProduct.setPartner(partner);
		productServiceImpl.save(updateProduct);
		
		productModel.setId(updateProduct.getId());
		return productModel;
	}
	
	
	private Partner getCurrentPartner(Long id , User currentUser) throws NotFoundException {
		return partnerServiceImpl.findByIdAndUser(id, currentUser)
				.orElseThrow(() -> new NotFoundException("Partner not found"));
	}
	



}
