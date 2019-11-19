package ua.squirrel.user.entity.store;

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
import ua.squirrel.user.entity.employee.Employee;
import ua.squirrel.user.entity.store.compositeproduct.node.StoreCompositeProductNode;
import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.user.entity.store.ingridient.node.StoreIngridientNode;
import ua.squirrel.user.entity.store.invoice.Invoice;
import ua.squirrel.web.entity.user.User;
/**
 * Класс Store описывает сущность магазина/торговой точки.
 * Хранит данные о адресе магазина, телефоне и мыле.
 * Класс хранит несколько узлов связаных с товаром а имено: 
 * узлы с остатками ингридиентов на магазине;
 * узлы с композитным  продуктом и его отпускной ценой для текущего магазина.
 * Класс храни связаные с ним данные в виде списков, а именно:
 * список со всеми накладными связаными с данным магазином;
 * список сотрудников задействованых на данном магазине;
 * список Инвойсов (чеков).
 * */
@Entity
@Table(name = "stores")
@Data

public class Store {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "store_id", nullable = false)
	private long id;
	//адрес
	@Column(name = "address", nullable = false)
	private String address;
	//телефон
	@Column(name = "phone")
	private String phone;
	//мыло
	@Column(name = "mail")
	private String mail;
	// ассортимент на магазине/композитный продукт и его цена
	@OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.DETACH,orphanRemoval=true)
	private List<StoreCompositeProductNode> storeCompositeProductNode;
	// остатки ингридиентов на магазине
	@OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<StoreIngridientNode> storeIngridientNode;
	// список накладных
	@OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Consignment> consignment;
	// список инвойсов
	@OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Invoice> invoice;
	//список сотрудников
	@OneToMany(mappedBy = "store",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Employee> employee;
	//пользователь
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "store_user_id", nullable = false)
	private User user;


}
