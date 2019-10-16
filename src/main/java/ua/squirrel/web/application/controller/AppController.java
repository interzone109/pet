package ua.squirrel.web.application.controller;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ua.squirrel.web.entity.user.Role; 
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.entity.user.UserModel;
import ua.squirrel.web.entity.user.UserSubscription;
import ua.squirrel.web.service.registration.RoleServiceImpl;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@Controller
public class AppController {
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private RoleServiceImpl roleServiceImpl;

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String index() {
		return "landing/index";
	}
	
	
	@RequestMapping(path = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		model.addAttribute("user",  new UserModel());
		return "landing/login";
	}
	
	
	
	@RequestMapping(path = "/registration", method = RequestMethod.GET)
	public String registration(Model model) {
		model.addAttribute("user",  new UserModel());
		model.addAttribute("state", "new");
		return "landing/registr";
	}
	
	@RequestMapping(path = "/registration", method = RequestMethod.POST)
	public String registrationNewUser(@ModelAttribute("user")@Valid UserModel userModel,BindingResult bindingResult,Model model) {
		boolean exist = userServiceImpl.existsByLoginOrMail(userModel.getLogin(), userModel.getMail());
		if (bindingResult.hasErrors()) {
			model.addAttribute("state", "fail");
			return "landing/registr";
        }else if(exist) {
        	 model.addAttribute("erorr", "existUser");
        	 model.addAttribute("state", "fail");
        	return "landing/registr";
        }else if( !userModel.getHashPass().equals(userModel.getRepidPass())) {
        	model.addAttribute("erorr", "notValidPass");
        	model.addAttribute("state", "fail");
        	return "landing/registr";
        }
		
		
		  Role role = roleServiceImpl.findOneByName("USER");
		  Set<Role> roles = new HashSet<>();
		  roles.add(role);
		  UserSubscription userSubscription = new UserSubscription();
		if(userModel.getSubscription().equals("USER_FREE")) {
			userSubscription.setEmployeesQuantity(1);
			userSubscription.setStoreQuantity(1);
			userSubscription.setPartnerQuantity(5);
			userSubscription.setProductQuantity(50);
		}else if(userModel.getSubscription().equals("USER_STANDART")) {
			userSubscription.setEmployeesQuantity(5);
			userSubscription.setStoreQuantity(5);
			userSubscription.setPartnerQuantity(25);
			userSubscription.setProductQuantity(200);
		} 
		userSubscription.setEmployeesCurrentQuantity(0);
		userSubscription.setStoreCurrentQuantity(0); 
		userSubscription.setPartnerCurrentQuantity(0); 
		userSubscription.setProductCurrentQuantity(0);
		
		User user = new User();
		user.setDate(LocalDate.now());
		user.setLogin(userModel.getLogin());
		user.setMail(userModel.getMail());
		user.setRoles(roles);
		user.setUserSubscription(userSubscription); 
		userSubscription.setUser(user);
		user.setHashPass(new BCryptPasswordEncoder().encode(userModel.getRepidPass()));
		try {
			userServiceImpl.save(user);
		}catch(Exception e) {
			model.addAttribute("state", "fail");
		}
		
		model.addAttribute("state", "success");
		return "landing/registr";
	}
	
	
	@RequestMapping(path = "/logout", method = RequestMethod.GET)
	public String logout(Model model) {
		SecurityContextHolder.clearContext();
		model.addAttribute("user",  new UserModel());
		return "logout";
	}
}
