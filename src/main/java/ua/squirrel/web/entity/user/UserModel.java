package ua.squirrel.web.entity.user;


import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;


@Data
public class UserModel {
	@NotNull
    @Size(min=6, max=20, message = "Не менее 6-ти знаков")
	private String login;
	@NotNull
    @Size(min=8, max=20, message = "Не менее 8-ти знаков")
	private String hashPass;
	@NotNull
    @Size(min=8, max=20, message = "Не менее 8-ти знаков")
	private String repidPass;
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
