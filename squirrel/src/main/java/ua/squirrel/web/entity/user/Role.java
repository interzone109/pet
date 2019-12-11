package ua.squirrel.web.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="roles")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "role_Id", nullable = false)
	private long id;
	@Column(name = "role_name", nullable = false)
	private String name ;

    
}
