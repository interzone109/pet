package ua.squirrel.web.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AppController {

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String index() {
		return "index";
	}
	
	
	@RequestMapping(path = "/public/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}
	
	@RequestMapping(path = "/public/registration", method = RequestMethod.GET)
	public String registration() {
		return "registration";
	}
	
}
