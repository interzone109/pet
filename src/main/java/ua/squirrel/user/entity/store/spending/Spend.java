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
import ua.squirrel.web.entity.user.User;

@Entity
@Table(name = "spends")
@Data
public class Spend {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "store_id", nullable = false)
	private long id;

	@Column(name = "name_id", nullable = false)
	private String name;

	@Column(name = "cost", nullable = false)
	private Integer cost;

	@Column(name = "is_regular_spend", nullable = false)
	private Boolean isRegular;

	@Column(name = "interval", nullable = false)
	private Integer interval;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "store_id", nullable = true)
	private Store store;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

}
