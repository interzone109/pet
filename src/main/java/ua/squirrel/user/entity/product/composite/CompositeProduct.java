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
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "product_group")
	private String group;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "properties_id")
	private PropertiesProduct propertiesProduct;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "measure_id")
	private MeasureProduct measureProduct;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_owner_id", nullable = false)
	private User user ; 
	
	@OneToMany(mappedBy = "compositeProduct", fetch = FetchType.LAZY 
			, cascade = CascadeType.ALL)
	private List<ProductMap> productMap;
	
	
	
	
	
	
	//поле хранит текущай расход и id расходуемого ингридиента
	@Column(name = "expend_product_id", length=512)
	@Deprecated
	private String productExpend ;
	
	//поле хранит предыдущий расход и дату его изменения
	@Column(name = "expend_update_id", length=1024)
	@Deprecated
	private String expendUpdate ;
	
	//поле хранит дату и ид удаленного ингридиента
	@Column(name = "delete_ingridient_date_id", length=1024)
	@Deprecated
	private String ingridientDelete ;
}
