
package es.bsc.dataclay.util;

import java.util.HashMap;
import java.util.Map;

/** Class used to read environmental variables. */
public final class ProcessEnvironment extends HashMap<String, String> { 
	
	/** Serial version uid. */
	private static final long serialVersionUID = 7392953750870044361L;
	
	/** Process environment. */
	private static ProcessEnvironment env;
	
	/**
	 * Get main instance
	 * @return Instance
	 */
	public static synchronized ProcessEnvironment getInstance() {
		if (env == null) {
			env = new ProcessEnvironment(System.getenv());
		}
		return env;
	}
 
	/**
	 * Process environment constructor
	 * @param copy From copy
	 */
	private ProcessEnvironment(final Map<String, String> copy) {
		super(copy);
	}
}
