/*
 * WelcomeController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.SystemConfigurationService;
import services.UtilityService;
import domain.Actor;

@Controller
@RequestMapping("/welcome")
public class WelcomeController extends AbstractController {
	
	// Services ---------------------------------------------------------------
	
	@Autowired
	private UtilityService utilityService;
	
	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	// Constructors -----------------------------------------------------------

	public WelcomeController() {
		super();
	}

	// Index ------------------------------------------------------------------		

	@RequestMapping(value = "/index")
	public ModelAndView index(@RequestParam(required = false, defaultValue = "John Doe") String name) {
		ModelAndView result = new ModelAndView("welcome/index");
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String moment = formatter.format(new Date());

		try {
			Actor principal = this.utilityService.findByPrincipal();
			Assert.isTrue(principal != null);

			result.addObject("name", principal.getName() + " " + principal.getSurname());
		} catch (Throwable oops) {
			result.addObject("name", name);
		}
		
		result.addObject("moment", moment);
		result.addObject("welcomeMsg", this.systemConfigurationService.findMySystemConfiguration().getWelcomeMessage());
		result.addObject("breachNotif", this.systemConfigurationService.findMySystemConfiguration().getBreachNotification());

		return result;
	}
}
