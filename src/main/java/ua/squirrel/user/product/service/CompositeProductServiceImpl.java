package ua.squirrel.user.product.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.user.product.entity.CompositeProduct;
import ua.squirrel.web.user.entity.User;

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
	public Optional<CompositeProduct> findByIdAndUser(Long id, User user) {
		return CompositeProductRepository.findByIdAndUser(id, user);
	}

	

}
