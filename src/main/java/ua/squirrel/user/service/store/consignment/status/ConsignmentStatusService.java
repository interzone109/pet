package ua.squirrel.user.service.store.consignment.status;

import java.util.Optional;

import ua.squirrel.user.entity.store.consignment.ConsignmentStatus;

public interface ConsignmentStatusService {
	
	Optional<ConsignmentStatus> findOneByName(String name);
}
