package ua.squirrel.user.service.store.ingridient.node;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.user.entity.store.ingridient.node.StoreIngridientNode;

@Service
public class StoreIngridientNodeServiceImpl implements StoreIngridientNodeService {
	@Autowired
	private StoreIngridientNodeRepository storeIngridientNodeRepository;

	public List<StoreIngridientNode> saveAll(Set<StoreIngridientNode> storeIngridientNode) {
		return storeIngridientNodeRepository.saveAll(storeIngridientNode);
	}

	public StoreIngridientNode save(StoreIngridientNode storeIngridientNode) {
		return storeIngridientNodeRepository.save(storeIngridientNode);

	}
}
