package ua.squirrel.user.assortment.product.helper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import ua.squirrel.user.assortment.product.Product;


@Data
@Entity
@Table(name="group_products")
public class GroupProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "group_product_Id", nullable = false)
	private long id;
	@Column(name = "group_product_name", nullable = false)
	private String name ;
	
}
