package ua.squirrel.user.entity.product;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import ua.squirrel.user.entity.partner.Partner;
import ua.squirrel.user.entity.product.map.ProductMap;
import ua.squirrel.web.entity.user.User;

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
	
	@Column(name = "product_group")
	private String group;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "properties_id")
	private PropertiesProduct propertiesProduct;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "measure_id")
	private MeasureProduct measureProduct;

	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "product_partner_id", nullable = false)
	private Partner partner;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "product_user_id", nullable = false)
	private User user;

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY 
			, cascade = CascadeType.ALL)
	private List<ProductMap> productMap;
}
