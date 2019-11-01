package ua.squirrel.user.entity.store.consignment;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.consignment.node.ConsignmentNode; 

/***
 * Класс Consignment описывает сущность Накладной.
 * Хранит данные о дате создания, узлы с данными о товаре,
 * магазин на который поступил товар и тип накладной.
 * Обьекты класса формируют массив данных о движении товара в организации.
 * */
@Data
@Entity
@Table(name = "consignments")
public class Consignment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "consignment_id", nullable = false)
	private long id;
	// дата создание / прихода товара
	private LocalDate date;
	//узел описывающий товар и его количество, которое поступило 
	@OneToMany(mappedBy = "consignment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ConsignmentNode> consignmentNode;
	// служебные данные, хранят описание накладной, откуда и куда поступил товар
	@Column(name = "meta", nullable = false)
	private String meta;
	//состояние накладной, если = true то накладная не может быть редактирована, а данные попадают в аналитику
	@Column(name = "is_approved", nullable = false)
	private boolean isApproved;
	// статус партии
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "consignment_status_id", nullable = false, updatable = false)
	private ConsignmentStatus consignmentStatus;
	// магазин к которому подвязывается накладная
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

}
