package ch.heigvd.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "criteria")
public class Criterion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private int id;

	@Column(name = "userId", nullable = false)
	private int userId;

	@Column(name = "value", nullable = false)
	private int value;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "triggers_criteria", joinColumns = {
			@JoinColumn(name = "triggerId", updatable = false)
	}, inverseJoinColumns = {
			@JoinColumn(name = "criterionId", updatable = false)
	})
	private Set<Trigger> triggers;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Set<Trigger> getTriggers() {
		return triggers;
	}

	public void setTriggers(Set<Trigger> triggers) {
		this.triggers = triggers;
	}
}
