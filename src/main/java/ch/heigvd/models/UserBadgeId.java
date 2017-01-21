package ch.heigvd.models;

import javax.persistence.ManyToOne;
import java.io.Serializable;

public class UserBadgeId implements Serializable {
	@ManyToOne
	private User user;
	@ManyToOne
	private Badge badge;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Badge getBadge() {
		return badge;
	}

	public void setBadge(Badge badge) {
		this.badge = badge;
	}
}
