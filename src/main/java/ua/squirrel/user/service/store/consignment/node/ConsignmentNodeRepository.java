package ua.squirrel.user.service.store.consignment.node;


import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ua.squirrel.user.entity.product.Product;
import ua.squirrel.user.entity.store.consignment.node.ConsignmentNode; 

public interface ConsignmentNodeRepository extends JpaRepository<ConsignmentNode, Long> {

	
	@Query("select distinct c from ConsignmentNode c  inner join c.product p where p IN :products AND c.consignment.consignmentStatus =2")
	List<ConsignmentNode> getAllProductFIFO( Collection<Product> products);
	
	
}
