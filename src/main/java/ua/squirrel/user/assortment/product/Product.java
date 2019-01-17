package ua.squirrel.user.assortment.product;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import ua.squirrel.user.assortment.partner.Partner;
import ua.squirrel.web.entity.user.User;

@Data
@Entity
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id", nullable = false)
	private long id;
	@Column(name = "product_name", nullable = false)
	private String name;
	@Column(name = "description")
	private String description;
	@ManyToOne(targetEntity = Partner.class)
	private Partner partner ;
	@OneToOne
	private User user ;
	
}
