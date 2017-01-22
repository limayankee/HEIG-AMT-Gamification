package ch.heigvd.dto;

public class EventProcessingResultDTO {
	private ScriptEngineResultDTO rules;
	private ScriptEngineResultDTO triggers;

	public EventProcessingResultDTO(ScriptEngineResultDTO rules, ScriptEngineResultDTO triggers) {
		this.rules = rules;
		this.triggers = triggers;
	}

	public ScriptEngineResultDTO getRules() {
		return rules;
	}

	public void setRules(ScriptEngineResultDTO rules) {
		this.rules = rules;
	}

	public ScriptEngineResultDTO getTriggers() {
		return triggers;
	}

	public void setTriggers(ScriptEngineResultDTO triggers) {
		this.triggers = triggers;
	}
}
