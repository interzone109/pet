package ua.squirrel.web.entity.user;

import java.util.List;
import java.util.Set;
import javax.persistence.*;
import lombok.Data;
import ua.squirrel.user.assortment.product.Product;


@Entity
@Table(name = "user")
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", nullable = false,unique=true)
	private long id;
	
	@Column(name = "login", nullable = false,unique=true)
	private String login;
	
	@Column(name = "password", nullable = false)
	private String hashPass;
	
	@Column(name = "mail", nullable = false,unique=true)
	private String mail ;
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_state", joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "state_id"))
	private Set<State> states;
	
	
	
	@OneToMany(fetch = FetchType.LAZY , cascade=CascadeType.ALL)
    @JoinTable(name = "user_product", joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "product_id"))
	private List<Product> products;
	
	
}
