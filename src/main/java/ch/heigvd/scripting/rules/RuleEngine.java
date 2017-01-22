package ch.heigvd.scripting.rules;

import ch.heigvd.dao.BadgeRepository;
import ch.heigvd.dao.CriterionRepository;
import ch.heigvd.dao.UserBadgeRepository;
import ch.heigvd.dto.EventDTO;
import ch.heigvd.models.Application;
import ch.heigvd.models.Criterion;
import ch.heigvd.models.Rule;
import ch.heigvd.models.User;
import ch.heigvd.scripting.BadgeAwardingEngine;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RuleEngine extends BadgeAwardingEngine {
	private final EventDTO event;
	private final CriterionRepository criterionRepository;
	private final Map<String, CriterionDelta> updatedCriteria = new TreeMap<>();

	public RuleEngine(Application app, User user, EventDTO event, CriterionRepository criterionRepository,
	                  BadgeRepository badgeRepository, UserBadgeRepository userBadgeRepository) {
		super(app, user, badgeRepository, userBadgeRepository);
		this.event = event;
		this.criterionRepository = criterionRepository;
	}

	@Override
	protected void defineProperties() {
		defineProperty("payload", importPayload(event.getPayload()), ScriptableObject.CONST);
	}

	@SuppressWarnings("unchecked")
	private Object importPayload(Object obj) {
		if (obj == null || obj instanceof Boolean || obj instanceof Number || obj instanceof String) {
			return obj;
		} else if (obj instanceof Map) {
			Map<String, Object> map = (Map<String, Object>) obj;
			ScriptableObject dict = (ScriptableObject) context.newObject(this);
			for (String key : map.keySet()) {
				dict.defineProperty(key, importPayload(map.get(key)), ScriptableObject.CONST);
			}
			return dict;
		} else if (obj instanceof List) {
			List<Object> list = (List<Object>) obj;
			Scriptable arr = context.newArray(this, list.size());
			int i = 0;
			for (Object item : list) {
				arr.put(i, arr, importPayload(item));
				i++;
			}
			return arr;
		} else {
			throw new IllegalArgumentException(obj.getClass().getName());
		}
	}

	@Override
	protected void defineFunctions() {
		super.defineFunctions();
		String[] names = new String[]{"reset", "increment", "decrement"};
		defineFunctionProperties(names, RuleEngine.class, ScriptableObject.DONTENUM);
	}

	private Criterion fetchCriterion(String name) {
		if (updatedCriteria.containsKey(name)) {
			return updatedCriteria.get(name).getCriterion();
		} else {
			Criterion criterion = criterionRepository.findByNameAndUserId(name, user.getId());
			if (criterion == null) {
				criterion = new Criterion();
				criterion.setName(name);
				criterion.setUserId(user.getId());
				criterion.setValue(0);
			}
			updatedCriteria.put(name, CriterionDelta.fromCriterion(criterion));
			return criterion;
		}
	}

	public void reset(String name, int value) {
		Criterion criterion = fetchCriterion(name);
		criterion.setValue(value);
		criterionRepository.save(criterion);
		trace(String.format("Reset criterion '%s' to value '%d'", name, value));
	}

	public void increment(String name, int delta) {
		Criterion criterion = fetchCriterion(name);
		criterion.setValue(criterion.getValue() + delta);
		criterionRepository.save(criterion);
		trace(String.format("Updated criterion '%s' to value %d (%s%d)",
				name, criterion.getValue(), delta < 0 ? "-" : "+", Math.abs(delta)));
	}

	public void decrement(String criterion, int delta) {
		increment(criterion, -delta);
	}

	public void executeRule(Rule rule) {
		execute(rule.getName(), rule.getExpr());
	}

	public Collection<CriterionDelta> getUpdatedCriteria() {
		return updatedCriteria.values();
	}
}
