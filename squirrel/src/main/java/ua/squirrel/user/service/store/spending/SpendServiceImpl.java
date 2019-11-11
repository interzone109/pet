package ua.squirrel.user.service.store.spending;

import java.time.LocalDate;
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
	
	public List<Spend> findAllByIsOpen(boolean isOpen){
		return spendRepository.findAllByIsOpen( isOpen);
	}
	
	@Autowired
	public SpendServiceImpl(SpendRepository spendRepository) {
		this.spendRepository = spendRepository;
	}

	@Override
	public List<Spend> findAllByUserAndLasteDateBetweenOrderByLasteDateAsc(User user, LocalDate from, LocalDate to) {
		// TODO Auto-generated method stub
		return spendRepository.findAllByUserAndLasteDateBetweenOrderByLasteDateAsc(user,  from,  to);
	}

	@Override
	public List<Spend> findAllByStoreAndLasteDateBetweenOrderByLasteDateAsc(Store store , LocalDate from, LocalDate to) {
		// TODO Auto-generated method stub
		return spendRepository.findAllByStoreAndLasteDateBetweenOrderByLasteDateAsc(store ,  from,  to);
	}

	@Override
	public List<Spend> findAllByUserAndStoreAndLasteDateBetweenOrderByLasteDateAsc(User user, Store store, LocalDate from,
			LocalDate to) {
		// TODO Auto-generated method stub
		return spendRepository.findAllByUserAndStoreAndLasteDateBetweenOrderByLasteDateAsc(user, store, from, to);
	}
	
	@Override
	public Optional<Spend> findOneByUserAndId(User user, Long id) {
		// TODO Auto-generated method stub
		return spendRepository.findOneByUserAndId(user, id);
	}

	public Spend save(Spend spend) {
		return spendRepository.save(spend);
	}
	
	public void remove(Spend spend) {
		 spendRepository.delete(spend);
	}

	public void saveAll(List<Spend> allSpends) {
		spendRepository.saveAll(allSpends);
	}


}
