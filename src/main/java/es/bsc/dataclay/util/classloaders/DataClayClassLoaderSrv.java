
package es.bsc.dataclay.util.classloaders;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.exceptions.DataClayClassNotFoundException;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.structs.LruCache;
import es.bsc.dataclay.util.structs.Tuple;

/**
 * This class contains a set of loaded classes identified by its MetaClassID. Used in deserialization.
 */
public final class DataClayClassLoaderSrv {

	/** Map of lockers for classes. */
	public static final ClassLockers LOCKERS = new ClassLockers();

	/** Cache of classes. */
	private static final LruCache<MetaClassID, Class<?>> CLASS_CACHE = new LruCache<>(Configuration.Flags.MAX_ENTRIES_DATASERVICE_CACHE.getIntValue());

	/** Cache of class names and namespace names identified by its MetaClassID. */
	private static final LruCache<MetaClassID, Tuple<String, String>> CLASSINFO_CACHE = new LruCache<>(
			Configuration.Flags.MAX_ENTRIES_DATASERVICE_CACHE.getIntValue());

	/** Cache of class loaders . */
	// CHECKSTYLE:OFF
	public static ClassLoader execEnvironmentClassLoader;
	// CHECKSTYLE:ON
	static {
		loadClassLoader();
	}

	/**
	 * Load classloader
	 */
	private static void loadClassLoader() {
		String execClassesFolder = Configuration.Flags.EXECUTION_CLASSES_PATH.getStringValue();
		final File clDir = new File(execClassesFolder);
		if (!clDir.exists()) {
			clDir.mkdir();
		}
		// TODO: jars folders
		final URL[] urls = new URL[1];
		try {
			urls[0] = clDir.toURI().toURL();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
		execEnvironmentClassLoader = new URLClassLoader(urls);
	}

	/**
	 * Constructor
	 */
	private DataClayClassLoaderSrv() {

	}

	/**
	 * Create an instance of the Class with ID provided
	 * @param classID
	 *            ID of the class to get
	 * @param objectID
	 *            ID of the object to instantiate
	 * @return The created instance
	 */
	public static DataClayObject newInstance(final MetaClassID classID, final ObjectID objectID) {
		return newInstanceInternal(classID, objectID);
	}

	/**
	 * Create an instance of the Class with ID provided
	 * @param classID
	 *            ID of the class to get
	 * @param objectID
	 *            ID of the object to instantiate
	 * @return The created instance
	 */
	private static DataClayObject newInstanceInternal(final MetaClassID classID,
			final ObjectID objectID) {
		final Class<?> clazz = getClass(classID);
		try {
			final Constructor<?> cons = clazz.getConstructor(ObjectID.class);
			cons.setAccessible(true);
			final DataClayObject obj = (DataClayObject) cons.newInstance(objectID);
			return obj;
		} catch (final Exception e) {
			e.printStackTrace();
			throw new DataClayClassNotFoundException(e.getMessage(), e.getCause(), true, true);
		}
	}

	/**
	 * Get the class with name provided
	 * @param fullClassName
	 *            Class name
	 * @return The class
	 * @throws ClassNotFoundException
	 *             if class was not found.
	 */
	public static Class<?> getClass(final String fullClassName) {
		try {
			return execEnvironmentClassLoader.loadClass(fullClassName);
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
			throw new DataClayClassNotFoundException(e.getMessage(), e.getCause(), true, true);
		}
	}

	/**
	 * Get the class with name and namespace provided
	 * @param classID
	 *            ID of the class
	 * @return The class
	 */
	public static Class<?> getClass(final MetaClassID classID) {

		final SyncClass clazz = LOCKERS.getLocker(classID);
		Class<?> resultClass = null;
		clazz.lock();
		try {
			resultClass = CLASS_CACHE.get(classID);
			if (resultClass == null) {
				Tuple<String, String> classNameAndNamespace = CLASSINFO_CACHE.get(classID);
				try {
					if (classNameAndNamespace == null) {
						classNameAndNamespace = DataClayObject.getLib().getLogicModuleAPI().getClassNameAndNamespaceForDS(classID);
						// Update cache
						CLASSINFO_CACHE.put(classID, classNameAndNamespace);
					}

					final String classname = classNameAndNamespace.getFirst();
					final String namespaceToUse = classNameAndNamespace.getSecond();

					resultClass = execEnvironmentClassLoader.loadClass(namespaceToUse + "." + classname);
				} catch (final ClassNotFoundException e) {
					throw new DataClayClassNotFoundException(e.getMessage(), e.getCause(), true, true);
				}
				CLASS_CACHE.put(classID, resultClass);
			}
		} finally {
			clazz.unlock();
		}
		return resultClass;
	}

	/**
	 * Reload a class loader
	 * @param namespace
	 *            Namespace of the class loader
	 */
	public static void reloadClassLoader(final String namespace) {
		loadClassLoader();
		CLASS_CACHE.clear();
		DataClayObject.clearExecStubInfosCache();
	}

	/**
	 * Clean cache of class loaders.
	 */
	public static void cleanCaches() {
		DataClayClassLoaderSrv.CLASSINFO_CACHE.clear();
		DataClayClassLoaderSrv.CLASS_CACHE.clear();
		LOCKERS.clear();

	}

}
