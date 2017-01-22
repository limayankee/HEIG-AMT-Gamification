package ch.heigvd.dto;

import java.util.List;

public class ScriptEngineResultDTO {
	private List<String> scripts;
	private List<String> traces;

	public ScriptEngineResultDTO(List<String> scripts, List<String> traces) {
		this.scripts = scripts;
		this.traces = traces;
	}

	public List<String> getScripts() {
		return scripts;
	}

	public void setScripts(List<String> scripts) {
		this.scripts = scripts;
	}

	public List<String> getTraces() {
		return traces;
	}

	public void setTraces(List<String> traces) {
		this.traces = traces;
	}
}
