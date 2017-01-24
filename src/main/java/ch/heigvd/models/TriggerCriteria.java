package ch.heigvd.models;

import javax.persistence.*;

@Entity
@Table(name = "triggers_criteria")
public class TriggerCriteria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@ManyToOne
	@JoinColumn(name = "triggerId")
	private Trigger trigger;

	@Column(name = "criterion_name", nullable = false)
	private String criterionName;

	public TriggerCriteria(){}
	public TriggerCriteria(Trigger trigger, String criterionName) {
		this.trigger = trigger;
		this.criterionName = criterionName;
	}

	public Trigger getTrigger() {
		return trigger;
	}

	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}

	public String getCriterionName() {
		return criterionName;
	}

	public void setCriterionName(String criterionName) {
		this.criterionName = criterionName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
