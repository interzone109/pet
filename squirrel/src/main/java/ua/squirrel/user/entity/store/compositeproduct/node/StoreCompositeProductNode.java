package ua.squirrel.user.entity.store.compositeproduct.node;

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
import ua.squirrel.user.entity.product.composite.CompositeProduct;
import ua.squirrel.user.entity.store.Store; 
/**
 * Класс StoreCompositeProductNode описывает узел хранящий данные 
 * о композитном продукте и его цене для связаного магазина.
 * Этот класс необходим для того что бы разные магазины имели возможность ставить различную цену
 * для одного и тогоже композитного продукта.
 * */
@Data
@Entity
@Table(name = "store_composite_product_node")
public class StoreCompositeProductNode {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "store_composite_product_node_id", nullable = false)
	private long id;
	// композитный продукт
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.DETACH)
	@JoinColumn(name = "composite_product_id", nullable = true)
	private CompositeProduct compositeProduct;
	//магазин
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.DETACH)
	@JoinColumn(name = "store_id", nullable = true)
	private Store store;
	//цена продукта для связаного магазина
	@Column(name="price")
	private int price ;
}
