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
/***
 * Класс UserSubscription описывает подписку пользователя
 * */
@Entity
@Table(name = "subscriptions")
@Data
public class UserSubscription {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "subscription_id", nullable = false)
	private long id;
	// дата последней оплаты
	private LocalDate lastPatyDate;
	// дата окончания подписки
	private LocalDate deadlineDate;
	//доступное количество магазинов
	private int storeQuantity ;
	//доступное количество работников
	private int employeesQuantity ;
	//доступное количество поставщиков
	private int partnerQuantity ;
	//доступное количество продуктов
	private int productQuantity ;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
	private User user;
}
