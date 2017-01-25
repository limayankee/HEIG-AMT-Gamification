package ch.heigvd.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "badges")
public class Badge {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private int id;

	@ManyToOne(cascade = CascadeType.ALL)
	private Application application; // to whom application it belongs

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "image", nullable = false)
	private String image;

	@Column(name = "points", nullable = false)
	private int points;

	@Column(name = "repeatable", nullable = false)
	private boolean repeatable;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pk.badge")
	private Set<UserBadge> userBadges = new HashSet<UserBadge>();

	public Badge() {}
	public Badge(String name, String image, int points, boolean repeatable, Application application)
	{
		this.name = name;
		this.image = image;
		this.points = points;
		this.repeatable = repeatable;
		this.application = application;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int count) {
		this.points = count;
	}

	public boolean isRepeatable() {
		return repeatable;
	}

	public void setRepeatable(boolean repeatable) {
		this.repeatable = repeatable;
	}

	public Application getApplication()
	{
		return application;
	}

	public void setApplication(Application application)
	{
		this.application = application;
	}
}
