package ua.squirrel.web.registration.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import ua.squirrel.web.entity.user.entity.UserModel;
import ua.squirrel.web.registration.user.service.UserServiceImpl;

@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	
	@GetMapping
	String hello(Authentication authentication) {
		if(authentication!= null) {
			return " all ready login";
		}
		log.info("enter method get url:/login");
		return "login";
	}
	
	@PostMapping
	UserModel registr(@RequestBody UserModel user ) {
		log.info("enter method post url:/registr"+user.getLogin());
		System.out.println("login :"+user.getLogin());
		System.out.println("pass :"+user.getHashPass());
		return user;
	}
}
