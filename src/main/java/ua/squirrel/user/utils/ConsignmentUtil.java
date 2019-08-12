package ua.squirrel.user.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

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
	 * Медот получает накладную , количество расхода ингридиентов и остатки на магазине ( для получение цены ингридиента)
	 * Далее добавляем в накладную расход и цену ингридиента и сохраняем данные.
	 * */
	@Deprecated
	public void addData(Consignment consignment, Map<Long, Integer> ingridientQuantity, String leftovers) {
		//получаем ид и количество ингридиентов из накладной
		Map<Long, Integer> consignmentIdsQuantity = super.spliteIdsValue(consignment.getConsignmentData(), "quantity[0-9]*price");
		// получаем ид и цену за ед ингридиента
		Map<Long, Integer> consignmentIdsPrice = new HashMap<>();
		if(consignment.getConsignmentData() != null && !consignment.getConsignmentData().isEmpty()) {
		String[] str = consignment.getConsignmentData().split("[0-9]*quantity|price");
			for (int i = 1; i < str.length; i+=2) {
				consignmentIdsPrice.put(Long.parseLong(str[i-1].split(":")[0]), Integer.parseInt(str[i]));
			}
		}
		
		Map<Long, Integer> storeIdsPrice =  new HashMap<>() ;
		if(leftovers != null && !leftovers.isEmpty()) {
			String[] strStore = leftovers.split("[0-9]*quantity|price");
			for (int i = 1; i < strStore.length; i+=2) {
				storeIdsPrice.put(Long.parseLong(strStore[i-1].split(":")[0]), Integer.parseInt(strStore[i]));
			}
		} 
		
		ingridientQuantity.keySet().forEach(id->{
			if(consignmentIdsQuantity.containsKey(id)) {//если ингридиент с айди есть в накладной но сумируем его остатки
				int newQuantity = consignmentIdsQuantity.get(id) + ingridientQuantity.get(id);
				consignmentIdsQuantity.put(id, newQuantity);
			}else {// если отсутствует то добавляем в мапу
				consignmentIdsQuantity.put(id, ingridientQuantity.get(id));
			}
		});
		
		StringBuilder consignmentData = new StringBuilder();
		consignmentIdsQuantity.keySet().forEach(id->{
			int price = storeIdsPrice.get(id) == null ?0 : storeIdsPrice.get(id);
			consignmentData.append(id+":"+consignmentIdsQuantity.get(id)+"quantity"+price+"price");
		});
		consignment.setConsignmentData(consignmentData.toString());
	}

}
