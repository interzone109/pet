package ua.squirrel.user.service.product;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.user.entity.product.CompositeProduct;
import ua.squirrel.web.entity.user.User;

@Service
public class CompositeProductServiceImpl implements CompositeProductService {

	private final CompositeProductRepository CompositeProductRepository;

	@Autowired
	public CompositeProductServiceImpl(CompositeProductRepository compositeProductRepository) {
		CompositeProductRepository = compositeProductRepository;
	}

	@Override
	public CompositeProduct save(CompositeProduct compositeProduct) {
		return CompositeProductRepository.save(compositeProduct);
	}
	@Override
	public List<CompositeProduct> saveAll(List<CompositeProduct> compositeProduct) {
		return CompositeProductRepository.saveAll(compositeProduct);
	}

	@Override
	public Optional<CompositeProduct> findByIdAndUser(Long id, User user) {
		return CompositeProductRepository.findByIdAndUser(id, user);
	}

	@Override
	public List<CompositeProduct> findAllByUser(User user) {
		return CompositeProductRepository.findAllByUser(user);
	}

}
