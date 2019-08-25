package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.DashboardService;
import services.UtilityService;
import domain.Actor;
import domain.Category;

@Controller
@RequestMapping("/statistics")
public class DashboardController extends AbstractController{
	
	/* Services */
	
	@Autowired
	private DashboardService dashboardService;	
	
	@Autowired
	private UtilityService utilityService;
	
	/* Display */
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		Actor principal;
		ModelAndView result = new ModelAndView("administrator/statistics");;
		
		try {
			principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"), "not.allowed");
			
			Double [] statsBookingsPerRoom = this.dashboardService.statsBookingsPerRoom();
			Double [] statsServicesPerRoom = this.dashboardService.statsServicesPerRoom();
			Double [] statsPricePerRoom = this.dashboardService.statsPricePerRoom();
			Double ratioRevisionPendingByFinalRooms = this.dashboardService.ratioRevisionPendingByFinalRooms();
			Double ratioAcceptedByFinalRooms = this.dashboardService.ratioAcceptedByFinalRooms();
			Double ratioRejectedByFinalRooms = this.dashboardService.ratioRejectedByFinalRooms();
			Double ratioRoomsOutOfService = this.dashboardService.ratioRoomsOutOfService();
			Category topCategoryByRooms = this.dashboardService.topCategoryByRooms();
			Double [] statsFinder = this.dashboardService.statsFinder();
			Double ratioFindersEmpty = this.dashboardService.ratioFindersEmpty();
			
			result.addObject("statsBookingsPerRoom",statsBookingsPerRoom);
			result.addObject("statsServicesPerRoom",statsServicesPerRoom);
			result.addObject("statsPricePerRoom",statsPricePerRoom);
			result.addObject("ratioRevisionPendingByFinalRooms",ratioRevisionPendingByFinalRooms);
			result.addObject("ratioAcceptedByFinalRooms",ratioAcceptedByFinalRooms);
			result.addObject("ratioRejectedByFinalRooms",ratioRejectedByFinalRooms);
			result.addObject("ratioRoomsOutOfService",ratioRoomsOutOfService);
			result.addObject("topCategoryByRooms",topCategoryByRooms);
			result.addObject("statsFinder",statsFinder);
			result.addObject("ratioFindersEmpty",ratioFindersEmpty);
			
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}
}

