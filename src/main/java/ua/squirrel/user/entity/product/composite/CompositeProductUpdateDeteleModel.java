package ua.squirrel.user.entity.product.composite;

import java.util.List;
import java.util.Map;

import lombok.Data;
@Data
public class CompositeProductUpdateDeteleModel {
	private Map<Long, Integer> composites ;
	private List<Long> deleteIds ;
}
