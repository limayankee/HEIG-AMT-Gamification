package ch.heigvd.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
	@Id
	@Column(name = "id")
	private int id; // absolute id for this api only

	@ManyToOne(cascade = CascadeType.ALL)
	private Application application; // to whom application it belongs

	@Column(name = "appUserId")
	private String applicationUserId; // userId in application

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = Badge.class)
	@JoinTable(name = "users_badges", joinColumns = {
			@JoinColumn(name = "userId", nullable = false, updatable = false)
	}, inverseJoinColumns = {
			@JoinColumn(name = "badgeId", nullable = false, updatable = false)
	})
	private Set<Badge> badges = new HashSet<Badge>();

	public Set<Badge> getBadges() {
		return badges;
	}

	public void setBadges(Set<Badge> badges) {
		this.badges = badges;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getApplicationUserId() {
		return applicationUserId;
	}

	public void setApplicationUserId(String applicationUserId) {
		this.applicationUserId = applicationUserId;
	}

}
