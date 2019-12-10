package ua.squirrel.web.entity.user;


import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Data;
import ua.squirrel.web.entity.account.AccountAppModel;


@Data
public class UserModel {
	
	private AccountAppModel accountAppModel;

	@NotNull
	@Email(message = "Проверьте правельность написания email")
	private String mail;
	private String role;
	private String state;
	private String subscription;
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
	//стоимость подписки
	private int price ;
}
