
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MessageRepository;
import domain.Actor;
import domain.Booking;
import domain.Message;
import domain.MessageBox;
import forms.MessageForm;

@Service
@Transactional
public class MessageService {

	// Repository

	@Autowired
	private MessageRepository			messageRepository;

	// Services

	@Autowired
	private UtilityService				utilityService;

	@Autowired
	private ActorService				actorService;

	@Autowired
	private Validator					validator;

	@Autowired
	private MessageBoxService			messageBoxService;

	@Autowired
	private SystemConfigurationService	systemConfigurationService;


	// CRUD Methods -----------------------------------------------------------
	public Message create() {

		final Message result;
		Actor principal;
		final Collection<MessageBox> boxes = new ArrayList<MessageBox>();

		principal = this.utilityService.findByPrincipal();
		Assert.notNull(principal);

		result = new Message();

		result.setSender(principal);
		result.setMessageBoxes(boxes);
		result.setIsSpam(false);
		result.setSentMoment(new Date(System.currentTimeMillis() - 1));

		return result;

	}

	public Message save(final Message message) {
		Message result = null;
		Actor principal;
		Date sentMoment;
		final Collection<MessageBox> messageBoxes = new ArrayList<MessageBox>();
		MessageBox inSpamBox = null;
		MessageBox outBox = null;
		// TODO: Check spam

		principal = this.utilityService.findByPrincipal();
		Assert.notNull(principal);

		Assert.notNull(message);
		Assert.isTrue(message.getId() == 0);

		sentMoment = new Date(System.currentTimeMillis() - 1);

		final List<String> atributosAComprobar = new ArrayList<>();
		atributosAComprobar.add(message.getBody());
		atributosAComprobar.add(message.getSubject());

		final boolean containsSpam = this.utilityService.isSpam(atributosAComprobar);
		if (containsSpam) {
			message.setIsSpam(true);
			principal.setIsSpammer(true);
			inSpamBox = this.messageBoxService.findByName(message.getReceiver().getId(), "Spam box");
		} else
			inSpamBox = this.messageBoxService.findByName(message.getReceiver().getId(), "In box");
		outBox = this.messageBoxService.findByName(principal.getId(), "Out box");
		Assert.notNull(outBox);
		messageBoxes.add(inSpamBox);
		messageBoxes.add(outBox);
		message.setSentMoment(sentMoment);
		message.setMessageBoxes(messageBoxes);
		result = this.messageRepository.save(message);
		outBox.getMessages().add(result);
		inSpamBox.getMessages().add(result);
		return result;
	}
	public void delete(final Message message) {
		Actor principal;
		MessageBox trashBoxPrincipal;
		Collection<Message> messagesTrashBox;

		principal = this.utilityService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(message.getSender().equals(principal) || message.getReceiver().equals(principal));

		Assert.notNull(message);
		Assert.isTrue(message.getId() != 0);

		trashBoxPrincipal = this.messageBoxService.findByName(principal.getId(), "Trash box");
		Assert.notNull(trashBoxPrincipal);

		messagesTrashBox = trashBoxPrincipal.getMessages();
		Assert.notNull(messagesTrashBox);

		if (messagesTrashBox.contains(message)) {
			if (this.findMessage(message))
				messagesTrashBox.remove(message);
			else {
				messagesTrashBox.remove(message);
				this.messageRepository.delete(message);

			}
		} else
			this.move(message, trashBoxPrincipal);

	}

	public Message findOne(final int messageId) {
		Message result;

		result = this.messageRepository.findOne(messageId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Message> findAll() {
		Collection<Message> result;

		result = this.messageRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	// Other business methods --------------------------------------------------

	public boolean findMessage(final Message message) {
		boolean result = false;
		Actor sender;
		Actor recipient;
		Collection<MessageBox> messageBoxSender, messageBoxRecipient;

		messageBoxRecipient = new ArrayList<MessageBox>();
		messageBoxSender = new ArrayList<MessageBox>();

		sender = message.getSender();
		Assert.notNull(sender);

		recipient = message.getReceiver();
		Assert.notNull(recipient);

		messageBoxRecipient = this.messageBoxService.findByOwner(recipient.getId());
		Assert.notNull(messageBoxRecipient);

		messageBoxSender = this.messageBoxService.findByOwner(sender.getId());
		Assert.notNull(messageBoxSender);

		for (final MessageBox mb : messageBoxSender)
			if (!mb.getName().equals("Trash box"))
				if (mb.getMessages().contains(message))
					result = true;

		for (final MessageBox mb : messageBoxRecipient)
			if (!mb.getName().equals("Trash box"))
				if (mb.getMessages().contains(message)) {
					result = true;
					break;
				}
		return result;
	}

	public void move(final Message message, final MessageBox destination) {
		Actor principal;
		MessageBox origin = null;
		Collection<MessageBox> principalBoxes, messageBoxes, recipientBoxes;
		Collection<Message> messages, updatedOriginBox, updatedDestinationBox;
		Actor recipient;

		principalBoxes = new ArrayList<MessageBox>();
		messageBoxes = new ArrayList<MessageBox>();
		messages = new ArrayList<Message>();
		updatedOriginBox = new ArrayList<Message>();
		updatedDestinationBox = new ArrayList<Message>();
		recipientBoxes = new ArrayList<MessageBox>();

		principal = this.utilityService.findByPrincipal();
		Assert.notNull(principal);

		recipient = message.getReceiver();

		Assert.isTrue(message.getId() != 0);
		Assert.isTrue(destination.getId() != 0);

		principalBoxes = this.messageBoxService.findByOwner(principal.getId());
		recipientBoxes = this.messageBoxService.findByOwner(recipient.getId());

		for (final MessageBox mb : principalBoxes)
			messages.addAll(mb.getMessages());

		Assert.isTrue(messages.contains(message));

		for (final MessageBox principalBox : principalBoxes)
			if (principalBox.getMessages().contains(message)) {
				origin = principalBox;
				break;
			}

		for (final MessageBox recipientBox : recipientBoxes)
			if (recipientBox.getMessages().contains(message))
				messageBoxes.add(recipientBox);

		messageBoxes.add(destination);

		Assert.isTrue(principalBoxes.contains(origin));
		Assert.isTrue(principalBoxes.contains(destination));

		message.setMessageBoxes(messageBoxes);

		updatedOriginBox = origin.getMessages();
		updatedDestinationBox = destination.getMessages();

		updatedOriginBox.remove(message);
		updatedDestinationBox.add(message);

		origin.setMessages(updatedOriginBox);
		destination.setMessages(updatedDestinationBox);

	}

	public void broadcast(final Message m) {
		Actor principal;
		String subject, priority, body;
		Date sentMoment;
		boolean isSpam;
		MessageBox outBoxAdmin;
		Collection<MessageBox> allBoxes, boxes, notificationBoxes;
		Message saved;
		Collection<Actor> recipients;

		allBoxes = new ArrayList<MessageBox>();
		notificationBoxes = new ArrayList<MessageBox>();
		boxes = new ArrayList<MessageBox>();
		recipients = new ArrayList<Actor>();

		recipients = this.actorService.findAll();

		principal = this.utilityService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"));

		Assert.notNull(m);

		subject = m.getSubject();
		body = m.getBody();
		priority = m.getPriority();
		isSpam = false;
		sentMoment = new Date(System.currentTimeMillis() - 1);

		for (final Actor a : recipients)
			if (!(this.utilityService.checkAuthority(a, "ADMIN")))
				notificationBoxes.add(this.messageBoxService.findByName(a.getId(), "Notification box"));

		outBoxAdmin = this.messageBoxService.findByName(principal.getId(), "Out box");
		Assert.notNull(outBoxAdmin);

		final Message message = new Message();
		message.setSubject(subject);
		message.setBody(body);
		message.setSentMoment(sentMoment);
		message.setPriority(priority);
		message.setIsSpam(isSpam);
		message.setReceiver(principal);
		message.setSender(principal);

		boxes.add(outBoxAdmin);
		boxes.addAll(notificationBoxes);

		message.setMessageBoxes(boxes);

		saved = this.messageRepository.save(message);

		for (final MessageBox notBox : notificationBoxes)
			notBox.getMessages().add(saved);

		outBoxAdmin.getMessages().add(saved);

	}

	public void deleteMessages(final Message m, final MessageBox mb) {
		final Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.messageBoxService.findByOwner(principal.getId()).contains(mb));
		Assert.isTrue(mb.getMessages().contains(m));
		final MessageBox trash = this.messageBoxService.findByName(principal.getId(), "Trash Box");
		//mb.getMessages().remove(m);
		m.getMessageBoxes().remove(mb);
		trash.getMessages().add(m);
		m.getMessageBoxes().add(trash);

	}

	public Message reconstruct(final MessageForm message, final BindingResult binding) {
		Message result = this.create();
		final Actor principal = this.utilityService.findByPrincipal();
		if (message.getId() == 0) {
			result.setSubject(message.getSubject());
			result.setBody(message.getBody());
			result.setPriority(message.getPriority());
			result.setReceiver(message.getReceiver());
		} else {
			final Message m = this.messageRepository.findOne(message.getId());
			result = m;
			result.setMessageBoxes(message.getMessageBoxes());
			Assert.isTrue(m.getSender().equals(principal) || m.getReceiver().equals(principal));
		}
		this.validator.validate(result, binding);

		return result;
	}

	public Message reconstructBroadcast(final MessageForm message, final BindingResult binding) {

		final Message result = this.create();
		Actor principal;

		principal = this.utilityService.findByPrincipal();

		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"));
		Assert.isTrue(message.getId() == 0);

		result.setSubject(message.getSubject());
		result.setBody(message.getBody());
		result.setPriority(message.getPriority());
		result.setReceiver(principal);

		this.validator.validate(result, binding);

		return result;

	}

	public Message changeStatusNotfication(final Booking booking, final Actor actor, final Date moment) {

		Message result;
		Collection<MessageBox> boxes;
		MessageBox notificationBox, outBox;
		Actor principal;
		Message saved;

		principal = this.utilityService.findByPrincipal();

		Assert.isTrue(this.utilityService.checkAuthority(principal, "OWNER"));
		Assert.isTrue(actor.getId() == booking.getRoom().getOwner().getId());

		boxes = new ArrayList<MessageBox>();

		notificationBox = this.messageBoxService.findByName(actor.getId(), "Notification box");
		Assert.notNull(notificationBox);

		outBox = this.messageBoxService.findByName(principal.getId(), "Out box");
		Assert.notNull(outBox);

		boxes.add(notificationBox);
		boxes.add(outBox);

		result = new Message();
		result.setSender(principal);
		result.setReceiver(actor);
		result.setPriority("NEUTRAL");
		result.setSentMoment(moment);
		result.setIsSpam(false);
		result.setMessageBoxes(boxes);
		String body = "Booking have been changed status to " + booking.getStatus();
		String status = "PENDING";
		switch (booking.getStatus()) {
		case "ACCEPTED":
			status = "ACEPTADO";
			break;
		case "REJECTED":
			status = "RECHAZADO";
			break;
		default:
		}

		body += " || El estado de la reserva a cambiado ha " + status;
		result.setBody(body);
		result.setSubject("Booking status changed||El estado de la reserva ha cambiabo");

		saved = this.messageRepository.save(result);

		outBox.getMessages().add(saved);
		notificationBox.getMessages().add(saved);

		return result;

	}
	public MessageForm createForm() {
		final MessageForm result;
		Actor principal;
		principal = this.utilityService.findByPrincipal();
		Assert.notNull(principal);
		result = new MessageForm();
		result.setSentMoment(new Date(System.currentTimeMillis() - 1));
		result.setId(0);
		return result;
	}

	public Message reconstructM(final MessageForm mensaje) {
		final Message result = this.findOne(mensaje.getId());
		return result;
	}

}
