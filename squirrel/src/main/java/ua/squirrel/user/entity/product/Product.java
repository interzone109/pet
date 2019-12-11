package ua.squirrel.user.entity.product;

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
import ua.squirrel.user.entity.partner.Partner;
import ua.squirrel.user.entity.product.node.ProductMap;
import ua.squirrel.web.entity.user.User;
/**
 * Класс Product описывает сущность Продукта (ингридиента).
 * Хранит данные об описании ингридиента, его поставщике и
 * узел связывающий его с продуктом в котором истульзуеться данный ингридиент
 * */
@Data
@Entity
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "product_id", nullable = false)
	private long id;
	// наименование игридиента
	@Column(name = "product_name", nullable = false)
	private String name;
	// описание игридиента
	@Column(name = "description")
	private String description;
	// группа игридиента
	@Column(name = "product_group")
	private String group;
	// свойство ( в дальнейшем данная переменная будет удалена из-за отсутствия надобности в ней)
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "properties_id")
	private PropertiesProduct propertiesProduct;
	// единица хранения игридиента (шт/гр/мл)
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "measure_id")
	private MeasureProduct measureProduct;
	// Поставщих данного ингридиента
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "product_partner_id", nullable = false)
	private Partner partner;
	//пользователь. Данная связь нужна для пресечения доступа к данным других пользователей
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "product_user_id", nullable = false)
	private User user;
	// узел связывающий ингридиент с композитным продуктом 
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY 
			, cascade = CascadeType.ALL)
	private List<ProductMap> productMap;
}
