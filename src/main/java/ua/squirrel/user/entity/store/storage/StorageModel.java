package ua.squirrel.user.entity.store.storage;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;
import ua.squirrel.user.entity.product.composite.CompositeProductModel;
import ua.squirrel.user.entity.store.consignment.ConsignmentModel;


@Builder
@Data
public class StorageModel {
	
	private Map<CompositeProductModel, Integer> productPrice ;

	private List<ConsignmentModel> consignment;
	
	private Map<Long, Integer> idsPrice ;
	
	private List<Long> removeProduct ;
}
