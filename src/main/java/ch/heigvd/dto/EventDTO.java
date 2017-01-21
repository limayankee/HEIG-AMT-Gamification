package ch.heigvd.dto;

import org.hibernate.validator.constraints.NotEmpty;

public class EventDTO {
	@NotEmpty
	private String type;

	@NotEmpty
	private String userId;

	public EventDTO() {}
	public EventDTO(String type, String userId) {
		this.type = type;
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
