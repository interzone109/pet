package ua.squirrel.user.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.stereotype.Component;

import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.user.entity.store.consignment.ConsignmentModel;

@Component
public class ConsignmentUtil extends SmallOneUtil {

	public Calendar convertDate(String date) {
		String [] splite = date.split("/");
		Calendar calendar = new GregorianCalendar(
				Integer.parseInt(splite[0]),
				Integer.parseInt(splite[1]),
				Integer.parseInt(splite[2]), 0, 0);

		
		//calendar.set(Calendar.HOUR_OF_DAY, 0);
		//calendar.set(Calendar.MINUTE, 0);
		//calendar.set(Calendar.SECOND, 0);
		//calendar.set(Calendar.MILLISECOND, 0);
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











