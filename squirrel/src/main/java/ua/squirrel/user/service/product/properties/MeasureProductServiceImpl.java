package ua.squirrel.user.service.product.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.user.entity.product.MeasureProduct;
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
