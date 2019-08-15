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
		
		this.allowedService(service);
		
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

		this.allowedService(service);
		
		domain.Service result = this.create();
		
		if (service.getId() != 0) {
			domain.Service aux = this.findOne(service.getId());
			
			result.setId(aux.getId());
			result.setVersion(aux.getVersion());
		} 
		
		result.setName(service.getName());
		result.setDescription(service.getDescription());
		result.setPrice(service.getPrice());

		this.validator.validate(result, binding);

		return result;
	}
	
	/* Check if the room is in draft mode and principal is the owner of said room */
	public void allowedService (domain.Service service) {
		Assert.notNull(service, "not.allowed");
		Owner principal = (Owner) this.utilityService.findByPrincipal();

		Assert.isTrue(service.getRoom().getOwner().equals(principal),"not.allowed");
		Assert.isTrue((service.getRoom().getVisibility() == "DRAFT"),"not.allowed");
	}

	public Collection<domain.Service> findServicesByRoomId (int roomId) {
		return this.serviceRepository.findServicesByRoomId(roomId);
	}
}
