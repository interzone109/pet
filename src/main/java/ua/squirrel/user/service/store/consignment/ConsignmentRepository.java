package ua.squirrel.user.service.store.consignment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

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
	
	
}
