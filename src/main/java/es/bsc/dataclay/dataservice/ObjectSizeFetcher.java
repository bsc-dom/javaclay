
package es.bsc.dataclay.dataservice;

import java.lang.instrument.Instrumentation;

/**
 * Agent to calculate size of object in Java memory.
 *
 */
public final class ObjectSizeFetcher {

	/** Instrumentation class. */
	private static Instrumentation instrumentation;

	/**
	 * Utility classes should have private constructor.
	 */
	private ObjectSizeFetcher() {

	}

	/**
	 * Agent Main
	 * @param agentArgs
	 *            Arguments
	 * @param inst
	 *            Instrumentation object
	 */
	public static void agentmain(final String agentArgs, final Instrumentation inst) {
		instrumentation = inst;
	}

	/**
	 * Get object size
	 * @param o
	 *            Object
	 * @return Size of o
	 */
	public static long getObjectSize(final Object o) {
		return instrumentation.getObjectSize(o);
	}
}
