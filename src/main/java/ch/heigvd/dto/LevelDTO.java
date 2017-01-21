package ch.heigvd.dto;

public class LevelDTO {
	private String name;
	private int threshold;

	public LevelDTO() {}

	public LevelDTO(String name, int threshold) {
		this.name = name;
		this.threshold = threshold;
	}

	public String getName() {
		return name;
	}

	public int getThreshold() {
		return threshold;
	}
}
