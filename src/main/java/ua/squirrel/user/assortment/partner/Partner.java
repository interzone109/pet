package ua.squirrel.user.assortment.partner;


import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import ua.squirrel.user.assortment.product.Product;
import ua.squirrel.web.entity.user.User;


@Entity
@Table(name = "partners")
@Data
public class Partner {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "partner_id", nullable = false)
	private long id;
	
	@Column(name = "company", nullable = false)
	private String company;
	
	@Column(name = "partner_phone")
	private String phonNumber;
	
	@Column(name = "partner_mail")
	private String partnerMail;
	
	/*@OneToMany(fetch = FetchType.LAZY , cascade=CascadeType.ALL)
	@JoinTable(name = "partner_product", joinColumns = @JoinColumn(name = "partner_id"),
    inverseJoinColumns = @JoinColumn(name = "product_id"))
	private List<Product> products;*/
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY  )
    @JoinColumn(name = "user_owner_id" ,nullable=false )
	private User userOwner ;
}
