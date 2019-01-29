package ua.squirrel.web.controller.registration;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.partner.Partner;
import ua.squirrel.web.entity.user.Role;
import ua.squirrel.web.entity.user.State;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.entity.user.UserModel;
import ua.squirrel.web.service.registration.RoleServiceImpl;
import ua.squirrel.web.service.registration.StateServiceImpl;
import ua.squirrel.web.service.registration.user.UserServiceImpl;
import ua.squirrel.z.util.FillDataUtil;
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
	@Autowired
	private FillDataUtil fillDataUtil ;

	@GetMapping
	public UserModel hello(Authentication authentication) {
	
		log.info("LOGGER: return new user model ");
		Set<Role> role = new HashSet<>();
		role.add(roleServiceImpl.findOneByName("USER"));
		Set<State> state = new HashSet<>();
		state.add(stateServiceImpl.findOneByName("ACTIVE"));

		User user1 = new User();
		user1.setLogin("test1");
		user1.setHashPass("user1");
		user1.setMail("user1@mail.com");
		user1.setRoles(role);
		user1.setStates(state);
		
		
		List<Partner> l = fillDataUtil.getPartner(user1);
		l.get(0).setUser(user1);
		l.get(1).setUser(user1);
		l.get(2).setUser(user1);
		
		userServiceImpl.save(user1);
		
		
		fillDataUtil.getProduct(user1);
		
		userServiceImpl.save(user1);
		
		return UserModel.builder()
				.login(user1.getLogin())
				.mail(user1.getMail())
				.build();
	}

	

	@PostMapping
	public UserModel registr(@RequestBody UserModel userModel) {
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

		return UserModel.builder()
				.login(user.getLogin())
				.mail(user.getMail())
				.build();
	}

}
