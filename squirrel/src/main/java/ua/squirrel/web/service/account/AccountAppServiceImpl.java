package ua.squirrel.web.service.account;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.web.entity.account.AccountApp;

@Service
public class AccountAppServiceImpl implements AccountAppService {
	private final AccountAppRepository accountAppRepository;

	@Autowired
	public  AccountAppServiceImpl(AccountAppRepository accountAppRepository) {
		this.accountAppRepository = accountAppRepository;
	}
	
	public void save(AccountApp accountApp) {
		accountAppRepository.save(accountApp);
	}
	@Override
	public Optional<AccountApp> findOneByLogin(String login) {
		return accountAppRepository.findOneByLogin(login);
	}

	@Override
	public boolean existsByLogin(String login) {
		return accountAppRepository.existsByLogin(login);
	}
}
