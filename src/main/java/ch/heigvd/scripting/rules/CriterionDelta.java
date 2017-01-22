package ch.heigvd.scripting.rules;

import ch.heigvd.models.Criterion;

public class CriterionDelta {
	public static CriterionDelta fromCriterion(Criterion criterion) {
		return new CriterionDelta(criterion, criterion.getValue());
	}

	private final Criterion criterion;
	private final int initialValue;

	private CriterionDelta(Criterion criterion, int initialValue) {
		this.criterion = criterion;
		this.initialValue = initialValue;
	}

	public Criterion getCriterion() {
		return criterion;
	}

	public int getInitialValue() {
		return initialValue;
	}
}
