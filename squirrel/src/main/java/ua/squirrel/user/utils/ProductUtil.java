package ua.squirrel.user.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import ua.squirrel.user.entity.product.Product;
import ua.squirrel.user.entity.product.ProductModel;

@Component
@Deprecated
public class ProductUtil extends SmallOneUtil {
	
		public List<ProductModel> createProductPriceQuantityModelList(List<Product> products, String idPriceQuantity){
			List<ProductModel> productList = new ArrayList<>();
			if(idPriceQuantity==null || idPriceQuantity.isEmpty()) {
				return productList;
			}
			Map <Long, String> priceQuantity = this.spliteIdValue1Value2(idPriceQuantity,"(:)|quantity|price");
			
			products.forEach(prod->{
				String [] values = priceQuantity.get(prod.getId()).split("val");
				productList.add(ProductModel.builder()
						.id(prod.getId())
						.name(prod.getName())
						.measureProduct(prod.getMeasureProduct().getMeasure())
						.propertiesProduct(values[0])//quantity
						.description(values[1])//price
						.group(prod.getGroup())
						.build()
						);
			});
			
			return productList;
		}
}
