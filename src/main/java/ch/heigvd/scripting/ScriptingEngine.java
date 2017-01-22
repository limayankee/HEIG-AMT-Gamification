package ch.heigvd.scripting;

import ch.heigvd.dto.ScriptEngineResultDTO;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.LinkedList;
import java.util.List;

public abstract class ScriptingEngine extends ScriptableObject implements AutoCloseable {
	private static Scriptable rootScope = null;

	private static Scriptable getRootScope() {
		if (rootScope == null) {
			Context ctx = Context.enter();
			rootScope = ctx.initSafeStandardObjects(null, true);
			Context.exit();
		}
		return rootScope;
	}

	protected final Context context = Context.enter();
	private boolean initialized = false;
	private List<String> scripts = new LinkedList<>();
	private List<String> errors = new LinkedList<>();
	private String output = "";

	private void lazyInitialize() {
		if (initialized) throw new IllegalStateException();
		setPrototype(getRootScope());
		setParentScope(null);
		defineFunctionProperties(new String[]{"print"}, ScriptingEngine.class, ScriptableObject.DONTENUM);
		defineFunctions();
		defineProperties();
		sealObject();
		initialized = true;
	}

	protected abstract void defineFunctions();

	protected abstract void defineProperties();

	public void execute(String name, String script) {
		if (!initialized) lazyInitialize();
		scripts.add(name);
		Scriptable scope = context.newObject(this);
		scope.setPrototype(this);
		scope.setParentScope(null);
		context.evaluateString(scope, script, name, 1, null);
	}

	public void print(String text) {
		output += text + "\n";
	}

	public void error(String text) {
		errors.add(text);
	}

	public ScriptEngineResultDTO getResult() {
		return new ScriptEngineResultDTO(scripts, errors, output);
	}

	@Override
	public void close() throws Exception {
		Context.exit();
	}

	@Override
	public String getClassName() {
		return getClass().getName();
	}
}
