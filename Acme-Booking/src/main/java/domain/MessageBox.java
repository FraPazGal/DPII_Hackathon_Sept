package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class MessageBox extends DomainEntity {

	// Attributes
	private String name;
	private boolean isPredefined;
	private Collection<Message> messages;
	private Actor owner;

	// Getters and setters

	@NotBlank
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getIsPredefined() {
		return isPredefined;
	}

	public void setIsPredefined(boolean isPredefined) {
		this.isPredefined = isPredefined;
	}

	@Valid
	@ManyToMany
	public Collection<Message> getMessages() {
		return messages;
	}

	public void setMessages(Collection<Message> messages) {
		this.messages = messages;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Actor getOwner() {
		return this.owner;
	}

	public void setOwner(final Actor owner) {
		this.owner = owner;
	}


}

