package ua.squirrel.user.service.store.consignment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.user.entity.store.consignment.ConsignmentStatus;

public interface ConsignmentService {

	Consignment save(Consignment consignment);

	List<Consignment> findByStoreAndConsignmentStatusAndDateBetween(Store store, ConsignmentStatus consignmentStatus,
			LocalDate start, LocalDate finish);

	Optional<Consignment> findOneByDateAndStoreAndConsignmentStatus(LocalDate date, Store store,
			ConsignmentStatus consignmentStatus);
}
