package ua.squirrel.web.controller.registration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AppController {

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String index() {
		return "index";
	}
		
	@RequestMapping(path = "/user", method = RequestMethod.GET)
	public String getUserPage() {
		return "user/userHome";
	}
	
	@RequestMapping(path = "/contacts", method = RequestMethod.GET)
	public String getUserPartners() {
		return "user/partners";
	}
}
