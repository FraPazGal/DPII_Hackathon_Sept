
package services;

import java.util.ArrayList;
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

import utilities.AbstractTest;
import domain.Actor;
import domain.Room;
import domain.Service;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ServiceServiceTest extends AbstractTest {

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
	 * Coverage of the total project (%): 8'3
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 2.100
	 * 
	 * ################################################################
	 * 
	 * Test 1: An owner displays rooms and makes a new service
	 * 
	 * Test 2: An owner edits a service
	 * 
	 * Test 3: An owner deletes a service
	 * 
	 * Test 4: An owner decommissions a service
	 */
	
	@Autowired
	private ServiceService	serviceService;
	
	@Autowired
	private RoomService	roomService;
	
	@Autowired
	private UtilityService	utilityService;

	//Test 1: An owner displays rooms and makes a new service
	//Req.10.3
	@Test
	public void driverCreateService() {
		final Object testingData[][] = {

			{
				"owner1", "customer1", "Name of service", "Description", 20.65, "ACTIVE", ClassCastException.class
			},
			//Negative test case, a customer tries to create a service

			{
				"owner1", "owner2", "Name of service", "Description", 20.65, "ACTIVE", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to create a service for a room of another owner
			
			{
				"owner1", "owner1", "", "Description", 20.65, "ACTIVE", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to create a service with a blank title
			
			{
				"owner2", "owner2", "Name of service", "Description", 20.65, "OUT-OF-SERVICE", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to create a service for a decommissioned room
			
			{
				"owner1", "owner1", "Name of service", "Description", 20.65, "DRAFT", null
			},
			//Positive test case, an owner creates a service for a room in draft mode
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], 
					(Double) testingData[i][4],(String) testingData[i][5], (Class<?>) testingData[i][6]);
		}
	}

	protected void template(final String user_1, final String user_2, final String name, final String description, final Double price, 
			final String status, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user_1);
			Room room = this.selectRoomWithStatus(status);
			this.unauthenticate();
			
			this.authenticate(user_2);
			this.createService(name, description, price, room);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}
	
	public void createService(final String name, final String description, final Double price, final Room room) {
		
		Service service = this.serviceService.create();
		
		service.setName(name);
		service.setDescription(description);
		service.setPrice(price);
		service.setRoom(room);
		
		final BindingResult binding = new BeanPropertyBindingResult(service, service.getClass().getName());
		Service result = this.serviceService.reconstruct(service, binding);
		Assert.isTrue(!binding.hasErrors());
		this.serviceService.save(result);
		
	}
	
	//Test 2: An owner edits a service
	//Req. 10.3
	@Test
	public void driverEditService() {
		final Object testingData[][] = {

			{
				"owner1", "customer1", "Name of service", "Description", 20.65, "DRAFT", ClassCastException.class
			},
			//Negative test case, a customer tries to edit a service

			{
				"owner1", "owner2", "Name of service", "Description", 20.65, "DRAFT", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to edit a service of another owner
			
			{
				"owner1", "owner1", "Name of service", "Description", 20.65, "ACTIVE", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to edit a service in final mode
			
			{
				"owner2", "owner2", "Name of service", "Description", 20.65, "OUT-OF-SERVICE", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to edits a decommissioned service
			
			{
				"owner1", "owner1", "Name of service", "Description", 20.65, "DRAFT", null
			},
			//Positive test case, an owner edits a service in draft mode
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template1((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], 
					(Double) testingData[i][4],(String) testingData[i][5], (Class<?>) testingData[i][6]);
		}
	}

	protected void template1(final String user_1, final String user_2, final String name, final String description, final Double price, 
			final String status, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user_1);
			Room room = this.selectRoomWithStatus(status);
			Collection<Service> services = this.serviceService.findServicesByRoomId(room.getId());
			Service service = null;
			
			for(Service s: services) {
				service = s;
				break;
			}
			
			this.unauthenticate();
			
			this.authenticate(user_2);
			this.editService(name, description, price, service);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}
	
	public void editService(final String name, final String description, final Double price, final Service service) {
		Service aux = this.serviceService.create();
		
		aux.setId(service.getId());
		aux.setName(name);
		aux.setDescription(description);
		aux.setPrice(price);
		
		final BindingResult binding = new BeanPropertyBindingResult(service, service.getClass().getName());
		Service result = this.serviceService.reconstruct(service, binding);
		Assert.isTrue(!binding.hasErrors());
		this.serviceService.save(result);
		
	}
	
	//Test 3: An owner deletes a service
	//Req. 10.3
	@Test
	public void driverDeleteService() {
		final Object testingData[][] = {

			{
				"owner1", "customer1", "DRAFT", ClassCastException.class
			},
			//Negative test case, a customer tries to delete a service

			{
				"owner1", "owner2", "DRAFT", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to delete a service of another owner
			
			{
				"owner1", "owner1", "ACTIVE", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to delete a service in final mode
			
			{
				"owner2", "owner2", "OUT-OF-SERVICE", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to delete a service already decommissioned
			
			{
				"owner1", "owner1", "DRAFT", null
			},
			//Positive test case, an owner deletes a service in draft mode
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template2((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
		}
	}

	protected void template2(final String user_1, final String user_2, final String status, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user_1);
			Room room = this.selectRoomWithStatus(status);
			Collection<Service> services = this.serviceService.findServicesByRoomId(room.getId());
			Service service = null;
			
			for(Service s: services) {
				service = s;
				break;
			}
			
			this.unauthenticate();
			
			this.authenticate(user_2);
			this.serviceService.delete(service);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}
	
	//Test 4: An owner decommissions a service
	//Req. 10.3
	@Test
	public void driverDecommisionService() {
		final Object testingData[][] = {

			{
				"owner1", "customer1", "DRAFT", IllegalArgumentException.class
			},
			//Negative test case, a customer tries to decommission a service

			{
				"owner1", "owner2", "DRAFT", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to decommission a service of another owner
			
			{
				"owner1", "owner1", "DRAFT", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to decommission a service in draft mode
			
			{
				"owner2", "owner2", "OUT-OF-SERVICE", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to decommission a service already decommissioned
			
			{
				"owner1", "owner1", "ACTIVE", null
			},
			//Positive test case, an owner decommissions an active service 
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template3((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
		}
	}

	protected void template3(final String user_1, final String user_2, final String status, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user_1);
			Room room = this.selectRoomWithStatus(status);
			Collection<Service> services = this.serviceService.findServicesByRoomId(room.getId());
			Service service = null;
			
			for(Service s: services) {
				service = s;
				break;
			}
			
			this.unauthenticate();
			
			this.authenticate(user_2);
			this.serviceService.decommission(service);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}
	
	public Room selectRoomWithStatus (String status) {
		Actor principal = this.utilityService.findByPrincipal();
		
		Room result = new Room();
		Collection<Room> rooms = new ArrayList<>();
		switch (status) {
		
		case "ACTIVE":
			rooms = this.roomService.findRoomsActiveAndMine(principal.getId());
			break;

		case "DRAFT":
			rooms = this.roomService.findRoomsDraftAndMine(principal.getId());
			break;
			
		case "REVISION-PENDING":
			rooms = this.roomService.findRoomsRevisionPendingAndMine(principal.getId());
			break;
			
		case "OUT-OF-SERVICE":
			rooms = this.roomService.findRoomsOutOfServiceAndMine(principal.getId());
			break;
			
		case "REJECTED":
			rooms = this.roomService.findRoomsRejectedAndMine(principal.getId());
			break;
		}
		
		for(Room r : rooms) {
			if (!this.serviceService.findServicesByRoomId(r.getId()).isEmpty()) {
				result = r;
				break;
			}
		}
		
		return result;
	}
}
