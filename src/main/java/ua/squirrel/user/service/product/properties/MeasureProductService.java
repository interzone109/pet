package ua.squirrel.user.service.product.properties;

import ua.squirrel.user.entity.product.MeasureProduct;

public interface MeasureProductService {
	public MeasureProduct findOneByMeasure(String measure);
}
