package ua.squirrel.web.service.account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.web.entity.account.AccountApp;

public interface AccountAppRepository extends JpaRepository<AccountApp, Long> {

	Optional<AccountApp> findOneByLogin(String login);
	
	boolean existsByLogin(String login);
}
