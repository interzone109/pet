package ua.squirrel.user.service.product.helper;

import ua.squirrel.user.entity.product.MeasureProduct;

public interface MeasureProductService {
	public MeasureProduct findOneByMeasure(String measure);
}
