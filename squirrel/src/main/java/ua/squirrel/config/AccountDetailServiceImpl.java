package ua.squirrel.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ua.squirrel.web.entity.account.AccountApp;
import ua.squirrel.web.service.account.AccountAppServiceImpl;

@Service
public class AccountDetailServiceImpl implements UserDetailsService  {

	 @Autowired
	  private AccountAppServiceImpl accountAppServiceImpl;

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		AccountApp accountApp = accountAppServiceImpl.findOneByLogin(login).orElseThrow(() -> new UsernameNotFoundException("user not found"));
		accountApp.getRoles().forEach(role->{
			System.err.println(role.getName());
		});
		// указываем роли для этого пользователя
     Set<GrantedAuthority> roles = new HashSet<>();
     accountApp.getRoles().forEach(role->roles.add(new SimpleGrantedAuthority(role.getName())) );
      
       // на основании полученных данных формируем объект UserDetails
       // который позволит проверить введенный пользователем логин и пароль
       // и уже потом аутентифицировать пользователя
       UserDetails userDetails =
               new org.springframework.security.core.userdetails.User(accountApp.getLogin(), 
            		   												  accountApp.getPassword(), 
                                                                      roles);

       return userDetails;
	}


	
}
