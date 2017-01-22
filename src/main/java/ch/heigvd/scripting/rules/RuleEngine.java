package ch.heigvd.scripting.rules;

import ch.heigvd.dao.BadgeRepository;
import ch.heigvd.dao.CriterionRepository;
import ch.heigvd.dao.UserBadgeRepository;
import ch.heigvd.dto.EventDTO;
import ch.heigvd.models.*;
import ch.heigvd.scripting.ScriptingEngine;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RuleEngine extends ScriptingEngine {
	private final Application app;
	private final User user;
	private final EventDTO event;
	private final CriterionRepository criterionRepository;
	private final BadgeRepository badgeRepository;
	private final UserBadgeRepository userBadgeRepository;
	private final Map<String, CriterionDelta> updatedCriteria = new TreeMap<>();

	public RuleEngine(Application app, User user, EventDTO event, CriterionRepository criterionRepository,
	                  BadgeRepository badgeRepository, UserBadgeRepository userBadgeRepository) {
		this.app = app;
		this.user = user;
		this.event = event;
		this.criterionRepository = criterionRepository;
		this.badgeRepository = badgeRepository;
		this.userBadgeRepository = userBadgeRepository;
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
		String[] names = new String[]{"reset", "set", "increment", "decrement", "award", "awardMultiple"};
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

	public void reset(String criterion) {
		set(criterion, 0);
	}

	public void set(String name, int value) {
		Criterion criterion = fetchCriterion(name);
		criterion.setValue(value);
		criterionRepository.save(criterion);
	}

	public void increment(String name, int delta) {
		Criterion criterion = fetchCriterion(name);
		criterion.setValue(criterion.getValue() + delta);
		criterionRepository.save(criterion);
	}

	public void decrement(String criterion, int delta) {
		increment(criterion, -delta);
	}

	public void award(String name) {
		awardMultiple(name, 1);
	}

	public void awardMultiple(String name, int count) {
		Badge badge = badgeRepository.findByNameAndApplicationId(name, app.getId());
		UserBadgeId pk = new UserBadgeId(user, badge);
		UserBadge userBadge = userBadgeRepository.findByPk(pk);
		if (userBadge == null) {
			userBadge = new UserBadge();
			userBadge.setPk(pk);
			userBadge.setCount(badge.isRepeatable() ? count : 1);
		} else if (!badge.isRepeatable()) {
			return;
		} else {
			userBadge.setCount(userBadge.getCount() + count);
		}
		userBadgeRepository.save(userBadge);
	}

	public void executeRule(Rule rule) {
		execute(rule.getName(), rule.getExpr());
	}

	public Collection<CriterionDelta> getUpdatedCriteria() {
		return updatedCriteria.values();
	}
}