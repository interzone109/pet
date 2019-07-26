package ua.squirrel.user.entity.store.consignment.node;
 

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne; 
import javax.persistence.Table;

import lombok.Data;
import ua.squirrel.user.entity.product.Product; 
import ua.squirrel.user.entity.store.consignment.Consignment;

@Data
@Entity
@Table(name = "consignments_node")
public class ConsignmentNode {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "consignment_node_id", nullable = false)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "consignment_id", nullable = false)
	private Consignment consignment;
	
	private int unitPrice;
	
	private int quantity;

}
