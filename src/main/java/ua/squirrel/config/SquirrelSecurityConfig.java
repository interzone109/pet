package ua.squirrel.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SquirrelSecurityConfig extends WebSecurityConfigurerAdapter {

	 @Autowired
     private H2ConsoleProperties console;

     @Override
     public void configure(HttpSecurity http) throws Exception {
         String path = this.console.getPath();
         String antPattern = (path.endsWith("/") ? path + "**" : path + "/**");
         HttpSecurity h2Console = http.antMatcher(antPattern);
         h2Console.csrf().disable();
         h2Console.httpBasic();
         h2Console.headers().frameOptions().sameOrigin();
         // config as you like
         http.authorizeRequests().anyRequest().permitAll();
     }
	
	
	
}
