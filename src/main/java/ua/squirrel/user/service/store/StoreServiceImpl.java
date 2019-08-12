package ua.squirrel.user.service.store;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.user.entity.store.Store;
import ua.squirrel.web.entity.user.User;
@Service
public class StoreServiceImpl implements StoreService {

	private StoreRepository storeRepository;

	@Autowired
	public StoreServiceImpl(StoreRepository storeRepository) {
		this.storeRepository = storeRepository;
	}

	@Override
	public Store save(Store store) {
		return storeRepository.save(store);
	}

	@Override
	public Optional<Store> findOneByIdAndUser(Long id, User user) {
		return storeRepository.findOneByIdAndUser(id, user);
	}

	@Override
	public List<Store> findAllByUser(User user) {
		return storeRepository.findAllByUser(user);
	}

	public void saveAll(Set<Store> storeList) {
		storeRepository.saveAll(storeList);
		
	}
	

}
