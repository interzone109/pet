package ua.squirrel.user.entity.store.spending;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import lombok.Data;
import ua.squirrel.user.entity.store.Store;

@Entity
@Table(name = "spends")
@Data
public class Spend {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "store_id", nullable = false)
	private long id;
	
	@Column(name = "name_id", nullable = false)
	private String name ;
	
	@Column(name = "cost_id", nullable = false)
	private Integer cost ;
	
	@Column(name = "is_regular_spend", nullable = false)
	private Boolean isRegular ;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	
	@Column(name = "status_id")
	private Integer status ;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "store_user_id", nullable = false)
	private Store store;
	
}
