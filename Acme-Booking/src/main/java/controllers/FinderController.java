
package controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import services.FinderService;
import domain.Finder;
import domain.Room;

@Controller
@RequestMapping("/finder")
public class FinderController extends AbstractController {

	// Services
	@Autowired
	private CategoryService	categoryService;

	@Autowired
	private FinderService	finderService;


	// Constructors

	public FinderController() {
		super();
	}
	// /list

	@RequestMapping(value = "/customer/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;

		try {
			final Finder finder = this.finderService.finderByCustomer();
			result = new ModelAndView("finder/search");
			result.addObject("finder", finder);
			result.addObject("categories", this.categoryService.findAll());
			result.addObject("rooms", finder.getResults());
			result.addObject("requestUri", "finder/customer/list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}
	// DELETE
	@RequestMapping(value = "/customer/search", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Finder finder, final BindingResult binding) {
		ModelAndView result;
		try {
			final Finder find = this.finderService.reconstruct(finder, binding);
			this.finderService.delete(find);
			result = new ModelAndView("redirect:search.do");
			result.addObject("requestUri", "finder/customer/list.do");

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}

		return result;
	}
	// search
	@RequestMapping(value = "/customer/search", method = RequestMethod.GET)
	public ModelAndView search() {
		ModelAndView result;
		try {
			final Finder finder = this.finderService.finderByCustomer();
			result = new ModelAndView("finder/search");
			result.addObject("finder", finder);
			result.addObject("categories", this.categoryService.findAll());
			result.addObject("rooms", finder.getResults());
			result.addObject("requestUri", "finder/customer/search.do");
		} catch (final Throwable oopsie) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	// searchAnon
	@RequestMapping(value = "/anon/search", method = RequestMethod.GET)
	public ModelAndView searchAnon(@RequestParam(required = false) final String keyWord) {
		ModelAndView result;
		Collection<Room> rooms = new ArrayList<>();
		try {
			result = new ModelAndView("finder/anon/search");
			rooms = this.finderService.searchAnon(keyWord);
			result.addObject("rooms", rooms);
			result.addObject("categories", this.categoryService.findAll());
			result.addObject("requestUri", "finder/anon/search.do");
			result.addObject("anon", true);
		} catch (final Throwable oopsie) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/customer/search", method = RequestMethod.POST, params = "save")
	public ModelAndView search(final Finder finder, final BindingResult binding) {
		ModelAndView result;
		try {
			final Finder find = this.finderService.reconstruct(finder, binding);
			if (binding.hasErrors()) {
				result = new ModelAndView("finder/search");
				result.addObject("finder", finder);
				result.addObject("requestUri", "finder/customer/list.do");

			} else {
				final Finder f = this.finderService.search(find);
				this.finderService.save(f);
				result = new ModelAndView("redirect:/finder/customer/list.do");
			}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			result.addObject("messageCode", "commit.error");
		}
		return result;
	}

}
