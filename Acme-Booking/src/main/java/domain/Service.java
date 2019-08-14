package domain;

import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Service extends DomainEntity {

	/* Attributes */

	private Map<String,String> name;
	private Double price;
	private boolean isFinal;

	/* Getters and setters */
	
	@ElementCollection
	public Map<String, String> getName() {
		return name;
	}
	
	public void setName(Map<String, String> name) {
		this.name = name;
	}
	
	@NotNull
	@Min(value = 0L, message = "The value must be positive")
	public Double getPrice() {
		return price;
	}
	
	public void setPrice(Double price) {
		this.price = price;
	}

	
	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}
	
	

}
