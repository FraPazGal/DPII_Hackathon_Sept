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
			
		} catch (Throwable oops) {
			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}
}

