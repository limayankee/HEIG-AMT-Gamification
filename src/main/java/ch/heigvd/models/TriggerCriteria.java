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

}
