package ua.squirrel.web.service.registration.user;

import java.util.Optional;

import ua.squirrel.web.entity.account.AccountApp;
import ua.squirrel.web.entity.user.User;

public interface UserService {
	Optional<User> findOneByAccount(AccountApp account);
	boolean existsByAccount(AccountApp account);
}
