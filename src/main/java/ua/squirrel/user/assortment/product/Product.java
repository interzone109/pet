package ua.squirrel.user.assortment.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Data;
import ua.squirrel.user.assortment.partner.Partner;

@Data
@Builder
@Entity
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "product_name", nullable = false)
	private String name;
	@Column(name = "description")
	private String description;
	@OneToMany
	private Partner partners ;
	
}
