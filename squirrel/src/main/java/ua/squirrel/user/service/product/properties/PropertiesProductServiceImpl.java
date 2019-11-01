package ua.squirrel.user.service.product.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.user.entity.product.PropertiesProduct;

@Service
public class  PropertiesProductServiceImpl implements PropertiesProductService{
	
	private PropertiesProductRepository propertiesProductRepository ;
	
	@Autowired
	public  PropertiesProductServiceImpl(PropertiesProductRepository propertiesProductRepository) {
		this.propertiesProductRepository =  propertiesProductRepository ;
	}

	@Override
	public  PropertiesProduct findOneByName(String name) {
		return propertiesProductRepository.findOneByName(name);
	}
	
	
}
