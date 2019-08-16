package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.RoomService;
import services.ServiceService;
import services.UtilityService;
import domain.Actor;
import domain.Room;
import domain.Service;

@Controller
@RequestMapping("/service")
public class ServiceController extends AbstractController {

	/* Services */

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private ServiceService serviceService;
	
	@Autowired
	private RoomService roomService;
	
	/* Display */
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int serviceId) {
		ModelAndView result = new ModelAndView("service/display");
		Service service = null;
		boolean isPrincipal = false;

		try {
			Actor principal = this.utilityService.findByPrincipal();
			if (this.serviceService.findOne(serviceId).getRoom().getOwner().equals(principal)) {
				service = this.serviceService.findOne(serviceId);
				isPrincipal = true;
			}
			service = this.serviceService.findOne(serviceId);
				
			result.addObject("service", service);
			result.addObject("isPrincipal", isPrincipal);
			result.addObject("requestURI", "service/display.do?serviceId=" + serviceId);
			
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
		}
		return result;
	}

	/* Create */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int roomId) {
		ModelAndView result = new ModelAndView("redirect:/");
		try {
			Service service = this.serviceService.create();
			Room room = this.roomService.findOne(roomId);
			this.roomService.assertOwnershipAndDraft(room);
			
			service.setRoom(room);
			result = this.createEditModelAndView(service);

		} catch (final Throwable oops) {
			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}

	/* Edit */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int serviceId) {
		ModelAndView result = new ModelAndView("redirect:service/display.do?serviceId=" + serviceId);
		try {
			Service service = this.serviceService.findOne(serviceId);
			
			this.serviceService.allowedService(service);
			result = this.createEditModelAndView(service);

		} catch (final Throwable oops) {
			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}

	/* Save */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Service service, BindingResult binding) {
		ModelAndView result = new ModelAndView("service/edit");

		try {
			Service toSave = this.serviceService.reconstruct(service, binding);
			if (binding.hasErrors()) {

				result.addObject("service", service);
			} else
				try {
					this.serviceService.save(toSave);
					result = new ModelAndView("redirect:room/display.do?roomId=" + toSave.getRoom().getId());

				} catch (final Throwable oops) {
					result.addObject("service", toSave);
					result.addObject("errMsg", oops.getMessage());
				}
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(service, oops.getMessage());
		}
		return result;
	}
	
	/* Delete */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int serviceId) {
		ModelAndView result = null;
		try {
			Service service = this.serviceService.findOne(serviceId);
			result = new ModelAndView("redirect:room/display.do?roomId=" + service.getRoom().getId());
			
			this.serviceService.delete(service);

		} catch (final Throwable oops) {
			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}

	/* Ancillary methods */
	protected ModelAndView createEditModelAndView(final Service service) {
		return this.createEditModelAndView(service, null);
	}

	protected ModelAndView createEditModelAndView(final Service service, final String messageCode) {
		ModelAndView result = new ModelAndView("service/edit");
		result.addObject("service", service);
		result.addObject("errMsg", messageCode);

		return result;
	}
}
