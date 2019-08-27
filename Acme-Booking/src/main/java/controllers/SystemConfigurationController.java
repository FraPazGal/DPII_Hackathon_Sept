
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.SystemConfigurationService;
import services.UtilityService;
import domain.SystemConfiguration;

@Controller
@RequestMapping("/config/admin")
public class SystemConfigurationController extends AbstractController {

	@Autowired
	private SystemConfigurationService	sysconfigService;

	@Autowired
	private UtilityService				utilityService;

	@Autowired
	private ActorService				actorService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		final ModelAndView result = new ModelAndView("config/display");

		try {
			Assert.isTrue(this.utilityService.checkAuthority(this.utilityService.findByPrincipal(), "ADMIN"), "not.allowed");

			final SystemConfiguration config = this.sysconfigService.findMySystemConfiguration();

			result.addObject("config", config);
			result.addObject("welcome", config.getWelcomeMessage());
		} catch (final Throwable oops) {
			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		final ModelAndView result = new ModelAndView("config/edit");

		try {
			Assert.isTrue(this.utilityService.checkAuthority(this.utilityService.findByPrincipal(), "ADMIN"), "not.allowed");

			final SystemConfiguration config = this.sysconfigService.findMySystemConfiguration();

			result.addObject("systemConfiguration", config);
			result.addObject("welcome", config.getWelcomeMessage());
		} catch (final Throwable oops) {
			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final SystemConfiguration config, @RequestParam(value = "welcomeES") final String welcomeES, @RequestParam(value = "welcomeEN") final String welcomeEN, final BindingResult binding) {

		ModelAndView result = new ModelAndView("config/edit");

		try {
			final SystemConfiguration reconstructed = this.sysconfigService.reconstruct(config, welcomeES, welcomeEN, binding);

			if (binding.hasErrors()) {

				result.addObject("systemConfiguration", config);
				result.addObject("welcome", reconstructed.getWelcomeMessage());
			} else {
				this.sysconfigService.save(reconstructed);

				result = new ModelAndView("redirect:display.do");
			}
		} catch (final Throwable oops) {
			result.addObject("errMsg", oops.getMessage());
		}
		return result;
	}

	/* List of spammers */
	@RequestMapping(value = "/listSA", method = RequestMethod.GET)
	public ModelAndView listSuspiciousActors() {
		ModelAndView result = new ModelAndView("config/listSA");

		try {
			result.addObject("actors", this.actorService.findAllExceptPrincipal());

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}

	/* Ban actor */
	@RequestMapping(value = "/ban", method = RequestMethod.GET, params = "actorId")
	public ModelAndView banActor(@RequestParam final int actorId) {
		ModelAndView result = new ModelAndView("redirect:listSA.do");

		try {
			this.actorService.ban(actorId);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}

	/* Unban actor */
	@RequestMapping(value = "/unban", method = RequestMethod.GET, params = "actorId")
	public ModelAndView unbanActor(@RequestParam final int actorId) {
		ModelAndView result = new ModelAndView("redirect:listSA.do");

		try {
			this.actorService.unban(actorId);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}
}
