package ua.squirrel.user.entity.report;


import java.util.List;

import lombok.Builder;
import lombok.Data;
import ua.squirrel.user.entity.product.ProductModel;
import ua.squirrel.user.entity.product.composite.CompositeProductModel;
import ua.squirrel.user.entity.store.invoice.InvoiceModel;

@Builder
@Data
public class ReportModel {
	
	private String dateStart;
	
	private String dateEnd;
	
	private List<ProductModel> productReportData;
	
	private List<CompositeProductModel> compositeProductReportData;
	
	private List<InvoiceModel> invoiceData;

}
