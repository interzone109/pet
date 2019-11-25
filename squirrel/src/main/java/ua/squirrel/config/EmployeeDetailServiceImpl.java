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

import ua.squirrel.user.entity.employee.Employee;
import ua.squirrel.user.service.employee.EmployeeServiceImpl;

@Service
public class EmployeeDetailServiceImpl implements UserDetailsService  {

	 @Autowired
	  private EmployeeServiceImpl employeeServiceImpl;

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		Employee employee = employeeServiceImpl.findOneByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Employee not found"));
		// указываем роли для этого пользователя
      Set<GrantedAuthority> roles = new HashSet<>();
      roles.add(new SimpleGrantedAuthority(employee.getRole().getName()));
       
        // на основании полученных данных формируем объект UserDetails
        // который позволит проверить введенный пользователем логин и пароль
        // и уже потом аутентифицировать пользователя
        UserDetails userDetails =
                new org.springframework.security.core.userdetails.User(employee.getLogin(), 
                													    employee.getPassword(), 
                                                                       roles);
		return userDetails ; 
	}

}
