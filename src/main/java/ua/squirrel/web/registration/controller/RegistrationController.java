package ua.squirrel.web.registration.controller;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
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

import ua.squirrel.entity.user.Role;
import ua.squirrel.entity.user.State;
import ua.squirrel.entity.user.User;
import ua.squirrel.web.registration.model.UserModel;
import ua.squirrel.web.registration.role.service.RoleRepository;
import ua.squirrel.web.registration.role.service.RoleServiceImpl;
import ua.squirrel.web.registration.state.service.StateRepository;
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
	UserModel hello(Authentication authentication) {
		logger.info("enter method get url:/registr");
		return new  UserModel();
	}
	
	@PostMapping
	User registr(@RequestBody UserModel userModel ) {
		logger.info("enter method post url:/registr"+userModel.getLogin());
		
		  Set<Role> role =   new HashSet<>();
		  role.add(roleServiceImpl.findOneByName("USER"));
		  Set<State> state =   new HashSet<>();
		  state.add(stateServiceImpl.findOneByName("ACTIVE"));
		
		User user = User.builder().login(userModel.getLogin())
				.hashPass(userModel.getHashPass())
				.roles(role)
				.states(state)
				.build();
		userServiceImpl.save(user);
		
		return user;
	}
}
