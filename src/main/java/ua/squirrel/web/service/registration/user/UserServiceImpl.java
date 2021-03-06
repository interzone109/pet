package ua.squirrel.web.service.registration.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.web.entity.user.User;

@Service
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	@Autowired
	public  UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	public void save(User user) {
		userRepository.save(user);
	}
	@Override
	public Optional<User> findOneByLogin(String login) {
		return userRepository.findOneByLogin(login);
	}
	public boolean existsByLoginOrMail(String login, String mail) {
		return userRepository.existsByLoginOrMail(login, mail);
	}
}
