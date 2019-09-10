
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
import domain.Category;
import domain.Room;
import forms.ActiveRoomForm;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RoomServiceTest extends AbstractTest {

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
	 * Coverage of the total project (%): 5.5%
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 927
	 * 
	 * ################################################################
	 * 
	 * Test 1: An owner creates a room
	 * 
	 * Test 2: An owner edits a room
	 * 
	 * Test 3: An owner deletes a room
	 * 
	 * Test 4: An owner decommissions a room
	 * 
	 * Test 5: An admin assigns a pending room to themselves and accepts or reject the room
	 */
	
	@Autowired
	private RoomService	roomService;
	
	@Autowired
	private CategoryService	categoryService;
	
	@Autowired
	private UtilityService	utilityService;
	
	
	//Test 1: An owner creates a room
	//Req. 10.1
	@Test
	public void driverCreateRoom() {
		final Object testingData[][] = {

			{
				"owner1", "Title", "Description", "Schedule Details", "Address", "http://www.photourl.com", "08:00", "18:00", "", 15.42, 11, "Cinema", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to create a room with a blank proveOfOwnership

			{
				"customer1", "Title", "Description", "Schedule Details", "Address", "http://www.photourl.com", "08:00", "18:00", "http://www.proveofownership.com", 15.42, 11, "Cinema", IllegalArgumentException.class
			},
			//Negative test case, a customer tries to create a room
			
			{
				"admin", "Title", "Description", "Schedule Details", "Address", "http://www.photourl.com", "08:00", "18:00", "http://www.proveofownership.com", 15.42, 11, "Cinema", IllegalArgumentException.class
			},
			//Positive test case, an admin tries to create a room
			
			{
				"owner1", "Title", "Description", "Schedule Details", "Address", "http://www.photourl.com", "08:00", "18:00", "http://www.proveofownership.com", 15.42, 11, "Cinema", null
			},
			//Positive test case, an owner creates a room
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], 
					(String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7], (String) testingData[i][8], 
					(Double) testingData[i][9], (Integer) testingData[i][10], (String) testingData[i][11], (Class<?>) testingData[i][12]);
		}
	}

	protected void template(final String user, final String title, final String description, final String scheduleDetails, final String address, 
			final String photos, final String openingHour, final String closingHour, final String proveOfOwnership, final Double pricePerHour, 
			final Integer capacity, final String parentEnglishTitle, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user);
			Category category = this.findCategoryByTitle(parentEnglishTitle);
			this.createRoom(title, description, scheduleDetails, address, photos, openingHour, closingHour, proveOfOwnership, pricePerHour,
					capacity, category);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void createRoom(final String title, final String description, final String scheduleDetails, final String address, 
			final String photos, final String openingHour, final String closingHour, final String proveOfOwnership, final Double pricePerHour, 
			final Integer capacity, final Category category) {
		
		Room room = this.roomService.create();
		
		room.setTitle(title);
		room.setDescription(description);
		room.setScheduleDetails(scheduleDetails);
		room.setAddress(address);
		room.setPhotos(photos);
		room.setOpeningHour(openingHour);
		room.setClosingHour(closingHour);
		room.setProveOfOwnership(proveOfOwnership);
		room.setPricePerHour(pricePerHour);
		room.setCapacity(capacity);
		
		Collection<Category> categories = room.getCategories();
		categories.add(category);
		
		room.setCategories(categories);
		
		final BindingResult binding = new BeanPropertyBindingResult(room, room.getClass().getName());
		final Room result = this.roomService.reconstructDraft(room, binding);
		Assert.isTrue(!binding.hasErrors());
		this.roomService.save(result);
	}
	
	//Test 2: An owner edits a room
	//Req. 10.1
	@Test
	public void driverEditRoom() {
		final Object testingData[][] = {

			{
				"owner1", "customer1", "Title", "Description", "Schedule Details", "Address", "http://www.photourl.com", "08:00", "18:00", "http://www.proveofownership.com", 15.42, 11, "Cinema", 
				"DRAFT", "DRAFT", IllegalArgumentException.class
			},
			//Negative test case, a customer tries to edit a room
			
			{
				"owner1", "admin", "Title", "Description", "Schedule Details", "Address", "http://www.photourl.com", "08:00", "18:00", "http://www.proveofownership.com", 15.42, 11, "Cinema",
				"DRAFT", "DRAFT", IllegalArgumentException.class
			},
			//Negative test case, an admin tries to edit a room
			
			{
				"owner1", "owner1", "Title", "Description", "Schedule Details", "Address", "http://www.photourl.com", "08:00", "18:00", "http://www.proveofownership.com", 15.42, 11, "Cinema", 
				"DRAFT", "ACTIVE", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to edit a room in draft mode as a one in final mode
			
			{
				"owner1", "owner1", "Title", "Description", "Schedule Details", "Address", "http://www.photourl.com", "08:00", "18:00", "http://www.proveofownership.com", 15.42, 11, "Cinema", 
				"ACTIVE", "DRAFT", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to edit a room in final mode as a one in draft mode
			
			{
				"owner1", "owner1", "", "Description", "Schedule Details", "Address", "http://www.photourl.com", "08:00", "18:00", "http://www.proveofownership.com", 15.42, 11, "Cinema", 
				"DRAFT", "DRAFT", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to edit a room with a blank title
			
			{
				"owner1", "owner1", "Title", "Description", "Schedule Details", "Address", "not a url", "08:00", "18:00", "http://www.proveofownership.com", 15.42, 11, "Cinema", 
				"DRAFT", "DRAFT", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to edit a room with an invalid photo url
			
			{
				"owner1", "owner2", "Title", "Description", "Schedule Details", "Address", "http://www.photourl.com", "08:00", "18:00", "http://www.proveofownership.com", 15.42, 11, "Cinema", 
				"DRAFT", "DRAFT", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to edit a room in draft mode of another owner
			
			{
				"owner2", "owner1", "Title", "Description", "Schedule Details", "Address", "http://www.photourl.com", "08:00", "18:00", "http://www.proveofownership.com", 15.42, 11, "Cinema", 
				"ACTIVE", "ACTIVE", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to edit a room in final mode of another owner
			
			{
				"owner1", "owner1", "Title", "Description", "Schedule Details", "Address", "http://www.photourl.com", "08:00", "18:00", "http://www.proveofownership.com", 15.42, 11, "Cinema", 
				"ACTIVE", "ACTIVE", null
			},
			//Positive test case, an owner edits a room in final mode
			
			{
				"owner1", "owner1", "Title", "Description", "Schedule Details", "Address", "http://www.photourl.com", "08:00", "18:00", "http://www.proveofownership.com", 15.42, 11, "Cinema",
				"DRAFT", "DRAFT", null
			},
			//Positive test case, an owner edits a room in final mode
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], 
					(String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7], (String) testingData[i][8], 
					(String) testingData[i][9], (Double) testingData[i][10], (Integer) testingData[i][11], (String) testingData[i][12], (String) testingData[i][13], 
					(String) testingData[i][14], (Class<?>) testingData[i][15]);
		}
	}

	protected void template(final String user_1, final String user_2, final String title, final String description, final String scheduleDetails, final String address, 
			final String photos, final String openingHour, final String closingHour, final String proveOfOwnership, final Double pricePerHour, 
			final Integer capacity, final String parentEnglishTitle, final String status, final String reconstructAs, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user_1);
			Room roomToEdit = this.selectRoomWithStatus(status);
			Category category = this.findCategoryByTitle(parentEnglishTitle);
			this.unauthenticate();
			
			this.authenticate(user_2);
			this.editRoom(title, description, scheduleDetails, address, photos, openingHour, closingHour, proveOfOwnership, pricePerHour,
					capacity, reconstructAs, category, roomToEdit);
			
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void editRoom(final String title, final String description, final String scheduleDetails, final String address, 
			final String photos, final String openingHour, final String closingHour, final String proveOfOwnership, final Double pricePerHour, 
			final Integer capacity, final String reconstructAs, final Category category, final Room roomToEdit) {
		
		Room result = new Room();
		
		if(reconstructAs == "DRAFT") {
			Room room = this.roomService.create();
			
			room.setId(roomToEdit.getId());
			room.setTitle(title);
			room.setDescription(description);
			room.setScheduleDetails(scheduleDetails);
			room.setAddress(address);
			room.setPhotos(photos);
			room.setOpeningHour(openingHour);
			room.setClosingHour(closingHour);
			room.setProveOfOwnership(proveOfOwnership);
			room.setPricePerHour(pricePerHour);
			room.setCapacity(capacity);
			
			Collection<Category> categories = room.getCategories();
			categories.add(category);
			
			room.setCategories(categories);
			
			final BindingResult binding = new BeanPropertyBindingResult(room, room.getClass().getName());
			result = this.roomService.reconstructDraft(room, binding);
			Assert.isTrue(!binding.hasErrors());
			
		} else {
			ActiveRoomForm activeRoomForm = new ActiveRoomForm(roomToEdit);
			
			activeRoomForm.setClosingHour(closingHour);
			activeRoomForm.setOpeningHour(openingHour);
			activeRoomForm.setScheduleDetails(scheduleDetails);
			
			final BindingResult binding = new BeanPropertyBindingResult(activeRoomForm, activeRoomForm.getClass().getName());
			result = this.roomService.reconstructActive(activeRoomForm, binding);
			Assert.isTrue(!binding.hasErrors());
		}
		
		this.roomService.save(result);
	}
	
	//Test 3: An owner deletes a room
	//Req. 10.2
	@Test
	public void driverDeleteRoom() {
		final Object testingData[][] = {

			{
				"owner1", "admin", "DRAFT", IllegalArgumentException.class
			},
			//Negative test case, an admin tries to delete a room

			{
				"owner1", "customer1", "ACTIVE", IllegalArgumentException.class
			},
			//Negative test case, a customer tries to delete a room
			
			{
				"owner1", "owner2", "DRAFT", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to delete a room of another owner

			{
				"owner1", "owner1", "REVISION-PENDING", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to delete a room in revision mode
			
			{
				"owner2", "owner2", "OUT-OF-SERVICE", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to delete an out-of-service room 

			{
				"owner2", "owner2", "ACTIVE", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to delete a room in final mode
			
			{
				"owner1", "owner1", "DRAFT", null
			},
			//Positive test case, an owner deletes a room in draft mode
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
			Room roomToDelete = this.selectRoomWithStatus(status);
			this.unauthenticate();
			
			this.authenticate(user_2);
			this.roomService.deleteAsDraft(roomToDelete);
			
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}
	
	//Test 4: An owner decommissions a room
	//Req. 10.2
	@Test
	public void driverDecommssionRoom() {
		final Object testingData[][] = {

			{
				"owner1", "admin", "DRAFT", IllegalArgumentException.class
			},
			//Negative test case, an admin tries to decommission a room

			{
				"owner1", "customer1", "ACTIVE", IllegalArgumentException.class
			},
			//Negative test case, a customer tries to decommission a room
			
			{
				"owner1", "owner2", "REVISION-PENDING", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to decommission a room of another owner

			{
				"owner1", "owner1", "REJECTED", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to decommission a room in rejected mode
			
			{
				"owner2", "owner2", "OUT-OF-SERVICE", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to decommission a room already in decommissioned mode

			{
				"owner2", "owner2", "DRAFT", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to decommission a room in draft mode
			
			{
				"owner1", "owner1", "ACTIVE", null
			},
			//Positive test case, an owner decommissions a room in final mode
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template4((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
		}
	}

	protected void template4(final String user_1, final String user_2, final String status, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user_1);
			Room roomToDecommission = this.selectRoomWithStatus(status);
			this.unauthenticate();
			
			this.authenticate(user_2);
			this.roomService.decommision(roomToDecommission);
			
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}
	
	//Test 5: An admin assigns a pending room to themselves and accepts or reject the room
	//Req. 12.3
	@Test
	public void driverAcceptRejectRoom() {
		final Object testingData[][] = {

			{
				"admin", "admin2", "ACCEPT", IllegalArgumentException.class
			},
			//Negative test case, an admin tries to accept a room assigned to another admin

			{
				"admin", "customer1", "REJECT", IllegalArgumentException.class
			},
			//Negative test case, a customer tries to reject a room

			{
				"admin", "admin", "REJECT", null
			},
			//Positive test case, an admin rejects a room 
			
			{
				"admin", "admin", "ACCEPT", null
			},
			//Positive test case, an admin accepts a room 
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template5((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
		}
	}

	protected void template5(final String user_1, final String user_2, final String action, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user_1);
			Room roomToEvaluate = this.selectRoomPendingWithoutAdmin();
			this.roomService.assignRoom(roomToEvaluate);
			this.unauthenticate();
			
			this.authenticate(user_2);
			if(action == "ACCEPT") {
				this.roomService.acceptRoom(roomToEvaluate);
				Assert.isTrue(roomToEvaluate.getStatus().equals("ACTIVE"));
			} else if(action == "REJECT") {
				this.roomService.rejectRoom(roomToEvaluate);
				Assert.isTrue(roomToEvaluate.getStatus().equals("REJECTED"));
			}
			
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}
	
	
	public Category findCategoryByTitle(String parentEnglishTitle) {
		Collection<Category> categories = this.categoryService.findAll();
		Category result = new Category();
		for(Category c : categories) {
			if(c.getTitle().containsValue(parentEnglishTitle)) {
				result = c;
				break;
			}
		}
		return result;
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
			result = r;
			break;
		}
		return result;
	}
	
	public Room selectRoomPendingWithoutAdmin () {
		Room result = new Room();
		Collection<Room> rooms = this.roomService.findRoomsToAssign();
		
		for(Room r : rooms) {
			result = r;
			break;
		}
		return result;
	}
}
