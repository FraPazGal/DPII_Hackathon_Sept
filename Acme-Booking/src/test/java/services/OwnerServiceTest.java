
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
import domain.Owner;
import forms.ActorForm;
import forms.ActorRegistrationForm;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class OwnerServiceTest extends AbstractTest {

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
	 * Coverage of the total project (%): 12'6
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 3.167
	 * 
	 * ################################################################
	 * 
	 * Test 1: A owner registers to the system
	 * 
	 * Test 2: A owner displays and edits their profile
	 * 
	 * Test 3: A owner exports their user data
	 * 
	 * Test 4: A owner deletes their account
	 */
	
	@Autowired
	private OwnerService	ownerService;
	
	@Autowired
	private UtilityService	utilityService;
	
	@Autowired
	private Validator validator;

	//Test 1: A owner registers to the system
	//Req. 8.1
	@Test
	public void driverRegisterOwner() {
		final Object testingData[][] = {

			{
				"ownerUsername", "password", "password", "Name", "MiddleName", "Surname", "http://www.photo.com", "owner@", "64578621", "C/ Address nº 5",
				true, IllegalArgumentException.class
			},
			//Negative test case, a user tries to register to the system as an owner with an invalid email

			{
				"customer1", "password", "password", "Name", "MiddleName", "Surname", "http://www.photo.com", "owner@email.com", "64578621", "C/ Address nº 5",
				true, IllegalArgumentException.class
			},
			//Negative test case, a user tries to register to the system as an owner with an existing username
			
			{
				"ownerUsername", "password", "password", "Name", "MiddleName", "Surname", "http://www.photo.com", "owner@email.com", "64578621", "C/ Address nº 5",
				true, null
			},
			//Positive test case, an owner registers to the system
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], 
					(String) testingData[i][4],(String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
					(String) testingData[i][8], (String) testingData[i][9], (Boolean) testingData[i][10],(Class<?>) testingData[i][11]);
		}
	}

	protected void template(final String username, final String password, final String confirmationPassword, 
			final String name, final String middleName, final String surname, final String photo, final String email, final String phoneNumber, 
			final String address, final Boolean terms, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.registerOwner(username, password, confirmationPassword, name, middleName, surname, photo, email, phoneNumber, address, terms);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void registerOwner(final String username, final String password, final String confirmationPassword, final String name, final String middleName, 
			final String surname, final String photo, final String email, final String phoneNumber, final String address, final Boolean terms) {
		
		ActorRegistrationForm actorRegistrationForm = new ActorRegistrationForm();
		
		actorRegistrationForm.setUsername(username);
		actorRegistrationForm.setPassword(password);
		actorRegistrationForm.setPasswordConfirmation(confirmationPassword);
		actorRegistrationForm.setName(name);
		actorRegistrationForm.setMiddleName(middleName);
		actorRegistrationForm.setSurname(surname);
		actorRegistrationForm.setPhoto(photo);
		actorRegistrationForm.setEmail(email);
		actorRegistrationForm.setPhoneNumber(phoneNumber);
		actorRegistrationForm.setAddress(address);
		actorRegistrationForm.setTermsAndConditions(terms);
		
		final BindingResult binding = new BeanPropertyBindingResult(actorRegistrationForm, actorRegistrationForm.getClass().getName());
		final Owner owner = this.ownerService.reconstruct(actorRegistrationForm, binding);
		this.validator.validate(actorRegistrationForm, binding);
		Assert.isTrue(!binding.hasErrors());
		this.ownerService.save(owner);
		
	}
	
	//Test 2: A owner displays and edits their profile
	//Req. 9.2
	@Test
	public void driverEditOwner() {
		final Object testingData[][] = {

			{
				"customer1", "Name", "MiddleName", "Surname", "http://www.photo.com", "owner@email.com", "64578621", "C/ Address nº 5",
				IllegalArgumentException.class
			},
			//Negative test case, a customer tries to edit the profile of an owner

			{
				"owner1", "", "MiddleName", "Surname", "http://www.photo.com", "owner@email.com", "64578621", "C/ Address nº 5",
				IllegalArgumentException.class
			},
			//Negative test case, an owner tries to edit their profile with a blank name
			
			{
				"owner1", "Name", "MiddleName", "Surname", "http://www.photo.com", "owner@email.com", "64578621", "C/ Address nº 5", null
			},
			//Positive test case, an owner edits their profile
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template2((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], 
					(String) testingData[i][4],(String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
					(Class<?>) testingData[i][8]);
		}
	}

	protected void template2(final String user, final String name, final String middleName, final String surname, final String photo, final String email, 
			final String phoneNumber, final String address, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user);
			this.editOwner(name, middleName, surname, photo, email, phoneNumber, address);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void editOwner(final String name, final String middleName, final String surname, final String photo, final String email, final String phoneNumber, 
			final String address) {
		
		ActorForm actorForm = new ActorForm();
		
		Actor principal = this.utilityService.findByPrincipal();
		
		actorForm.setId(principal.getId());
		actorForm.setName(name);
		actorForm.setSurname(surname);
		actorForm.setPhoto(photo);
		actorForm.setEmail(email);
		actorForm.setPhoneNumber(phoneNumber);
		actorForm.setAddress(address);
		
		final BindingResult binding = new BeanPropertyBindingResult(actorForm, actorForm.getClass().getName());
		final Owner owner = this.ownerService.reconstruct(actorForm, binding);
		this.validator.validate(actorForm, binding);
		Assert.isTrue(!binding.hasErrors());
		this.ownerService.save(owner);
		
	}
	
	//Test 3: A owner exports their user data
	//Req. 27
	@Test
	public void driverExportDataOwner() {
		final Object testingData[][] = {

			{
				"customer1", IllegalArgumentException.class
			},
			//Negative test case, a customer tries to export the user data of an owner

			{
				"admin", IllegalArgumentException.class
			},
			//Negative test case, an admin tries to export the user data of an owner
			
			{
				"owner1", null
			},
			//Positive test case, an owner exports their user data
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
			this.ownerService.exportData();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}
	
	
	//Test 4: A owner deletes their account
	//Req. 27
	@Test
	public void driverDeleteOwner() {
		final Object testingData[][] = {

			{
				"owner1", "admin", IllegalArgumentException.class
			},
			//Negative test case, an admin tries to delete the account of an owner

			{
				"owner1", "customer1", IllegalArgumentException.class
			},
			//Negative test case, a customer tries to delete the account of an owner
			
			{
				"owner1", "owner1", null
			},
			//Positive test case, an owner deletes their account
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
			Owner owner = (Owner) this.utilityService.findByPrincipal();
			this.unauthenticate();
			this.authenticate(user_2);
			this.ownerService.delete(owner);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

}
