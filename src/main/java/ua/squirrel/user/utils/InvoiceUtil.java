package ua.squirrel.user.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import ua.squirrel.user.entity.store.Store;
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
	
	public InvoiceModel createInvoiceMetaModel(Invoice invoice) {
		String [] invoiceMeta = invoice.getMeta().split(":%:");
		return InvoiceModel.builder()
				.id(invoice.getId())
				.storeId(invoice.getStore().getId())
				.cashBoxStartDay(Integer.parseInt(invoiceMeta[0]))
				.currentSell(Integer.parseInt(invoiceMeta[1]))
				.sellQuantity(Integer.parseInt(invoiceMeta[2]))
				.build();
	}

	public Invoice createNewInvoice(InvoiceModel invoiceModel, Store store) {
		StringBuilder meta = new StringBuilder();
		meta.append(invoiceModel.getCashBoxStartDay()+":%:"+invoiceModel.getCurrentSell()+":%:"+invoiceModel.getSellQuantity());
		Invoice invoice = new Invoice();
		invoice.setDate(LocalDate.now());
		invoice.setStore(store);
		invoice.setMeta(meta.toString());
		System.err.println(LocalDate.now());
		return invoice;
	}
}
