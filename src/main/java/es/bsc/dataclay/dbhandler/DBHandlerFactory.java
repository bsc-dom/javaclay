
package es.bsc.dataclay.dbhandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteHandler;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteHandlerConfig;

/**
 * Factory to return instances of specific DBHandlers, according to the provided type.
 */
public class DBHandlerFactory {
	private static final String NVRAM_HANDLER_CLASS_NAME = "es.bsc.dataclay.dbhandler.nvram.NVRAMHandler";
	
	/**	All the supported DBHandler subtypes. */
	public enum DBHandlerType {
			POSTGRES,
			SQLITE,
			NVRAM
	}

	/**
	 * Get the handler for a specific database.
	 * 
	 * @param type The desired type for the return DBHandler instance. 
	 * @return A DBHandler instance.
	 */
	public static DBHandler getDBHandler(DBHandlerType type) {
		switch(type){
			case SQLITE:
				return new SQLiteHandler(new SQLiteHandlerConfig("test.db"));
			case NVRAM:
				try {
					final Class<?> nvramHandlerClass = Class.forName(NVRAM_HANDLER_CLASS_NAME);
					if (DBHandler.class.isAssignableFrom(nvramHandlerClass)) {
						final Constructor<?> constructor = nvramHandlerClass.getConstructor();
						return (DBHandler) constructor.newInstance();
					} else {
						throw new IllegalArgumentException("Class NVRAMHandlerClass is not a DBHandler");
					}
				} catch (final ClassNotFoundException ex) {
					throw new IllegalArgumentException("Could not import class for NVRAMHandler, dependencies error");
				} catch (final NoSuchMethodException ex) {
					throw new IllegalArgumentException("Could not call NVRAMHandler constructor");
				} catch (final InstantiationException ex) {
					throw new IllegalArgumentException("Could not instantiate a NVRAMHandler instance");
				} catch (final InvocationTargetException ex) {
					throw new IllegalArgumentException("Could not invocate the NVRAMHandler constructor");
				} catch (final IllegalAccessException ex) {
					throw new IllegalArgumentException("Could not legally access and call the NVRAMHandler constructor");
				}
			default:
				throw new IllegalArgumentException(type + "is not currently a supported DBHandler");
		}
	}
}
