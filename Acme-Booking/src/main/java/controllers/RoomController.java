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
import forms.ActiveRoomForm;

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
		Room room = this.roomService.findOne(roomId);
		boolean isPrincipal = false;
		try {
			try {
				Actor principal = this.utilityService.findByPrincipal();
				if (room.getOwner().equals(principal)) {
					isPrincipal = true;
					
				} else if (room.getAdministrator() == null && room.getStatus().equals("REVISION-PENDING") ||
						room.getAdministrator().equals(principal)) {
					Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"), "not.allowed");
				} else {
					room = this.roomService.findOneToDisplay(roomId);
				}
			} catch (final Throwable oops) {
				room = this.roomService.findOneToDisplay(roomId);
			}
			result.addObject("room", room);
			result.addObject("isPrincipal", isPrincipal);
			result.addObject("requestURI", "room/display.do?roomId=" + roomId);
			result.addObject("photos", this.roomService.splitAttachments(room.getPhotos()));
			result.addObject("services", this.serviceService.findServicesByRoomId(room.getId()));
			
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Listing */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final String range) {
		ModelAndView result = new ModelAndView("room/list");
		Collection<Room> rooms = null;
		String isPrincipal = null;
		boolean listConf = false;
		try {
			try {
				Actor principal = this.utilityService.findByPrincipal();
				Assert.isTrue(this.utilityService.checkAuthority(principal, "OWNER") ||
						this.utilityService.checkAuthority(principal, "ADMIN"), "not.allowed");
				
				listConf = true;
			} catch (final Throwable oops){}
			
			if(range == null) {
				rooms = this.roomService.findRoomsForBooking();

			} else {
				Actor principal = this.utilityService.findByPrincipal();
				Assert.isTrue(this.utilityService.checkAuthority(principal, "OWNER") ||
						this.utilityService.checkAuthority(principal, "ADMIN"), "not.allowed");
				
				if(this.utilityService.checkAuthority(principal, "OWNER")) {
					isPrincipal = "OWNER";
					
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
						
					case "mineI":
						rooms = this.roomService.findRoomsRevisionPendingAndMine(principal.getId());
						break;
						
					case "mineR":
						rooms = this.roomService.findRoomsRejectedAndMine(principal.getId());
						break;
					}
				} else {
					isPrincipal = "ADMIN";
					switch (range) {
					case "toAssign":
						rooms = this.roomService.findRoomsToAssign();
						break;
						
					case "toReview":
						rooms = this.roomService.findRoomsRevisionPendingAndMine(principal.getId());
						break;
						
					case "accepted":
						rooms = this.roomService.findRoomsActiveAndMine(principal.getId());
						break;
						
					case "rejected":
						rooms = this.roomService.findRoomsRejectedAndMine(principal.getId());
						break;
					}
				}
			} 
			Assert.notNull(rooms, "not.allowed");
			
			result.addObject("rooms", rooms);
			result.addObject("listConf", listConf);
			result.addObject("isPrincipal", isPrincipal);
			result.addObject("range", range);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Create */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result = new ModelAndView("room/list");
		try {
			final Room room = this.roomService.create();

			result = this.createEditDraftModelAndView(room);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Edit */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editDraft(@RequestParam final int roomId) {
		ModelAndView result = new ModelAndView("room/list");
		try {
			Room room = this.roomService.findOne(roomId);
			
			this.roomService.assertOwnershipAndEditable(room);
			result = this.createEditDraftModelAndView(room);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Save */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveDraft (Room room, BindingResult binding) {
		ModelAndView result = new ModelAndView("redirect:list.do?range=mineD");
		try {
			Room toSave = this.roomService.reconstructDraft(room, binding);
			if (binding.hasErrors()) {

				result = this.createEditDraftModelAndView(room);
			} else {
				this.roomService.save(toSave);
			}
		} catch (final Throwable oops) {
			result = this.createEditDraftModelAndView(room, oops.getMessage());
		}
		return result;
	}
	
	/* Save as Final */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveFinal")
	public ModelAndView saveDraftFinal(Room room, BindingResult binding) {
		ModelAndView result = new ModelAndView("redirect:list.do?range=mineI");
		try {
			Room toSave = this.roomService.reconstructDraft(room, binding);
			if (binding.hasErrors()) {

				result = this.createEditDraftModelAndView(room);
			} else {
				
				toSave.setStatus("REVISION-PENDING");
				this.roomService.saveAsFinal(toSave);
			}
		} catch (final Throwable oops) {
			result = this.createEditDraftModelAndView(room, oops.getMessage());
		}
		return result;
	}
	
	/* Edit active room*/
	@RequestMapping(value = "/editA", method = RequestMethod.GET)
	public ModelAndView editActive(@RequestParam final int roomId) {
		ModelAndView result = new ModelAndView("room/list");
		try {
			Room room = this.roomService.findOne(roomId);
			ActiveRoomForm activeRoomForm = new ActiveRoomForm(room);
			
			this.roomService.assertOwnershipAndEditable(room);
			result = this.createEditActiveModelAndView(activeRoomForm);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Save active room*/
	@RequestMapping(value = "/editA", method = RequestMethod.POST, params = "save")
	public ModelAndView saveActive(ActiveRoomForm activeRoomForm, BindingResult binding) {
		ModelAndView result = new ModelAndView("redirect:list.do?range=mineA");

		try {
			Room toSave = this.roomService.reconstructActive(activeRoomForm, binding);
			
			if (binding.hasErrors()) {
				result = this.createEditActiveModelAndView(activeRoomForm);
			} else {
				this.roomService.save(toSave);
			}
		} catch (final Throwable oops) {
			result = this.createEditActiveModelAndView(activeRoomForm, oops.getMessage());
		}
		return result;
	}
	
	/* Change status of a room by owner (decommission) or admin (accept/reject)*/
	@RequestMapping(value = "/action", method = RequestMethod.GET)
	public ModelAndView actionsRoom(@RequestParam final String action, @RequestParam final int roomId) {
		ModelAndView result = new ModelAndView("redirect:/room/list.do?range=toReview");
		try {
			Room room = this.roomService.findOne(roomId);

			if (action.equals("decommission")) {
				this.roomService.decommision(room);
				result = new ModelAndView("redirect:/room/list.do?range=mineO");
			} else if (action.equals("assign")) {
				this.roomService.assignRoom(room);
			} else if (action.equals("accept")) {
				this.roomService.acceptRoom(room);
				result = new ModelAndView("redirect:/room/list.do?range=accepted");
			} else if (action.equals("reject")) {
				this.roomService.rejectRoom(room);
				result = new ModelAndView("redirect:/room/list.do?range=rejected");
			}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}
	
	/* Delete */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int roomId) {
		ModelAndView result = new ModelAndView("redirect:/room/list.do?range=mineO");
		try {
			this.roomService.deleteAsDraft(this.roomService.findOne(roomId));

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Ancillary methods */
	protected ModelAndView createEditDraftModelAndView(final Room room) {
		return this.createEditDraftModelAndView(room, null);
	}

	protected ModelAndView createEditDraftModelAndView(final Room room, final String messageCode) {
		ModelAndView result = new ModelAndView("room/edit");
		result.addObject("room", room);
		result.addObject("errMsg", messageCode);
		result.addObject("categories", this.categoryService.findAll());

		return result;
	}
	
	protected ModelAndView createEditActiveModelAndView(final ActiveRoomForm activeRoomForm) {
		return this.createEditActiveModelAndView(activeRoomForm, null);
	}

	protected ModelAndView createEditActiveModelAndView(final ActiveRoomForm activeRoomForm, final String messageCode) {
		ModelAndView result = new ModelAndView("room/editA");
		result.addObject("activeRoomForm", activeRoomForm);
		result.addObject("errMsg", messageCode);

		return result;
	}
}
