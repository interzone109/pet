package ua.squirrel.user.entity.store.invoice;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.invoice.node.InvoiceNode;
/***
 * Класс Invoice описывает сущность чека.
 * Хранит данные о дате создания, денег в кассе на начало рабочего дня,
 * служебную информацию а также магазин на котором произошла продажа.
 * */
@Entity
@Table(name = "invoices")
@Data
public class Invoice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "invoice_id", nullable = false)
	private long id;
	//денег в кассе на начало рабочего дня
	@Column(name = "cash_box_start_day", nullable = false)
	private int cashBoxStartDay;
	//итоговая касса
	@Column(name = "cash_box_total", nullable = false)
	private int cashBox ;
	//количество продаж в единицах
	@Column(name = "sell_quantity", nullable = false)
	private int sellQuantity;
	//количество заказов
	@Column(name = "order_quantity", nullable = false)
	private int orderQuantity;
	// дата создания
	private LocalDate date;
	//список проданых композитных продуктов
	@OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<InvoiceNode> invoiceNode;
	//служебные данные
	private String meta ;
	//магазин на котором происходит продажа
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;
	
	
	
	
	
	

	// Id продукта и количество продаж - 3:5sale
	@Deprecated
	@Column(name = "invoice_data", nullable = true, length = 4048)
	private String invoiceData;
}
