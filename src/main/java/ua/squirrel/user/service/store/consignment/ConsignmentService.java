package ua.squirrel.user.service.store.consignment;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.user.entity.store.consignment.ConsignmentStatus;
import ua.squirrel.user.entity.store.storage.Storage;

public interface ConsignmentService {

	Consignment save(Consignment consignment);
	
	Optional<Consignment> findOneByDateAndStorage(Calendar date, Storage storage);
	
	List<Consignment> findByStorageAndConsignmentStatusAndDateBetween(Storage storage,
			ConsignmentStatus consignmentStatus, Calendar from, Calendar to);
	
	Optional<Consignment> findOneByDateAndStorageAndConsignmentStatus(Calendar date, Storage storage,
			ConsignmentStatus consignmentStatus);
}
