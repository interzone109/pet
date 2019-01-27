package ua.squirrel.user.product.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.user.partner.entity.Partner;
import ua.squirrel.user.product.entity.Product;
import ua.squirrel.web.user.entity.User;

public interface ProductRepository extends JpaRepository<Product, Long>{
	
		Optional<Product> findOneByIdAndPartner(Long id ,Partner partner);

		 List<Product> findAllByIdAndUser(Iterable<Long> ids ,User user );
}
