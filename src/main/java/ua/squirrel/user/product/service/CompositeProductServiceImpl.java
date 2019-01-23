package ua.squirrel.user.product.service;

import org.springframework.beans.factory.annotation.Autowired;

import ua.squirrel.user.product.entity.CompositeProduct;

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



}
