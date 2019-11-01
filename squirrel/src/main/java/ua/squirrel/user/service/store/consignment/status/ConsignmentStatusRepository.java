package ua.squirrel.user.service.store.consignment.status;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.user.entity.store.consignment.ConsignmentStatus;

public interface ConsignmentStatusRepository extends JpaRepository<ConsignmentStatus, Long>{
	
	Optional<ConsignmentStatus> findOneByName(String name);
}
