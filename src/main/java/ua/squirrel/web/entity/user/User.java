package ua.squirrel.web.entity.user;


import java.time.LocalDate;
import java.util.Set;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", nullable = false, unique = true, updatable= false)
	private long id;

	@Column(name = "login", nullable = false, unique = true)
	private String login;

	@Column(name = "password", nullable = false)
	private String hashPass;

	@Column(name = "mail", nullable = false, unique = true)
	private String mail;
	
	// дата регистрации
	private LocalDate date;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;
	

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL,
    fetch = FetchType.LAZY, optional = false)
	private UserSubscription userSubscription;

}
