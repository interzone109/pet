package ua.squirrel.user.entity.store.consignment;

import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import ua.squirrel.user.entity.store.Store;

@Data
@Entity
@Table(name = "consignments")
public class Consignment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "consignment_id", nullable = false)
	private long id;

	// дата к которой подвязываются все поступления
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar date;

	// Id и количество товара
	@Column(name = "consignment_data", nullable = true, length = 2048)
	private String consignmentData;

	// служебные данные
	@Column(name = "meta", nullable = false)
	private String meta;

	// состояние подтверждения партии
	// если парти не подтверждена то она не выбираеться при аналитике
	@Column(name = "is_approved", nullable = false)
	private boolean isApproved;

	// поле отвечает за остатки товара в партии
	// если ВЕСЬ товар закончился принимает значение true
	@Column(name = "is_consignment_empty", nullable = false)
	private boolean isConsignmentEmpty;

	// статус партии
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "properties_id", nullable = false, updatable = false)
	private ConsignmentStatus consignmentStatus;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

}
