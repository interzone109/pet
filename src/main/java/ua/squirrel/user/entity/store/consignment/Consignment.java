package ua.squirrel.user.entity.store.consignment;

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
import ua.squirrel.user.entity.store.storage.Storage;

@Data
@Entity
@Table(name = "consignments")
public class Consignment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "consignment_id", nullable = false)
	private long id;
	
	// дата к которой подвязываются все поступления
	@Column(name = "receipt_date", nullable = false)
	private String receiptDate;
	
	//Id и количество товара
	@Column(name = "start_consignment", nullable = false)
	private String startConsignment;
	
	//id и текущее количество товара
	@Column(name = "rest_consignment", nullable = false)
	private String restConsignment;
	
	//id и цена ( указываеться за начальное количество товара)
	@Column(name = "price_consignment", nullable = false)
	private String priceConsignment;
	
	//поле отвечает за остатки товара в партии
	//если ВЕСЬ товар закончился принимает значение true
	@Column(name = "is_consignment_empty", nullable = false)
	private boolean isConsignmentEmpty;
	
	//поле отвечает за подтверждение партии
	@Column(name = "is_confirm", nullable = false)
	private boolean isСonfirm;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "storage_id", nullable = false)
	private Storage storage ;
	
	
}
