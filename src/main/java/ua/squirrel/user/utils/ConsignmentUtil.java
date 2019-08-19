package ua.squirrel.user.utils;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import ua.squirrel.user.entity.product.Product;
import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.user.entity.store.consignment.ConsignmentModel;

@Component
public class ConsignmentUtil extends SmallOneUtil {

	

	public List<ConsignmentModel> createConsignmentModelList(List<Consignment> inputList) {
		List<ConsignmentModel> modelList = new ArrayList<>();
		inputList.forEach(consignment -> {
			modelList.add(ConsignmentModel.builder().id(consignment.getId())
					.consignmentStatus(consignment.getConsignmentStatus().getName())
					.date(consignment.getDate().toString()).isApproved(consignment.isApproved())
					.meta(consignment.getMeta()).build());
		});

		return modelList;
	}

	public ConsignmentModel createConsignmentModel(Consignment consignment ) {
		return ConsignmentModel.builder().id(consignment.getId()) 
				.consignmentStatus(consignment.getConsignmentStatus().getName())
				.date(consignment.getDate().toString()).isApproved(consignment.isApproved())
				.meta(consignment.getMeta()).build();
	}

	/**
	 * 1. проходимся по масиву накладных начиная самой моследней
	 * 2.1 в каждом узле нужно получить цену и обновить остатки
	 * 2.2 те остатки что не перекрыты накладной попадают в следующую итерацию
	 * **/
	public Map<Product, Integer> formFIFOIngridientPrice(List<Consignment> consignmentFIFOList, Map<Product, Integer> productRateMap) {
		Map<Product, Integer> productщеPriceMap = new HashMap<>();
		consignmentFIFOList.forEach(consignment->{// проход по накладным
			consignment.getConsignmentNode().stream().forEach(consignmentNode->{// проход по узлам
				if(productRateMap.containsKey(consignmentNode.getProduct())) {
				Product product = consignmentNode.getProduct();
				//получаем общее количество расхода ингридиента на текущий чек
				int totalProductSpend = productRateMap.get(product);
				//остатки в накладной (остатки в партии)
				int nodeLeftover = consignmentNode.getCurrentQuantity();
				//остатки в totalProductSpend больше нуля (если 0 то цена получена)
				// и остатки в партии больше чем расход в чеке
				if( totalProductSpend > 0 && totalProductSpend > nodeLeftover ) {
					//отмимаем расход от остатков
					int productRest =  totalProductSpend - nodeLeftover;
					//ложим остатки обратно в мапу с расходами 
					productRateMap.put(product, productRest);
					consignmentNode.setCurrentQuantity(0);// устанавливаем остаток в партии 0
					//сохраняем продукт и расход по накладной
					if(productщеPriceMap.containsKey(consignmentNode.getProduct())) {
						System.out.println("расчет средней стоимости по накладным");
					}else {
						productщеPriceMap.put(product, consignmentNode.getUnitPrice());
					}
				}else{//если расход меньше остатков в партии
					int consigmentRest =  consignmentNode.getCurrentQuantity() - totalProductSpend  ;
					productRateMap.put(product, 0);
					consignmentNode.setCurrentQuantity(consigmentRest);
					//сохраняем продукт и расход по накладной
					if(productщеPriceMap.containsKey(consignmentNode.getProduct())) {
						System.out.println("расчет средней стоимости по накладным");
					}else {
						productщеPriceMap.put(product, consignmentNode.getUnitPrice());
					}
				}
			}
			});
			System.err.println("curent date - "+consignment.getDate());
		});
		
		return productщеPriceMap ;
	}
	

}
