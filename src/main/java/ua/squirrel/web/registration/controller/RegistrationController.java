package ua.squirrel.web.registration.controller;

import java.util.ArrayList;
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
import ua.squirrel.user.partner.Partner;
import ua.squirrel.user.product.Product;
import ua.squirrel.web.entity.user.Role;
import ua.squirrel.web.entity.user.State;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.entity.user.UserModel;
import ua.squirrel.web.registration.service.RoleServiceImpl;
import ua.squirrel.web.registration.service.StateServiceImpl;
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
		log.info("LOGGER: return new user model ");
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

		List<Partner> l = getPartner();
		l.get(0).setUser(user1);
		l.get(1).setUser(user1);
		user1.setPartners(l);
			
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
	

	
	/**
	 * 
	 * 
	 * тестовое заполнение полей пользователя
	 * данными о партнерах и их продуктах
	 * 
	 * */
	
	private List<Partner> getPartner() {
		List<Partner> partner = new ArrayList<>();
		
		for (int i = 0; i < 2; i++) {
			Partner p = new Partner();
			p.setCompany("partner "+i);
			p.setPartnerMail("partner"+i+"@mail.com");
			p.setProducts(getProduct(p));
			p.setPhonNumber("21313");
			partner.add(p);
		}
	
		return partner;
	}



	private List<Product> getProduct(Partner partner) {
		List<Product> product = new ArrayList<>();
		
		for (int i = 0; i < 2; i++) {
		Product p = new Product();
		p.setName("food "+i);
		p.setDescription("desc "+i);
		p.setPrice(12.5f+i);
		p.setPartner(partner);
		product.add(p);
		}
		
		return product;
	}
	
}
