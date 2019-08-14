package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;

import repositories.MessageBoxRepository;

@Service
@Transactional
public class MessageBoxService {

	// Repository

	@Autowired
	private MessageBoxRepository messageBoxRepository;

	// Services

	@Autowired
	private ActorService actorService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private Validator validator;

	// CRUD METHODS

 
}
