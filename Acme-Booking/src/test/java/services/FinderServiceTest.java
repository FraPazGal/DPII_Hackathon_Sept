
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
import domain.Finder;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class FinderServiceTest extends AbstractTest {

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
	 * Coverage of the total project (%): 6'5
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 1.638
	 * 
	 * ################################################################
	 * 
	 * Test 1: A customer search for rooms to cached
	 * 
	 * Test 2: A customer delete his cached finder
	 */

	@Autowired
	private FinderService	finderService;

	@Autowired
	private Validator		validator;

	@Autowired
	private UtilityService	utilityService;


	//Test 1: A customer search for rooms to cached
	@Test
	public void finderDriver() {
		final Object testingData[][] = {
			{
				"customer1", "customer1", "estudiantes", "Limpieza", "Presentación", 25.0, 25, null
			},//positive
			{
				"customer1", "admin", "estudiantes", "Limpieza", "Presentación", 25.0, 25, IllegalArgumentException.class
			},//negative: invalid authenticated
			{
				"customer1", "customer1", "estudiantes", "service", "asdf", 0.0, 0, IllegalArgumentException.class
			}
		//negative: invalid data
		};

		for (int i = 0; i < testingData.length; i++)
			this.finderTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Double) testingData[i][5], (Integer) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}

	private void finderTemplate(final String userList, final String user, final String keyWord, final String service, final String category, final Double fee, final Integer capacity, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userList);
			final Finder finder = this.finderService.finderByCustomer();
			this.unauthenticate();
			this.authenticate(user);
			this.finderRoom(finder, keyWord, service, category, fee, capacity);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}
		super.checkExceptions(expected, caught);

	}

	public void finderRoom(final Finder finder, final String keyWord, final String service, final String category, final Double fee, final Integer capacity) {
		final Finder f = this.finderService.create();
		f.setCapacity(capacity);
		f.setCategory(category);
		f.setId(finder.getId());
		f.setKeyWord(keyWord);
		f.setMaximumFee(fee);
		f.setService(service);
		final BindingResult binding = new BeanPropertyBindingResult(f, f.getClass().getName());
		final Finder fin = this.finderService.reconstruct(f, binding);
		Assert.isTrue(binding.hasErrors() == false);
		final Finder find = this.finderService.search(fin);
		Assert.isTrue(!find.getResults().isEmpty());
	}
	//Test 2: A customer delete his cached finder
	@Test
	public void finderDeleteDriver() {
		final Object testingData[][] = {
			{
				"customer1", "customer1", null
			},//positive
			{
				"customer1", "admin", IllegalArgumentException.class
			},//negative: invalid authenticated
			{
				"customer1", "customer2", IllegalArgumentException.class
			}
		//negative: invalid data
		};

		for (int i = 0; i < testingData.length; i++)
			this.finderDeleteTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	private void finderDeleteTemplate(final String userList, final String user, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userList);
			final Finder finder = this.finderService.finderByCustomer();
			this.unauthenticate();
			this.authenticate(user);
			this.finderDeleteRoom(finder);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}
		super.checkExceptions(expected, caught);

	}

	public void finderDeleteRoom(final Finder finder) {
		final BindingResult binding = new BeanPropertyBindingResult(finder, finder.getClass().getName());
		final Finder fin = this.finderService.reconstruct(finder, binding);
		Assert.isTrue(binding.hasErrors() == false);
		Assert.isTrue(finder.getCustomer().equals(fin.getCustomer()));
		this.finderService.delete(fin);
	}
}
