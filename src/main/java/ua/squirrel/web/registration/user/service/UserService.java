package ua.squirrel.web.registration.user.service;

import java.util.Optional;

import ua.squirrel.entity.user.User;

public interface UserService {
	Optional<User> findOneByLogin(String login); 
}
