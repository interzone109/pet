package ua.squirrel.user.entity.storage;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.web.entity.user.User;

@Entity
@Table(name = "Storags")
@Data
public class Storage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "store_id", nullable = false)
	private long id;
	
	
	@OneToOne(mappedBy = "post", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, optional = false)
	private Store store;
	
	
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "consignment_store_id", nullable = false)
	private List<Consignment> consignment ;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
}
