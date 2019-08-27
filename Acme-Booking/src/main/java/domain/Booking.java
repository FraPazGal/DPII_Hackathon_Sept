
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
public class Booking extends DomainEntity {

	/* Attributes */

	private String				title, bookingReason, status, rejectionReason;
	private Integer				expectedAttendance, duration;
	private Double				reservationPrice;
	private Date				requestedMoment, reservationDate;
	private Collection<Service>	services;
	private Customer			customer;
	private Room				room;


	/* Getters and setters */

	@Valid
	@ManyToOne(optional = true)
	public Room getRoom() {
		return this.room;
	}

	public void setRoom(final Room room) {
		this.room = room;
	}

	@Valid
	@ManyToOne(optional = true)
	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(final Customer customer) {
		this.customer = customer;
	}

	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	@Type(type = "text")
	public String getBookingReason() {
		return this.bookingReason;
	}

	public void setBookingReason(final String bookingReason) {
		this.bookingReason = bookingReason;
	}

	@NotBlank
	@Pattern(regexp = "\\b(PENDING|ACCEPTED|REJECTED)\\b")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public String getRejectionReason() {
		return this.rejectionReason;
	}

	public void setRejectionReason(final String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	@NotNull
	@Min(value = 1L)
	public Integer getExpectedAttendance() {
		return this.expectedAttendance;
	}

	public void setExpectedAttendance(final Integer expectedAttendance) {
		this.expectedAttendance = expectedAttendance;
	}

	@NotNull
	@Min(value = 1L)
	public Integer getDuration() {
		return this.duration;
	}

	public void setDuration(final Integer duration) {
		this.duration = duration;
	}

	@NotNull
	@Min(value = 0L)
	public Double getReservationPrice() {
		return this.reservationPrice;
	}

	public void setReservationPrice(final Double reservationPrice) {
		this.reservationPrice = reservationPrice;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getRequestedMoment() {
		return this.requestedMoment;
	}

	public void setRequestedMoment(final Date requestedMoment) {
		this.requestedMoment = requestedMoment;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getReservationDate() {
		return this.reservationDate;
	}

	public void setReservationDate(final Date reservationDate) {
		this.reservationDate = reservationDate;
	}

	@Valid
	@NotNull
	@ManyToMany
	public Collection<Service> getServices() {
		return this.services;
	}

	public void setServices(final Collection<Service> services) {
		this.services = services;
	}

}
