package ch.heigvd.dto;

import java.util.ArrayList;
import java.util.List;

public class UserDTO {
	private String id;
	private LevelDTO level;
	private LevelDTO nextLevel;
	private int points;
	private List<BadgeDTO> badges = new ArrayList<>();

	public UserDTO() {}

	public UserDTO(String id, LevelDTO level, int points, List<BadgeDTO> badges) {
		this.id = id;
		this.level = level;
		this.points = points;
		this.badges = badges;
	}

	public UserDTO(String id, LevelDTO level, LevelDTO nextLevel, int points, List<BadgeDTO> badges) {
		this.id = id;
		this.level = level;
		this.nextLevel = nextLevel;
		this.points = points;
		this.badges = badges;
	}

	public String getId() {
		return id;
	}

	public LevelDTO getLevel() {
		return level;
	}

	public LevelDTO getNextLevel() {
		return nextLevel;
	}

	public int getPoints() {
		return points;
	}

	public List<BadgeDTO> getBadges() {
		return badges;
	}
}
