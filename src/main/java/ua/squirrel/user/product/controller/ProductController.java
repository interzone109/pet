package ua.squirrel.user.product.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.partner.entity.Partner;
import ua.squirrel.user.partner.service.PartnerServiceImpl;
import ua.squirrel.user.product.entity.Product;
import ua.squirrel.user.product.entity.ProductModel;
import ua.squirrel.user.product.helper.service.MeasureProductServiceImpl;
import ua.squirrel.user.product.helper.service.PropertiesProductServiceImpl;
import ua.squirrel.user.product.service.ProductServiceImpl;
import ua.squirrel.web.registration.user.service.UserServiceImpl;
import ua.squirrel.web.user.entity.User;

@RestController
@RequestMapping("/partners/{partner_id}/info/{product_id}/edit")
@Slf4j
//@Secured("USER")
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
	
	
	/**
	 * мeтод получает список новых продуктов и добавляет их 
	 * к текущему поставщику.
	 * */
	
	@PostMapping
	public List<ProductModel> dddProductToPartner(Authentication authentication, @PathVariable("partner_id") Long id,
			@RequestBody List<ProductModel> productsModel) throws NotFoundException {
		log.info("LOGGER: add product to current partner");

		User userCurrentSesion = userServiceImpl.findOneByLogin("test1").get();

		Partner partner = getCurrentPartner(id, userCurrentSesion);

		productsModel.stream().forEach(obj -> {
			Product addProduct = new Product();
			addProduct.setDescription(obj.getDescription());
			addProduct.setName(obj.getName());
			addProduct.setPropertiesProduct( 
					propertiesProductServiceImpl.findOneByName(obj.getPropertiesProduct().getName()));
			addProduct.setMeasureProduct(
					measureProductServiceImpl.findOneByMeasure(obj.getMeasureProduct().getMeasure()));
			addProduct.setGroup(obj.getGroup());
			addProduct.setPartner(partner);

			partner.getProducts().add(addProduct);
		});

		partnerServiceImpl.save(partner);

		return productsModel;
	}
	
	/**
	 * мeтод обновляет выбранный продукт
	 * */
	
	@PutMapping
	public ProductModel updateProduct(Authentication authentication, @PathVariable("partner_id") Long id,
			@PathVariable("product_id") Long productId ,@RequestBody ProductModel productModel) throws NotFoundException {
		log.info("LOGGER: update current product");
		
		User userCurrentSesion = userServiceImpl.findOneByLogin("test1").get();

		Partner partner = getCurrentPartner(id, userCurrentSesion);
		
		Product updateProduct = productServiceImpl.findOneByIdAndPartner(productId, partner)
				.orElseThrow(() -> new NotFoundException("Product not found"));
		
		updateProduct.setName(productModel.getName());
		updateProduct.setDescription(productModel.getDescription());
		updateProduct.setPropertiesProduct( 	
				propertiesProductServiceImpl.findOneByName(productModel.getPropertiesProduct().getName()));
		updateProduct.setMeasureProduct( 
				measureProductServiceImpl.findOneByMeasure(productModel.getMeasureProduct().getMeasure()));
		updateProduct.setGroup(productModel.getGroup());
		updateProduct.setPartner(partner);
		productServiceImpl.save(updateProduct);
		
		return productModel;
	}
	
	
	@DeleteMapping
	public void deleteProduct(Authentication authentication, @PathVariable("partner_id") Long id,
			@PathVariable("product_id") Long productId ) throws NotFoundException {
		log.info("LOGGER: delete product ");

	}
	
	private Partner getCurrentPartner(Long id , User currentUser) throws NotFoundException {
		return partnerServiceImpl.findByIdAndUser(id, currentUser)
				.orElseThrow(() -> new NotFoundException("Partner not found"));
	}
	
	/*
	 [
   {
      
      "name": "картошечка",
      "description": "очень укусная картошечка фри",
      "price": 1.25,
      "groupProduct": {
         "name": "CONSUMABLES"
      },
      "measureProduct": {
         "measure": "UNIT"
      }
   },
   {
      "name": "картошечка",
      "description": "очень укусная картошечка фри",
      "price": 1.25,
      "groupProduct": {
         "name": "CONSUMABLES"
      },
      "measureProduct": {
         "measure": "UNIT"
      }
   }
]
	 
	 */


}
