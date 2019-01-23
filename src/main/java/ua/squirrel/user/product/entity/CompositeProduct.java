package ua.squirrel.user.product.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import ua.squirrel.web.user.entity.User;

@Data
@Entity
@Table(name = "сomposite_product")
public class CompositeProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "сomposite_product_id", nullable = false)
	private long id;
	@Column(name = "name")
	private String name;
	
	/*@JsonManagedReference
	@ManyToMany(cascade =  CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable( name = "сomposite_product", 
        joinColumns =  @JoinColumn(name = "сomposite_product_id"), 
        inverseJoinColumns =  @JoinColumn(name = "product_id"))
    private List<Product> products ;
	
	@ElementCollection
	@CollectionTable(
	        name="consumption_id",
	        joinColumns=@JoinColumn(name="user_id"))
	private List<Integer> consumption ;*/
	
	
	@ElementCollection
    @MapKeyColumn(name="product_id")
    @Column(name="product_id")
    @CollectionTable(name="merchandise_consumption", joinColumns=@JoinColumn(name="consuption_id"))
	private Map<Product, Integer> productsConsumption;
	
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_owner_id", nullable = false)
	private User user ; 
}
