package forms;

import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;

import domain.Room;

public class ActiveRoomForm {

	/* Attributes */

	private int id, version;
	private String openingHour, closingHour, scheduleDetails;

	/* Getters and setters */

	public ActiveRoomForm() {

	}

	public ActiveRoomForm(Room room) {
		this.id = room.getId();
		this.version = room.getVersion();
		this.openingHour = room.getOpeningHour();
		this.closingHour = room.getClosingHour();
		this.scheduleDetails = room.getScheduleDetails();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@NotBlank
	@Pattern(regexp = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")
	public String getOpeningHour() {
		return openingHour;
	}

	public void setOpeningHour(String openingHour) {
		this.openingHour = openingHour;
	}

	@NotBlank
	@Pattern(regexp = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")
	public String getClosingHour() {
		return closingHour;
	}

	public void setClosingHour(String closingHour) {
		this.closingHour = closingHour;
	}
	
	@NotBlank
	@Type(type="text")
	public String getScheduleDetails() {
		return scheduleDetails;
	}

	public void setScheduleDetails(String scheduleDetails) {
		this.scheduleDetails = scheduleDetails;
	}

}
