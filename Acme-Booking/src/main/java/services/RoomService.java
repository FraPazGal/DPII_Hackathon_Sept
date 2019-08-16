package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.RoomRepository;
import domain.Owner;
import domain.Room;

@Transactional
@Service
public class RoomService {

	// Managed repository ------------------------------------

	@Autowired
	private RoomRepository roomRepository;

	// Supporting services -----------------------------------

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
		result.setStatus("IN-REVISION");
		
		return result;
	}
	
	public Room findOne(final int roomId) {
		Room result = this.roomRepository.findOne(roomId);
		Assert.notNull(result, "wrong.id");
		return result;
	}

	public Collection<Room> findAll() {
		return this.roomRepository.findAll();
	}
	
	public Room save (Room room) {
		Assert.notNull(room, "not.allowed");
		Owner principal = (Owner) this.utilityService.findByPrincipal();
		Assert.isTrue(room.getOwner().equals(principal), "not.allowed");
		
		if(room.getId() != 0) {
			Room aux = this.findOne(room.getId());
			Assert.isTrue(!(room.getVisibility() == "OUTOFSERVICE"), "not.allowed");
			
			if(!(room.getVisibility() == "DRAFT")){
				Assert.isTrue(room.getTitle().equals(aux.getTitle()), "not.allowed");
				Assert.isTrue(room.getAddress().equals(aux.getAddress()), "not.allowed");
				Assert.isTrue(room.getPhotos().equals(aux.getPhotos()), "not.allowed");
				Assert.isTrue(room.getDescription().equals(aux.getDescription()), "not.allowed");
				Assert.isTrue(room.getPricePerHour().equals(aux.getPricePerHour()), "not.allowed");
				Assert.isTrue(room.getCategory().equals(aux.getCategory()), "not.allowed");
				Assert.isTrue(room.getCapacity().equals(aux.getCapacity()), "not.allowed");
				Assert.isTrue(room.getAttachments().equals(aux.getAttachments()), "not.allowed");
			}
		}
		
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
	
	public Room reconstruct(final Room room, BindingResult binding) {
		Assert.notNull(room, "not.allowed");
		Owner principal = (Owner) this.utilityService.findByPrincipal();

		Room result = this.create();

		if (room.getId() != 0) {
			Room aux = this.findOne(room.getId());
			Assert.isTrue(room.getOwner().equals(principal), "not.allowed");
			
			Assert.isTrue(!(room.getVisibility() == "OUTOFSERVICE"), "not.allowed");
			
			if((aux.getVisibility() == "DRAFT")) {
				result.setAddress(room.getAddress());
				result.setAttachments(room.getAttachments());
				result.setCapacity(room.getCapacity());
				result.setCategory(room.getCategory());
				result.setDescription(room.getDescription());
				result.setPhotos(room.getPhotos());
				result.setPricePerHour(room.getPricePerHour());
				result.setTitle(room.getTitle());
			} else {
				result.setAddress(aux.getAddress());
				result.setAttachments(aux.getAttachments());
				result.setCapacity(aux.getCapacity());
				result.setCategory(aux.getCategory());
				result.setDescription(aux.getDescription());
				result.setPhotos(aux.getPhotos());
				result.setPricePerHour(aux.getPricePerHour());
				result.setTitle(aux.getTitle());
			}
			result.setId(aux.getId());
			result.setVersion(aux.getVersion());

		} else {
			result.setAddress(room.getAddress());
			result.setAttachments(room.getAttachments());
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
	
	public boolean uniqueTicket(String ticker) {
		return this.roomRepository.uniqueTicket(ticker);
	}
	
	public void assertOwnershipAndEditable (Room room) {
		Assert.notNull(room, "wrong.id");
		Owner principal = (Owner) this.utilityService.findByPrincipal();
		
		Assert.isTrue(room.getOwner().equals(principal), "not.allowed");
		Assert.isTrue(!(room.getVisibility() == "OUTOFSERVICE"), "not.allowed");
	}
	
	public void assertOwnershipAndDraft (Room room) {
		Assert.notNull(room, "wrong.id");
		Owner principal = (Owner) this.utilityService.findByPrincipal();
		
		Assert.isTrue(room.getOwner().equals(principal), "not.allowed");
		Assert.isTrue(!(room.getVisibility() == "DRAFT"), "not.allowed");
	}
	
	public Room findOneToDisplay (int roomId) {
		Room result = this.findOne(roomId);
		Assert.isTrue(!(result.getVisibility() == "OUTOFSERVICE"), "not.allowed");
		Assert.isTrue(!(result.getVisibility() == "DRAFT"), "wrong.id");
		
		return result;
	}
	
	public Collection<Room> findRoomsForBooking () {
		return this.roomRepository.findRoomsForBooking();
	}
	
	public Collection<Room> findRoomsDraftAndMine (int ownerId) {
		return this.roomRepository.findRoomsDraftAndMine(ownerId);
	}
	
	public Collection<Room> findRoomsActiveAndMine (int ownerId) {
		return this.roomRepository.findRoomsActiveAndMine(ownerId);
	}
	
	public Collection<Room> findRoomsOutOfServiceAndMine (int ownerId) {
		return this.roomRepository.findRoomsOutOfServiceAndMine(ownerId);
	}
	
	public void decommision(Room room) {
		Assert.isTrue(!(room.getVisibility() == "OUTOFSERVICE"), "already.decomissioned");
		this.roomRepository.save(room);
	}
	
	public void deleteAsDraft(Room room) {
		Assert.isTrue((room.getVisibility() == "DRAFT"), "not.allowed");
		this.delete(room);
	}
	
}
