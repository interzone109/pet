package ua.squirrel.web.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.entity.user.UserModel;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@Controller
public class UserController {
	@Autowired
	  private UserServiceImpl userServiceImpl;
	
	
	@RequestMapping(path = "/user/home", method = RequestMethod.GET)
	public String getUserHomePage(Authentication authentication, Model model) {
		User user = userServiceImpl.findOneByLogin(authentication.getName()).get();
		System.err.println(authentication.getPrincipal().toString());
		UserModel userModel = new UserModel ();
		userModel.setLogin(user.getLogin());
		/*userModel.setLastPatyDate(user.getUserSubscription().getLastPatyDate());
		 userModel.setDeadlineDate(user.getUserSubscription().getDeadlineDate());*/
		userModel.setStoreQuantity(user.getUserSubscription().getStoreQuantity());
		userModel.setEmployeesQuantity(user.getUserSubscription().getEmployeesQuantity());
		userModel.setPartnerQuantity(user.getUserSubscription().getPartnerQuantity());
		userModel.setProductQuantity(user.getUserSubscription().getProductQuantity());
		userModel.setPrice(user.getUserSubscription().getPrice());
				
		
		
		model.addAttribute("userModel", userModel);
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
	
	@RequestMapping(path = "/user/report", method = RequestMethod.GET)
	public String getUserreport() {
		return "user/report";
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
	@RequestMapping(path = "/user/spends", method = RequestMethod.GET)
	public String getSpends() {
		return "user/spends";
	}
	
	
}
