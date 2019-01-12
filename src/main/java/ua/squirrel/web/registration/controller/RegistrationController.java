package ua.squirrel.web.registration.controller;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ua.squirrel.web.entity.user.Role;
import ua.squirrel.web.entity.user.State;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.registration.model.UserModel;
import ua.squirrel.web.registration.role.service.RoleServiceImpl;
import ua.squirrel.web.registration.state.service.StateServiceImpl;
import ua.squirrel.web.registration.user.service.UserServiceImpl;


@RestController
@RequestMapping("/registr")
public class RegistrationController {
	private Logger logger = LoggerFactory.getLogger(RegistrationController.class);
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private StateServiceImpl stateServiceImpl;
	@Autowired
	private  RoleServiceImpl roleServiceImpl;
	
	
	@GetMapping
	public String hello(Authentication authentication) {
		Set<Role> role =   new HashSet<>();
		  role.add(roleServiceImpl.findOneByName("USER"));
		  Set<State> state =   new HashSet<>();
		  state.add(stateServiceImpl.findOneByName("ACTIVE"));
		
		User user = new User();
		user.setLogin("test");
		user.setHashPass("user");
		user.setRoles(role);
		user.setMail("user@mail.com");
		user.setStates(state);
		
		userServiceImpl.save(user);
		
		return "user create";
	}
	
	@PostMapping
	public User registr(@RequestBody UserModel userModel ) {
		logger.info("enter method post url:/registr"+userModel.getLogin());
		
		Set<Role> role =   new HashSet<>();
		  role.add(roleServiceImpl.findOneByName("USER"));
		  Set<State> state =   new HashSet<>();
		  state.add(stateServiceImpl.findOneByName("ACTIVE"));
		
		User user = new User();
		user.setLogin("test");
		user.setHashPass("user");
		user.setRoles(role);
		user.setMail("user@mail.com");
		user.setStates(state);
		
		return user;
	}
}
