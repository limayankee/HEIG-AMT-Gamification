package ch.heigvd.dto;

import ch.heigvd.models.Badge;
import ch.heigvd.models.UserBadge;

public class UserBadgeDTO {
	private String name;
	private String image;
	private int count;
	private boolean repeatable;

	public UserBadgeDTO() {}
	public UserBadgeDTO(String name, String image, int count, boolean repeatable) {
		this.name = name;
		this.image = image;
		this.count = count;
		this.repeatable = repeatable;
	}

	public static UserBadgeDTO fromUserBadge(UserBadge ub) {
		Badge b = ub.getPk().getBadge();
		return new UserBadgeDTO(b.getName(), b.getImage(), ub.getCount(), b.isRepeatable());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public boolean isRepeatable() {
		return repeatable;
	}

	public void setRepeatable(boolean repeatable) {
		this.repeatable = repeatable;
	}
}
