package ua.squirrel.web.entity.user;

import java.time.LocalDate;
 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
/***
 * Класс UserSubscription описывает подписку пользователя
 * */
@Entity
@Table(name = "subscriptions")
@Data
@EqualsAndHashCode(exclude="user")
public class UserSubscription {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "subscription_id", nullable = false)
	private long id;
	// дата последней оплаты
	private LocalDate lastPatyDate;
	// дата окончания подписки
	private LocalDate deadlineDate;
	//доступное количество магазинов
	private int storeQuantity ;
	//текущее количество магазинов
	private int storeCurrentQuantity ;
	//доступное количество работников
	private int employeesQuantity ;
	//текущее количество работников
	private int employeesCurrentQuantity ;
	//доступное количество поставщиков
	private int partnerQuantity ;
	//текущее количество поставщиков
	private int partnerCurrentQuantity ;
	//доступное количество продуктов
	private int productQuantity ;
	//текущее количество продуктов
	private int productCurrentQuantity ;
	//стоимость подписки
	private int price ;
		

	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
	private User user;
}
