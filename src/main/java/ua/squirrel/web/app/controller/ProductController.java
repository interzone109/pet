package ua.squirrel.web.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.assortment.partner.Partner;
import ua.squirrel.user.assortment.partner.service.PartnerServiceImpl;
import ua.squirrel.user.assortment.product.Product;
import ua.squirrel.user.assortment.product.service.ProductServiceImpl;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.registration.model.UserModel;
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
	private PartnerServiceImpl partnerServiceImpl;
	
	
	/*@PostMapping
	public User registr(@RequestBody UserModel userModel) {
		log.info("LOGGER: regist new user " + userModel.getLogin());
		
	}*/
	
	
	
	
	@GetMapping
	public List<Product> getSaveProduct() {
		
		User user =  userServiceImpl.findOneByLogin("test1").get();
		testProduct(user);
		return productServiceImpl.findByUser(user);
	}
	
	
	private void testProduct(User user){
		
		Partner partner = new Partner();
		partner.setCompany("картошечные братишки");
		partner.setPartnerMail("Potato@potatomail.com");
		partner.setPhonNumber("8-800-555-555");

		partnerServiceImpl.save(partner);
	
		
		
		List<Product> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Product productToSave = new Product();
			productToSave.setName("картошечка");
			productToSave.setDescription("очень укусная картошечка фри");
			productToSave.setUser(user);
			//productToSave.setPartner(partner);
			list.add(productToSave);
		}
		

		
		for (Product productToSave : list) {
			productServiceImpl.save(productToSave);
		}
		
	}
}
