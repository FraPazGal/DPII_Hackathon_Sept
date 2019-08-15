package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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

	private String title, visibility, ticker, description, scheduleDetails, address, photos, openingHour, closingHour, status, attachments;
	private Double pricePerHour;
	private Integer capacity;
	private Category category;
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
	@Pattern(regexp = "\b(DRAFT|ACTIVE|OUTOFSERVICE)\b")
	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
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
		return scheduleDetails;
	}

	public void setDetailedDescription(String detailedDescription) {
		this.scheduleDetails = detailedDescription;
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
	@Pattern(regexp = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$", message = "wrong.hour")
	public String getOpeningHour() {
		return openingHour;
	}

	public void setOpeningHour(String openingHour) {
		this.openingHour = openingHour;
	}

	@NotBlank
	@Pattern(regexp = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$", message = "wrong.hour")
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

	@Valid
	@ManyToOne(optional = true)
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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
