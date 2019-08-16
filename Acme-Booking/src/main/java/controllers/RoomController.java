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

import services.CategoryService;
import services.RoomService;
import services.ServiceService;
import services.UtilityService;
import domain.Actor;
import domain.Room;

@Controller
@RequestMapping("/room")
public class RoomController extends AbstractController {

	/* Services */

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private RoomService roomService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ServiceService serviceService;

	/* Display */
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int roomId) {
		ModelAndView result = new ModelAndView("room/display");
		Room room = null;
		boolean isPrincipal = false;

		try {
			try {
				Actor principal = this.utilityService.findByPrincipal();
				if (this.roomService.findOne(roomId).getOwner().equals(principal)) {
					room = this.roomService.findOne(roomId);
					isPrincipal = true;
				} else {
					room = this.roomService.findOneToDisplay(roomId);
				}
			} catch (final Throwable oops) {
				room = this.roomService.findOneToDisplay(roomId);
			}
			result.addObject("room", room);
			result.addObject("isPrincipal", isPrincipal);
			result.addObject("requestURI", "room/display.do?roomId=" + roomId);
			result.addObject("services", this.serviceService.findServicesByRoomId(room.getId()));
			
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
		}
		return result;
	}

	/* Listing */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(
			@RequestParam(required = false) final String range,
			@RequestParam(required = false) final Integer ownerId) {
		ModelAndView result = new ModelAndView("room/list");
		Collection<Room> rooms = null;
		Actor principal = null;
		boolean isPrincipal = false;
		boolean listConf = false;

		try {
			try {
				principal = this.utilityService.findByPrincipal();
				if(this.utilityService.checkAuthority(principal, "OWNER"))
					listConf = true;
			} catch (final Throwable oops) {}
			if(range == null && ownerId == null) {
				rooms = this.roomService.findRoomsForBooking();
			} else if (range != null){
				Assert.isTrue(this.utilityService.checkAuthority(principal, "OWNER"), "not.allowed");
				isPrincipal = true;
				
				switch (range) {
				case "mineD":
					rooms = this.roomService.findRoomsDraftAndMine(principal.getId());
					break;

				case "mineA":
					rooms = this.roomService.findRoomsActiveAndMine(principal.getId());
					break;
					
				case "mineO":
					rooms = this.roomService.findRoomsOutOfServiceAndMine(principal.getId());
					break;
				}
			} 
			result.addObject("rooms", rooms);
			result.addObject("listConf", listConf);
			result.addObject("isPrincipal", isPrincipal);
			result.addObject("range", range);
		} catch (final Throwable oops) {
			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}

	/* Create */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result = new ModelAndView("room/list");
		try {
			final Room room = this.roomService.create();

			result = this.createEditModelAndView(room);

		} catch (final Throwable oops) {
			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}

	/* Edit */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int roomId) {
		ModelAndView result = new ModelAndView("room/list");
		try {
			Room room = this.roomService.findOne(roomId);
			
			this.roomService.assertOwnershipAndEditable(room);
			result = this.createEditModelAndView(room);

		} catch (final Throwable oops) {
			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}

	/* Save */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Room room, BindingResult binding) {
		ModelAndView result = new ModelAndView("room/edit");

		try {
			Room toSave = this.roomService.reconstruct(room, binding);
			if (binding.hasErrors()) {

				result.addObject("room", room);
			} else
				try {
					this.roomService.save(toSave);
					result = new ModelAndView("redirect:list.do?range=mineD");

				} catch (final Throwable oops) {
					result.addObject("Room", toSave);
					result.addObject("errMsg", oops.getMessage());
				}
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(room, oops.getMessage());
		}
		return result;
	}
	
	/* Save as Final */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveFinal")
	public ModelAndView saveFinal(Room room, BindingResult binding) {
		ModelAndView result = new ModelAndView("room/edit");
		try {
			Room toSave = this.roomService.reconstruct(room, binding);
			if (binding.hasErrors()) {

				result.addObject("room", room);
			} else
				try {
					toSave.setVisibility("ACTIVE");
					this.roomService.save(toSave);
					result = new ModelAndView("redirect:list.do?range=mineA");

				} catch (final Throwable oops) {
					result.addObject("room", toSave);
					result.addObject("errMsg", oops.getMessage());
				}
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(room, oops.getMessage());
		}
		return result;
	}
	
	/* Decommission a room */
	@RequestMapping(value = "/action", method = RequestMethod.GET)
	public ModelAndView actionsEnrolments(@RequestParam final String action, @RequestParam final int roomId) {
		ModelAndView result = new ModelAndView("redirect:/room/list.do?range=mineO");
		try {
			Room room = this.roomService.findOne(roomId);
			this.roomService.assertOwnershipAndEditable(room);

			if (action.equals("decommission")) {
				
				this.roomService.decommision(room);
			} 
		} catch (final Throwable oops) {
			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}

	/* Delete */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int roomId) {
		ModelAndView result = new ModelAndView("redirect:/room/list.do?range=mineA");
		try {
			Room room = this.roomService.findOne(roomId);
			this.roomService.assertOwnershipAndEditable(room);
			
			this.roomService.deleteAsDraft(room);

		} catch (final Throwable oops) {
			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}

	/* Ancillary methods */
	protected ModelAndView createEditModelAndView(final Room room) {
		return this.createEditModelAndView(room, null);
	}

	protected ModelAndView createEditModelAndView(final Room room, final String messageCode) {
		ModelAndView result = new ModelAndView("room/edit");
		result.addObject("room", room);
		result.addObject("errMsg", messageCode);
		result.addObject("categories", this.categoryService.findAll());

		return result;
	}
}
