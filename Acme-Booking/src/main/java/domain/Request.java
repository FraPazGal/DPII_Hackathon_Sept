package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Request extends DomainEntity {

	/* Attributes */

	private String title, bookingReason, status, rejectionReason;
	private Integer expectedAttendance, duration;
	private Double reservationPrice;
	private Date requestedMoment, reservationDate;
	private Collection<Service> services;
	
	/* Getters and setters */
	
	@NotBlank
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@NotBlank
	@Type(type="text")
	public String getBookingReason() {
		return bookingReason;
	}
	
	public void setBookingReason(String bookingReason) {
		this.bookingReason = bookingReason;
	}
	
	@NotBlank
	@Pattern(regexp = "\b(PENDING|ACCEPTED|REJECTED)\b")
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getRejectionReason() {
		return rejectionReason;
	}
	
	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}
	
	@NotNull
	@Min(value = 0L, message = "The value must be positive")
	public Integer getExpectedAttendance() {
		return expectedAttendance;
	}
	
	public void setExpectedAttendance(Integer expectedAttendance) {
		this.expectedAttendance = expectedAttendance;
	}
	
	@NotNull
	@Min(value = 0L, message = "The value must be positive")
	public Integer getDuration() {
		return duration;
	}
	
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
	@NotNull
	@Min(value = 0L, message = "The value must be positive")
	public Double getReservationPrice() {
		return reservationPrice;
	}
	
	public void setReservationPrice(Double reservationPrice) {
		this.reservationPrice = reservationPrice;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getRequestedMoment() {
		return requestedMoment;
	}
	
	public void setRequestedMoment(Date requestedMoment) {
		this.requestedMoment = requestedMoment;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getReservationDate() {
		return reservationDate;
	}
	
	public void setReservationDate(Date reservationDate) {
		this.reservationDate = reservationDate;
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


}
