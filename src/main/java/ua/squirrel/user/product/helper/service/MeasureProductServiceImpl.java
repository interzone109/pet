package ua.squirrel.user.product.helper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.user.product.MeasureProduct;
@Service
public class MeasureProductServiceImpl implements MeasureProductService {
	
	
	@Autowired
	private MeasureProductRepository measureProductRepository;
	@Override
	public MeasureProduct findOneByMeasure(String measure) {
		// TODO Auto-generated method stub
		return measureProductRepository.findOneByMeasure(measure);
	}

}
