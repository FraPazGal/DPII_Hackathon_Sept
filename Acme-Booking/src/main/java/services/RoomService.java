package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.RoomRepository;
import domain.Administrator;
import domain.Owner;
import domain.Room;
import forms.ActiveRoomForm;

@Transactional
@Service
public class RoomService {

	// Managed repository ------------------------------------

	@Autowired
	private RoomRepository roomRepository;

	// Supporting services -----------------------------------

	@Autowired
	private ServiceService serviceService;
	
	@Autowired
	private UtilityService utilityService;

	@Autowired
	private Validator validator;

	// CRUD Methods ------------------------------------------

	public Room create() {
		Room result;
		
		Owner principal = (Owner) this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "OWNER"),"not.allowed");

		result = new Room();
		result.setTicker(this.utilityService.generateTicker(principal));
		result.setOwner(principal);
		result.setStatus("DRAFT");
		
		return result;
	}
	
	public Room findOne(final int roomId) {
		Room result = this.roomRepository.findOne(roomId);
		Assert.notNull(result, "wrong.room.id");
		return result;
	}

	public Collection<Room> findAll() {
		return this.roomRepository.findAll();
	}
	
	public Room save (Room room) {
		Assert.notNull(room, "not.allowed");
		this.assertOwnershipAndEditable(room);
		
		Room result = this.roomRepository.save(room);
		Assert.notNull(result, "commit.error");
		
		return result;
	}
	
	public void delete (Room room) {
		Assert.notNull(room, "not.allowed");
		Owner principal = (Owner) this.utilityService.findByPrincipal();
		Assert.isTrue(room.getOwner().equals(principal), "not.allowed");
		
		this.roomRepository.delete(room);
	}
	
	
	// Other business methods -------------------------------
	
	public Room reconstructDraft(final Room room, BindingResult binding) {
		Assert.notNull(room, "not.allowed");

		Room result = this.create();

		if (room.getId() != 0) {
			Room aux = this.findOne(room.getId());
			
			this.assertOwnershipAndStatus(aux, "DRAFT");
			
			result.setAddress(room.getAddress());
			result.setProveOfOwnership(room.getProveOfOwnership());
			result.setCapacity(room.getCapacity());
			result.setCategory(room.getCategory());
			result.setDescription(room.getDescription());
			result.setPhotos(room.getPhotos());
			result.setPricePerHour(room.getPricePerHour());
			result.setTitle(room.getTitle());

			result.setId(aux.getId());
			result.setVersion(aux.getVersion());
			result.setTicker(aux.getTicker());
			result.setStatus(aux.getStatus());

		} else {
			result.setAddress(room.getAddress());
			result.setProveOfOwnership(room.getProveOfOwnership());
			result.setCapacity(room.getCapacity());
			result.setCategory(room.getCategory());
			result.setDescription(room.getDescription());
			result.setPhotos(room.getPhotos());
			result.setPricePerHour(room.getPricePerHour());
			result.setTitle(room.getTitle());
		}
		
		result.setScheduleDetails(room.getScheduleDetails());
		result.setOpeningHour(room.getOpeningHour());
		result.setClosingHour(room.getClosingHour());

		this.validator.validate(result, binding);

		return result;
	}
	
	public Room reconstructActive(final ActiveRoomForm form, BindingResult binding) {
		Assert.isTrue(form.getId() != 0 , "not.allowed");

		Room result = this.create();
		Room aux = this.findOne(form.getId());
		
		this.assertOwnershipAndStatus(aux, "ACTIVE");
		
		result.setId(aux.getId());
		result.setVersion(aux.getVersion());
		result.setAddress(aux.getAddress());
		result.setProveOfOwnership(aux.getProveOfOwnership());
		result.setCapacity(aux.getCapacity());
		result.setCategory(aux.getCategory());
		result.setDescription(aux.getDescription());
		result.setPhotos(aux.getPhotos());
		result.setPricePerHour(aux.getPricePerHour());
		result.setTitle(aux.getTitle());
		result.setAdministrator(aux.getAdministrator());
		result.setTicker(aux.getTicker());
		result.setStatus(aux.getStatus());
		
		result.setScheduleDetails(form.getScheduleDetails());
		result.setOpeningHour(form.getOpeningHour());
		result.setClosingHour(form.getClosingHour());

		this.validator.validate(result, binding);

		return result;
	}
	
	public boolean uniqueTicket(String ticker) {
		return this.roomRepository.uniqueTicket(ticker);
	}
	
	public Room findOneToDisplay (int roomId) {
		Room result = this.findOne(roomId);
		Assert.notNull(result, "wrong.room.id");
		
		Assert.isTrue(result.getStatus().contains("ACTIVE"), "wrong.status");
		
		return result;
	}
	
	public Collection<Room> findRoomsForBooking () {
		return this.roomRepository.findRoomsForBooking();
	}
	
	public Collection<Room> findRoomsDraftAndMine (int ownerId) {
		return this.roomRepository.findRoomsDraftAndMine(ownerId);
	}
	
	public Collection<Room> findRoomsRevisionPendingAndMine (int actorId) {
		return this.roomRepository.findRoomsRevisionPendingAndMine(actorId);
	}
	
	public Collection<Room> findRoomsActiveAndMine (int actorId) {
		return this.roomRepository.findRoomsActiveAndMine(actorId);
	}
	
	public Collection<Room> findRoomsRejectedAndMine (int actorId) {
		return this.roomRepository.findRoomsRejectedAndMine(actorId);
	}
	
	public Collection<Room> findRoomsOutOfServiceAndMine (int ownerId) {
		return this.roomRepository.findRoomsOutOfServiceAndMine(ownerId);
	}
	
	public Collection<Room> findRoomsToAssign () {
		return this.roomRepository.findRoomsToAssign();
	}
	
	public Room saveAsFinal (Room room) {
		Assert.notNull(room, "not.allowed");
		this.assertOwnershipAndStatus(room, "REVISION-PENDING");
		
		Room result = this.roomRepository.save(room);
		Assert.notNull(result, "commit.error");
		
		return result;
	}
	
	public void acceptRoom(Room room) {
		
		this.assertAdminAndEditable(room);
		this.changeStatus(room, "ACTIVE");
	}
	
	public void rejectRoom(Room room) {
		
		this.assertAdminAndEditable(room);
		this.changeStatus(room, "REJECTED");
	}
	
	public void decommision(Room room) {
		
		this.assertOwnershipAndStatus(room, "ACTIVE");
		this.changeStatus(room, "OUT-OF-SERVICE");
	}
	
	public void deleteAsDraft(Room room) {
		
		this.assertOwnershipAndStatus(room, "DRAFT");
		this.serviceService.deleteServicesOfRoom(room.getId());
		this.delete(room);
	}
	
	public void assignRoom(Room room) {
		Assert.notNull(room, "wrong.room.id");
		
		Assert.isTrue(room.getStatus().contains("REVISION-PENDING"), "wrong.status");
		Assert.isTrue((room.getAdministrator() == null), "already.assigned");
		
		Administrator principal = (Administrator) this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"),"not.allowed");
		
		room.setAdministrator(principal);
		this.roomRepository.save(room);
	}
	
	public void assertOwnershipAndEditable (Room room) {
		Assert.notNull(room, "wrong.room.id");
		Owner principal = (Owner) this.utilityService.findByPrincipal();
		
		Assert.isTrue(room.getOwner().equals(principal), "not.allowed");
		Assert.isTrue(room.getStatus().contains("DRAFT") || room.getStatus().contains("ACTIVE"), "wrong.status");
	}
	
	public void assertOwnershipAndStatus (Room room, String status) {
		Assert.notNull(room, "wrong.room.id");
		Owner principal = (Owner) this.utilityService.findByPrincipal();
		
		Assert.isTrue(room.getOwner().equals(principal), "not.allowed");
		Assert.isTrue(room.getStatus().contains(status), "wrong.status");
	}
	
	public void assertAdminAndEditable (Room room) {
		Assert.notNull(room, "wrong.room.id");
		Administrator principal = (Administrator) this.utilityService.findByPrincipal();
		
		Assert.isTrue(room.getAdministrator().equals(principal), "not.allowed");
		Assert.isTrue(room.getStatus().contains("REVISION-PENDING"), "wrong.status");
	}
	
	private void changeStatus(Room room, String newStatus) {
		Assert.isTrue((newStatus == "DRAFT" || newStatus == "REVISION-PENDING" || newStatus == "ACTIVE" ||
				newStatus == "REJECTED" || newStatus == "OUT-OF-SERVICE"), "invalid.status");
		
		room.setStatus(newStatus);
		this.roomRepository.save(room);
	}
}
