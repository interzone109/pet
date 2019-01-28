package ua.squirrel.web.service.registration.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.web.entity.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findOneByLogin(String login);

}
