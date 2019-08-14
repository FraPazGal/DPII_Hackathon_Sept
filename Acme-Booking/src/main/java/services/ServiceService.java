package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import repositories.ServiceRepository;

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

	

}
