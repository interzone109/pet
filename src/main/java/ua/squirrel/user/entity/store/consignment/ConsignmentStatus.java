package ua.squirrel.user.entity.store.consignment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "consignments_status")
public class ConsignmentStatus {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "consignment_status_id", nullable = false)
	private long id;
	@Column(name = "status_name", nullable = false, unique = true, updatable=false)
	private String name;
}
