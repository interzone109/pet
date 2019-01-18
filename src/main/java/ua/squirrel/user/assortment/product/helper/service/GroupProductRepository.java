package ua.squirrel.user.assortment.product.helper.service;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.user.assortment.product.helper.GroupProduct;

public interface GroupProductRepository extends JpaRepository< GroupProduct, Long> {
	public GroupProduct findOneByName(String name);
}
