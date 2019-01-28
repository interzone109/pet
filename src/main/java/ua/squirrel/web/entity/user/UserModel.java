package ua.squirrel.web.entity.user;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserModel {
	private String login;
	private String hashPass;
	private String repidPass;
	private String mail;
}