package ch.heigvd.models;

import javax.persistence.*;

@Entity
@Table(name = "criteria")
public class Criterion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private int id;

	@Column(name = "name", nullable = false)
	private String name;

	@ManyToOne
	private User user;

	@ManyToOne
	private Application application;

	@Column(name = "value", nullable = false)
	private int value;

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setApplication(Application application){
		this.application = application;
	}

	public Application getApplication(){
		return application;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
