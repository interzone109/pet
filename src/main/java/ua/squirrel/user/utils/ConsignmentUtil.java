package ua.squirrel.user.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.user.entity.store.consignment.ConsignmentModel;

@Component
public class ConsignmentUtil extends SmallOneUtil {

	public LocalDate convertDate(String date) {
		String [] splite = date.split("[.]");
		LocalDate calendar = LocalDate.of(
				Integer.parseInt(splite[2].trim()),
				Integer.parseInt(splite[1].trim()),
				Integer.parseInt(splite[0].trim()));

		return calendar;
	}

	public List<ConsignmentModel> createConsignmentModelList(List<Consignment> inputList) {
		List<ConsignmentModel> modelList = new ArrayList<>() ;
		inputList.forEach(consignment ->{
			modelList.add(ConsignmentModel.builder()
					.id(consignment.getId())
					.consignmentStatus(consignment.getConsignmentStatus().getName())
					.date(consignment.getDate().toString())
					.isApproved(consignment.isApproved())
					.meta(consignment.getMeta())
					.build()
					);
		});
		
		return modelList;
	}


}











