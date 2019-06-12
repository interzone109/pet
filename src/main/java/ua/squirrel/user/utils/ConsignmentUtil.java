package ua.squirrel.user.utils;


import java.util.ArrayList;
import java.util.List;

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

}
