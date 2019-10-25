
package es.bsc.dataclay;

import es.bsc.dataclay.commonruntime.DataClayRuntime;

/** DataClayObject class for mock tests. */
public final class DataClayMockObject {

	// CHECKSTYLE:OFF
	/**
	 * DataClay library to use. We use a map identified by thread in order to distinguish which DSLib to use per thread.
	 */
	private static transient ThreadLocal<DataClayRuntime> commonLibs = new ThreadLocal<>();

	// CHECKSTYLE:ON

	/**
	 * Utility classes should have private constructor.
	 */
	private DataClayMockObject() {

	}

	/**
	 * Add common lib to use by current thread
	 * @param threadID
	 *            Thread id
	 * @param dsLib
	 *            DS lib to use
	 */
	public static void setCurrentThreadLib(final DataClayRuntime dsLib) {
		commonLibs.set(dsLib);
	}

	/**
	 * Remove common lib to use by current thread
	 * @param threadID
	 *            Thread id
	 */
	public static void removeCurrentThreadLib() {
		commonLibs.remove();
	}

	/**
	 * Get DataService client library
	 * @return DataService client library
	 */
	public static DataClayRuntime getLib() {
		final DataClayRuntime clib = commonLibs.get();
		return clib;
	}

}
