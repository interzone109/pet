package ua.squirrel.web.registration.user.service;

import java.util.Optional;

import ua.squirrel.web.entity.user.User;

public interface UserService {
	Optional<User> findOneByLogin(String login); 
	public User findByIdAndFetchPartnersEagerly( Long id);
}
