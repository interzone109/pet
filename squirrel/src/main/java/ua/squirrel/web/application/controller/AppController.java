package ua.squirrel.web.application.controller;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ua.squirrel.user.entity.employee.EmployeeModel;
import ua.squirrel.web.entity.account.AccountApp;
import ua.squirrel.web.entity.account.AccountAppModel;
import ua.squirrel.web.entity.user.Role; 
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.entity.user.UserModel;
import ua.squirrel.web.entity.user.UserSubscription;
import ua.squirrel.web.service.account.AccountAppServiceImpl;
import ua.squirrel.web.service.registration.RoleServiceImpl;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@Controller
public class AppController {
	@Autowired
	private AccountAppServiceImpl accountAppServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private RoleServiceImpl roleServiceImpl;

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String index() {
		return "landing/index";
	}
	
	@RequestMapping(path = "/route", method = RequestMethod.GET)
	public String route(Authentication authentication) {
		 Optional<AccountApp> accountOption = accountAppServiceImpl.findOneByLogin(authentication.getName());
		 
		if(accountOption.isPresent()) {
			
			AccountApp account = accountOption.get();
			
			for(Role role:account.getRoles()){
				if(role.getName().startsWith("USER_ROLE")) {
					return "redirect:user/home";
				}else if(role.getName().equals("EMPLOYEE_WITH_ACCESS")) {
					return "redirect:employee/home";
				}
			}
			
		
		}
		return "redirect:login";
	}
	
	@RequestMapping(path = "/login", method = RequestMethod.GET)
	public String login(Model model) {
	 
		model.addAttribute("account",new AccountAppModel());
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
		boolean exist = accountAppServiceImpl.existsByLogin(userModel.getAccountAppModel().getLogin());
		if (bindingResult.hasErrors()) {
			
			model.addAttribute("state", "fail");
			return "landing/registr";
        }else if(exist) {
        	 model.addAttribute("erorr", "existUser");
        	 model.addAttribute("state", "fail");
        	return "landing/registr";
        }else if( !userModel.getAccountAppModel().getPassword().equals(userModel.getAccountAppModel().getRepidPassword())) {
        	model.addAttribute("erorr", "notValidPass");
        	model.addAttribute("state", "fail");
        	return "landing/registr";
        }
		
		
		  Role role = roleServiceImpl.findOneByName("USER_ROLE");
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
		
		AccountApp account = new AccountApp();
		account.setLogin(userModel.getAccountAppModel().getLogin());
		account.setPassword(new BCryptPasswordEncoder().encode(userModel.getAccountAppModel().getPassword()));
		account.setRoles(roles);
		
		User user = new User();
		user.setDate(LocalDate.now());
		user.setUserSubscription(userSubscription); 
		userSubscription.setUser(user);
		user.setMail(userModel.getMail());
		user.setAccount(account);
		
		
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
		model.addAttribute("employee",   EmployeeModel.builder().build());
		return "logout";
	}
}
