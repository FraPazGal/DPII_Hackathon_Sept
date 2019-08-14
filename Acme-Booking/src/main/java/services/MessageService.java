package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;

import repositories.MessageRepository;

@Service
@Transactional
public class MessageService {

	// Repository

	@Autowired
	private MessageRepository messageRepository;

	// Services

	@Autowired
	private ActorService actorService;

	@Autowired
	private Validator validator;

	@Autowired
	private MessageBoxService messageBoxService;

	@Autowired
	private SystemConfigurationService systemConfigurationService;

	// CRUD METHODS

}