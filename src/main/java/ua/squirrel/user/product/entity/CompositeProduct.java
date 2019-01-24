package ua.squirrel.user.product.entity;

import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;


import lombok.Data;
import ua.squirrel.web.user.entity.User;

/**
 * Класс описывает продукт который создает пользовтель 
 * из доступных товаров у поставщика
 * 
 * */

@Data
@Entity
@Table(name = "сomposite_product")
public class CompositeProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "сomposite_product_id", nullable = false)
	private long id;
	
	@Column(name = "name", unique = true)
	private String name;
	
	@ElementCollection
    @MapKeyColumn(name="product_id")
    @Column(name="quantity")
    @CollectionTable(name="merchandise_consumption", joinColumns=@JoinColumn(name="consuption_id"))
	private Map<Long, Integer> productsConsumption;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_owner_id", nullable = false)
	private User user ; 
}
