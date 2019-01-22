package ua.squirrel.user.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "products_measure")
public class MeasureProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "measure_product_Id", nullable = false)
	private long id;
	@Column(name = "measure_product", nullable = false, unique = true)
	private String measure;
}
