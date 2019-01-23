package ua.squirrel.web.registration.user.service;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ua.squirrel.web.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findOneByLogin(String login);

}
