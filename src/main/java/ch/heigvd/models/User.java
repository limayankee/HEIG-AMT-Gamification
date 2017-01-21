package ch.heigvd.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id; // absolute id for this api only

	@ManyToOne(cascade = CascadeType.ALL)
	private Application application; // to whom application it belongs

	@Column(name = "appUserId")
	private String appUserId; // userId in application

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

	public User() {}

	public User(Application application, String appUserId) {
		this.application = application;
		this.appUserId = appUserId;
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

	public String getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(String appUserId) {
		this.appUserId = appUserId;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}
}
