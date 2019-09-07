
package services;

import java.util.Collection;

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
import domain.MessageBox;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MessageBoxServiceTest extends AbstractTest {

	/*
	 * Total coverage of all tests
	 * 
	 * 
	 * Coverage of the total project (%):
	 * 
	 * 
	 * Coverage of the total project (lines of codes):
	 * 
	 * ################################################################
	 * 
	 * Total coverage by exclusively executing this test class
	 * 
	 * 
	 * Coverage of the total project (%):
	 * 
	 * 
	 * Coverage of the total project (lines of codes):
	 * 
	 * ################################################################
	 * 
	 * Test 1: Create box
	 * 
	 * Test 2: List and edit box
	 * 
	 * Test 2: List and delete box
	 */
	@Autowired
	private MessageBoxService	messageBoxService;

	@Autowired
	private Validator			validator;

	@Autowired
	private UtilityService		utilityService;


	//Test 1: Create box
	@Test
	public void createDriver() {
		final Object testingData[][] = {
			{
				"customer1", "customer1", "Test", null
			},//positive
			{
				"customer1", "admin", "Test", IllegalArgumentException.class
			},//negative: invalid authenticated
			{
				"customer1", "customer1", "", IllegalArgumentException.class
			}
		//negative: invalid data
		};

		for (int i = 0; i < testingData.length; i++)
			this.createTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	private void createTemplate(final String userList, final String user, final String keyWord, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userList);
			final Actor actor = this.utilityService.findByPrincipal();
			this.unauthenticate();
			this.authenticate(user);
			this.createBox(actor, keyWord);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();

		}
		super.checkExceptions(expected, caught);

	}

	public void createBox(final Actor id, final String name) {
		final Actor actor = this.utilityService.findByPrincipal();
		final MessageBox box = this.messageBoxService.create(actor);
		box.setName(name);
		box.setOwner(id);
		final BindingResult binding = new BeanPropertyBindingResult(box, box.getClass().getName());
		final MessageBox b = this.messageBoxService.reconstruct(box, binding);
		Assert.isTrue(binding.hasErrors() == false);
		final MessageBox result = this.messageBoxService.save(b);
		Assert.isTrue(id.equals(result.getOwner()));
	}
	//Test 2: List and edit box
	@Test
	public void editDriver() {
		final Object testingData[][] = {
			{
				"admin", "admin", "Test", false/* predefined */, null
			},//positive
			{
				"admin", "customer1", "Test", false/* predefined */, IllegalArgumentException.class
			},//negative: invalid authenticated
			{
				"admin", "admin", "Test", true/* predefined */, IllegalArgumentException.class
			}
		//negative: invalid box
		};

		for (int i = 0; i < testingData.length; i++)
			this.editTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	private void editTemplate(final String userList, final String user, final String keyWord, final Boolean predefined, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userList);
			final Collection<MessageBox> boxes = this.messageBoxService.findByOwnerFirst();
			this.unauthenticate();
			this.authenticate(user);
			this.editBox(boxes, keyWord, predefined);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}
		super.checkExceptions(expected, caught);

	}

	public void editBox(final Collection<MessageBox> boxes, final String name, final Boolean predefined) {
		final Actor principal = this.utilityService.findByPrincipal();
		for (final MessageBox box : boxes)
			if (box.getIsPredefined() == predefined) {
				final MessageBox test = this.messageBoxService.create(principal);
				test.setName(name);
				test.setOwner(box.getOwner());
				test.setId(box.getId());
				final BindingResult binding = new BeanPropertyBindingResult(box, box.getClass().getName());
				final MessageBox b = this.messageBoxService.reconstruct(box, binding);
				Assert.isTrue(binding.hasErrors() == false);
				final MessageBox result = this.messageBoxService.save(b);
				Assert.isTrue(box.getOwner().equals(result.getOwner()));
				break;
			}

	}

	//Test 2: List and delete box
	@Test
	public void deleteDriver() {
		final Object testingData[][] = {

			{
				"admin", "customer1", false/* predefined */, IllegalArgumentException.class
			},//negative: invalid authenticated
			{
				"admin", "admin", true/* predefined */, IllegalArgumentException.class
			},
			//negative: invalid box
			{
				"admin", "admin", false/* predefined */, null
			}
		//positive
		};

		for (int i = 0; i < testingData.length; i++)
			this.editTemplate((String) testingData[i][0], (String) testingData[i][1], (Boolean) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	private void editTemplate(final String userList, final String user, final Boolean predefined, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userList);
			final Collection<MessageBox> boxes = this.messageBoxService.findByOwnerFirst();
			this.unauthenticate();
			this.authenticate(user);
			this.deleteBox(boxes, predefined);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}
		super.checkExceptions(expected, caught);

	}

	public void deleteBox(final Collection<MessageBox> boxes, final Boolean predefined) {
		for (final MessageBox box : boxes)
			if (box.getIsPredefined() == predefined) {
				this.messageBoxService.delete(box);
				break;
			}
	}
}
