package ua.step.organaizer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.step.organaizer.enteties.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByEmail(String email);
}