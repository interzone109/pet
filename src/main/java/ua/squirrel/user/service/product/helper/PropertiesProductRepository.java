package ua.squirrel.user.service.product.helper;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.user.entity.product.PropertiesProduct;

public interface PropertiesProductRepository extends JpaRepository< PropertiesProduct, Long> {
	public PropertiesProduct findOneByName(String name);
}
