package ua.squirrel.user.product.helper.service;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.user.product.GroupProduct;

public interface GroupProductRepository extends JpaRepository< GroupProduct, Long> {
	public GroupProduct findOneByName(String name);
}
