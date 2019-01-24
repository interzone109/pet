package ua.squirrel.user.product.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


@Data
@Entity
@Table(name="properties_product")
public class PropertiesProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "properties_product_Id", nullable = false)
	private long id;
	@Column(name = "properties_product_name", nullable = false , unique=true)
	private String name ;
	
}
