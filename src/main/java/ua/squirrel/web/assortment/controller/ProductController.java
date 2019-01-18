package ua.squirrel.web.assortment.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.assortment.partner.service.PartnerServiceImpl;
import ua.squirrel.user.assortment.product.Product;
import ua.squirrel.user.assortment.product.helper.service.GroupProductServiceImpl;
import ua.squirrel.user.assortment.product.helper.service.MeasureProductServiceImpl;
import ua.squirrel.user.assortment.product.service.ProductServiceImpl;
import ua.squirrel.web.registration.user.service.UserServiceImpl;

@RestController
@RequestMapping("/product")
@Slf4j
//@Secured("USER")
public class ProductController {
	@Autowired
	private ProductServiceImpl productServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private GroupProductServiceImpl groupProductServiceImpl;
	@Autowired
	private PartnerServiceImpl partnerServiceImpl;
	@Autowired
	private MeasureProductServiceImpl measureProductServiceImpl;
	
	/*@PostMapping
	public User registr(@RequestBody UserModel userModel) {
		log.info("LOGGER: regist new user " + userModel.getLogin());
		
	}*/
	
	@GetMapping
	public List<Product> getSaveProduct() {
		
		return null ;
	}
	
	@PostMapping
	public List <Product> addNewProduct(@RequestBody List <Product> newProduct) {
		
		return null;
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
      "id": 1,
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
	
	
	
	
	
	
	
	
	
	/*@GetMapping
	public List<Product> getSaveProduct() {
		List<Partner> list = new ArrayList<>();
		Partner p = new Partner();
		p.setCompany("вкуснфые плюшечки");
		p.setPartnerMail("sobaka@baka.com");
		p.setPhonNumber("55-99-88");
		
		
		partnerServiceImpl.save(p);
		list.add(p);
		
		
		
		
		List<Product> product = new ArrayList<>();
		
		Product productToSave = new Product();
		productToSave.setName("картошечка");
		productToSave.setDescription("очень укусная картошечка фри");
		productToSave.setPrice(1.25f);
		productToSave.setGroupProduct(groupProductServiceImpl.findOneByName("CONSUMABLES"));
		productToSave.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		
		productServiceImpl.save(productToSave);
		
		product.add(productToSave);
		
		p.setProducts(product);
		partnerServiceImpl.save(p);
		
		
		User user =  userServiceImpl.findOneByLogin("test1").get();
		user.setPartners(list);
		userServiceImpl.save(user);
		
		
		
		
		
		
		
		
		product.add(productToSave);
		
		return product;
	}*/
	
	

}
