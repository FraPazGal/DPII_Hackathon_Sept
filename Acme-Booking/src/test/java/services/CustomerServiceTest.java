
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import utilities.AbstractTest;
import domain.Actor;
import domain.Customer;
import forms.CustomerForm;
import forms.CustomerRegistrationForm;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CustomerServiceTest extends AbstractTest {

	/*
	 * Total coverage of all tests
	 * 
	 * 
	 * Coverage of the total project (%): 65'1
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 16.418
	 * 
	 * ################################################################
	 * 
	 * Total coverage by exclusively executing this test class
	 * 
	 * 
	 * Coverage of the total project (%): 12'7
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 3.211
	 * 
	 * ################################################################
	 * 
	 * Test 1: A customer registers to the system
	 * 
	 * Test 2: A customer displays and edits their profile
	 * 
	 * Test 3: A customer exports their user data
	 * 
	 * Test 4: A customer deletes their account
	 */
	
	@Autowired
	private CustomerService	customerService;
	
	@Autowired
	private UtilityService	utilityService;
	
	@Autowired
	private Validator validator;

	//Test 1: A customer registers to the system
	//Req. 8.1
	@Test
	public void driverRegisterCustomer() {
		final Object testingData[][] = {

			{
				"customerUsername", "password", "password", "Name", "MiddleName", "Surname", "http://www.photo.com", "customer@email.com", "64578621", "C/ Address nº 5",
				"Name of holder", "VISA", "1111222233334444", 10, 18, 164, true, IllegalArgumentException.class
			},
			//Negative test case, a customer tries to register to the system with an invalid credit card (expired)

			{
				"customerUsername", "password", "password", "Name", "MiddleName", "Surname", "not a photo url", "customer@email.com", "64578621", "C/ Address nº 5",
				"Name of holder", "VISA", "1111222233334444", 10, 20, 164, true, IllegalArgumentException.class
			},
			//Negative test case, a customer tries to register to the system with an invalid photo URL
			
			{
				"customerUsername", "password", "password", "Name", "MiddleName", "Surname", "http://www.photo.com", "customer@email.com", "64578621", "C/ Address nº 5",
				"Name of holder", "VISA", "1111222233334444", 10, 20, 164, true, null
			},
			//Positive test case, a customer registers to the system
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], 
					(String) testingData[i][4],(String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
					(String) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10], (String) testingData[i][11],
					(String) testingData[i][12], (Integer) testingData[i][13],(Integer) testingData[i][14],
					(Integer) testingData[i][15], (Boolean) testingData[i][16],(Class<?>) testingData[i][17]);
		}
	}

	protected void template(final String username, final String password, final String confirmationPassword, 
			final String name, final String middleName, final String surname, final String photo, final String email, final String phoneNumber, 
			final String address, final String holder, final String make, final String number,
			final Integer expirationMonth, final Integer expirationYear, final Integer CVV, final Boolean terms, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.registerCustomer(username, password, confirmationPassword, name, middleName, surname, photo, email, phoneNumber, address,
					holder, make, number, expirationMonth, expirationYear, CVV,terms);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void registerCustomer(final String username, final String password, final String confirmationPassword, final String name, final String middleName, 
			final String surname, final String photo, final String email, final String phoneNumber, final String address, final String holder, 
			final String make, final String number,	final Integer expirationMonth, final Integer expirationYear, final Integer CVV, final Boolean terms) {
		
		CustomerRegistrationForm customerRegistrationForm = new CustomerRegistrationForm();
		
		customerRegistrationForm.setUsername(username);
		customerRegistrationForm.setPassword(password);
		customerRegistrationForm.setPasswordConfirmation(confirmationPassword);
		customerRegistrationForm.setName(name);
		customerRegistrationForm.setMiddleName(middleName);
		customerRegistrationForm.setSurname(surname);
		customerRegistrationForm.setPhoto(photo);
		customerRegistrationForm.setEmail(email);
		customerRegistrationForm.setPhoneNumber(phoneNumber);
		customerRegistrationForm.setAddress(address);
		customerRegistrationForm.setHolder(holder);
		customerRegistrationForm.setMake(make);
		customerRegistrationForm.setNumber(number);
		customerRegistrationForm.setExpirationMonth(expirationMonth);
		customerRegistrationForm.setExpirationYear(expirationYear);
		customerRegistrationForm.setCVV(CVV);
		customerRegistrationForm.setTermsAndConditions(terms);
		
		final BindingResult binding = new BeanPropertyBindingResult(customerRegistrationForm, customerRegistrationForm.getClass().getName());
		final Customer customer = this.customerService.reconstruct(customerRegistrationForm, binding);
		this.validator.validate(customerRegistrationForm, binding);
		Assert.isTrue(!binding.hasErrors());
		this.customerService.save(customer);
		
	}
	
	//Test 2: A customer displays and edits their profile
	//Req. 9.2
	@Test
	public void driverEditCustomer() {
		final Object testingData[][] = {

			{
				"owner1", "Name", "MiddleName", "Surname", "http://www.photo.com", "customer@email.com", "64578621", "C/ Address nº 5",
				"Name of holder", "VISA", "1111222233334444", 10, 18, 164, IllegalArgumentException.class
			},
			//Negative test case, an owner tries to edit the profile of a customer

			{
				"customer1", "Name", "MiddleName", "Surname", "http://www.photo.com", "customer@email.com", "64578621", "C/ Address nº 5",
				"Name of holder", "VISA", "15648761314", 10, 20, 164, IllegalArgumentException.class
			},
			//Negative test case, a customer tries edit their profile with an invalid credit card (number)
			
			{
				"customer1", "Name", "MiddleName", "Surname", "http://www.photo.com", "customer@email.com", "64578621", "C/ Address nº 5",
				"Name of holder", "VISA", "1111222233334444", 10, 20, 164, null
			},
			//Positive test case, a customer edits their profile
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template2((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], 
					(String) testingData[i][4],(String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
					(String) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10],
					(Integer) testingData[i][11], (Integer) testingData[i][12],(Integer) testingData[i][13], (Class<?>) testingData[i][14]);
		}
	}

	protected void template2(final String user, final String name, final String middleName, final String surname, final String photo, final String email,  
			final String phoneNumber, final String address, final String holder, final String make, final String number,
			final Integer expirationMonth, final Integer expirationYear, final Integer CVV, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user);
			this.editCustomer(name, middleName, surname, photo, email, phoneNumber, address,
					holder, make, number, expirationMonth, expirationYear, CVV);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void editCustomer(final String name, final String middleName, final String surname, final String photo, final String email, final String phoneNumber, 
			final String address, final String holder, final String make, final String number,	final Integer expirationMonth, 
			final Integer expirationYear, final Integer CVV) {
		
		CustomerForm customerForm = new CustomerForm();
		
		Actor principal = this.utilityService.findByPrincipal();
		
		customerForm.setId(principal.getId());
		customerForm.setName(name);
		customerForm.setMiddleName(middleName);
		customerForm.setSurname(surname);
		customerForm.setPhoto(photo);
		customerForm.setEmail(email);
		customerForm.setPhoneNumber(phoneNumber);
		customerForm.setAddress(address);
		customerForm.setHolder(holder);
		customerForm.setMake(make);
		customerForm.setNumber(number);
		customerForm.setExpirationMonth(expirationMonth);
		customerForm.setExpirationYear(expirationYear);
		customerForm.setCVV(CVV);
		
		final BindingResult binding = new BeanPropertyBindingResult(customerForm, customerForm.getClass().getName());
		final Customer customer = this.customerService.reconstruct(customerForm, binding);
		this.validator.validate(customerForm, binding);
		Assert.isTrue(!binding.hasErrors());
		this.customerService.save(customer);
		
	}
	
	//Test 3: A customer exports their user data
	//Req. 27
	@Test
	public void driverExportDataCustomer() {
		final Object testingData[][] = {

			{
				"owner1", ClassCastException.class
			},
			//Negative test case, an owner tries to export the user data of a customer

			{
				"admin", ClassCastException.class
			},
			//Negative test case, an admin tries to export the user data of a customer
			
			{
				"customer1", null
			},
			//Positive test case, a customer exports their user data
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template3((String) testingData[i][0], (Class<?>) testingData[i][1]);
		}
	}

	protected void template3(final String user, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user);
			this.customerService.exportData();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}
	
	
	//Test 4: A customer deletes their account
	//Req. 27
	@Test
	public void driverDeleteCustomer() {
		final Object testingData[][] = {

			{
				"customer1", "admin", IllegalArgumentException.class
			},
			//Negative test case, an admin tries to delete the account of a customer

			{
				"customer1", "owner1", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to delete the account of a customer
			
			{
				"customer1", "customer1", null
			},
			//Positive test case, a customer deletes their account
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template4((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
		}
	}

	protected void template4(final String user_1, final String user_2, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user_1);
			Customer customer = (Customer) this.utilityService.findByPrincipal();
			this.unauthenticate();
			this.authenticate(user_2);
			this.customerService.delete(customer);
	
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}
	
		super.checkExceptions(expected, caught);
	}

}
