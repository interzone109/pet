package ua.squirrel.user.service.store.consignment.node;

 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import ua.squirrel.user.entity.store.consignment.node.ConsignmentNode; 

@Service
public class ConsignmentNodeServiceImpl implements ConsignmentNodeService {
	@Autowired
	private ConsignmentNodeRepository сonsignmentNodeRepository;

	public List<ConsignmentNode> saveAll(Iterable<ConsignmentNode> сonsignmentNode) {
		return сonsignmentNodeRepository.saveAll(сonsignmentNode);
	}

	public ConsignmentNode save(ConsignmentNode сonsignmentNode) {
		return сonsignmentNodeRepository.save(сonsignmentNode);
	}
	
	
}
