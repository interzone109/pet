package ua.squirrel.user.controller.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@RestController
@Slf4j
@RequestMapping("/user/report/data")
public class RepordController {
	@Autowired
	private UserServiceImpl userServiceImpl;
	

}
