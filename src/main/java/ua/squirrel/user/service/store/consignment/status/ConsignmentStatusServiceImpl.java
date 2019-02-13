package ua.squirrel.user.service.store.consignment.status;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.user.entity.store.consignment.ConsignmentStatus;

@Service
public class ConsignmentStatusServiceImpl implements ConsignmentStatusService {

	private ConsignmentStatusRepository consignmentStatusRepository;

	@Autowired
	public ConsignmentStatusServiceImpl(ConsignmentStatusRepository consignmentStatusRepository) {

		this.consignmentStatusRepository = consignmentStatusRepository;
	}

	@Override
	public Optional<ConsignmentStatus> findOneByName(String name) {
		return consignmentStatusRepository.findOneByName(name);
	}

}
