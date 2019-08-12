package ua.squirrel.user.service.store.consignment;

import java.time.LocalDate;
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

	public Optional<Consignment> findOneById(Long id) {
		return consignmentRepository.findById(id) ;
	}

	public List<Consignment> findByStoreAndConsignmentStatusAndDateBetween(Store store,
			ConsignmentStatus consignmentStatus, LocalDate from, LocalDate to) {
		return consignmentRepository.findByStoreAndConsignmentStatusAndDateBetween(store, consignmentStatus, from, to);
	}

	public Optional<Consignment> findOneByDateAndStoreAndConsignmentStatusAndIsApprovedAndMetaIgnoreCaseContaining
	(LocalDate date, Store store, ConsignmentStatus consignmentStatus,boolean isApproved, String meta) {
		return consignmentRepository.findOneByDateAndStoreAndConsignmentStatusAndIsApprovedAndMetaIgnoreCaseContaining(date, store, consignmentStatus, isApproved, meta);
	}

	public List<Consignment> findByStoreAndConsignmentStatusAndMetaIgnoreCaseContainingAndDateBetween(Store store,
			ConsignmentStatus consignmentStatus,String meta,  LocalDate start, LocalDate finish){
		return consignmentRepository.findByStoreAndConsignmentStatusAndMetaIgnoreCaseContainingAndDateBetween(store, consignmentStatus, meta, start, finish);
	}

	public Optional<Consignment> findOneByIdAndStore(Long consignmentId, Store store) {
		return consignmentRepository.findOneByIdAndStore( consignmentId,  store);
	}

	public void saveAll(List<Consignment> consigmentList) {
		consignmentRepository.saveAll(consigmentList);
		
	}


}
