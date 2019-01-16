package ua.squirrel.web.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.assortment.partner.Partner;
import ua.squirrel.user.assortment.partner.service.PartnerServiceImpl;
import ua.squirrel.user.assortment.product.Product;
import ua.squirrel.user.assortment.product.service.ProductServiceImpl;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.registration.user.service.UserServiceImpl;

@RestController
@RequestMapping("/index")
@Slf4j
//@Secured("USER")
public class IndexController {
	@Autowired
	private ProductServiceImpl productServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private PartnerServiceImpl partnerServiceImpl;
	
	@GetMapping
	public Product getSaveProduct() {
		
		Partner partner = new Partner();
		partner.setCompany("картошечные братишки");
		partner.setPartnerMail("Potato@potatomail.com");
		
		partnerServiceImpl.save(partner);
		
		User user =  userServiceImpl.findOneByLogin("test").get();

		Product productToSave = new Product();
		productToSave.setName("картошечка");
		productToSave.setDescription("очень укусная картошечка фри");
		productToSave.setUserOwner(user);
		productToSave.setPartner(partner);

		productServiceImpl.save(productToSave);
		return productToSave;
	}
}
