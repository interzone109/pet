package ua.squirrel.web.service.account;

import java.util.Optional;

import ua.squirrel.web.entity.account.AccountApp;

public interface AccountAppService {
	Optional<AccountApp> findOneByLogin(String login); 
	boolean existsByLogin(String login);
}
