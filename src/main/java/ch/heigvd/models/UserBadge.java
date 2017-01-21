package ch.heigvd.models;

import javax.persistence.*;

@Entity
@Table(name = "users_badges")
@AssociationOverrides({
		@AssociationOverride(name = "pk.user", joinColumns = @JoinColumn(name = "user_id")),
		@AssociationOverride(name = "pk.badge", joinColumns = @JoinColumn(name = "badge_id"))
})
public class UserBadge {
	@EmbeddedId
	private UserBadgeId pk = new UserBadgeId();

	@Column(name = "count", nullable = false)
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

}
