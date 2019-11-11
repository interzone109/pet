package ua.squirrel.user.entity.store.consignment.node;
 

 

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
import ua.squirrel.user.entity.store.consignment.Consignment;
/***
 * Класс ConsignmentNode описывает сущность узла между накладной и товаром.
 * Хранит данные о товаре, а именно:
 * количество товара в партии, стоимость условной единицы товара,
 * количественный остаток товара в партии (для организации учета в  FIFO).
 * Также узел подвязываеться  непосредственно к накладной и к продукту.
 * */
@Data
@Entity
@Table(name = "consignments_node")
public class ConsignmentNode {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "consignment_node_id", nullable = false)
	private long id;
	//товар
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;
	//накладная к которой подвязан данный товар
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "consignment_id", nullable = false)
	private Consignment consignment;
	//цена за шт/кг/л товара
	@Column(name = "unit_price")
	private int unitPrice;
	//начальное количество товара в партии
	@Column(name = "start_quantity")
	private int quantity;
	//количественный остаток товара в партии
	@Column(name = "current_quantity")
	private int currentQuantity;
}
