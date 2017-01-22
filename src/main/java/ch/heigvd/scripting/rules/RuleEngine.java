package ch.heigvd.scripting.rules;

import ch.heigvd.dao.CriterionRepository;
import ch.heigvd.dto.EventDTO;
import ch.heigvd.models.Criterion;
import ch.heigvd.models.Rule;
import ch.heigvd.models.User;
import ch.heigvd.scripting.ScriptingEngine;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.*;

public class RuleEngine extends ScriptingEngine {
	private final User user;
	private final EventDTO event;
	private final CriterionRepository criterionRepository;
	private final Set<String> updatedCriteria = new TreeSet<>();

	public RuleEngine(User user, EventDTO event, CriterionRepository criterionRepository) {
		this.user = user;
		this.event = event;
		this.criterionRepository = criterionRepository;
	}

	@Override
	protected void defineProperties() {
		defineProperty("payload", importPayload(event.getPayload()), ScriptableObject.CONST);
	}

	@SuppressWarnings("unchecked")
	private Object importPayload(Object obj) {
		if (obj instanceof Boolean || obj instanceof Number || obj instanceof String) {
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
		String[] names = new String[]{"reset", "set", "increment", "decrement", "award", "awardMultiple"};
		defineFunctionProperties(names, RuleEngine.class, ScriptableObject.DONTENUM);
	}

	private Criterion fetchCriterion(String name) {
		Criterion criterion = criterionRepository.findByNameAndUserId(name, user.getId());
		if (criterion == null) {
			criterion = new Criterion();
			criterion.setName(name);
			criterion.setUserId(user.getId());
			criterion.setValue(0);
		}
		return criterion;
	}

	public void reset(String criterion) {
		set(criterion, 0);
	}

	public void set(String name, int value) {
		Criterion criterion = fetchCriterion(name);
		criterion.setValue(value);
		criterionRepository.save(criterion);
		updatedCriteria.add(name);
	}

	public void increment(String name, int delta) {
		Criterion criterion = fetchCriterion(name);
		criterion.setValue(criterion.getValue() + delta);
		criterionRepository.save(criterion);
		updatedCriteria.add(name);
	}

	public void decrement(String criterion, int delta) {
		increment(criterion, -delta);
	}

	public void award(String badge) {
		awardMultiple(badge, 1);
	}

	public void awardMultiple(String badge, int count) {
		System.out.format("Awarding %s x %d\n", badge, count);
	}

	public void executeRule(Rule rule) {
		execute(rule.getName(), rule.getExpr());
	}

	public Set<String> getUpdatedCriteria() {
		return updatedCriteria;
	}
}
