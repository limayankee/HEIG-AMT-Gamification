package ch.heigvd.dto;

public class EventDTO {
	private String type;
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
