package ua.squirrel.user.entity.store.invoice;


import java.util.Collection;
import java.util.Map;

import lombok.Builder;
import lombok.Data;
import ua.squirrel.user.entity.product.composite.CompositeProductModel;


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
	// id продукта и количество продаж
	private Collection< CompositeProductModel> invoiceNode ;
	//денег на начало рабочего дня
	private int cashBoxStartDay;
	//текущая касса
	private int cashBox;
	//количество продаж в шт
	private int sellQuantity;
	//количество заказов
	private int orderQuantity;
	
	private Long storeId;
}
