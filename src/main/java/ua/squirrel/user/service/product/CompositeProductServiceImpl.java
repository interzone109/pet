package ua.squirrel.user.service.product;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.user.entity.product.composite.CompositeProduct;
import ua.squirrel.web.entity.user.User;

@Service
public class CompositeProductServiceImpl implements CompositeProductService {

	private final CompositeProductRepository compositeProductRepository;

	@Autowired
	public CompositeProductServiceImpl(CompositeProductRepository compositeProductRepository) {
		this.compositeProductRepository = compositeProductRepository;
	}

	@Override
	public CompositeProduct save(CompositeProduct compositeProduct) {
		return compositeProductRepository.save(compositeProduct);
	}

	@Override
	public List<CompositeProduct> saveAll(List<CompositeProduct> compositeProduct) {
		return compositeProductRepository.saveAll(compositeProduct);
	}

	@Override
	public Optional<CompositeProduct> findByIdAndUser(Long id, User user) {
		return compositeProductRepository.findByIdAndUser(id, user);
	}

	@Override
	public List<CompositeProduct> findAllByUser(User user) {
		return compositeProductRepository.findAllByUser(user);
	}

	public List<CompositeProduct> findAllByUserAndIdIn(User user, Iterable<Long> id) {
		return compositeProductRepository.findAllByUserAndIdIn(user, id);
	}

}
