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
import ua.squirrel.user.assortment.partner.Partner;
import ua.squirrel.user.assortment.partner.service.PartnerServiceImpl;
import ua.squirrel.user.assortment.product.Product;
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
	public User hello(Authentication authentication) {

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
		l.get(0).setUserOwner(user1);
		l.get(1).setUserOwner(user1);
		user1.setPartners(l);
		
		userServiceImpl.save(user1);
		
		log.info("LOGGER: return new user model ");
		return user1;
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
	
	
	

	@Autowired
	private PartnerServiceImpl partnerServiceImpl;
	
	private List<Partner> getPartner() {
		List<Partner> partner = new ArrayList<>();
		
		for (int i = 0; i < 2; i++) {
			Partner p = new Partner();
			p.setCompany("partner "+i);
			p.setPartnerMail("partner"+i+"@mail.com");
			p.setPhonNumber("21313");
			partner.add(p);
		}
	
		return partner;
	}



	private List<Product> getProduct() {
		List<Product> product = new ArrayList<>();
		
		for (int i = 0; i < 2; i++) {
		Product p = new Product();
		p.setName("food "+i);
		p.setDescription("desc "+i);
		p.setPrice(12.5f+i);
		product.add(p);
		}
		
		return product;
	}
	
}
