package ch.heigvd.dto;

import ch.heigvd.models.Trigger;
import ch.heigvd.models.TriggerCriteria;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TriggerDTO {
	@NotEmpty
	private String name;

	@NotEmpty
	private String expr;

	public List<String> getCriteria() {
		return criteria;
	}

	public void setCriteria(List<String> criteria) {
		this.criteria = criteria;
	}

	List<String> criteria = new ArrayList<>();

	public TriggerDTO(){}

	public TriggerDTO(String name, String expr, List<String> criteria){
		this.name = name;
		this.expr = expr;
		this.criteria = criteria;
	}

	public static TriggerDTO fromTrigger(Trigger trigger){
		List<String> criteria = trigger.getTriggerCriteria()
		                               .stream()
		                               .map(TriggerCriteria::getCriterionName)
		                               .distinct()
		                               .collect(Collectors.toList());
		return new TriggerDTO(trigger.getName(), trigger.getExpr(), criteria);
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
}
