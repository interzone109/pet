package ua.squirrel.user.entity.store.invoice;

import java.util.Map;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class InvoiceModel {
	private long id;
	// дата создания/начало промежутка
	private String dateStart;
	// конец промежутка
	private String dateEnd;
	private boolean isBetween ;
	// id продукта и количество продаж
	private Map<Long, Integer> invoiceData ;
	//денег на начало рабочего дня
	private int cashBoxStartDay;
	//текущая касса
	private int currentSell;
	//количество продаж
	private int sellQuantity;
	
	private Long storeId;
}