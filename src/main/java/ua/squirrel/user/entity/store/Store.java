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
	
	@Column(name = "address", nullable = false)
	private String address;
	
	@Column(name = "phone")
	private String phone;
	
	@Column(name = "mail")
	private String mail;
	
	//композитный продукт (id) - цена для магазина
	@Column(name = "comosite_product_price_id", length = 40000)
	private String productPrice;
	
	//композитный продукт (id) -старая цена для магазина и дата изменения цены
	@Column(name = "price_update_id", length = 4000)
	private String priceUpdate;
	
	//удаленные продукты и дата удаления
	@Column(name = "product_delete_id", length = 4000)
	private String productDelete;
	
	//остатки ингридиентов (id) и их количество ("quantity")  на магазине
	@Column(name = "product_leftovers", length = 4000)
	private String productLeftovers;
	
	//накладные на движения товара
	@OneToMany(mappedBy = "store", fetch = FetchType.LAZY, 
			 cascade = CascadeType.ALL)
	private List<Consignment> consignment;
	
	@OneToMany( fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Employee> employee;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "store_user_id", nullable = false)
	private User user;
	
}
