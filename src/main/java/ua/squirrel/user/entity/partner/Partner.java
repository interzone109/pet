package ua.squirrel.user.entity.partner;

import java.util.List;
import javax.persistence.*;
import lombok.Data;
import ua.squirrel.user.entity.product.Product;
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

	
	@OneToMany(mappedBy = "partner", fetch = FetchType.LAZY 
			,orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Product> products;

	
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "partner_user_id", nullable = false)
	private User user;

}
