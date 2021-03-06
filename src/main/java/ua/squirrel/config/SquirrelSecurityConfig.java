package ua.squirrel.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
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
public class SquirrelSecurityConfig extends WebSecurityConfigurerAdapter {

	  @Autowired
     private H2ConsoleProperties console; 
	 @Autowired
	 private UserDetailServiceImpl userDetailsService;

     @Override
     public void configure(HttpSecurity http) throws Exception {
    /*	String path = this.console.getPath();
         String antPattern = (path.endsWith("/h2") ? path + "**" : path + "/h2/**");
         HttpSecurity h2Console = http.antMatcher(antPattern);
         h2Console.csrf().disable();
         h2Console.httpBasic();
         h2Console.headers().frameOptions().sameOrigin();
         // config as you like
        // http.authorizeRequests().anyRequest().permitAll();*/
    	 
         	// предостовление доступа к адресам, определенным ролям
            http.authorizeRequests()
         // добавление шаблонов страниц, требующих авторизации
         .antMatchers("/","/registration", "/test","/css/**","/js/**").permitAll()
         .antMatchers("/user/**").hasAuthority("USER")
       .and()
         // указание формы для аутентификации
         .formLogin()
         .loginProcessingUrl("/j_spring_security_check") 
         .loginPage("/login")
         .defaultSuccessUrl("/user/home" )
         .failureUrl("/login?error=true")
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
