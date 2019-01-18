package ua.squirrel.user.assortment.product;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import ua.squirrel.user.assortment.product.helper.GroupProduct;
import ua.squirrel.user.assortment.product.helper.MeasureProduct;


@Data
@Entity
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id", nullable = false)
	private long id;
	@Column(name = "product_name", nullable = false)
	private String name;
	@Column(name = "description")
	private String description;
	@Column(name = "price")
	private float price;
	@OneToOne
	private GroupProduct groupProduct;
	@OneToOne
	private MeasureProduct measureProduct;

	
	/*@ManyToOne(targetEntity = Partner.class)
	private Partner partner ;
	@OneToOne
	private User user ;*/
	
}
