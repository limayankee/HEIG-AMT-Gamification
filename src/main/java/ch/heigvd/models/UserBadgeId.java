package ch.heigvd.models;

import javax.persistence.ManyToOne;
import java.io.Serializable;

public class UserBadgeId implements Serializable {
	@ManyToOne
	private User user;
	@ManyToOne
	private Badge badge;

	public UserBadgeId() {}
	public UserBadgeId(User user, Badge badge) {
		this.user = user;
		this.badge = badge;
	}

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
