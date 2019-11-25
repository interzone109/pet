package ua.squirrel.web.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class EmployeeController {
	
	
	@RequestMapping(path = "/employee/home", method = RequestMethod.GET)
	public String getEmployeePage() {
		return "employee/employeeHome";
	}
	
	@RequestMapping(path = "/employee/leftover", method = RequestMethod.GET)
	public String getStoreLeftovert() {
		return "employee/storeLeftover";
	}
	
	
	@RequestMapping(path = "/employee/cashbox/work", method = RequestMethod.GET)
	public String getCashBoxWork() {
		return "employee/cashBoxWork";
	}
	
	@RequestMapping(path = "/employee/consignment", method = RequestMethod.GET)
	public String getConsignment() {
		return "employee/consignment";
	}
}
