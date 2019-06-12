package ua.squirrel.user.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import ua.squirrel.user.entity.store.invoice.Invoice;
import ua.squirrel.user.entity.store.invoice.InvoiceModel;

@Component
public class InvoiceUtil extends SmallOneUtil{

	public List<InvoiceModel> createInvoiceModel(List<Invoice> invoices) {
		List<InvoiceModel> invoiceModel = new ArrayList<>();
		invoices.forEach(invoice->{
			//плучаем ид конечного продукта и количество его продаж
		  Map<Long, Integer> idsSale = this.spliteIdsValue(invoice.getInvoiceData(), "sale");
		  
		  invoiceModel.add(InvoiceModel.builder()
				  .id(invoice.getId())
				  .dateStart(invoice.getDate().toString())
				  .invoiceData(idsSale)
				  .build()
				  );
		});
		return invoiceModel;
	}

}
