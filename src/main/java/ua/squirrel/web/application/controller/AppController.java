package ua.squirrel.web.application.controller;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ua.squirrel.web.entity.user.Role;
import ua.squirrel.web.entity.user.State;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.entity.user.UserModel;
import ua.squirrel.web.entity.user.UserSubscription;
import ua.squirrel.web.service.registration.RoleServiceImpl;
import ua.squirrel.web.service.registration.StateServiceImpl;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@Controller
public class AppController {
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private RoleServiceImpl roleServiceImpl;
	@Autowired
	private StateServiceImpl stateServiceImpl;

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
		return "landing/registr";
	}
	
	@RequestMapping(path = "/registration", method = RequestMethod.POST)
	public String registrationNewUser(UserModel userModel) {
		  Role role = roleServiceImpl.findOneByName(userModel.getSubscription());
		  Set<Role> roles = new HashSet<>();
		  roles.add(role);
		  UserSubscription userSubscription = new UserSubscription();
		if(role.equals("USER_FREE")) {
			userSubscription.setEmployeesQuantity(1);
			userSubscription.setStoreQuantity(1);
			userSubscription.setPartnerQuantity(5);
			userSubscription.setProductQuantity(50);
		}else if(role.equals("USER_STANDART")) {
			userSubscription.setEmployeesQuantity(5);
			userSubscription.setStoreQuantity(5);
			userSubscription.setPartnerQuantity(25);
			userSubscription.setProductQuantity(200);
		}
		Set<State> state = new HashSet<>();
		state.add(stateServiceImpl.findOneByName("ACTIVE"));
		User user = new User();
		user.setDate(LocalDate.now());
		user.setLogin(userModel.getLogin());
		user.setMail(userModel.getMail());
		user.setRoles(roles);
		user.setStates(state);
		userSubscription.setUser(user);
		user.setHashPass(new BCryptPasswordEncoder().encode(userModel.getRepidPass()));
 
		userServiceImpl.save(user);
		return "landing/registrSuccess";
	}
	
	
	@RequestMapping(path = "/logout", method = RequestMethod.GET)
	public String logout(Model model) {
		SecurityContextHolder.clearContext();
		model.addAttribute("user",  new UserModel());
		return "logout";
	}
}
