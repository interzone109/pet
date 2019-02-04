package ua.squirrel.user.entity.store.storage;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateDeleteStorageModel {
	private Map<Long, Integer> idsPrice;

	private List<Long> removeProduct;
}
