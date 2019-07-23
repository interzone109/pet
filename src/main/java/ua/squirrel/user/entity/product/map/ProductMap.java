package ua.squirrel.user.entity.product.map;


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
import ua.squirrel.user.entity.product.Product;
import ua.squirrel.user.entity.product.composite.CompositeProduct; 

@Data
@Entity
@Table(name = "products_map")
public class ProductMap {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_map_id", nullable = false)
	private long id;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "composite_product_id", nullable = false)
	private CompositeProduct compositeProduct;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "products_map_product_id", nullable = false)
	private Product product;

	private int rate ;
}
