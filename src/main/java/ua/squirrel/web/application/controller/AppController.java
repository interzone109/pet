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
		return "index";
	}
	
	
	@RequestMapping(path = "/public/login", method = RequestMethod.GET)
	public String login(Model model) {
		model.addAttribute("user",  new UserModel());
		return "login";
	}
	
	
	
	@RequestMapping(path = "/public/registration", method = RequestMethod.GET)
	public String registration(Model model) {
		model.addAttribute("user",  new UserModel());
		return "registration";
	}
	@RequestMapping(path = "/logout", method = RequestMethod.GET)
	public String logout(Model model) {
		SecurityContextHolder.clearContext();
		model.addAttribute("user",  new UserModel());
		return "logout";
	}
}
