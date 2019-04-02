package ua.squirrel.user.service.store.consignment;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.user.entity.store.consignment.ConsignmentStatus;

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

	public Optional<Consignment> findOneByDateAndStore(Calendar date, Store store) {
		return consignmentRepository.findOneByDateAndStore(date, store);
	}

	public List<Consignment> findByStoreAndConsignmentStatusAndDateBetween(Store store,
			ConsignmentStatus consignmentStatus, Calendar from, Calendar to) {
		return consignmentRepository.findByStoreAndConsignmentStatusAndDateBetween(store, consignmentStatus, from, to);
	}

	public Optional<Consignment> findOneByDateAndStoreAndConsignmentStatus(Calendar date, Store store,
			ConsignmentStatus consignmentStatus) {
		return consignmentRepository.findOneByDateAndStoreAndConsignmentStatus(date, store, consignmentStatus);
	}

}
