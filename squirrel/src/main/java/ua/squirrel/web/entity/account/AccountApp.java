package ua.squirrel.web.entity.account;


import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;
import ua.squirrel.web.entity.user.Role;
@Data
@Entity
@Table(name = "accounts_app")
public class AccountApp {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "account_app_id", nullable = false)
	private long id;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "account_roles", joinColumns = @JoinColumn(name = "account_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;
	@Column(name = "login",  nullable = false, unique = true)
	private String login;
	@Column(name = "password",  nullable = false)
	private String password;
}
