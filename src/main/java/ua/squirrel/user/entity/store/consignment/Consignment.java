package ua.squirrel.user.entity.store.consignment;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.consignment.node.ConsignmentNode; 

@Data
@Entity
@Table(name = "consignments")
public class Consignment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "consignment_id", nullable = false)
	private long id;

	// дата 
	private LocalDate date;

	@OneToMany(mappedBy = "consignment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ConsignmentNode> consignmentNode;
	
	
	// служебные данные
	@Column(name = "meta", nullable = false)
	private String meta;

	// состояние подтверждения партии
	// если парти не подтверждена то она не выбираеться при аналитике
	@Column(name = "is_approved", nullable = false)
	private boolean isApproved;

	// статус партии
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "consignment_status_id", nullable = false, updatable = false)
	private ConsignmentStatus consignmentStatus;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;
	
	// Id, количество ингридиентов и входная цена
	@Deprecated
	@Column(name = "consignment_data", nullable = true, length = 2048)
	private String consignmentData;

}
