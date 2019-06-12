package ua.squirrel.user.entity.store.invoice;

import java.time.LocalDate;

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
import ua.squirrel.user.entity.store.Store;

@Entity
@Table(name = "invoices")
@Data
public class Invoice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "invoice_id", nullable = false)
	private long id;
	
	// дата создания
	private LocalDate date;

	// Id продукта и количество продаж - 3:5sale
	@Column(name = "invoice_data", nullable = true, length = 4048)
	private String invoiceData;
	
	private String meta ;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;
}
