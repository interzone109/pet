package ua.squirrel.user.service.store.consignment;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.user.entity.store.consignment.ConsignmentStatus;
import ua.squirrel.user.entity.store.storage.Storage;

@Service
public class ConsignmentServiceImpl implements ConsignmentService {

	private ConsignmentRepository consignmentRepository;

	@Autowired
	public ConsignmentServiceImpl(ConsignmentRepository consignmentRepository) {
		this.consignmentRepository = consignmentRepository;
	}

	public Consignment save(Consignment consignment) {
		return consignmentRepository.save(consignment);
	}

	public Optional<Consignment> findOneByDateAndStorage(Calendar date, Storage storage) {
		return consignmentRepository.findOneByDateAndStorage(date, storage);
	}

	public Optional<Consignment> findOneByDateAndStorageAndConsignmentStatus(Calendar date, Storage storage, ConsignmentStatus consignmentStatus) {
		return consignmentRepository.findOneByDateAndStorageAndConsignmentStatus(date, storage,consignmentStatus);
	}
	
	public List<Consignment> findByStorageAndDateBetween(Storage storage, Calendar from, Calendar to) {
		return consignmentRepository.findByStorageAndDateBetween(storage, from, to);
	}

}
