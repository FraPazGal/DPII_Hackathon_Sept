
package controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.MessageBoxService;
import services.OwnerService;
import services.UtilityService;
import domain.Actor;
import domain.Owner;
import forms.ActorForm;
import forms.ActorRegistrationForm;

@Controller
@RequestMapping("/owner")
public class OwnerController extends AbstractController {

	/* Services */
	@Autowired
	private OwnerService		ownerService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private UtilityService		utilityService;

	@Autowired
	private MessageBoxService	messageBoxService;


	/* Methods */

	/* Display */
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam (required = false) final Integer ownerId) {
		ModelAndView result = new ModelAndView("owner/display");
		Actor actor = null;	
 		boolean isPrincipal = false;
		try {
			if(ownerId == null) {
				actor = this.utilityService.findByPrincipal();
				Assert.isTrue(this.utilityService.checkAuthority(actor, "OWNER"), "not.allowed");
				isPrincipal = true;
			} else {
				actor = this.ownerService.findOne(ownerId);
			}
			result.addObject("isPrincipal", isPrincipal);
			result.addObject("owner", actor);
			
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Registration */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView registerNewOwner() {
		final ActorRegistrationForm actorRegistrationForm = new ActorRegistrationForm();
		ModelAndView result = null;
		
		try {
			result = this.createRegisterModelAndView(actorRegistrationForm);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Save Registration */
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView register(@Valid final ActorRegistrationForm actorRegistrationForm, final BindingResult binding) {
		ModelAndView result = new ModelAndView("redirect:/");
		try {
			final Owner owner = this.ownerService.reconstruct(actorRegistrationForm, binding);

			if (binding.hasErrors())
				result = this.createRegisterModelAndView(actorRegistrationForm);
			else {
				final Owner saved = this.ownerService.save(owner);
				this.messageBoxService.initializeDefaultBoxes(saved);
			}
		} catch (final Throwable oops) {
			result = this.createRegisterModelAndView(actorRegistrationForm, oops.getMessage());
		}
		return result;
	}

	/* Edit */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editOwner() {
		ModelAndView result = new ModelAndView("customer/display");
		;
		try {
			final Actor principal = this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService.checkAuthority(principal, "OWNER"), "not.allowed");

			final ActorForm actorForm = new ActorForm(principal);

			result = this.createEditModelAndView(actorForm);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Save Edit */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(final ActorForm actorForm, final BindingResult binding) {
		ModelAndView result = new ModelAndView("redirect:/owner/display.do");
		try {
			Assert.isTrue(this.utilityService.findByPrincipal().getId() == actorForm.getId() && this.actorService.findOne(this.utilityService.findByPrincipal().getId()) != null, "not.allowed");

			final Owner owner = this.ownerService.reconstruct(actorForm, binding);

			if (binding.hasErrors())
				result = this.createEditModelAndView(actorForm);
			else
				this.ownerService.save(owner);
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(actorForm, oops.getMessage());
		}
		return result;
	}

	/* Delete */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView deleteRookie(final ActorForm actorForm, final BindingResult binding, final HttpSession session) {
		ModelAndView result = new ModelAndView("redirect:/welcome/index.do");

		final Owner owner = this.ownerService.findOne(actorForm.getId());

		if (binding.hasErrors())
			result = this.createEditModelAndView(actorForm);
		else
			try {
				this.ownerService.delete(owner);
				session.invalidate();
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(actorForm, oops.getMessage());
			}
		return result;
	}

	/* Auxiliary methods */

	/* Registration related */
	protected ModelAndView createRegisterModelAndView(final ActorRegistrationForm actorRegistrationForm) {

		return this.createRegisterModelAndView(actorRegistrationForm, null);
	}

	protected ModelAndView createRegisterModelAndView(final ActorRegistrationForm actorRegistrationForm, final String messageCode) {
		final ModelAndView result = new ModelAndView("owner/register");

		actorRegistrationForm.setTermsAndConditions(false);
		result.addObject("actorRegistrationForm", actorRegistrationForm);
		result.addObject("errMsg", messageCode);

		return result;
	}

	/* Edition related */
	protected ModelAndView createEditModelAndView(final ActorForm actorForm) {

		return this.createEditModelAndView(actorForm, null);
	}

	protected ModelAndView createEditModelAndView(final ActorForm actorForm, final String messageCode) {
		final ModelAndView result = new ModelAndView("owner/edit");

		result.addObject("actorForm", actorForm);
		result.addObject("errMsg", messageCode);

		return result;
	}

}
