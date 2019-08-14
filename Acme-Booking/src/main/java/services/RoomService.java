package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;

import repositories.RoomRepository;

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
	private OwnerService ownerService;

	@Autowired
	private Validator validator;

	// CRUD Methods ------------------------------------------

}
