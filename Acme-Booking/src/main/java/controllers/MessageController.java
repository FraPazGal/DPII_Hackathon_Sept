
package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.MessageBoxService;
import services.MessageService;
import services.SystemConfigurationService;
import services.UtilityService;
import domain.Actor;
import domain.Message;
import domain.MessageBox;
import forms.MessageForm;

@Controller
@RequestMapping("message/actor")
public class MessageController extends AbstractController {

	@Autowired
	private MessageService				messageService;

	@Autowired
	private UtilityService				utilityService;

	@Autowired
	private MessageBoxService			messageBoxService;

	@Autowired
	private SystemConfigurationService	systemConfigurationService;


	// Create
	// ----------------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final MessageForm messageForm;
		try {
			messageForm = this.messageService.createForm();
			result = this.createEditModelAndView(messageForm);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	// Edition ------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int messageId) {
		ModelAndView result;
		Message message;
		try {
			message = this.messageService.findOne(messageId);
			Assert.notNull(message);
			result = new ModelAndView("message/display");
			result.addObject("messageO", message);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int messageId) {
		ModelAndView result;
		Message message;
		try {

			message = this.messageService.findOne(messageId);
			Assert.notNull(message);
			final MessageForm mess = new MessageForm(message);
			result = this.createEditModelAndView(mess);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;

	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final MessageForm mensaje, final BindingResult binding) {
		ModelAndView result;
		Message message;
		try {
			message = this.messageService.reconstruct(mensaje, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(mensaje);
			else {
				this.messageService.save(message);
				result = new ModelAndView("redirect:/messagebox/list.do");
			}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "move")
	public ModelAndView move(final MessageForm mensaje, final BindingResult binding) {
		ModelAndView result;
		MessageBox destination;
		Message message;
		try {
			message = this.messageService.reconstruct(mensaje, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(mensaje);
			else {
				destination = message.getMessageBoxes().iterator().next();
				this.messageService.move(message, destination);
				result = new ModelAndView("redirect:/messagebox/list.do");
			}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final MessageForm mensaje, final BindingResult binding) {
		ModelAndView result;

		Message message;
		try {
			message = this.messageService.reconstruct(mensaje, binding);

			this.messageService.delete(message);
			result = new ModelAndView("redirect:/messagebox/list.do");

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final MessageForm mensaje) {
		ModelAndView result;

		result = this.createEditModelAndView(mensaje, null);

		return result;

	}

	protected ModelAndView createEditModelAndView(final MessageForm mensaje, final String messageError) {
		ModelAndView result;
		Collection<MessageBox> boxes, messageBoxes;
		Collection<Message> messages;
		Actor sender;
		boolean possible = false;
		Actor principal;
		Date sentMoment;
		Collection<Actor> receivers;

		principal = this.utilityService.findByPrincipal();
		Assert.notNull(principal);

		messages = new ArrayList<Message>();
		receivers = new ArrayList<Actor>();

		boxes = this.messageBoxService.findByOwner(principal.getId());
		if (mensaje.getId() != 0) {
			final Message mess = this.messageService.reconstructM(mensaje);

			for (final MessageBox mb : boxes) {
				messages.addAll(mb.getMessages());
				if (mb.getMessages().contains(mess) && mb.getOwner().equals(principal))
					possible = true;
			}
		}

		sentMoment = mensaje.getSentMoment();
		messageBoxes = mensaje.getMessageBoxes();
		sender = mensaje.getSender();

		receivers = this.utilityService.findAllExceptPrincipal();

		result = new ModelAndView("message/edit");
		result.addObject("sentMoment", sentMoment);
		result.addObject("messageBoxes", messageBoxes);
		result.addObject("sender", sender);
		result.addObject("messageForm", mensaje);
		result.addObject("boxes", boxes);
		result.addObject("requestURI", "message/actor/edit.do");
		result.addObject("possible", possible);
		result.addObject("broadcast", false);
		result.addObject("message", messageError);
		result.addObject("receivers", receivers);

		return result;
	}
}
