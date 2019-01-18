package ua.squirrel.user.assortment.product.helper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name="measure_products")
public class MeasureProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "measure_product_Id", nullable = false)
	private long id;
	@Column(name = "measure_product", nullable = false)
	private String measure ;
}
