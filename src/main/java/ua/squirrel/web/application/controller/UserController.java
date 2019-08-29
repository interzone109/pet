package ua.squirrel.web.application.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {
	@RequestMapping(path = "/user/home", method = RequestMethod.GET)
	public String getUserHomePage(Authentication authentication) {
		/*if(authentication!= null) {
			return "user/userHome";
		}
		return "login";*/
		return  "user/userHome";
	}
	
	@RequestMapping(path = "/user/employee", method = RequestMethod.GET)
	public String getEmployeePageUser() {
		return "/user/employee";
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
