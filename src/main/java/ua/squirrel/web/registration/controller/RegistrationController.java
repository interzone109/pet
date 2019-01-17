package ua.squirrel.web.registration.controller;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import ua.squirrel.web.app.controller.ProductController;
import ua.squirrel.web.entity.user.Role;
import ua.squirrel.web.entity.user.State;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.registration.model.UserModel;
import ua.squirrel.web.registration.role.service.RoleServiceImpl;
import ua.squirrel.web.registration.state.service.StateServiceImpl;
import ua.squirrel.web.registration.user.service.UserServiceImpl;
/**
 * @author Maksim Gromko
 * контролер регистрации пользователя
 * */
@RestController
@RequestMapping("/")
@Slf4j
public class RegistrationController {
	

	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private StateServiceImpl stateServiceImpl;
	@Autowired
	private RoleServiceImpl roleServiceImpl;

	@GetMapping
	public UserModel hello(Authentication authentication) {

		Set<Role> role = new HashSet<>();
		role.add(roleServiceImpl.findOneByName("USER"));
		Set<State> state = new HashSet<>();
		state.add(stateServiceImpl.findOneByName("ACTIVE"));

		User user1 = new User();
		user1.setLogin("test1");
		user1.setHashPass("user1");
		user1.setRoles(role);
		user1.setMail("user1@mail.com");
		user1.setStates(state);

		userServiceImpl.save(user1);
		
		role = new HashSet<>();
		role.add(roleServiceImpl.findOneByName("USER"));
		
		state = new HashSet<>();
		state.add(stateServiceImpl.findOneByName("ACTIVE"));
		
		User user2 = new User();
		user2.setLogin("test2");
		user2.setHashPass("user2");
		user2.setRoles(role);
		user2.setMail("user2@mail.com");
		user2.setStates(state);

		userServiceImpl.save(user2);

		log.info("LOGGER: return new user model ");
		return new UserModel();
	}

	@PostMapping
	public User registr(@RequestBody UserModel userModel) {
		log.info("LOGGER: regist new user " + userModel.getLogin());

		Set<Role> role = new HashSet<>();
		role.add(roleServiceImpl.findOneByName("USER"));
		Set<State> state = new HashSet<>();
		state.add(stateServiceImpl.findOneByName("ACTIVE"));

		User user = new User();
		user.setLogin(userModel.getLogin());
		user.setHashPass(userModel.getHashPass());
		user.setRoles(role);
		user.setMail(userModel.getMail());
		user.setStates(state);

		return user;
	}
}
