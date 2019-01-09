package ua.squirrel.entity.user;

import java.util.Set;
import javax.persistence.*;
import lombok.Builder;
import lombok.Data;


@Entity
@Table(name = "user")
@Builder
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String login;
	private String hashPass;
	@ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
	 private Set<Role> roles;
	@JoinTable(name = "user_state", joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "state_id"))
	private Set<State> states;
}
