package ua.step.organaizer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	// предостовление доступа к адресам, определенным ролям
        http.authorizeRequests()
        // добавление шаблонов страниц, требующих авторизации
        .antMatchers("/", "/public/**").permitAll()
        .antMatchers("/users/**").hasAuthority("ADMIN")
        .antMatchers("/emp/**").hasAuthority("USER")
        .anyRequest().fullyAuthenticated()
        .and()
        // указание формы для аутентификации
        .formLogin()
        .loginPage("/login")
        .failureUrl("/login?error")
        .usernameParameter("email")
        .permitAll()
        .and()
        // указание страницы отображающейся при выходе
        .logout()
        .logoutUrl("/logout")
        .deleteCookies("remember-me")
        .logoutSuccessUrl("/")
        .permitAll()
        .and()
        .rememberMe();
    }

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// задание способа шифрования пароля
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}
}