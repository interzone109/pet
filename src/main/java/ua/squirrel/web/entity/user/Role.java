package ua.squirrel.web.entity.user;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="roles")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_Id", nullable = false)
	private long id;
	@Column(name = "role_name", nullable = false)
	private String name ;
   /* @ManyToMany(mappedBy = "roles", fetch=FetchType.LAZY)
	private Set<User> users;*/
    
}
