package ua.squirrel.user.product.service;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.user.partner.entity.Partner;
import ua.squirrel.user.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	
		Optional<Product> findOneByIdAndPartner(Long id ,Partner partner);
}
