package ua.squirrel.user.service.store.invoice.node;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.user.entity.store.invoice.node.InvoiceNode;

@Service
public class InvoiceNodeServiceImpl implements  InvoiceNodeService {
	@Autowired
	private  InvoiceNodeRepository  invoiceNodeRepository;

	public List< InvoiceNode> saveAll(Iterable< InvoiceNode>  invoiceNode) {
		return invoiceNodeRepository.saveAll(invoiceNode);
	}

	public  InvoiceNode save( InvoiceNode  invoiceNode) {
		return invoiceNodeRepository.save(invoiceNode);

	}
}
