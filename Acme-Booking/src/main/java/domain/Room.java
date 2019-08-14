package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Room extends DomainEntity {

	/* Attributes */

	private String title, ticker, description, detailedDescription, address, photos, openingHour, closingHour, status, attachments;
	private Double pricePerHour;
	private Integer capacity;
	private boolean isOutOfService, isDraft;
	private Category category;
	private Collection<Service> services;
	private Owner owner;

	/* Getters and setters */

	@NotBlank
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@NotBlank
	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	@NotBlank
	@Type(type="text")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@NotBlank
	@Type(type="text")
	public String getDetailedDescription() {
		return detailedDescription;
	}

	public void setDetailedDescription(String detailedDescription) {
		this.detailedDescription = detailedDescription;
	}

	@NotBlank
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	@NotBlank
	@Pattern(regexp = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")
	public String getOpeningHour() {
		return openingHour;
	}

	public void setOpeningHour(String openingHour) {
		this.openingHour = openingHour;
	}

	@NotBlank
	@Pattern(regexp = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")
	public String getClosingHour() {
		return closingHour;
	}

	public void setClosingHour(String closingHour) {
		this.closingHour = closingHour;
	}

	@NotBlank
	@Pattern(regexp = "\b(IN-REVISION|APPROVED|REJECTED)\b")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@NotBlank
	public String getAttachments() {
		return attachments;
	}

	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}

	@NotNull
	@Min(value = 0L, message = "The value must be positive")
	public Double getPricePerHour() {
		return pricePerHour;
	}

	public void setPricePerHour(Double pricePerHour) {
		this.pricePerHour = pricePerHour;
	}
	
	@NotNull
	@Min(value = 0L, message = "The value must be positive")
	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public boolean getIsOutOfService() {
		return isOutOfService;
	}

	public void setIsOutOfService(boolean isOutOfService) {
		this.isOutOfService = isOutOfService;
	}
	
	public boolean getisDraft() {
		return isDraft;
	}

	public void setisDraft(boolean isDraft) {
		this.isDraft = isDraft;
	}
	
	@Valid
	@ManyToOne(optional = true)
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	@Valid
	@NotNull
	@ManyToMany
	public Collection<Service> getServices() {
		return services;
	}

	public void setServices(Collection<Service> services) {
		this.services = services;
	}

	@Valid
	@ManyToOne(optional = true)
	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

}
