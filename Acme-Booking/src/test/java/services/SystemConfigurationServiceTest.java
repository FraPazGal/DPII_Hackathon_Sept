
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
import domain.Category;
import domain.SystemConfiguration;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SystemConfigurationServiceTest extends AbstractTest {

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
	 * Test 1: Edit system configuration
	 * 
	 * Test 2: Display dashboard
	 * 
	 * Test 3: Ban/Unban user
	 */

	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	@Autowired
	private DashboardService			dashboardService;

	@Autowired
	private ActorService				actorService;

	@Autowired
	private Validator					validator;

	@Autowired
	private UtilityService				utilityService;


	//Test 1: Edit system configuration
	@Test
	public void editDriver() {
		final Object testingData[][] = {
			{
				"admin", "admin", "systemName", "http://us.es", "+035", "makers", "spamWords", 10, 5, 0.2, null
			},//positive
			{
				"admin", "customer", "systemName", "http://us.es", "+035", "makers", "spamWords", 10, 5, 0.2, IllegalArgumentException.class
			},//negative: invalid authenticated
			{
				"admin", "admin", null, "http://us.es", "+35", "makers", "spamWords", 10, 5, 2.0, IllegalArgumentException.class
			}
		//negative: invalid data
		};

		for (int i = 0; i < testingData.length; i++)
			this.editTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (Integer) testingData[i][7],
				(Integer) testingData[i][8], (Double) testingData[i][9], (Class<?>) testingData[i][10]);
	}

	private void editTemplate(final String userList, final String user, final String systemName, final String banner, final String countryCode, final String makers, final String spamWords, final Integer time, final Integer max, final Double VATTax,
		final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userList);
			final SystemConfiguration sys = this.systemConfigurationService.findMySystemConfiguration();
			this.unauthenticate();
			this.authenticate(user);
			this.editRoom(sys, systemName, banner, countryCode, makers, spamWords, time, max, VATTax);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}
		super.checkExceptions(expected, caught);

	}

	public void editRoom(final SystemConfiguration sys, final String systemName, final String banner, final String countryCode, final String makers, final String spamWord, final Integer time, final Integer max, final Double VATTax) {
		final SystemConfiguration s = new SystemConfiguration();
		s.setBanner(banner);
		s.setBreachNotification(sys.getBreachNotification());
		s.setCountryCode(countryCode);
		s.setId(sys.getId());
		s.setMakers(makers);
		s.setMaxResults(max);
		s.setSpamWords(spamWord);
		s.setSystemName(systemName);
		s.setTimeResultsCached(time);
		s.setVATTax(VATTax);
		s.setWelcomeMessage(sys.getWelcomeMessage());
		final BindingResult binding = new BeanPropertyBindingResult(s, s.getClass().getName());
		final String wes = sys.getWelcomeMessage().get("Español");
		final String wen = sys.getWelcomeMessage().get("English");
		final SystemConfiguration system = this.systemConfigurationService.reconstruct(s, wes, wen, "", "", binding);
		Assert.isTrue(binding.hasErrors() == false);
		this.systemConfigurationService.save(system);
	}
	//Test 2: Display dashboard
	@Test
	public void dashboardDriver() {
		final Object testingData[][] = {

			{
				"customer1", IllegalArgumentException.class
			},//negative: invalid authenticated
			{
				"owner1", IllegalArgumentException.class
			},//negative: ban a not spammer user
			{
				"admin", null
			}
		//positive
		};

		for (int i = 0; i < testingData.length; i++)
			this.dashboardTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	private void dashboardTemplate(final String userList, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userList);
			this.dashboardActor();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}
		super.checkExceptions(expected, caught);

	}

	public void dashboardActor() {
		final Double[] statsBookingsPerRoom = this.dashboardService.statsBookingsPerRoom();
		final Double[] statsServicesPerRoom = this.dashboardService.statsServicesPerRoom();
		final Double[] statsPricePerRoom = this.dashboardService.statsPricePerRoom();
		final Double ratioRevisionPendingByFinalRooms = this.dashboardService.ratioRevisionPendingByFinalRooms();
		final Double ratioAcceptedByFinalRooms = this.dashboardService.ratioAcceptedByFinalRooms();
		final Double ratioRejectedByFinalRooms = this.dashboardService.ratioRejectedByFinalRooms();
		final Double ratioRoomsOutOfService = this.dashboardService.ratioRoomsOutOfService();
		final Category topCategoryByRooms = this.dashboardService.topCategoryByRooms();
		final Double[] statsFinder = this.dashboardService.statsFinder();
		final Double ratioFindersEmpty = this.dashboardService.ratioFindersEmpty();
	}
	//Test 3: Ban/Unban user
	@Test
	public void banDriver() {
		final Object testingData[][] = {

			{
				"admin", "customer1", "unban", true/* is spammer */, IllegalArgumentException.class
			},//negative: invalid authenticated
			{
				"admin", "admin", "ban", false/* is spammer */, IllegalArgumentException.class
			},//negative: ban a not spammer user
			{
				"admin", "admin", "unban", true/* is spammer */, null
			}
		//positive
		};

		for (int i = 0; i < testingData.length; i++)
			this.banTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	private void banTemplate(final String userList, final String user, final String ban, final Boolean spammer, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userList);
			final Collection<Actor> actores = this.actorService.findAllExceptPrincipal();
			this.unauthenticate();
			this.authenticate(user);
			this.banActor(actores, ban, spammer);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}
		super.checkExceptions(expected, caught);

	}

	public void banActor(final Collection<Actor> actores, final String ban, final Boolean spammer) {
		for (final Actor a : actores)
			if (a.getIsSpammer() == spammer) {
				if (ban == "ban")
					this.actorService.ban(a.getId());
				if (ban == "unban")
					this.actorService.unban(a.getId());
				break;
			}
	}
}
