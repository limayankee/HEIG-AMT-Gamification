package ch.heigvd.scripting;

import ch.heigvd.dto.ScriptEngineResultDTO;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public abstract class ScriptingEngine extends ScriptableObject implements AutoCloseable {
	private static Scriptable rootScope = null;
	private Stack<String> currentScriptName = new Stack<>();

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
	private List<String> traces = new LinkedList<>();

	public ScriptingEngine() {
		currentScriptName.push("Unknown");
	}

	private void lazyInitialize() {
		if (initialized) throw new IllegalStateException();
		setPrototype(getRootScope());
		setParentScope(null);
		defineFunctionProperties(new String[]{"trace"}, ScriptingEngine.class, ScriptableObject.DONTENUM);
		defineFunctions();
		defineProperties();
		sealObject();
		initialized = true;
	}

	protected abstract void defineFunctions();

	protected abstract void defineProperties();

	public String getCurrentScriptName() {
		return currentScriptName.peek();
	}

	public void execute(String name, String script) {
		if (!initialized) lazyInitialize();
		currentScriptName.push(name);
		try {
			scripts.add(name);
			Scriptable scope = context.newObject(this);
			scope.setPrototype(this);
			scope.setParentScope(null);
			try {
				context.evaluateString(scope, script, name, 1, null);
			} catch (EvaluatorException ee) {
				trace("Evaluation of script failed: " + ee.getMessage());
			}
		} finally {
			currentScriptName.pop();
		}
	}

	public void trace(String text) {
		traces.add(String.format("[%s] - %s", getCurrentScriptName(), text));
	}

	public ScriptEngineResultDTO getResult() {
		return new ScriptEngineResultDTO(scripts, traces);
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
