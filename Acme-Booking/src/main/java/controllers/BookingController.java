
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BookingService;
import services.RoomService;
import services.ServiceService;
import services.UtilityService;
import domain.Booking;

@Controller
@RequestMapping("/booking")
public class BookingController extends AbstractController {

	// Services

	@Autowired
	private BookingService	bookingService;

	@Autowired
	private ServiceService	serviceService;

	@Autowired
	private RoomService		roomService;

	@Autowired
	private UtilityService	utilityService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Booking> bookings;
		try {
			bookings = this.bookingService.getListAll();
			result = new ModelAndView("booking/list");
			result.addObject("bookings", bookings);
			result.addObject("bookingURI", "booking/list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/listRoom", method = RequestMethod.GET)
	public ModelAndView listByRoom(@RequestParam final int Id) {
		ModelAndView result;
		Collection<Booking> bookings;
		try {
			bookings = this.bookingService.getList(Id);
			result = new ModelAndView("booking/list");
			result.addObject("bookings", bookings);
			result.addObject("bookingURI", "booking/listRoom.do?Id=" + Id);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	// Create
	// ----------------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int roomId) {
		ModelAndView result;
		Booking booking;
		try {
			booking = this.bookingService.create(this.roomService.findOne(roomId));
			result = new ModelAndView("booking/edit");
			result.addObject("booking", booking);
			final Collection<domain.Service> services = this.serviceService.findServicesByRoomId(roomId);
			result.addObject("services", services);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	// Display ------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int Id) {
		ModelAndView result;
		Booking booking;
		try {
			booking = this.bookingService.findOneMode(Id);
			result = new ModelAndView("booking/display");
			result.addObject("booking", booking);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;

	}
	// Save ------------------------------------------------------------
	//
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editt(@RequestParam final int Id) {
		ModelAndView result;
		Booking booking;
		try {
			booking = this.bookingService.findOneMode(Id);
			Assert.isTrue(this.utilityService.checkAuthority(this.utilityService.findByPrincipal(), "OWNER"));
			Assert.isTrue(booking.getRejectionReason() == null);
			result = new ModelAndView("booking/edit");
			result.addObject("booking", booking);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageCode", "position.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveFinal(final Booking bookingF, final BindingResult binding) {
		ModelAndView result;
		Booking booking;
		try {
			booking = this.bookingService.reconstruct(bookingF, binding);
			if (binding.hasErrors()) {
				result = new ModelAndView("booking/edit");
				result.addObject("booking", bookingF);
				final Collection<domain.Service> services = this.serviceService.findServicesByRoomId(bookingF.getRoom().getId());
				result.addObject("services", services);
			} else {
				this.bookingService.save(booking);
				result = new ModelAndView("redirect:/booking/list.do");
			}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;

	}

	// Manage ------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "rejected")
	public ModelAndView rejected(final Booking bookingF, final BindingResult binding) {
		ModelAndView result;
		Booking booking;
		try {
			bookingF.setStatus("REJECTED");
			booking = this.bookingService.reconstruct(bookingF, binding);
			if (binding.hasErrors()) {
				result = new ModelAndView("booking/edit");
				result.addObject("booking", bookingF);
				final Collection<domain.Service> services = this.serviceService.findServicesByRoomId(bookingF.getRoom().getId());
				result.addObject("services", services);
			} else {
				this.bookingService.rejected(booking.getId());
				result = new ModelAndView("redirect:/booking/list.do");
			}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "accepted")
	public ModelAndView accepted(final Booking bookingF, final BindingResult binding) {
		ModelAndView result;
		Booking booking;
		try {
			bookingF.setStatus("ACCEPTED");
			booking = this.bookingService.reconstruct(bookingF, binding);
			if (binding.hasErrors()) {
				result = new ModelAndView("booking/edit");
				result.addObject("booking", bookingF);
				final Collection<domain.Service> services = this.serviceService.findServicesByRoomId(bookingF.getRoom().getId());
				result.addObject("services", services);
			} else {
				this.bookingService.acepted(booking.getId());
				result = new ModelAndView("redirect:/booking/list.do");
			}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

}
