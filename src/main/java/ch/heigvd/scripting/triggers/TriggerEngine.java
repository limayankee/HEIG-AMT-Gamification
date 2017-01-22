package ch.heigvd.scripting.triggers;

import ch.heigvd.dao.BadgeRepository;
import ch.heigvd.dao.UserBadgeRepository;
import ch.heigvd.models.Application;
import ch.heigvd.models.Criterion;
import ch.heigvd.models.Trigger;
import ch.heigvd.models.User;
import ch.heigvd.scripting.BadgeAwardingEngine;
import ch.heigvd.scripting.rules.CriterionDelta;
import org.mozilla.javascript.ScriptableObject;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TriggerEngine extends BadgeAwardingEngine {
	private Collection<CriterionDelta> updatedCriteria;

	public TriggerEngine(Application app, User user, Collection<CriterionDelta> updatedCriteria,
	                     BadgeRepository badgeRepository, UserBadgeRepository userBadgeRepository) {
		super(app, user, badgeRepository, userBadgeRepository);
		this.updatedCriteria = updatedCriteria;
	}

	@Override
	protected void defineFunctions() {
		super.defineFunctions();
	}

	@Override
	protected void defineProperties() {
		ScriptableObject criteria = (ScriptableObject) context.newObject(this);
		for (CriterionDelta delta : updatedCriteria) {
			ScriptableObject item = (ScriptableObject) context.newObject(this);
			item.defineProperty("old", delta.getInitialValue(), ScriptableObject.CONST);
			item.defineProperty("value", delta.getCriterion().getValue(), ScriptableObject.CONST);
			item.defineProperty("delta", delta.getDelta(), ScriptableObject.CONST);
			criteria.defineProperty(delta.getCriterion().getName(), item, ScriptableObject.CONST);
		}
		defineProperty("criteria", criteria, ScriptableObject.CONST);
	}

	public void loadCriteriaData(List<Criterion> criteria) {
		Stream<CriterionDelta> additions = criteria.stream().map(CriterionDelta::fromCriterion);
		updatedCriteria = Stream.concat(updatedCriteria.stream(), additions).collect(Collectors.toList());
	}

	public void executeTrigger(Trigger trigger) {
		execute(trigger.getName(), trigger.getExpr());
	}

	public Set<String> updatedCriteriaNames() {
		return updatedCriteria.stream()
		                      .map(CriterionDelta::getCriterion)
		                      .map(Criterion::getName)
		                      .collect(Collectors.toSet());
	}
}
