
package services;

import java.util.Calendar;
import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import utilities.AbstractTest;
import domain.Booking;
import domain.Room;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class BookingServiceTest extends AbstractTest {

	/*
	 * Total coverage of all tests
	 * 
	 * 
	 * Coverage of the total project (%):
	 * 
	 * 
	 * Coverage of the total project (lines of codes):
	 * 
	 * ################################################################
	 * 
	 * Total coverage by exclusively executing this test class
	 * 
	 * 
	 * Coverage of the total project (%):
	 * 
	 * 
	 * Coverage of the total project (lines of codes):
	 * 
	 * ################################################################
	 * 
	 * Test 1: Create a booking
	 * 
	 * Test 2: Display a booking
	 */
	@Autowired
	private BookingService	bookingService;

	@Autowired
	private RoomService		roomService;

	@Autowired
	private Validator		validator;

	@Autowired
	private UtilityService	utilityService;


	//	//Test 1: Create a booking
	@Test
	public void createDriver() {
		final Object testingData[][] = {
			{
				"customer1", "title", "bookingReason", 20, 2, null
			},//positive//
			{
				"owner1", "title", "bookingReason", 20, 2, IllegalArgumentException.class
			},//negative: invalid authenticated
			{
				"customer1", null, "bookingReason", 20, 2, IllegalArgumentException.class
			}
		//negative: invalid data
		};

		for (int i = 0; i < testingData.length; i++)
			this.finderTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	private void finderTemplate(final String user, final String title, final String bookingReason, final Integer attendance, final Integer time, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(user);
			this.create(title, bookingReason, attendance, time);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}
		super.checkExceptions(expected, caught);

	}

	public void create(final String title, final String bookingReason, final Integer attendance, final Integer time) {
		final Collection<Room> rooms = this.roomService.findRoomsForBooking();
		final Booking b = this.bookingService.create(rooms.iterator().next());
		b.setTitle(title);
		b.setBookingReason(bookingReason);
		b.setExpectedAttendance(attendance);
		b.setDuration(time);
		final Calendar date = Calendar.getInstance();
		//		date.add(Calendar.MONTH, 1);
		//		date.set(Calendar.HOUR, 18);
		date.set(2019, 10, 8, 18, 30);
		b.setReservationDate(date.getTime());
		final BindingResult binding = new BeanPropertyBindingResult(b, b.getClass().getName());
		final Booking booking = this.bookingService.reconstruct(b, binding);
		Assert.isTrue(binding.hasErrors() == false);
		this.bookingService.save(booking);
	}
	//Test 2: Display a booking
	@Test
	public void displayDriver() {
		final Object testingData[][] = {
			{
				"customer1", "customer1", null
			},//positive
			{
				"customer1", "admin", IllegalArgumentException.class
			},//negative: invalid authenticated
			{
				"customer1", "customer2", IllegalArgumentException.class
			}
		//negative: other authentication
		};

		for (int i = 0; i < testingData.length; i++)
			this.finderDeleteTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	private void finderDeleteTemplate(final String userList, final String user, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userList);
			final Collection<Booking> bookings = this.bookingService.getListAll();
			this.unauthenticate();
			this.authenticate(user);
			this.display(bookings);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}
		super.checkExceptions(expected, caught);

	}

	public void display(final Collection<Booking> bookings) {
		for (final Booking b : bookings)
			this.bookingService.findOneMode(b.getId());
	}
}
