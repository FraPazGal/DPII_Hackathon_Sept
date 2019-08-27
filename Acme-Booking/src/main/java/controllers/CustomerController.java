
package controllers;

import java.util.Arrays;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CustomerService;
import services.FinderService;
import services.MessageBoxService;
import services.SystemConfigurationService;
import services.UtilityService;
import domain.Customer;
import forms.CustomerForm;
import forms.CustomerRegistrationForm;

@Controller
@RequestMapping("/customer")
public class CustomerController extends AbstractController {

	/* Services */

	@Autowired
	private CustomerService				customerService;

	@Autowired
	private ActorService				actorService;

	@Autowired
	private UtilityService				utilityService;

	@Autowired
	private FinderService				finderService;

	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	@Autowired
	private MessageBoxService			messageBoxService;
	@Autowired
	private FinderService				finderBoxService;


	/* Display */
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result = new ModelAndView("customer/display");

		try {
			final Customer customer = (Customer) this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService.checkAuthority(customer, "CUSTOMER"), "not.allowed");

			result.addObject("customer", customer);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}

		return result;
	}

	/* Registration */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView registerNewAuthor() {
		final CustomerRegistrationForm customerRegistrationForm = new CustomerRegistrationForm();
		ModelAndView result = null;
		
		try {
			result = this.createRegisterModelAndView(customerRegistrationForm);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Save Registration */
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView register(@Valid final CustomerRegistrationForm customerRegistrationForm, final BindingResult binding) {
		ModelAndView result = new ModelAndView("redirect:../welcome/index.do");

		try {
			final Customer customer = this.customerService.reconstruct(customerRegistrationForm, binding);

			if (binding.hasErrors())
				result = this.createRegisterModelAndView(customerRegistrationForm);
			else {
				final Customer saved = this.customerService.save(customer);
				this.finderService.defaultFinder(saved);
				this.messageBoxService.initializeDefaultBoxes(saved);
			}
		} catch (final Throwable oops) {
			result = this.createRegisterModelAndView(customerRegistrationForm, oops.getMessage());
		}
		return result;
	}

	/* Edit */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editAuthor() {
		ModelAndView result = new ModelAndView("customer/display");
		;
		try {
			final Customer principal = (Customer) this.utilityService.findByPrincipal();
			Assert.isTrue(this.utilityService.checkAuthority(principal, "CUSTOMER"), "not.allowed");

			final CustomerForm customerForm = new CustomerForm(principal);

			result = this.createEditModelAndView(customerForm);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	/* Save Edit */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@Valid final CustomerForm customerForm, final BindingResult binding) {
		ModelAndView result = new ModelAndView("redirect:/customer/display.do");

		try {
			Assert.isTrue(this.utilityService.findByPrincipal().getId() == customerForm.getId() && this.actorService.findOne(this.utilityService.findByPrincipal().getId()) != null, "not.allowed");

			final Customer customer = this.customerService.reconstruct(customerForm, binding);

			if (binding.hasErrors())
				result = this.createEditModelAndView(customerForm);
			else {
				final Customer cust = this.customerService.save(customer);
				if (customer.getId() == 0) {

				}
			}
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(customerForm, oops.getMessage());
		}
		return result;
	}

	/* Delete */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView deleteRookie(final CustomerForm customerForm, final BindingResult binding, final HttpSession session) {
		ModelAndView result = new ModelAndView("redirect:/welcome/index.do");

		try {
			this.customerService.delete((Customer) this.utilityService.findByPrincipal());
			session.invalidate();
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(customerForm, oops.getMessage());
		}
		return result;
	}

	/* Auxiliary methods */
	protected ModelAndView createRegisterModelAndView(final CustomerRegistrationForm customerRegistrationForm) {

		return this.createRegisterModelAndView(customerRegistrationForm, null);
	}

	protected ModelAndView createRegisterModelAndView(final CustomerRegistrationForm customerRegistrationForm, final String messageCode) {
		final ModelAndView result = new ModelAndView("customer/register");

		customerRegistrationForm.setTermsAndConditions(false);
		result.addObject("customerRegistrationForm", customerRegistrationForm);
		result.addObject("errMsg", messageCode);
		final String[] aux = this.systemConfigurationService.findMySystemConfiguration().getMakers().split(",");
		result.addObject("makers", Arrays.asList(aux));

		return result;
	}

	protected ModelAndView createEditModelAndView(final CustomerForm customerForm) {

		return this.createEditModelAndView(customerForm, null);
	}

	protected ModelAndView createEditModelAndView(final CustomerForm customerForm, final String messageCode) {
		final ModelAndView result = new ModelAndView("customer/edit");

		result.addObject("customerForm", customerForm);
		result.addObject("errMsg", messageCode);
		final String[] aux = this.systemConfigurationService.findMySystemConfiguration().getMakers().split(",");
		result.addObject("makers", Arrays.asList(aux));

		return result;
	}

}
