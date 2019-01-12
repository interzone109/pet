package ua.squirrel.web.registration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.squirrel.user.assortment.partner.Partner;
import ua.squirrel.user.assortment.product.Product;
import ua.squirrel.user.assortment.product.service.ProductServiceImpl;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.registration.user.service.UserServiceImpl;


@RestController
@RequestMapping("/testProduct")
public class TestProduct {
	@Autowired
	private ProductServiceImpl productServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;
	@GetMapping
	public String hello() {
		
		Partner partner = new Partner();
		partner.setCompany("картошечные братишки");
		partner.setPartnerMail("Potato@potatomail.com");
		User user = null ;
		try {
		 user =   userServiceImpl.findOneByLogin("test").get();
		}catch (Exception e) {
			System.out.println("user fail");
		}
		Product productToSave = new Product();
		productToSave.setName("картошечка");
		productToSave.setDescription("очень укусная картошечка фри");
		productToSave.setUser(user);
		productToSave.setPartner(partner);
		
		try {
		productServiceImpl.save(productToSave);
		}catch (Exception e) {
			System.out.println("тут беда !!!!!!");
			System.out.println(productToSave.getPartner());
			System.out.println(productToSave.getUser());
		}
	
		return "product save";
	}
}
