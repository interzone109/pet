package ua.squirrel.user.product.helper.service;

import ua.squirrel.user.product.entity.MeasureProduct;

public interface MeasureProductService {
	public MeasureProduct findOneByMeasure(String measure);
}
