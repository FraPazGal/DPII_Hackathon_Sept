
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.CustomerRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.CreditCard;
import domain.Customer;
import forms.CustomerForm;
import forms.CustomerRegistrationForm;

@Transactional
@Service
public class CustomerService {

	// Managed repository ------------------------------------

	@Autowired
	private CustomerRepository			customerRepository;

	// Supporting services -----------------------------------

	@Autowired
	private SystemConfigurationService	systemConfigurationService;
	
	@Autowired
	private BookingService				bookingService;

	@Autowired
	private UtilityService				utilityService;

	@Autowired
	private CreditCardService			creditCardService;

	@Autowired
	private FinderService				finderService;


	// CRUD Methods ------------------------------------------

	public Customer create() {
		Customer res;

		UserAccount userAccount;
		Authority auth;
		Collection<Authority> authority;

		auth = new Authority();
		authority = new ArrayList<Authority>();
		userAccount = new UserAccount();
		res = new Customer();

		auth.setAuthority(Authority.CUSTOMER);
		authority.add(auth);
		userAccount.setAuthorities(authority);
		res.setUserAccount(userAccount);

		return res;
	}

	public Customer findOne(final Integer customerId) {

		final Customer result = this.customerRepository.findOne(customerId);
		Assert.notNull(result, "wrong.id");
		return result;
	}

	public List<Customer> findAll() {
		return this.customerRepository.findAll();
	}

	public Customer save(final Customer customer) {
		Customer res;
		Assert.notNull(customer, "not.allowed");

		if (customer.getId() != 0) {
			Actor principal = this.utilityService.findByPrincipal();

			customer.setUserAccount(principal.getUserAccount());
		}
		res = this.customerRepository.save(customer);

		return res;
	}

	public void delete(final Customer customer) {
		final Actor principal = this.utilityService.findByPrincipal();

		Assert.notNull(customer, "not.allowed");
		Assert.isTrue(principal.getId() == customer.getId(), "not.allowed");
		Assert.isTrue(this.utilityService.checkAuthority(principal, "CUSTOMER"), "not.allowed");

		this.finderService.deleteAccountCustomer();
		this.bookingService.deleteFromCustomerId(customer.getId());
		this.customerRepository.delete(customer.getId());
	}

	// Other business methods -------------------------------

	public Customer reconstruct(final CustomerRegistrationForm form, final BindingResult binding) {

		final Customer res = this.create();

		res.setName(form.getName());
		res.setSurname(form.getSurname());
		res.setPhoto(form.getPhoto());
		res.setEmail(form.getEmail());
		res.setPhoneNumber(form.getPhoneNumber());
		res.setAddress(form.getAddress());

		if (form.getMiddleName() != null)
			res.setMiddleName(form.getMiddleName());

		/* Creating user account */
		final UserAccount userAccount = new UserAccount();

		final List<Authority> authorities = new ArrayList<Authority>();
		final Authority authority = new Authority();
		authority.setAuthority(Authority.CUSTOMER);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);

		userAccount.setUsername(form.getUsername());

		Md5PasswordEncoder encoder;
		encoder = new Md5PasswordEncoder();
		userAccount.setPassword(encoder.encodePassword(form.getPassword(), null));

		res.setUserAccount(userAccount);

		if (!form.getHolder().isEmpty() || !form.getNumber().isEmpty() || form.getCVV() != null ||
				form.getExpirationMonth() != null || form.getExpirationYear() != null) {

			try {
				Assert.isTrue(form.getHolder() != "" && form.getMake() != "" && form.getNumber() != "" && form.getCVV() != null && form.getExpirationMonth() != null && form.getExpirationYear() != null);

				try {
					Assert.isTrue(!this.creditCardService.checkIfExpired(form.getExpirationMonth(), form.getExpirationYear()));
				} catch (final Throwable oops) {
					binding.rejectValue("expirationMonth", "card.date.error");
				}
				
			} catch (final Throwable oops) {
				binding.rejectValue("CVV", "card.invalid");
			}
			
			try {
				Assert.isTrue(this.utilityService.isValidCCMake(form.getMake()));
			} catch (Throwable oops) {
				binding.rejectValue("make", "invalid.make");
			}

			final CreditCard cc = new CreditCard();

			cc.setHolder(form.getHolder());
			cc.setMake(form.getMake());
			cc.setNumber(form.getNumber());
			cc.setCVV(form.getCVV());
			cc.setExpirationMonth(form.getExpirationMonth());
			cc.setExpirationYear(form.getExpirationYear());

			res.setCreditCard(cc);
		}

		/* Username */
		if (form.getUsername() != null)
			try {
				Assert.isTrue(this.utilityService.existsUsername(form.getUsername()));
			} catch (final Throwable oops) {
				binding.rejectValue("username", "username.error");
			}

		/* Password confirmation */
		if (!form.getPassword().isEmpty() && !form.getPasswordConfirmation().isEmpty())
			try {
				Assert.isTrue(form.getPassword().equals(form.getPasswordConfirmation()));
			} catch (final Throwable oops) {
				binding.rejectValue("passwordConfirmation", "password.confirmation.error");
			}

		/* Terms&Conditions */
		if (form.getTermsAndConditions() != null)
			try {
				Assert.isTrue((form.getTermsAndConditions()));
			} catch (final Throwable oops) {
				binding.rejectValue("termsAndConditions", "terms.error");
			}

		/* Email */
		if (!form.getEmail().isEmpty())
			try {
				Assert.isTrue(this.utilityService.checkEmail(form.getEmail(), "CUSTOMER"));
			} catch (final Throwable oops) {
				binding.rejectValue("email", "email.error");
			}

		/* Managing phone number */
		if (form.getPhoneNumber() != null)
			try {
				final char[] phoneArray = form.getPhoneNumber().toCharArray();
				if ((!form.getPhoneNumber().equals(null) && !form.getPhoneNumber().equals("")))
					if (phoneArray[0] != '+' && Character.isDigit(phoneArray[0])) {
						final String sc = this.systemConfigurationService.findMySystemConfiguration().getCountryCode();
						form.setPhoneNumber(sc + " " + form.getPhoneNumber());
					}
			} catch (final Throwable oops) {
				binding.rejectValue("phoneNumber", "phone.error");
			}
		return res;
	}

	public Customer reconstruct(final CustomerForm form, final BindingResult binding) {

		final Customer res = this.create();

		final Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "CUSTOMER"), "not.allowed");
		Assert.isTrue(principal.getId() == form.getId(), "not.allowed");

		res.setId(form.getId());
		res.setVersion(form.getVersion());
		res.setName(form.getName());
		res.setSurname(form.getSurname());
		res.setPhoto(form.getPhoto());
		res.setEmail(form.getEmail());
		res.setPhoneNumber(form.getPhoneNumber());
		res.setAddress(form.getAddress());

		if (form.getMiddleName() != null)
			res.setMiddleName(form.getMiddleName());

		if (!form.getHolder().isEmpty() || !form.getNumber().isEmpty() || form.getCVV() != null ||
				form.getExpirationMonth() != null || form.getExpirationYear() != null) {

			try {
				Assert.isTrue(form.getHolder() != "" && form.getMake() != "" && form.getNumber() != "" && form.getCVV() != null && form.getExpirationMonth() != null && form.getExpirationYear() != null);

				try {
					Assert.isTrue(!this.creditCardService.checkIfExpired(form.getExpirationMonth(), form.getExpirationYear()));
				} catch (final Throwable oops) {
					binding.rejectValue("expirationMonth", "card.date.error");
				}

			} catch (final Throwable oops) {
				binding.rejectValue("CVV", "card.invalid");
			}
			
			try {
				Assert.isTrue(this.utilityService.isValidCCMake(form.getMake()));
			} catch (Throwable oops) {
				binding.rejectValue("make", "invalid.make");
			}

			final CreditCard cc = new CreditCard();

			cc.setHolder(form.getHolder());
			cc.setMake(form.getMake());
			cc.setNumber(form.getNumber());
			cc.setCVV(form.getCVV());
			cc.setExpirationMonth(form.getExpirationMonth());
			cc.setExpirationYear(form.getExpirationYear());

			res.setCreditCard(cc);
		}

		/* Email */
		if (!form.getEmail().isEmpty())
			try {
				Assert.isTrue(this.utilityService.checkEmail(form.getEmail(), "CUSTOMER"));
			} catch (final Throwable oops) {
				binding.rejectValue("email", "email.error");
			}

		/* Managing phone number */
		if (form.getPhoneNumber() != null)
			try {
				final char[] phoneArray = form.getPhoneNumber().toCharArray();
				if ((!form.getPhoneNumber().equals(null) && !form.getPhoneNumber().equals("")))
					if (phoneArray[0] != '+' && Character.isDigit(phoneArray[0])) {
						final String sc = this.systemConfigurationService.findMySystemConfiguration().getCountryCode();
						form.setPhoneNumber(sc + " " + form.getPhoneNumber());
					}
			} catch (final Throwable oops) {
				binding.rejectValue("phoneNumber", "phone.error");
			}
		return res;
	}

	public void flush() {
		this.customerRepository.flush();
	}
	
	public String exportData() {
		Customer customer = (Customer) this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(customer, "CUSTOMER"), "not.allowed");
		
		String res;
		
		res = "Data of your user account:";
		res += "\r\n\r\n";
		res += "Name: " + customer.getName() 
				+ " \r\n" + "Middle Name: " + customer.getMiddleName() + " \r\n"
				+ " \r\n" + "Surname: "	+ customer.getSurname() + " \r\n"
				+ " \r\n" + "Photo: " + customer.getPhoto() + " \r\n" + "Email: "
				+ customer.getEmail() + " \r\n" + "Phone Number: "
				+ customer.getPhoneNumber() + " \r\n" + "Address: "
				+ customer.getAddress() + " \r\n" + " \r\n" + "\r\n"
				
				+ "Credit Card:" + "\r\n" + "Holder:"
				+ customer.getCreditCard().getHolder() + "\r\n" +
				"Make:" + customer.getCreditCard().getMake() + "\r\n" + "Number:"
				+ customer.getCreditCard().getNumber() + "\r\n"
				+ "Date expiration:"
				+ customer.getCreditCard().getExpirationMonth() + "/"
				+ customer.getCreditCard().getExpirationYear() + "\r\n" + "CVV:"
				+ customer.getCreditCard().getCVV();
		
		res += "\r\n\r\n";
		res += "----------------------------------------";
		res += "\r\n\r\n";
		
		res += "Finder:";
		res += "\r\n";
		res += "Results in last search:" + "\r\n" 
				+ this.finderService.findFinderByCustomerId(customer.getId()).getResults()
				+ "\r\n\r\n";

		res += "\r\n\r\n";
		res += "----------------------------------------";
		
		return res;
	}

}
