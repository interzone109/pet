package ua.squirrel.user.entity.store;

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
import javax.persistence.Table;
import lombok.Data;
import ua.squirrel.user.entity.employee.Employee;
import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.web.entity.user.User;

@Entity
@Table(name = "stores")
@Data
public class Store {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "store_id", nullable = false)
	private long id;
	
	@Column(name = "address", unique = true, nullable = false)
	private String address;
	
	@Column(name = "phone")
	private String phone;
	
	@Column(name = "mail")
	private String mail;
	
	@Column(name = "comosite_product_price_id", length = 40000)
	private String productPrice;

	@Column(name = "price_update_id", length = 4000)
	private String priceUpdate;
	
	@Column(name = "product_delete_id", length = 4000)
	private String productDelete;

	@OneToMany(mappedBy = "store", fetch = FetchType.LAZY, 
			 cascade = CascadeType.ALL)
	private List<Consignment> consignment;
	
	@OneToMany( fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Employee> employee;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "store_user_id", nullable = false)
	private User user;
	
}
