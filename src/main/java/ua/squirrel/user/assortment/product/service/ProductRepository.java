package ua.squirrel.user.assortment.product.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.user.assortment.product.Product;
import ua.squirrel.web.entity.user.User;

public interface ProductRepository extends JpaRepository<Product, Long>{
	List<Product> findByUser(User user);

}
