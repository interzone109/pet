package ua.squirrel.user.partner;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import ua.squirrel.user.product.ProductModel;

@Builder
@Data
public class PartnerModel {
	
	private long id;
	
	private String company;
	
	private String phonNumber;
	
	private String partnerMail;
	
	private List<ProductModel> productsModel;
}
