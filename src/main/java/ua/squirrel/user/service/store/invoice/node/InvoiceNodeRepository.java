package ua.squirrel.user.service.store.invoice.node;

import org.springframework.data.jpa.repository.JpaRepository;
 
import ua.squirrel.user.entity.store.invoice.node.InvoiceNode;

public interface InvoiceNodeRepository extends JpaRepository< InvoiceNode, Long> {
}
