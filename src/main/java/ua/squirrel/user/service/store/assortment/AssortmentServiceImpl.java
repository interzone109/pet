package ua.squirrel.user.service.store.assortment;

import java.util.Optional;

import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;

import ua.squirrel.user.entity.store.StoreAssortment;

public class AssortmentServiceImpl implements AssortmentService{
	
	private AssortmentRepository assortmentRepository;
	
	@Autowired
	public AssortmentServiceImpl(AssortmentRepository assortmentRepository) {
		this.assortmentRepository = assortmentRepository;
	}

	@Override
	public StoreAssortment save(StoreAssortment storeAssortment) {
		return assortmentRepository.save(storeAssortment);
	}
	public Optional<StoreAssortment> findOneByStore(Store store){
		return assortmentRepository.findOneByStore(store);
	}

}
