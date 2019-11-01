package ua.squirrel.user.service.store.compositeproduct.node;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.user.entity.store.compositeproduct.node.StoreCompositeProductNode; 

@Service
public class StoreCompositeProductServiceImpl implements StoreCompositeProductService {
	@Autowired
	private StoreCompositeProductRepository storeCompositeProductRepository;

	public List<StoreCompositeProductNode> saveAll(Iterable<StoreCompositeProductNode> compositesPriceNode) {
		return storeCompositeProductRepository.saveAll(compositesPriceNode);
	}

	public StoreCompositeProductNode save(StoreCompositeProductNode storeCompositeProductNode) {
		return storeCompositeProductRepository.save(storeCompositeProductNode);

	}
	
	public void deleteById(Long id){
		 storeCompositeProductRepository.deleteById(id);
	}
	
}
