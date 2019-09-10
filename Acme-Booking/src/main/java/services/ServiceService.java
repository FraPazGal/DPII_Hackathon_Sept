package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ServiceRepository;
import domain.Owner;

@Transactional
@Service
public class ServiceService {
	
	// Managed repository ------------------------------------

	@Autowired
	private ServiceRepository serviceRepository;
	
	// Supporting services -----------------------------------

	@Autowired
	private UtilityService utilityService;
	
	@Autowired
	private RoomService roomService;

	@Autowired
	private Validator validator;
	
	// CRUD Methods ------------------------------------------

	public domain.Service create() {
		Owner principal = (Owner) this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "OWNER"),"not.allowed");

		return new domain.Service();
	}
	
	public domain.Service findOne(final int serviceId) {
		domain.Service result = this.serviceRepository.findOne(serviceId);
		Assert.notNull(result, "wrong.id");
		return result;
	}

	public Collection<domain.Service> findAll() {
		return this.serviceRepository.findAll();
	}
	
	public domain.Service save (domain.Service service) {
		
		if(service.getId() != 0) {
			this.allowedService(service);
		} else {
			this.roomService.assertOwnership(service.getRoom());
		}
		
		Assert.isTrue(service.getRoom().getStatus().equals("DRAFT") ||
				service.getRoom().getStatus().equals("ACTIVE"), "not.allowed");
		
		domain.Service result = this.serviceRepository.save(service);
		Assert.notNull(result, "commit.error");
		
		return result;
	}
	
	public void delete (domain.Service service) {

		this.allowedService(service);
		this.serviceRepository.delete(service);
	}
	
	
	// Other business methods -------------------------------
	
	public domain.Service reconstruct(final domain.Service service, BindingResult binding) {
		
		domain.Service result = this.create();
		
		if (service.getId() != 0) {
			this.allowedService(service);
			Assert.isTrue(service.getRoom().getStatus().equals("DRAFT") ||
					service.getRoom().getStatus().equals("ACTIVE"), "not.allowed");
			
			domain.Service aux = this.findOne(service.getId());
			
			result.setId(aux.getId());
			result.setVersion(aux.getVersion());
		}  else {
			this.roomService.assertOwnership(service.getRoom());
		}
		
		result.setName(service.getName());
		result.setDescription(service.getDescription());
		result.setPrice(service.getPrice());
		result.setRoom(service.getRoom());

		this.validator.validate(result, binding);

		return result;
	}
	
	/* Check if the room is in draft mode and principal is the owner of said room */
	public void allowedService (domain.Service service) {
		Assert.notNull(service, "not.allowed");
		Owner principal = (Owner) this.utilityService.findByPrincipal();

		Assert.isTrue(service.getRoom().getOwner().equals(principal),"not.allowed");
		Assert.isTrue((service.getRoom().getStatus().contains("DRAFT")),"not.allowed");
	}

	public Collection<domain.Service> findServicesByRoomId (int roomId) {
		return this.serviceRepository.findServicesByRoomId(roomId);
	}
	
	public void deleteServicesOfRoom (Integer roomId, String roomStatus) {
		Collection<domain.Service> toDelete = this.serviceRepository.findServicesByRoomId(roomId);
		if(roomStatus == "DRAFT") {
			for(domain.Service service : toDelete) {
				this.delete(service);
			}
		} else {
			for(domain.Service service : toDelete) {
				this.deleteServiceToDeleteOwner(service);
			}
		}
	}
	
	private void deleteServiceToDeleteOwner (domain.Service service) {
		Assert.notNull(service, "not.allowed");
		Owner principal = (Owner) this.utilityService.findByPrincipal();

		Assert.isTrue(service.getRoom().getOwner().equals(principal),"not.allowed");
		
		this.serviceRepository.delete(service);
	}
	
	public void decommission (domain.Service toDecommission) {
		Assert.notNull(toDecommission.getRoom(), "already.decomissioned");
		this.roomService.assertOwnershipAndStatus(toDecommission.getRoom(), "ACTIVE");
		
		toDecommission.setRoom(null);
		this.serviceRepository.save(toDecommission);
		
	}
}
