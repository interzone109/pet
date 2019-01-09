package ua.squirrel.web.registration.controller;
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
import ua.squirrel.web.registration.user.service.UserServiceImpl;


@RestController
@RequestMapping("/registr")
public class RegistrationController {
	private Logger logger = LoggerFactory.getLogger(RegistrationController.class);
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	
	@GetMapping
	UserModel hello(Authentication authentication) {
		logger.info("enter method get url:/registr");
		return new  UserModel();
	}
	
	@PostMapping
	User registr(@RequestBody UserModel userModel ) {
		logger.info("enter method post url:/registr"+userModel.getLogin());
		User user = User.builder().login(userModel.getLogin())
				.hashPass(userModel.getHashPass())
				//.role(Role.USER)
				//.state(State.ACTIVE)
				.build();
		userServiceImpl.save(user);
		
		return user;
	}
}
