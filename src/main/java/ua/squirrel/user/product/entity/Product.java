package ua.squirrel.user.product.entity;

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
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import ua.squirrel.user.partner.entity.Partner;

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
	
	@Column(name = "unit_price")
	private long price;
	
	@OneToOne
	private GroupProduct groupProduct;
	
	@OneToOne
	private MeasureProduct measureProduct;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "product_partner_id", nullable = false)
	private Partner partner;

	
	/*@JsonBackReference
	@ManyToMany(mappedBy = "products",fetch = FetchType.LAZY)
	private List<CompositeProduct> compositeProduct;*/
	
}
