package ua.squirrel.user.entity.store.ingridient.node;

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
import ua.squirrel.user.entity.store.Store; 
/**
 * Класс StoreIngridientNode описывает узел который хранит промежуточные данные
 * о магазине и ингридиентах которые числяться на магазине.
 * Основным назначением узла является хранение текущих остатков ингридиентов на магазине.
 * */
@Data
@Entity
@Table(name = "store_ingridient_node")
public class StoreIngridientNode {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "store_ingridient_id", nullable = false)
	private long id;
	//продукт к которому подвязан узел
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;
	//магазин к которому подвязан узел
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;
	//остатки ингридиентов на текущий момент времени
	@Column(name="left_overs")
	private int leftOvers ;
	//сумма на остатки ингридиентов 
	@Column(name="summ")
	private int summ ;
}
