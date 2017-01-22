package ch.heigvd.scripting;

import ch.heigvd.dao.BadgeRepository;
import ch.heigvd.dao.UserBadgeRepository;
import ch.heigvd.models.*;
import org.mozilla.javascript.ScriptableObject;

public abstract class BadgeAwardingEngine extends ScriptingEngine {
	protected final Application app;
	protected final User user;
	protected final BadgeRepository badgeRepository;
	protected final UserBadgeRepository userBadgeRepository;

	protected BadgeAwardingEngine(Application app, User user, BadgeRepository badgeRepository, UserBadgeRepository userBadgeRepository) {
		this.app = app;
		this.user = user;
		this.badgeRepository = badgeRepository;
		this.userBadgeRepository = userBadgeRepository;
	}

	protected void defineFunctions() {
		defineFunctionProperties(new String[]{"award"}, BadgeAwardingEngine.class, ScriptableObject.DONTENUM);
	}

	public void award(String name, int count) {
		if (count < 1) {
			trace("Failed to award badge: count = '" + count + "' is < 1.");
			return;
		}
		Badge badge = badgeRepository.findByNameAndApplicationId(name, app.getId());
		if (badge == null) {
			trace("Unable to award badge: '" + name + "' does not exist.");
			return;
		}
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
		trace(String.format("Awarded badge '%s' to user '%s' (x%d), now has %d",
				name, user.getAppUserId(), count, userBadge.getCount()));
	}
}
