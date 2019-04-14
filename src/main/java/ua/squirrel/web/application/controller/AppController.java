package ua.squirrel.web.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AppController {

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String index() {
		return "index";
	}
	
	@RequestMapping(path = "/user", method = RequestMethod.GET)
	public String getUserPage() {
		return "user/userHome";
	}
	
	@RequestMapping(path = "/user/contacts", method = RequestMethod.GET)
	public String getUserPartners() {
		return "user/partners";
	}
	
	@RequestMapping(path = "/user/products", method = RequestMethod.GET)
	public String getUserProducts() {
		return "user/products";
	}
	
	@RequestMapping(path = "/user/storePrice", method = RequestMethod.GET)
	public String getStore() {
		return "user/storePrice";
	}
}
