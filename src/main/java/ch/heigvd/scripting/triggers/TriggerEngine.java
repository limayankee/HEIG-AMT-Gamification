package ch.heigvd.scripting.triggers;

import ch.heigvd.dao.BadgeRepository;
import ch.heigvd.dao.UserBadgeRepository;
import ch.heigvd.models.Application;
import ch.heigvd.models.Criterion;
import ch.heigvd.models.User;
import ch.heigvd.scripting.BadgeAwardingEngine;
import ch.heigvd.scripting.rules.CriterionDelta;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class TriggerEngine extends BadgeAwardingEngine {
	private final Collection<CriterionDelta> updatedCriteria;

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

	}

	public Set<String> updatedCriteriaNames() {
		return updatedCriteria.stream()
		                      .map(CriterionDelta::getCriterion)
		                      .map(Criterion::getName)
		                      .collect(Collectors.toSet());
	}
}
