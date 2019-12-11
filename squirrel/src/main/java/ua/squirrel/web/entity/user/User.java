package ua.squirrel.web.entity.user;


import java.time.LocalDate;
import javax.persistence.*;
import lombok.Data;
import lombok.ToString;
import ua.squirrel.web.entity.account.AccountApp;

@Entity
@Table(name = "users")
@Data
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "user_id", nullable = false, unique = true, updatable= false)
	private long id;


	@Column(name = "mail", nullable = false, unique = true)
	private String mail;
	
	// дата регистрации
	private LocalDate date;
	
	@OneToOne(cascade={CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name="account_id")
	private AccountApp account;
	

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL,
    fetch = FetchType.LAZY, optional = false)
	@ToString.Exclude
	private UserSubscription userSubscription;

}
