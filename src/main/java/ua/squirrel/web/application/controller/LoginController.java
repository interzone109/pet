package ua.squirrel.web.application.controller;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import ua.squirrel.web.entity.user.UserModel;

@RestController
@RequestMapping("/public/login/in")
@Slf4j
public class LoginController {
	
	
	
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
