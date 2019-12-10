package ua.squirrel.web.entity.account;

 
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import ua.squirrel.web.entity.user.Role; 

@Data
public class AccountAppModel {

	private Set<Role> roles; 
	@NotNull
    @Size(min=6, max=20, message = "Не менее 6-ти знаков")
	private String login; 
	@NotNull
    @Size(min=8, max=20, message = "Не менее 8-ти знаков")
	private String password;
	@NotNull
    @Size(min=8, max=20, message = "Не менее 8-ти знаков")
	private String repidPassword;
	
}
