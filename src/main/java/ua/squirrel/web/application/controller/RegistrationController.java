package ua.squirrel.web.application.controller;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.partner.Partner;
import ua.squirrel.web.entity.user.Role;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.entity.user.UserModel;
import ua.squirrel.web.entity.user.UserSubscription;
import ua.squirrel.web.service.registration.RoleServiceImpl;
import ua.squirrel.web.service.registration.UserSubscriptionServiceImpl;
import ua.squirrel.web.service.registration.user.UserServiceImpl;
import ua.squirrel.z.util.FillDataUtil;
/**
 * @author Maksim Gromko
 * контролер регистрации пользователя
 * */
@RestController
@RequestMapping("/test")
@Slf4j
public class RegistrationController {
	

	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private RoleServiceImpl roleServiceImpl;
	@Autowired
	private FillDataUtil fillDataUtil ;
	@Autowired
	private UserSubscriptionServiceImpl userSubscriptionServiceImpl;

	@GetMapping
	public UserModel hello(Authentication authentication) {
	
		log.info("LOGGER: return new user model ");
		Set<Role> role = new HashSet<>();
		role.add(roleServiceImpl.findOneByName("USER"));
		
		UserSubscription userSubscription = new UserSubscription();
		userSubscription.setEmployeesCurrentQuantity(0);
		userSubscription.setEmployeesQuantity(1);
		userSubscription.setStoreCurrentQuantity(0);
		userSubscription.setStoreQuantity(1);
		userSubscription.setPartnerCurrentQuantity(0);
		userSubscription.setPartnerQuantity(2);
		userSubscription.setProductCurrentQuantity(0);
		userSubscription.setProductQuantity(2);
		

		User user1 = new User();
		user1.setUserSubscription(userSubscription);
		user1.setLogin("test1");
		user1.setHashPass(new BCryptPasswordEncoder().encode("user1") );
		user1.setMail("user1@mail.com");
		user1.setRoles(role);
		
		userSubscription.setUser(user1);
		//userSubscriptionServiceImpl.save(userSubscription);
		userServiceImpl.save(user1);
		
		List<Partner> l = fillDataUtil.getPartner(user1);
		l.get(0).setUser(user1);
		l.get(1).setUser(user1);
		l.get(2).setUser(user1);
		
		fillDataUtil.getProduct(user1);
		
		//userServiceImpl.save(user1);
		
		fillDataUtil.getStore(user1);
		
		//fillDataUtil.setSpend(user1);
		
		//fillDataUtil.setInvoice(user1);
		
		//fillDataUtil.setEmployee(user1);
		UserModel us = new UserModel();
		us.setLogin(user1.getLogin());
		us.setHashPass(user1.getHashPass());
		us.setMail(user1.getMail());
		
		return us;
	}

	

}
