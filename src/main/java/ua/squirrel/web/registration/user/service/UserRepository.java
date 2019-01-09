package ua.squirrel.web.registration.user.service;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.entity.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findOneByLogin(String login); 
}
