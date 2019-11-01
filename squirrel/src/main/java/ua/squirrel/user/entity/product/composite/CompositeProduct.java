package ua.squirrel.user.entity.product.composite;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


import lombok.Data;
import ua.squirrel.user.entity.product.MeasureProduct;
import ua.squirrel.user.entity.product.PropertiesProduct;
import ua.squirrel.user.entity.product.node.ProductMap;
import ua.squirrel.web.entity.user.User;

/**
 * Класс CompositeProduct описывает карту товара 
 * (набор ингридиентов из которых состоит итоговый продукт).
 * Хранит данные описывающие композитный продукт, узлы с расходом ингридиента.
 * */
@Data
@Entity
@Table(name = "сomposite_product")
public class CompositeProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "сomposite_product_id", nullable = false)
	private long id;
	// наименование
	@Column(name = "name", nullable = false)
	private String name;
	//группа 
	@Column(name = "product_group")
	private String group;
	// единица расхода продукта (шт/гр/мл)
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "measure_id")
	private MeasureProduct measureProduct;
	//пользователь
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_owner_id", nullable = false)
	private User user ; 
	// узлы с ингридиентом и расходом на условную единицу композитного продукта
	@OneToMany(mappedBy = "compositeProduct", fetch = FetchType.LAZY 
			, cascade = CascadeType.ALL,orphanRemoval=true)
	private List<ProductMap> productMap;
	
	// свойство ( в дальнейшем данная переменная будет удалена из-за отсутствия надобности в ней)
	@Deprecated
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "properties_id")
	private PropertiesProduct propertiesProduct;
	

}
