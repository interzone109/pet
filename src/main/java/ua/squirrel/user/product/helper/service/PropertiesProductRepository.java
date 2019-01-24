package ua.squirrel.user.product.helper.service;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.user.product.entity.PropertiesProduct;

public interface PropertiesProductRepository extends JpaRepository< PropertiesProduct, Long> {
	public PropertiesProduct findOneByName(String name);
}
