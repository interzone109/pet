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
import ua.squirrel.user.entity.product.CompositeProduct;
import ua.squirrel.user.entity.product.Product;

@Entity
@Table(name = "store_assortment")
@Data
public class StoreAssortment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "assortment_id", nullable = false)
	private long id;
	
	@OneToMany( fetch = FetchType.LAZY , cascade = CascadeType.ALL)
	private List<Product> product;
	
	@OneToMany( fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<CompositeProduct> compositeProduct;
	
	@OneToMany( fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Employee> employees;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;
	
}
