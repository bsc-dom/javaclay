
package es.bsc.dataclay.serialization.java;

import es.bsc.dataclay.serialization.DataClaySerializable;

/**
 * Interface class for Java objects wrappers in DataClay.
 */
public abstract class DataClayJavaWrapper implements DataClaySerializable {

	/**
	 * Constructor.
	 */
	public DataClayJavaWrapper() {

	}

	/**
	 * Get the Java object from the wrapper
	 * @return the Java object from the wrapper
	 */
	public abstract Object getJavaObject();

	/**
	 * Indicates if object is immutable or not
	 * @return TRUE if immutable, FALSE otherwise.
	 */
	public abstract boolean isImmutable();

	/**
	 * Indicates if wrapped object is null.
	 * @return TRUE if null.
	 */
	public abstract boolean isNull();

}
