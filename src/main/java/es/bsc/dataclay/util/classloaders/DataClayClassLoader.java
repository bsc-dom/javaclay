
package es.bsc.dataclay.util.classloaders;

import java.lang.reflect.Constructor;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.structs.MemoryCache;

/**
 * This class contains a set of loaded classes identified by its MetaClassID. Used in deserialization.
 */
public class DataClayClassLoader {

	/** Cache of class names identified by its MetaClassID. */
	public static final MemoryCache<MetaClassID, String> CLASSNAMES_CACHE = new MemoryCache<>();

	/** Map of lockers for classes. */
	private static final ClassLockers LOCKERS = new ClassLockers();

	/**
	 * Constructor
	 */
	protected DataClayClassLoader() {

	}

	/**
	 * Create an instance of the Class with ID provided and Object ID provided
	 * @param classID
	 *            ID of the class to get
	 * @param oid
	 *            OID
	 * @return The created instance
	 */
	public static DataClayObject newInstance(final MetaClassID classID, final ObjectID oid) {
		return newInstanceInternal(classID, oid);
	}

	/**
	 * Create an instance of the Class with ID provided
	 * @param classID
	 *            ID of the class to get
	 * @param oid
	 *            OID of the object
	 * @param state
	 *            State of the object at creation time
	 * @return The created instance
	 */
	private static DataClayObject newInstanceInternal(final MetaClassID classID, final ObjectID oid) {

		final SyncClass clazzLock = LOCKERS.getLocker(classID);
		clazzLock.lock();
		String className = CLASSNAMES_CACHE.get(classID);
		if (className == null) {
			className = DataClayObject.getLib().getLogicModuleAPI().getClassNameForDS(classID);
			// Update cache
			CLASSNAMES_CACHE.put(classID, className);
		}
		LOCKERS.tryRemoveLocker(clazzLock);
		clazzLock.unlock();
		DataClayObject obj = null;
		try {
			final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			final Class<?> clazz = classLoader.loadClass(className);
			final Constructor<?> cons = clazz.getConstructor(ObjectID.class);
			cons.setAccessible(true);
			obj = (DataClayObject) cons.newInstance(oid);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}

		return obj;
	}

	/**
	 * Get the Class with name provided
	 * @param className
	 *            Name of the class to get
	 * @return The class
	 */
	public static Class<?> getClass(final String className) {
		final Class<?> clazz;
		try {

			final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			clazz = classLoader.loadClass(className);
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return clazz;
	}

	/**
	 * Get the Class with ID provided
	 * @param classID
	 *            ID of the class to get
	 * @return The class
	 */
	public static Class<?> getClass(final MetaClassID classID) {
		final SyncClass clazzLock = LOCKERS.getLocker(classID);
		clazzLock.lock();
		String className = CLASSNAMES_CACHE.get(classID);
		if (className == null) {
			className = DataClayObject.getLib().getLogicModuleAPI().getClassNameForDS(classID);
			// Update cache
			CLASSNAMES_CACHE.put(classID, className);
		}
		final Class<?> clazz = getClass(className);
		LOCKERS.tryRemoveLocker(clazzLock);
		clazzLock.unlock();
		return clazz;
	}

}
