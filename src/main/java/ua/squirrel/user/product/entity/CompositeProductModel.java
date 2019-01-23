package ua.squirrel.user.product.entity;

import java.util.List;
import java.util.Set;

import lombok.Data;

@Data
public class CompositeProductModel {

	private long id;

	private Set<Product> products;

	private List<Integer> consumption;
}
