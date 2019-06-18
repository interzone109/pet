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
	
	@RequestMapping(path = "/user/consignment", method = RequestMethod.GET)
	public String getStoreConsignment() {
		return "user/storeConsignment";
	}
	
	@RequestMapping(path = "/user/leftover", method = RequestMethod.GET)
	public String getStoreLeftovert() {
		return "user/storeLeftover";
	}
	
	@RequestMapping(path = "/user/cashbox", method = RequestMethod.GET)
	public String getCashBox() {
		return "user/cashBox";
	}
	@RequestMapping(path = "/user/cashbox/work", method = RequestMethod.GET)
	public String getCashBoxWork() {
		return "user/cashBoxWork";
	}
}
