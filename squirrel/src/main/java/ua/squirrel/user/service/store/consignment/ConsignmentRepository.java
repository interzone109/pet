package ua.squirrel.user.service.store.consignment;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ua.squirrel.user.entity.product.Product;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.user.entity.store.consignment.ConsignmentStatus; 



public interface ConsignmentRepository extends JpaRepository<Consignment, Long> {

	
	List<Consignment> findByStoreAndConsignmentStatusAndDateBetween(Store store,
			ConsignmentStatus consignmentStatus, LocalDate start, LocalDate finish);

	Optional<Consignment> findOneByDateAndStoreAndConsignmentStatusAndIsApprovedAndMetaIgnoreCaseContaining(LocalDate date, Store store,
			ConsignmentStatus consignmentStatus, boolean isApproved, String meta);
	
	
	List<Consignment> findByStoreAndConsignmentStatusAndMetaIgnoreCaseContainingAndDateBetween(Store store,
			ConsignmentStatus consignmentStatus,String meta,  LocalDate start, LocalDate finish);

	Optional<Consignment> findOneByIdAndStore(Long consignmentId, Store store);
	
	@Query("SELECT distinct c FROM Consignment c   inner join c.consignmentNode p WHERE c.consignmentStatus =1 AND c.store = :storeId AND p.currentQuantity >0 AND  p.product IN :products order by c.date")
	List<Consignment> getConsigmentFIFO(@Param("storeId")Store storeId,  @Param("products")Collection<Product> products);
}
