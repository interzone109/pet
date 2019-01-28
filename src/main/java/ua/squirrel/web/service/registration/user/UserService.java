package ua.squirrel.web.service.registration.user;

import java.util.Optional;

import ua.squirrel.web.entity.user.User;

public interface UserService {
	Optional<User> findOneByLogin(String login); 
}
