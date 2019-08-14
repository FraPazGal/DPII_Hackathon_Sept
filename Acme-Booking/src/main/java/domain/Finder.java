package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Finder extends DomainEntity {

	/* Attributes */

	private String keyWord, minimumHour, maximumHour;
	private Double maximumFee;
	private Integer capacity;
	private Date searchMoment;
	private Collection<Room> results;
	private Customer customer;

	/* Getters and setters */

	@Length(max = 100, message = "The keyword is too long")
	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	
	public String getMinimumHour() {
		return minimumHour;
	}

	@Pattern(regexp="^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$", message="invalid.hour")
	public void setMinimumHour(String minimumHour) {
		this.minimumHour = minimumHour;
	}

	public String getMaximumHour() {
		return maximumHour;
	}

	@Pattern(regexp="^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$", message="invalid.hour")
	public void setMaximumHour(String maximumHour) {
		this.maximumHour = maximumHour;
	}

	@Min(value = 0L, message = "The value must be positive")
	public Double getMaximumFee() {
		return maximumFee;
	}

	public void setMaximumFee(Double maximumFee) {
		this.maximumFee = maximumFee;
	}
	
	@Min(value = 0L, message = "The value must be positive")
	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getSearchMoment() {
		return searchMoment;
	}

	public void setSearchMoment(Date searchMoment) {
		this.searchMoment = searchMoment;
	}

	@Valid
	@ManyToMany
	@NotNull
	public Collection<Room> getResults() {
		return results;
	}

	public void setResults(Collection<Room> results) {
		this.results = results;
	}

	@Valid
	@NotNull
	@OneToOne(optional = false)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
