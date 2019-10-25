package es.bsc.dataclay.commonruntime;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.dataservice.api.DataServiceAPI;
import es.bsc.dataclay.logic.api.LogicModuleAPI;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.SessionID;

/**
 * Utility class for client library.
 */
public final class RuntimeUtils {

	/**
	 * Utility classes should have private constructor.
	 */
	private RuntimeUtils() {

	}

	/**
	 * Check if some parameter is null and throw exception if so.
	 * 
	 * @param paramNames
	 *            Names of parameters
	 * @param params
	 *            Parameters to check.
	 */
	public static void checkNullParams(final String[] paramNames, final Object[] params) {
		int i = 0;
		for (final Object param : params) {
			if (param == null) {
				throw new IllegalArgumentException(paramNames[i] + " is null");
			}
			i++;
		}
	}

	/**
	 * Check if connection to LogicModule is OK
	 * 
	 * @param logicModule
	 *            API to connect to
	 */
	public static void checkConnection(final LogicModuleAPI logicModule) {
		if (logicModule == null) {
			throw new RuntimeException("Client is not able to connect to DataClay LogicModule");
		}
	}

	/**
	 * Check if connection to DataService is OK
	 * 
	 * @param dataservice
	 *            DataService API to connect to
	 */
	public static void checkConnection(final DataServiceAPI dataservice) {
		if (dataservice == null) {
			throw new RuntimeException("Client is not able to connect to DataClay DataService");
		}
	}

	/**
	 * Check if session ID is OK
	 * 
	 * @param sessionID
	 *            ID of session
	 */
	public static void checkSession(final SessionID sessionID) {
		if (!Configuration.Flags.CHECK_SESSION.getBooleanValue()) {
			return;
		}
		if (sessionID == null) {
			throw new RuntimeException("Session ID not found, please verify you started the session.");
		}
	}

	/**
	 * Get new reference
	 * 
	 * @param dcObject
	 *            DataClayObject to get reference from.
	 * @return Reference.
	 */
	public static Reference<DataClayObject> newReference(final DataClayObject dcObject) {
		final Reference<DataClayObject> newRef = new WeakReference<>(dcObject);
		return newRef;
	}

}
