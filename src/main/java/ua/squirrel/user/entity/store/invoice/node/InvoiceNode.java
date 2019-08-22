package ua.squirrel.user.entity.store.invoice.node;


import java.time.LocalDateTime;

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
import ua.squirrel.user.entity.store.invoice.Invoice; 
/**
 * Класс InvoiceNode описывает зел связи между инвойсом и композитным продуктом.
 * Класс хранит общее количество продаж композитного продукта для инвойса.
 * */
@Entity
@Table(name = "invoice_node")
@Data
public class InvoiceNode {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "invoice_node_id", nullable = false)
	private long id;
	//продукт
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "composite_product_id", nullable = false)
	private CompositeProduct compositeProduct;
	//инвойс
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "invoice_id", nullable = false)
	private Invoice invoice;
	//количество продаж
	private int saleQuantity;
	//время продажи
	private LocalDateTime time;
	//цена
	private int price;

}
