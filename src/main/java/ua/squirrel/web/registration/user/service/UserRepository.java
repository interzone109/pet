package ua.squirrel.web.registration.user.service;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ua.squirrel.web.entity.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findOneByLogin(String login); 
	
	@Query("SELECT p FROM User p JOIN FETCH p.partners WHERE p.id = (:id)")
    public User findByIdAndFetchPartnersEagerly( Long id);
}
