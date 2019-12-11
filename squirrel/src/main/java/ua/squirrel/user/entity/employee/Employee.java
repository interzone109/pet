package ua.squirrel.user.entity.employee;


import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.web.entity.account.AccountApp;
import ua.squirrel.web.entity.user.User;

@Data
@Entity
@Table(name = "employees")
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "employee_id", nullable = false)
	private long id;
	@Column(name = "first_name", nullable = false)
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "salary")
	private int salary;
	@Column(name = "cash_box_type")
	private Integer cashBoxType; 
	

	@Column(name = "hairing_date")
	private LocalDate hairingDate;
	@Column(name = "status")
	private String status;
	
	@OneToOne(cascade={CascadeType.PERSIST})
    @JoinColumn(name="account_id")
	private AccountApp accountApp;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "store_employee_id", nullable = false)
	private Store store;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "employee_user_id", nullable = false)
	private User user;
	
}
