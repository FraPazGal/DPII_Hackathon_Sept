
package forms;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import domain.Actor;
import domain.Message;
import domain.MessageBox;

public class MessageForm {

	//Attributesç
	int								id, version;
	private Date					sentMoment;
	private String					subject;
	private String					body;
	private Actor					receiver;
	private String					priority;
	private Collection<MessageBox>	messageBoxes;
	private Actor					sender;


	public MessageForm() {

	}

	public MessageForm(final Message mes) {
		this.id = mes.getId();
		this.version = mes.getVersion();
		this.sentMoment = mes.getSentMoment();
		this.subject = mes.getSubject();
		this.body = mes.getBody();
		this.receiver = mes.getReceiver();
		this.sender = mes.getSender();
		this.priority = mes.getPriority();
		this.messageBoxes = mes.getMessageBoxes();
	}

	//Getters and Setters

	@Valid
	@NotNull
	public Actor getSender() {
		return this.sender;
	}

	public void setSender(final Actor sender) {
		this.sender = sender;
	}

	@Valid
	@NotNull
	public Collection<MessageBox> getMessageBoxes() {
		return this.messageBoxes;
	}

	public void setMessageBoxes(final Collection<MessageBox> messageBoxes) {
		this.messageBoxes = messageBoxes;
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	@Past
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getSentMoment() {
		return this.sentMoment;
	}

	public void setSentMoment(final Date sentMoment) {
		this.sentMoment = sentMoment;
	}

	@NotBlank
	public String getSubject() {
		return this.subject;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	@NotBlank
	@Type(type = "text")
	public String getBody() {
		return this.body;
	}

	public void setBody(final String body) {
		this.body = body;
	}

	@Valid
	@NotNull
	public Actor getReceiver() {
		return this.receiver;
	}

	public void setReceiver(final Actor receiver) {
		this.receiver = receiver;
	}

	@NotBlank
	@Pattern(regexp = "\\b(HIGH|NEUTRAL|LOW)\\b")
	public String getPriority() {
		return this.priority;
	}

	public void setPriority(final String priority) {
		this.priority = priority;
	}

}
