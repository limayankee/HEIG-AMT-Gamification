package ch.heigvd.dto;

import org.hibernate.validator.constraints.NotEmpty;

public class EventDTO {
	@NotEmpty
	private String type;

	@NotEmpty
	private String userId;

	private Object payload;

	public EventDTO() {}
	public EventDTO(String type, String userId, Object payload) {
		this.type = type;
		this.userId = userId;
		this.payload = payload;
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

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}
}
