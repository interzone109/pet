package ua.squirrel.user.service.product;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.user.entity.partner.Partner;
import ua.squirrel.user.entity.product.Product;
import ua.squirrel.web.entity.user.User;

public interface ProductRepository extends JpaRepository<Product, Long>{
	
		Optional<Product> findOneByIdAndPartner(Long id ,Partner partner);

		 List<Product> findAllByUserAndIdIn(User user,Iterable<Long> id );
		 
		 List<Product> findAllByUser(User user );
}
