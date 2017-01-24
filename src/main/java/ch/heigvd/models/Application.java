package ch.heigvd.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "applications")
public class Application {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "password")
	private String password;

	@Column(name = "name", unique = true)
	private String name;

	@Column(name = "enabled")
	private Boolean enabled;

	@Column(name = "role", nullable = false)
	private String role;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "application")
	private Set<Badge> badges;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "application")
	private Set<Level> levels;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "application")
	private Set<Rule> rules;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "application")
	private Set<Trigger> triggers;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "application")
	private Set<User> users;


	public Application() {
	}

	public Application(String name, String password) {
		this.name = name;
		this.password = password;
		this.enabled = true;
		this.role = "APPLICATION";
	}

    public int getId(){
        return id;
    }

    public boolean isEnabled(){
        return enabled;
    }

    public String getName(){
        return name;
    }

    public String getRole(){
        return role;
    }

}
