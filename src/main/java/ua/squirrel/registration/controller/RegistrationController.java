package ua.squirrel.registration.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class RegistrationController {
	private Logger logger = LoggerFactory.getLogger(RegistrationController.class);
	
	@GetMapping
	String hello() {
		logger.info("enter method get url:/");
		return "HELLO";
	}
}
