package ch.heigvd.dto;

import java.util.List;

public class ScriptEngineResultDTO {
	private List<String> scripts;
	private List<String> errors;
	private String output;

	public ScriptEngineResultDTO(List<String> scripts, List<String> errors, String output) {
		this.output = output;
		this.errors = errors;
		this.scripts = scripts;
	}

	public List<String> getScripts() {
		return scripts;
	}

	public void setScripts(List<String> scripts) {
		this.scripts = scripts;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}
}
