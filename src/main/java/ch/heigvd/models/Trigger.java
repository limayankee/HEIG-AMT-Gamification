package ch.heigvd.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "triggers")
public class Trigger {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private int id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "expr", nullable = false, columnDefinition="TEXT")
	private String expr;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "application_id", nullable = false)
	private Application application;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "trigger")
	private Set<TriggerCriteria> triggerCriterias = new HashSet<>();

	public Trigger() {}
	public Trigger(String name, String expr, Application application) {
		this.name = name;
		this.expr = expr;
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

	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public Set<TriggerCriteria> getTriggerCriterias() {
		return triggerCriterias;
	}

	public void setTriggerCriterias(Set<TriggerCriteria> triggerCriterias) {
		this.triggerCriterias = triggerCriterias;
	}
}
