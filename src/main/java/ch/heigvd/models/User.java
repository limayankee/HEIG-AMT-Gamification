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

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pk.user")
	private Set<UserBadge> userBadges = new HashSet<UserBadge>();


	public User() {}

	public User(Application application, String appUserId) {
		this.application = application;
		this.appUserId = appUserId;
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

	public Set<UserBadge> getUserBadges() {
		return userBadges;
	}

	public void setUserBadges(Set<UserBadge> userBadges) {
		this.userBadges = userBadges;
	}
}
