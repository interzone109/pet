package ua.squirrel.user.entity.product.composite;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;


import lombok.Data;
import ua.squirrel.user.entity.product.PropertiesProduct;
import ua.squirrel.web.entity.user.User;

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
	
	@Column(name = "expend_product_id")
	private String productExpend ;
	
	@Column(name = "expend_update_id", length=1024)
	private String expendUpdate ;
	
	@Column(name = "product_group")
	private String group;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "properties_id")
	private PropertiesProduct propertiesProduct;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_owner_id", nullable = false)
	private User user ; 
}
