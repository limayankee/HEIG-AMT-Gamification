package ch.heigvd.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users_badges")
@AssociationOverrides({
		@AssociationOverride(name = "pk.user", joinColumns = @JoinColumn(name = "user_id")),
		@AssociationOverride(name = "pk.badge", joinColumns = @JoinColumn(name = "badge_id"))
})
public class UserBadge {
	@EmbeddedId
	private UserBadgeId pk = new UserBadgeId();

	@Column(name = "count")
	private int count;

	public UserBadgeId getPk() {
		return pk;
	}

	public void setPk(UserBadgeId pk) {
		this.pk = pk;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}


	class UserBadgeId implements Serializable {
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
}
