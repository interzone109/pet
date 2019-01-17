package ua.squirrel.user.assortment.partner;


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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import ua.squirrel.user.assortment.product.Product;

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
	private List<Product> assortment;*/
}
