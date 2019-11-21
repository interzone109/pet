package ua.squirrel.user.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.squirrel.user.entity.product.Product;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.user.entity.store.consignment.ConsignmentModel;
import ua.squirrel.user.entity.store.consignment.node.ConsignmentNode;
import ua.squirrel.user.service.store.consignment.status.ConsignmentStatusServiceImpl;

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

	public ConsignmentModel createConsignmentModel(Consignment consignment) {
		return ConsignmentModel.builder().id(consignment.getId())
				.consignmentStatus(consignment.getConsignmentStatus().getName()).date(consignment.getDate().toString())
				.isApproved(consignment.isApproved()).meta(consignment.getMeta()).build();
	}

	/**
	 * 1. проходимся по масиву накладных начиная самой моследней 
	 * 2.1 в каждом узленужно получить цену и обновить остатки 
	 * 2.2 те остатки что не перекрыты накладной попадают в следующую итерацию
	 **/
	@Autowired
	private ConsignmentStatusServiceImpl consignmentStatusServiceImpl;
	public Map<Product, Integer> formFIFOIngridientPrices(List<Consignment> consignmentFIFOList, Map<Product, Integer> productRateMap, Store store) {
		Map<Product, Integer> productщеPriceMap = new HashMap<>();
		if(!consignmentFIFOList.isEmpty()) {
			consignmentFIFOList.forEach(consignment -> {// проход по накладным
			consignment.getConsignmentNode().stream().forEach(consignmentNode -> {// проход по узлам
				if (productRateMap.containsKey(consignmentNode.getProduct())) {
					Product product = consignmentNode.getProduct();// текущий ингридиент
					int totalProductSpend = productRateMap.get(product);// получаем общее количество расхода ингридиента на текущий чекe
					int nodeLeftover = consignmentNode.getCurrentQuantity();// остатки в накладной (остатки в партии)
					//проверяем если продукт храниться в кг/л то получаем цену за грам
					String measure = product.getMeasureProduct().getMeasure();
					int totalSumm = 0;
					if(measure.equals("LITER") || measure.equals("KILOGRAM")) {
						// получаем сумму по старой партии и выщитываем стоимость за  грамм продукта
						 totalSumm  = productщеPriceMap.containsKey(product)? productщеPriceMap.get(product)/1000:0;
					}else {
						 totalSumm  = productщеPriceMap.containsKey(product)? productщеPriceMap.get(product):0;// получаем сумму по старой партии
					}
					
					
					if (totalProductSpend > 0 && totalProductSpend > nodeLeftover) {
						// отмимаем расход от остатков
						int productRest = totalProductSpend - nodeLeftover;
						// ложим остатки обратно в мапу с расходами
						productRateMap.put(product, productRest);
						consignmentNode.setCurrentQuantity(0);// устанавливаем остаток в партии 0
						// сохраняем продукт и цену в партии умноженую на количество из накладной 
						productщеPriceMap.put(product, (consignmentNode.getUnitPrice() * nodeLeftover) + totalSumm);
					} else if (totalProductSpend > 0 && nodeLeftover >0){
						int consigmentRest = consignmentNode.getCurrentQuantity() - totalProductSpend;
						productRateMap.put(product, 0);
						consignmentNode.setCurrentQuantity(consigmentRest);
						// сохраняем продукт и цену в партии умноженую на общий расход ; totalSumm = 0
						productщеPriceMap.put(product, (consignmentNode.getUnitPrice() * totalProductSpend) + totalSumm);
					}
				}
			});
		});
	}
		//перерасчет на случай излишних остатков
		List<Product> overrunProduct = productRateMap.keySet().stream().filter(product-> productRateMap.get(product) !=0).collect(Collectors.toList());
		   if(overrunProduct.size()>0) {
				Consignment overrunConsignment = new Consignment();
				overrunConsignment.setDate(LocalDate.now());
				overrunConsignment.setConsignmentStatus(consignmentStatusServiceImpl.findOneByName("ARRIVAL").get());
				overrunConsignment.setApproved(false);
				overrunConsignment.setMeta("autoARRIVAL:%:Не учтенные остатки");
				overrunConsignment.setStore(store);
				List <ConsignmentNode> consignmentNodeList = new ArrayList<>();
				overrunProduct.forEach(product->{
					ConsignmentNode consignmentNode  = new ConsignmentNode();
					consignmentNode.setConsignment(overrunConsignment);
					consignmentNode.setProduct(product);
					consignmentNode.setCurrentQuantity(productRateMap.get(product));
					consignmentNode.setQuantity(productRateMap.get(product));
					consignmentNode.setUnitPrice(0);
					consignmentNodeList.add(consignmentNode);
						if(!productщеPriceMap.containsKey(product)) {
							productщеPriceMap.put(product, 0);
						}
				});
				overrunConsignment.setConsignmentNode(consignmentNodeList);
				consignmentFIFOList.add(overrunConsignment);
		   }
		
		return  productщеPriceMap;
	}
	

}
