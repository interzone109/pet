package ua.squirrel.user.service.store.consignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.user.entity.store.consignment.Consignment;

@Service
public class ConsignmentServiceImpl implements ConsignmentService{
	
	private ConsignmentRepository consignmentRepository;

	@Autowired
	public ConsignmentServiceImpl(ConsignmentRepository consignmentRepository) {
		this.consignmentRepository = consignmentRepository;
	}
	
	public Consignment save(Consignment consignment) {
		return consignmentRepository.save(consignment);
	}

}
