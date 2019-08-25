
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.MessageService;
import services.SystemConfigurationService;
import services.UtilityService;
import domain.Message;
import forms.MessageForm;

@Controller
@RequestMapping("/message/administrator")
public class MessageAdministratorController extends AbstractController {

	// Services -----------------------------------------------------------

	@Autowired
	private MessageService				messageService;

	@Autowired
	private ActorService				actorService;
	@Autowired
	private UtilityService				utilityService;
	@Autowired
	private SystemConfigurationService	systemConfigurationService;


	// Creation --------------------------------------------------------

	@RequestMapping(value = "/broadcast", method = RequestMethod.GET)
	public ModelAndView broadcast() {
		ModelAndView result;
		try {

			final MessageForm messageForm = this.messageService.createForm();
			messageForm.setReceiver(this.utilityService.findByPrincipal());
			result = new ModelAndView("message/broadcast");
			result.addObject("messageForm", messageForm);
			result.addObject("broadcast", true);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}
	// Broadcast ---------------------------------------------------------------

	@RequestMapping(value = "/broadcast", method = RequestMethod.POST, params = "saveBroadcast")
	public ModelAndView broadcast(final MessageForm mensaje, final BindingResult binding) {
		ModelAndView result;
		Message message;
		try {

			message = this.messageService.reconstructBroadcast(mensaje, binding);

			if (binding.hasErrors()) {
				result = new ModelAndView("message/broadcast");
				result.addObject("messageForm", mensaje);
				result.addObject("broadcast", true);
			} else {
				this.messageService.broadcast(message);
				result = new ModelAndView("redirect:/messagebox/list.do");
			}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}
}
