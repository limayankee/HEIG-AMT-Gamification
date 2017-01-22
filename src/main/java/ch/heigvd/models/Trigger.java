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

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "triggers_criteria", joinColumns = {
			@JoinColumn(name = "triggerId", updatable = false)
	}, inverseJoinColumns = {
			@JoinColumn(name = "criterionId", updatable = false)
	})
	private Set<Criterion> criteria = new HashSet<Criterion>();

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


	public Set<Criterion> getCriteria() {
		return criteria;
	}

	public void setCriteria(Set<Criterion> criteria) {
		this.criteria = criteria;
	}
}
