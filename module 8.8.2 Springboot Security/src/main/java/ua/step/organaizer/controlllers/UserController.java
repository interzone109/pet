package ua.step.organaizer.controlllers;

import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ua.step.organaizer.dto.UserCreateForm;
import ua.step.organaizer.services.user.UserService;
import ua.step.organaizer.validators.UserCreateFormValidator;

@Controller
public class UserController {

	private final UserService userService;
	private final UserCreateFormValidator userCreateFormValidator;

	@Autowired
	public UserController(UserService userService, UserCreateFormValidator userCreateFormValidator) {
		this.userService = userService;
		this.userCreateFormValidator = userCreateFormValidator;
	}
	
	// анотация связывает поля фрмы с валидатором
	@InitBinder("form")
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(userCreateFormValidator);
	}

	@RequestMapping("/user/{id}")
	public ModelAndView getUserPage(@PathVariable Long id) {
		return new ModelAndView("user", "user", userService.getUserById(id)
				.orElseThrow(() -> new NoSuchElementException(String.format("User=%s not found", id))));
	}
	@RequestMapping("/emp")
	public ModelAndView getUserPage() {
		return new ModelAndView("users", "users", userService.getAllUsers());
	}

	@RequestMapping(value = "/user/create", method = RequestMethod.GET)
	public ModelAndView getUserCreatePage() {
		return new ModelAndView("user_create", "form", new UserCreateForm());
	}

	@RequestMapping(value = "/user/create", method = RequestMethod.POST)
	public String handleUserCreateForm(@Valid @ModelAttribute("form") UserCreateForm form,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "user_create";
		}
		try {
			userService.create(form);
		} catch (DataIntegrityViolationException e) {
			bindingResult.reject("email.exists", "Email already exists");
			return "user_create";
		}
		return "redirect:/users";
	}

}