package ua.squirrel.user.service.product.properties;

import ua.squirrel.user.entity.product.PropertiesProduct;

public interface PropertiesProductService {
	public  PropertiesProduct findOneByName(String name);
}
