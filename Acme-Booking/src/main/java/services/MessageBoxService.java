
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MessageBoxRepository;
import domain.Actor;
import domain.Message;
import domain.MessageBox;

@Service
@Transactional
public class MessageBoxService {

	// Repository

	@Autowired
	private MessageBoxRepository	messageBoxRepository;

	// Services

	@Autowired
	private UtilityService			utilityService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private Validator				validator;


	public MessageBox create(final Actor actor) {
		final MessageBox b = new MessageBox();
		b.setOwner(actor);
		b.setIsPredefined(false);
		b.setMessages(new ArrayList<Message>());
		return b;
	}

	public void initializeDefaultBoxes(final Actor a) {

		final MessageBox in = this.create(a);
		in.setIsPredefined(true);
		in.setName("In box");
		this.save(in);

		final MessageBox trash = this.create(a);
		trash.setIsPredefined(true);
		trash.setName("Trash box");
		this.save(trash);

		final MessageBox out = this.create(a);
		out.setIsPredefined(true);
		out.setName("Out box");
		this.save(out);

		final MessageBox spam = this.create(a);
		spam.setIsPredefined(true);
		spam.setName("Spam box");
		this.save(spam);

		final MessageBox notification = this.create(a);
		notification.setIsPredefined(true);
		notification.setName("Notification box");
		this.save(notification);

	}

	public Collection<MessageBox> findByOwnerFirst() {
		final Actor actor = this.utilityService.findByPrincipal();
		return this.messageBoxRepository.firstBoxesByActor(actor.getId());
	}

	public MessageBox findOne(final int id) {
		return this.messageBoxRepository.findOne(id);
	}

	public MessageBox findByName(final int actorId, final String name) {
		return this.messageBoxRepository.boxByName(actorId, name);
	}

	public Collection<MessageBox> findByParent(final int idBox) {
		final Collection<MessageBox> boxes = this.messageBoxRepository.findByParent(idBox);
		if (!boxes.isEmpty()) {
			final List<MessageBox> childs = new ArrayList<>(boxes);
			for (int i = 0; i < childs.size(); i++)
				boxes.addAll(this.findByParent(childs.get(i).getId()));
		}
		return boxes;
	}

	public MessageBox findOneOwner(final int id) {
		final Actor actorLogged = this.utilityService.findByPrincipal();
		final MessageBox box = this.findOne(id);
		Assert.notNull(box);
		Assert.isTrue(box.getOwner().equals(actorLogged));
		return box;
	}

	public MessageBox insertMessage(MessageBox box, final Message message) {
		// We insert a new message to the box
		box = this.messageBoxRepository.findOne(box.getId());
		final Collection<Message> messages = box.getMessages();
		if (!messages.contains(message)) {
			messages.add(message);
			box.setMessages(messages);
		}
		final MessageBox saved = this.messageBoxRepository.save(box);
		return saved;
	}

	public Collection<MessageBox> findByOwner(final int id) {
		return this.messageBoxRepository.boxesByActor(id);
	}

	public void delete(final MessageBox messageBox) {
		final Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue((principal.equals(messageBox.getOwner())), "Box not belong to the logged actor");
		Assert.isTrue(!(messageBox.getIsPredefined()), "Box undeleteable");
		final Collection<MessageBox> childBoxes = this.messageBoxRepository.findByParent(messageBox.getId());
		if (!childBoxes.isEmpty())
			for (final MessageBox b : childBoxes)
				this.delete(b);
		if (messageBox.getMessages() != null)
			for (final Message m : messageBox.getMessages())
				this.messageService.deleteMessages(m, messageBox);
		this.messageBoxRepository.delete(messageBox);

	}

	public MessageBox reconstruct(final MessageBox messageBox, final BindingResult binding) {
		final Actor actor = this.utilityService.findByPrincipal();
		final MessageBox result = this.create(actor);
		if (messageBox.getId() != 0) {
			final MessageBox bd = this.messageBoxRepository.findOne(messageBox.getId());
			Assert.notNull(bd);

			result.setIsPredefined(bd.getIsPredefined());
			result.setId(bd.getId());
		}
		result.setName(messageBox.getName());
		result.setParentMessageBoxes(messageBox.getParentMessageBoxes());
		this.validator.validate(result, binding);

		if (!binding.hasErrors()) {
			boolean bool = true;
			final MessageBox mb = this.findByName(actor.getId(), messageBox.getName());
			if (mb != null && mb.getId() != messageBox.getId())
				bool = false;
			try {
				Assert.isTrue(bool);
			} catch (final Throwable oops) {
				binding.rejectValue("name", "name.error");
			}
		}
		return result;
	}

	public MessageBox save(final MessageBox box) {
		// We search boxes from logged actor
		MessageBox saved;

		if (box.getId() != 0) {
			final Actor actor = this.utilityService.findByPrincipal();
			final MessageBox boxBD = this.findOne(box.getId());
			Assert.isTrue(actor.equals(box.getOwner()) && actor.equals(boxBD.getOwner()));
			final Collection<MessageBox> boxes = this.findByOwnerFirst();
			if (box.getParentMessageBoxes() != null)
				Assert.isTrue(boxes.contains(box.getParentMessageBoxes()));
			Assert.isTrue(boxBD.getIsPredefined() == false);
			boxBD.setName(box.getName());
			Assert.isTrue(this.checkParentBox(boxBD, box));
			boxBD.setParentMessageBoxes(box.getParentMessageBoxes());
			boxBD.setMessages(box.getMessages());
			saved = this.messageBoxRepository.save(boxBD);
		} else
			saved = this.messageBoxRepository.save(box);
		return saved;
	}

	public boolean checkParentBox(final MessageBox boxBD, final MessageBox box) {
		boolean bool = true;
		final Collection<MessageBox> boxes = this.messageBoxRepository.findByParent(boxBD.getId());
		if (box.getParentMessageBoxes() != null)
			if (boxes.contains(box.getParentMessageBoxes()))
				bool = false;
		return bool;

	}
}
