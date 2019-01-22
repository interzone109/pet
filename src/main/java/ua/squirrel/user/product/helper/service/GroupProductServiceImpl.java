package ua.squirrel.user.product.helper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.user.product.GroupProduct;
@Service
public class GroupProductServiceImpl implements GroupProductService{
	
	private GroupProductRepository groupProductRepository ;
	
	@Autowired
	public GroupProductServiceImpl(GroupProductRepository groupProductRepository) {
		this.groupProductRepository =  groupProductRepository ;
	}

	@Override
	public GroupProduct findOneByName(String name) {
		return groupProductRepository.findOneByName(name);
	}
	
	
}
