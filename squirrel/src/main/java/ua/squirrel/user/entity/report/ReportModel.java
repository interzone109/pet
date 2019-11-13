package ua.squirrel.user.entity.report;


import java.util.List;

import lombok.Builder;
import lombok.Data;
import ua.squirrel.user.entity.product.ProductModel;

@Builder
@Data
public class ReportModel {
	
	private String dateStart;
	
	private String dateEnd;
	
	private List<ProductModel> productReportData;

}
