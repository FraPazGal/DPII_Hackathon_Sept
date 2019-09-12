
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.BookingRepository;
import domain.Actor;
import domain.Booking;
import domain.Customer;
import domain.Room;

@Transactional
@Service
public class BookingService {

	@Autowired
	private RoomService			roomService;

	@Autowired
	private ServiceService		serviceService;

	@Autowired
	private MessageService		messageService;

	@Autowired
	private BookingRepository	bookingRepository;

	@Autowired
	private UtilityService		utilityService;

	@Autowired
	private Validator			validator;


	public Booking create(final Room room) {
		Booking result;
		Assert.isTrue(room.getStatus().equals("ACTIVE"));
		result = new Booking();
		final Collection<domain.Service> services = new ArrayList<>();
		result.setServices(services);
		result.setStatus("PENDING");
		result.setRoom(room);
		return result;
	}

	public Collection<Booking> getListAll() {
		this.checkBooking();

		Actor principal;
		principal = this.utilityService.findByPrincipal();
		Collection<Booking> booking = new ArrayList<Booking>();
		if (this.utilityService.checkAuthority(principal, "OWNER"))
			booking = this.bookingRepository.findAllFinal(principal.getId());
		else if (this.utilityService.checkAuthority(principal, "CUSTOMER"))
			booking = this.bookingRepository.findAllCustomer(principal.getId());

		return booking;
	}
	public Booking findOne(final int id) {
		this.checkBooking();
		return this.bookingRepository.findOne(id);
	}

	public Booking findOneMode(final int id) {
		this.checkBooking();
		final Booking booking = this.findOne(id);
		final Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(booking.getRoom().getOwner().equals(principal) || booking.getCustomer().equals(principal));
		return booking;
	}

	public void delete(final int id) {
		//this.checkBooking();

		final Actor principal = this.utilityService.findByPrincipal();
		Assert.notNull(principal);
		final Booking booking = this.findOne(id);
		Assert.notNull(booking);
		Assert.isTrue(booking.getId() != 0);
		Assert.isTrue(booking.getCustomer().equals(principal));
		this.bookingRepository.delete(booking);
	}

	public Booking reconstruct(final Booking bookingF, final BindingResult binding) {
		this.checkBooking();
		final Booking result = this.create(bookingF.getRoom());
		final Actor principal = this.utilityService.findByPrincipal();
		Booking orig = null;
		Assert.isTrue(bookingF.getRoom().getStatus().equals("ACTIVE"));
		if (bookingF.getId() != 0) {
			orig = this.findOne(bookingF.getId());
			Assert.isTrue(this.utilityService.checkAuthority(principal, "OWNER"));
			Assert.notNull(orig);
			Assert.isTrue(orig.getRoom().getOwner().equals(principal));
			Assert.isTrue(orig.getStatus().equals("PENDING"));
			result.setBookingReason(bookingF.getRejectionReason());
			result.setCustomer(orig.getCustomer());
			result.setBookingReason(orig.getBookingReason());
			result.setDuration(orig.getDuration());
			result.setExpectedAttendance(orig.getExpectedAttendance());
			result.setRequestedMoment(orig.getRequestedMoment());
			result.setReservationDate(orig.getReservationDate());
			result.setReservationPrice(orig.getReservationPrice());
			result.setRoom(orig.getRoom());
			result.setServices(orig.getServices());
			result.setStatus(bookingF.getStatus());
			result.setTitle(orig.getTitle());
			result.setId(orig.getId());
		} else {
			final Integer duration = (bookingF.getDuration() == null) ? 0 : bookingF.getDuration();
			Assert.isTrue(this.utilityService.checkAuthority(principal, "CUSTOMER"));
			result.setCustomer((Customer) principal);
			result.setBookingReason(bookingF.getBookingReason());
			result.setDuration(duration);
			result.setExpectedAttendance(bookingF.getExpectedAttendance());
			final Date d = new Date();
			result.setRequestedMoment(d);
			result.setReservationDate(bookingF.getReservationDate());
			Double reservationPrice = duration * bookingF.getRoom().getPricePerHour();
			if (bookingF.getServices() != null) {
				for (final domain.Service s : bookingF.getServices())
					reservationPrice += s.getPrice();
				result.setServices(bookingF.getServices());
			}
			result.setReservationPrice(reservationPrice);
			result.setRoom(bookingF.getRoom());
			result.setStatus("PENDING");
			result.setTitle(bookingF.getTitle());
		}
		this.validator.validate(result, binding);
		if (result.getId() != 0 && bookingF.getStatus().equals("REJECTED"))
			try {
				Assert.isTrue(!bookingF.getRejectionReason().isEmpty() && bookingF.getRejectionReason() != null);

			} catch (final Throwable oops) {
				binding.rejectValue("rejectionReason", "rejectionReason.error");
			}
		if (result.getExpectedAttendance() != null)
			try {
				Assert.isTrue(result.getExpectedAttendance() <= result.getRoom().getCapacity());

			} catch (final Throwable oops) {
				binding.rejectValue("expectedAttendance", "expectedAttendance.error");
			}
		if (result.getReservationDate() != null)
			try {
				final Calendar calendar = Calendar.getInstance();
				calendar.setTime(result.getReservationDate());
				final int h = calendar.get(Calendar.HOUR_OF_DAY);
				final int m = calendar.get(Calendar.MINUTE);
				final String open = result.getRoom().getOpeningHour();
				final String[] timeOpen = open.split(":");
				final String close = result.getRoom().getClosingHour();
				final String[] timeClose = close.split(":");
				Assert.isTrue(Integer.parseInt(timeOpen[0]) <= h);
				if (Integer.parseInt(timeOpen[0]) == h)
					Assert.isTrue(Integer.parseInt(timeOpen[1]) <= m);
				final Integer asd = h + result.getDuration();
				Assert.isTrue((Integer.parseInt(timeClose[0])) >= (h + result.getDuration()));
				if (Integer.parseInt((timeClose[0])) == (h + result.getDuration()))
					Assert.isTrue((Integer.parseInt(timeClose[1])) >= m);

			} catch (final Throwable oops) {
				binding.rejectValue("reservationDate", "reservationDate.error");
			}
		if (result.getReservationDate() != null)
			try {
				final Date init = result.getReservationDate();
				Date end = result.getReservationDate();
				final Calendar calendar = Calendar.getInstance();
				calendar.setTime(end);
				calendar.add(Calendar.HOUR_OF_DAY, result.getDuration());
				end = calendar.getTime();
				final Collection<Booking> books = this.bookingRepository.findOcupped(init, end);
				Assert.isTrue(books.isEmpty());

			} catch (final Throwable oops) {
				binding.rejectValue("reservationDate", "reservationDate.error.occuped");
			}
		if (result.getReservationDate() != null)
			try {
				final Date now = new Date();
				Assert.isTrue(result.getReservationDate().after(now));

			} catch (final Throwable oops) {
				binding.rejectValue("reservationDate", "reservationDate.error.future");
			}
		return result;
	}
	public Booking save(final Booking booking) {
		this.checkBooking();
		final Actor principal = this.utilityService.findByPrincipal();
		Booking result = this.create(booking.getRoom());
		Assert.isTrue(this.utilityService.checkAuthority(principal, "OWNER") || this.utilityService.checkAuthority(principal, "CUSTOMER"));
		Assert.isTrue(booking.getId() == 0);
		Assert.isTrue(this.utilityService.checkAuthority(principal, "CUSTOMER"));
		result = booking;
		result = this.bookingRepository.save(result);
		return result;
	}

	public Collection<Booking> getList(final int id) {
		this.checkBooking();
		final Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "MANAGER"));
		Collection<Booking> bookings = new ArrayList<>();
		bookings = this.bookingRepository.findByRoom(id);
		return bookings;
	}

	public void rejected(final int id) {
		this.checkBooking();
		;
		final Actor principal = this.utilityService.findByPrincipal();
		Assert.notNull(principal);
		Booking booking = this.findOne(id);
		Assert.isTrue(this.utilityService.checkAuthority(principal, "OWNER"));
		Assert.notNull(booking);
		Assert.isTrue(booking.getId() != 0);
		Assert.isTrue(booking.getRoom().getOwner().equals(principal));
		Assert.isTrue(booking.getRoom().getStatus().equals("ACTIVE"));
		Assert.isTrue(booking.getStatus().equals("PENDING"));
		booking.setStatus("REJECTED");
		booking = this.bookingRepository.save(booking);
		final Date moment = new Date();
		this.messageService.changeStatusNotfication(booking, principal, moment);
	}

	//	public void checkBooking() {
	//		final Date now = new Date();
	//		final Collection<Booking> bookings = this.bookingRepository.getOld(now);
	//		if (!bookings.isEmpty())
	//			for (final Booking r : bookings) {
	//				r.setStatus("REJECTED");
	//				this.bookingRepository.save(r);
	//			}
	//	}

	public void deleteAccountManager(final Room room) {
		final Collection<Booking> req = this.bookingRepository.findByRoom(room.getId());
		if (!req.isEmpty()) {
			for (final Booking r : req) {
				r.setRoom(null);
				r.setCustomer(null);
				this.bookingRepository.save(r);
			}
			this.bookingRepository.deleteInBatch(req);
		}

	}

	public void deleteAccountCustomer() {
		final Actor principal = this.utilityService.findByPrincipal();
		final Collection<Booking> booking = this.bookingRepository.findAllCustomer(principal.getId());
		if (!booking.isEmpty()) {
			for (final Booking r : booking) {
				r.setRoom(null);
				r.setCustomer(null);
				this.bookingRepository.save(r);
			}
			this.bookingRepository.deleteInBatch(booking);
		}

	}

	public void acepted(final int id) {
		this.checkBooking();
		final Actor principal = this.utilityService.findByPrincipal();
		Assert.notNull(principal);
		Booking booking = this.findOne(id);
		Assert.isTrue(this.utilityService.checkAuthority(principal, "OWNER"));
		Assert.notNull(booking);
		Assert.isTrue(booking.getId() != 0);
		Assert.isTrue(booking.getRoom().getOwner().equals(principal));
		Assert.isTrue(booking.getRoom().getStatus().equals("ACTIVE"));
		Assert.isTrue(booking.getStatus().equals("PENDING"));
		booking.setStatus("ACCEPTED");
		booking = this.bookingRepository.save(booking);
		final Date moment = new Date();
		this.messageService.changeStatusNotfication(booking, principal, moment);
	}

	public void deleteFromCustomerId(final Integer customerId) {
		final Collection<Booking> toDelete = this.bookingRepository.deleteFromCustomerId(customerId);
		for (final Booking booking : toDelete)
			this.delete(booking.getId());
	}

	public Collection<Booking> futureBookingsOfRoom(final Integer roomId) {
		final Date now = new Date(System.currentTimeMillis() - 1);

		return this.bookingRepository.futureBookingsOfRoom(roomId, now);
	}

	public void changeStatus(final Booking booking, final String newStatus) {
		final Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "OWNER") || this.utilityService.checkAuthority(principal, "ADMIN"));
		Assert.isTrue((newStatus == "PENDING" || newStatus == "ACCEPTED" || newStatus == "REJECTED"), "invalid.status");

		booking.setStatus(newStatus);
		this.bookingRepository.save(booking);
	}

	public Collection<Booking> pendingByRoomId(final Integer roomId) {
		return this.bookingRepository.pendingByRoomId(roomId);
	}

	public void checkBooking() {
		final Date now = new Date();
		final Collection<Booking> bookings = this.bookingRepository.getOld(now);
		if (!bookings.isEmpty())
			for (final Booking b : bookings) {
				b.setStatus("REJECTED");
				this.bookingRepository.save(b);
			}
	}

}
