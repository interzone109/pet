package ua.squirrel.user.entity.store.spending;

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
import ua.squirrel.web.entity.user.User;
/**
 * Класс описывает сущность операционной деятельности (расходов).
 * Расходы поделены на два типа регулярные и единоразовые.
 * 
 * */
@Entity
@Table(name = "spends")
@Data
public class Spend {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "spend_id", nullable = false)
	private long id;
	//наименование
	@Column(name = "name", nullable = false)
	private String name;
	//цена вопроса
	@Column(name = "cost", nullable = false)
	private Integer cost;
	//шаг суммы
	@Column(name = "step", nullable = false)
	private Integer step;
	// являеться ли расход постоянным
	@Column(name = "is_open_spend", nullable = false)
	private Boolean isOpen;
	//регулярность платежа
	@Column(name = "interval", nullable = false)
	private Integer interval;
	//дата с которой стартует оплата платежа
	private LocalDate date;
	//дата последнего платежа
	private LocalDate lasteDate;
	//магазин
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "store_id", nullable = true)
	private Store store;
	//пользователь
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

}
