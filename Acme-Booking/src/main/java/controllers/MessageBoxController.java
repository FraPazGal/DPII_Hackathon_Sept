
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

import services.MessageBoxService;
import services.UtilityService;
import domain.Actor;
import domain.MessageBox;

@Controller
@RequestMapping("/messagebox")
public class MessageBoxController extends AbstractController {

	@Autowired
	private UtilityService		utilityService;

	@Autowired
	private MessageBoxService	messageBoxService;


	public MessageBoxController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listAll() {
		ModelAndView result;
		try {
			result = new ModelAndView("messagebox/list");
			final Collection<MessageBox> boxes = this.messageBoxService.findByOwnerFirst();
			result.addObject("requestURI", "/messagebox/list.do");
			result.addObject("messageboxes", boxes);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:../welcome/index.do");
			result.addObject("messageCode", "box.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "content", method = RequestMethod.GET)
	public ModelAndView listBox(@RequestParam final int Id) {
		ModelAndView result;
		try {
			result = new ModelAndView("messagebox/content");
			final Actor actor = this.utilityService.findByPrincipal();
			final MessageBox box = this.messageBoxService.findOne(Id);
			Assert.isTrue(box.getOwner().equals(actor));
			result.addObject("requestURI", "/messagebox/content.do");
			final Collection<MessageBox> childBoxes = this.messageBoxService.findByParent(box.getId());
			result.addObject("box", box);
			result.addObject("messageBoxes", childBoxes);
			result.addObject("messages", box.getMessages());
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:../welcome/index.do");
			result.addObject("messageCode", "messagebox.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteBox(@RequestParam final int Id) {
		ModelAndView result;
		try {
			final MessageBox box = this.messageBoxService.findOne(Id);
			this.messageBoxService.delete(box);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageCode", "messagebox.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView saveBox(final MessageBox box, final BindingResult binding) {
		ModelAndView result;
		MessageBox boxres;
		try {
			final Actor actorLogged = this.utilityService.findByPrincipal();
			boxres = this.messageBoxService.reconstruct(box, binding);
			final Collection<MessageBox> boxes = this.messageBoxService.findByOwner(actorLogged.getId());
			boxes.remove(box);
			final Collection<MessageBox> childBoxes = this.messageBoxService.findByParent(box.getId());
			boxes.removeAll(childBoxes);
			if (binding.hasErrors()) {
				result = new ModelAndView("messagebox/edit");
				result.addObject("messageBox", box);
				result.addObject("messageBoxes", boxes);
			} else {

				this.messageBoxService.save(boxres);
				result = new ModelAndView("redirect:list.do");
			}
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editBox(@RequestParam final int Id) {
		ModelAndView result;
		MessageBox box;
		try {
			result = new ModelAndView("messagebox/edit");
			final Collection<MessageBox> boxes = this.messageBoxService.findByOwnerFirst();
			box = this.messageBoxService.findOneOwner(Id);
			final Collection<MessageBox> childBoxes = this.messageBoxService.findByParent(box.getId());
			boxes.remove(box);
			boxes.removeAll(childBoxes);
			result.addObject("messageBox", box);
			result.addObject("messageBoxes", boxes);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageCode", "messagebox.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		try {
			final Actor actor = this.utilityService.findByPrincipal();
			result = new ModelAndView("messagebox/edit");
			final MessageBox box = this.messageBoxService.create(actor);
			result.addObject("messageBox", box);
			final Collection<MessageBox> boxes = this.messageBoxService.findByOwnerFirst();
			result.addObject("messageBoxes", boxes);
		} catch (final Throwable opps) {
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageCode", "messagebox.commit.error");
		}
		return result;

	}

}
