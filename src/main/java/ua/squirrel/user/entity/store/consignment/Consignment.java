package ua.squirrel.user.entity.store.consignment;

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
import ua.squirrel.user.entity.store.storage.Storage;

@Data
@Entity
@Table(name = "consignments")
public class Consignment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "consignment_id", nullable = false)
	private long id;

	
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "storage_id", nullable = false)
	private Storage storage ;
	
	
}
