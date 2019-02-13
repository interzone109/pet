package ua.squirrel.user.service.store.consignment;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.user.entity.store.storage.Storage;


public interface ConsignmentRepository extends JpaRepository<Consignment, Long> {

	Optional<Consignment> findOneByDateAndStorage(Calendar date, Storage storage);
	
	List<Consignment> findByStorageAndDateBetween(Storage storage, Calendar from, Calendar to);
}
