package ua.squirrel.user.assortment.product.service;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.user.assortment.product.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	

}
