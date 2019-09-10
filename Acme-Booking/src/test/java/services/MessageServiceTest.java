
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
import domain.Message;
import domain.MessageBox;
import forms.MessageForm;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MessageServiceTest extends AbstractTest {

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
	 * Coverage of the total project (%): 7'3
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 1.838
	 * 
	 * ################################################################
	 * 
	 * Test 1: Send message
	 * 
	 * Test 2: List and edit
	 * 
	 * Test 3: List and delete
	 */

	@Autowired
	private MessageService		messageService;

	@Autowired
	private MessageBoxService	messageboxService;

	@Autowired
	private Validator			validator;

	@Autowired
	private UtilityService		utilityService;


	//Test 1: Send message
	@Test
	public void sendDriver() {
		final Object testingData[][] = {
			{
				"owner1", "admin", "asunto", "cuerpo", "HIGH", null
			},//positive
			{
				"owner1", "", "asunto", "cuerpo", "HIGH", IllegalArgumentException.class
			},//negative: invalid authenticated
			{
				"emisor", "admin", "", "cuerpo", "HIGH", IllegalArgumentException.class
			}
		//negative: invalid data
		};

		for (int i = 0; i < testingData.length; i++)
			this.sendTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	private void sendTemplate(final String emisor, final String receptor, final String asunto, final String cuerpo, final String prioridad, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(receptor);
			final Actor actor = this.utilityService.findByPrincipal();
			this.unauthenticate();
			this.authenticate(emisor);
			this.send(actor, asunto, cuerpo, prioridad);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}
		super.checkExceptions(expected, caught);
	}

	public void send(final Actor receptor, final String asunto, final String cuerpo, final String prioridad) {
		final MessageForm m = new MessageForm(this.messageService.create());
		m.setBody(cuerpo);
		m.setSubject(asunto);
		m.setPriority(prioridad);
		m.setReceiver(receptor);
		final BindingResult binding = new BeanPropertyBindingResult(m, m.getClass().getName());
		final Message message = this.messageService.reconstruct(m, binding);
		Assert.isTrue(binding.hasErrors() == false);
		this.messageService.save(message);
	}

	//Test 2: List and edit
	@Test
	public void editDriver() {
		final Object testingData[][] = {
			{
				"admin", "admin", "Hola"/* destination */, "Out box"/* origen */, NullPointerException.class
			},//negative: invalid box
			{
				"admin", "owner1", "Spam box"/* destination */, "In box"/* origen */, IllegalArgumentException.class
			},
			//negative: invalid box owner
			{
				"admin", "admin", "Spam box"/* destination */, "Out box"/* origen */, null
			}
		//positive
		};

		for (int i = 0; i < testingData.length; i++)
			this.editTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	private void editTemplate(final String owner, final String editor, final String boxDestino, final String boxOrigen, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(owner);
			final Actor principal = this.utilityService.findByPrincipal();
			final MessageBox bDestino = this.messageboxService.findByName(principal.getId(), boxDestino);
			this.unauthenticate();
			this.authenticate(editor);
			this.edit(bDestino, boxOrigen);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}
		super.checkExceptions(expected, caught);
	}

	public void edit(final MessageBox boxDestination, final String origen) {
		final Actor principal = this.utilityService.findByPrincipal();
		final MessageBox bOrigen = this.messageboxService.findByName(principal.getId(), origen);
		for (final Message m : bOrigen.getMessages()) {
			final MessageForm mf = new MessageForm(m);
			final BindingResult binding = new BeanPropertyBindingResult(mf, mf.getClass().getName());
			final Message message = this.messageService.reconstruct(mf, binding);
			Assert.isTrue(binding.hasErrors() == false);
			this.messageService.move(message, boxDestination);
			break;
		}
	}

	//Test 3: List and delete
	@Test
	public void deleteDriver() {
		final Object testingData[][] = {
			{
				"admin", "admin", "Hello"/* box */, NullPointerException.class
			},//negative: invalid box
			{
				"admin", "owner2", "Out box"/* destination */, IllegalArgumentException.class
			},
			//negative: invalid box owner
			{
				"admin", "admin", "Out box"/* box */, null
			}
		//positive
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	private void deleteTemplate(final String owner, final String editor, final String boxOrigen, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(owner);
			final Actor principal = this.utilityService.findByPrincipal();
			final MessageBox bDestino = this.messageboxService.findByName(principal.getId(), boxOrigen);
			this.unauthenticate();
			this.authenticate(editor);
			this.delete(bDestino);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}
		super.checkExceptions(expected, caught);
	}

	public void delete(final MessageBox boxDestination) {
		for (final Message m : boxDestination.getMessages()) {
			final MessageForm mf = new MessageForm(m);
			final BindingResult binding = new BeanPropertyBindingResult(mf, mf.getClass().getName());
			final Message message = this.messageService.reconstruct(mf, binding);
			Assert.isTrue(binding.hasErrors() == false);
			this.messageService.delete(message);
			break;
		}
	}
}
