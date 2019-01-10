package ua.squirrel.web.registration.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/main")
@Slf4j
@Secured("USER")
public class MainController {
	
	@GetMapping
	String hello(Authentication authentication) {
		if(authentication!= null) {
			return " all ready login";
		}
		log.info("enter method get url:/main");
		return "main method";
	}
	
}
