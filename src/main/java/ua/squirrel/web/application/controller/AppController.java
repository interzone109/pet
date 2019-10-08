package ua.squirrel.web.application.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ua.squirrel.web.entity.user.UserModel;

@Controller
public class AppController {

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
		 
		return "landing/registrSuccess";
	}
	
	
	@RequestMapping(path = "/logout", method = RequestMethod.GET)
	public String logout(Model model) {
		SecurityContextHolder.clearContext();
		model.addAttribute("user",  new UserModel());
		return "logout";
	}
}
