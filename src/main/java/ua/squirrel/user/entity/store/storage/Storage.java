package ua.squirrel.user.entity.store.storage;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


import lombok.Data;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.consignment.Consignment;

@Entity
@Table(name = "storages")
@Data
public class Storage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "store_id", nullable = false)
	private long id;

	@Column(name = "comosite_product_price_id", length = 40000)
	private String productPrice;

	@Column(name = "price_update_id", length = 4000)
	private String priceUpdate;
	
	@Column(name = "product_delete_id", length = 4000)
	private String productDelete;

	@OneToMany(mappedBy = "storage", fetch = FetchType.LAZY, 
			orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Consignment> consignment;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;
}
