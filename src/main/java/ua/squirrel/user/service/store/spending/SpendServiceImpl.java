package ua.squirrel.user.service.store.spending;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.spending.Spend;
import ua.squirrel.web.entity.user.User;

@Service
public class SpendServiceImpl implements SpendService {
	
	private SpendRepository spendRepository;
	
	@Autowired
	public SpendServiceImpl(SpendRepository spendRepository) {
		this.spendRepository = spendRepository;
	}

	@Override
	public List<Spend> findAllByUserOrderByDateAsc(User user) {
		return spendRepository.findAllByUserOrderByDateAsc(user);
	}

	@Override
	public List<Spend> findAllByStoreOrderByDateAsc(Store store) {
		return spendRepository.findAllByStoreOrderByDateAsc(store);
	}

	@Override
	public Optional<Spend> findOneByUserAndIdOrderByDateAsc(User user, Long id) {
		return spendRepository.findOneByUserAndIdOrderByDateAsc(user, id);
	}

	public List<Spend> findByUserAndDateBetween(User user, Date from, Date to){
		return spendRepository.findByUserAndDateBetween(user, from, to);
	}
	
	@Override
	public Spend save(Spend spend) {
		return spendRepository.save(spend);
	}

}
