package ch.heigvd.models;

import javax.persistence.*;

@Entity
@Table(name = "rules")
public class Rule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private int id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "eventType", nullable = false)
	private String eventType;

	@Column(name = "expr", nullable = false, columnDefinition="TEXT")
	private String expr;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "application_id", nullable = false)
	private Application application;

	public Rule(){

	}

	public Rule(String name, String eventType, String expr, Application application){
		this.name = name;
		this.eventType = eventType;
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

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

	public Application getApplication(){
		return application;
	}
}
