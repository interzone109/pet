package ua.step.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ua.step.spring.entity.User;
import ua.step.spring.entity.UserRoleEnum;

import java.util.HashSet;
import java.util.Set;

/**
 * Security.
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userService.getUser("root");
		Set<GrantedAuthority> roles = new HashSet<>();
		roles.add(new SimpleGrantedAuthority(UserRoleEnum.USER.name()));

		UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getLogin(),
				user.getPassword(), roles);

		return userDetails;
	}
}