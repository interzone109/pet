package ua.squirrel.user.product.service;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.user.product.entity.CompositeProduct;

public interface CompositeProductRepository extends JpaRepository<CompositeProduct, Long> {

}
