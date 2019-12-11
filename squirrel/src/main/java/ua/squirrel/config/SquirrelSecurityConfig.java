package ua.squirrel.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SquirrelSecurityConfig  extends WebSecurityConfigurerAdapter{
	
	 @Autowired
	 private AccountDetailServiceImpl accountDetailServiceImpl;

     @Override
     public void configure(HttpSecurity http) throws Exception {
    	 
         	// предостовление доступа к адресам, определенным ролям
            http.authorizeRequests()
         // добавление шаблонов страниц, требующих авторизации
         .antMatchers("/","/registration", "/test","/css/**","/js/**").permitAll()
         .antMatchers("/route").hasAnyAuthority("USER_ROLE","EMPLOYEE_WITH_ACCESS")
         .antMatchers("/user/**").hasAuthority("USER_ROLE")
         .antMatchers("/employee/**").hasAuthority("EMPLOYEE_WITH_ACCESS")
       .and()
         // указание формы для аутентификации
         .formLogin()
         .loginProcessingUrl("/j_spring_security") 
         .loginPage("/login")
         .defaultSuccessUrl("/route" )
         .failureUrl("/login?error_user=true")
         .usernameParameter("login")
         .passwordParameter("password")
         .permitAll()
         .and()
         // указание страницы отображающейся при выходе
         .logout()
         .logoutUrl("/logout")
         .logoutSuccessUrl("/logout")
         .deleteCookies("remember-me")
         .logoutSuccessUrl("/")
         .permitAll()
         .and()
         .rememberMe()
         .and().csrf().disable();
     }
	
    
     @Override
 	public void configure(AuthenticationManagerBuilder auth) throws Exception {
 		// задание способа шифрования пароля
 		auth.userDetailsService(accountDetailServiceImpl).passwordEncoder(new BCryptPasswordEncoder());
 	}
	
	
	
	
	
/***
	 @Configuration
	 @Order(1)
	public class UserSecurityConfig  extends WebSecurityConfigurerAdapter {

	  @Autowired
     private H2ConsoleProperties console; 
	 @Autowired
	 private UserDetailServiceImpl userDetailsService;

     @Override
     public void configure(HttpSecurity http) throws Exception {
    	//String path = this.console.getPath();
        // String antPattern = (path.endsWith("/h2") ? path + "**" : path + "/h2/**");
        // HttpSecurity h2Console = http.antMatcher(antPattern);
        // h2Console.csrf().disable();
        // h2Console.httpBasic();
        // h2Console.headers().frameOptions().sameOrigin();
         // config as you like
        // http.authorizeRequests().anyRequest().permitAll();
    	 
         	// предостовление доступа к адресам, определенным ролям
            http.authorizeRequests()
         // добавление шаблонов страниц, требующих авторизации
         .antMatchers("/","/registration", "/test","/css/**","/js/**").permitAll()
         .antMatchers("/user/**").hasAuthority("USER")
         .antMatchers("/employee/**").hasAuthority("EMPLOYEE")
       .and()
         // указание формы для аутентификации
         .formLogin()
         .loginProcessingUrl("/j_spring_security_check") 
         .loginPage("/login")
         .defaultSuccessUrl("/user/home" )
         .failureUrl("/login?error_user=true")
         .usernameParameter("login")
         .passwordParameter("hashPass")
         .permitAll()
         .and()
         // указание страницы отображающейся при выходе
         .logout()
         .logoutUrl("/logout")
         .logoutSuccessUrl("/logout")
         .deleteCookies("remember-me")
         .logoutSuccessUrl("/")
         .permitAll()
         .and()
         .rememberMe()
         .and().csrf().disable();
     }
	
    
     @Override
 	public void configure(AuthenticationManagerBuilder auth) throws Exception {
 		// задание способа шифрования пароля
 		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
 	}
	
}
	
	 @Configuration
	 @Order(2)
	public class EmployeeSecurityConfig  extends WebSecurityConfigurerAdapter {
 
	 @Autowired
	 private EmployeeDetailServiceImpl employeeDetailServiceImpl;

     @Override
     public void configure(HttpSecurity http) throws Exception {
         	// предостовление доступа к адресам, определенным ролям
            http.authorizeRequests()
         // добавление шаблонов страниц, требующих авторизации
         .antMatchers("/","/registration", "/test","/css/**","/js/**").permitAll()
         .antMatchers("/employee/**").hasAuthority("EMPLOYEE")
       .and()
         // указание формы для аутентификации
         .formLogin()
         .loginProcessingUrl("/j_spring_security_check") 
         .loginPage("/login")
         .defaultSuccessUrl("/employee/home" )
         .failureUrl("/login?error_employee=true")
         .usernameParameter("login")
         .passwordParameter("hashPass")
         .permitAll()
         .and()
         // указание страницы отображающейся при выходе
         .logout()
         .logoutUrl("/logout")
         .logoutSuccessUrl("/logout")
         .deleteCookies("remember-me")
         .logoutSuccessUrl("/")
         .permitAll()
         .and()
         .rememberMe()
         .and().csrf().disable();
     }
	
    
     @Override
 	public void configure(AuthenticationManagerBuilder auth) throws Exception {
 		// задание способа шифрования пароля
 		auth.userDetailsService(employeeDetailServiceImpl).passwordEncoder(new BCryptPasswordEncoder());
 	}
	
}

****/
}
