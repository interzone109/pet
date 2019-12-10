package ua.squirrel.web.service.registration.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.web.entity.account.AccountApp;
import ua.squirrel.web.entity.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findOneByAccount(AccountApp account);
	boolean existsByAccount(AccountApp account);
}
