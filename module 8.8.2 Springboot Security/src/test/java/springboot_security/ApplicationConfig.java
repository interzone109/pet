package springboot_security;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class ApplicationConfig { 
 
    @Autowired 
    private DataSource dataSource; 
 
    public ApplicationConfig(DataSource dataSource) { 
        this.dataSource = dataSource; 
    } 
 
    @Bean("clientRepository") 
    ClientRepository jpaClientRepository() { 
        return new JpaClientRepository(); 
    } 
} 