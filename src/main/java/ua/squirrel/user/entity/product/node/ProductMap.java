package ua.squirrel.user.entity.product.node;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import ua.squirrel.user.entity.product.Product;
import ua.squirrel.user.entity.product.composite.CompositeProduct; 
/**
 * Класс ProductMap являеться связным узлом между CompositeProduct и Product.
 * Хранит данные о количестве Product на одну усовную единицу CompositeProduct
 * */
@Data
@Entity
@Table(name = "composite_products_node")
public class ProductMap {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_map_id", nullable = false)
	private long id;
	//Композитный продукт
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.DETACH)
	@JoinColumn(name = "composite_product_id", nullable = false)
	private CompositeProduct compositeProduct;
	//ингридиент
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.DETACH)
	@JoinColumn(name = "products_map_product_id", nullable = false)
	private Product product;
	//количество ингридиента на единицу композитного продукта
	@Column(name="rate")
	private int rate ;
}
