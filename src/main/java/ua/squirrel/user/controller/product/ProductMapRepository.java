package ua.squirrel.user.controller.product;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.user.entity.product.map.ProductMap;

public interface ProductMapRepository extends JpaRepository<ProductMap, Long> {

}
