
package es.bsc.dataclay.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.dbhandler.DBHandlerFactory.DBHandlerType;

/**
 * Configurations.
 */
public final class Configuration {
	private static final Logger LOGGER = LogManager.getLogger("util.Configuration");

	// CHECKSTYLE:OFF
	/** Defines the file where global properties can be defined. */
	public static final String GLOBALPROPS_PATH = System.getProperty("user.dir") + File.separatorChar + "cfgfiles"
			+ File.separatorChar + "global.properties";

	/** Environment variable where configuration file can be specified. */
	public static final String GLOBALPROPS_ENV = "DATACLAYGLOBALCONFIG";

	/** Indicates if we are doing a mock test. */
	public static boolean mockTesting = false;

	/** Indicates if we are doing a mock test using COMPSs. */
	public static boolean mockTestingCompss = false;

	/** Format for date parsing. Can be used also in Yaml (see MockSimple). */
	public static final String DATE_FORMAT_STR = "yyyy-MM-dd'T'HH:mm:ss";

	/** Format for date parsing. */
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STR);

	/**
	 * Session expiration date. Static function needed for parsing.
	 * 
	 * @return Session expiration date.
	 */
	private static Date getSessionExpirationDate() {
		try {
			return DATE_FORMAT.parse("2120-09-10T20:00:04");
		} catch (final ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	// CHECKSTYLE:ON

	/** ConfigurationTestFlags properties file. */
	private static Properties testFlagsProps = null;

	/**
	 * Get the properties' flags in a string.
	 * @return A string, empty if there are no flags.
	 */
	public static String getFlagsInString() {
		if (testFlagsProps != null) {
			return testFlagsProps.toString();
		}
		return "";
	}

	/**
	 * Utility classes should have a private constructor.
	 */
	private Configuration() {

	}

	// CHECKSTYLE:OFF
	/**
	 * Type of configuration values.
	 */
	public enum ConfType {
		INTEGER, SHORT, LONG, FLOAT, BOOLEAN, STRING, DBHANDLER, DATE
	}

	/**
	 * Return whether the DEBUG level in the logging is enabled. 
	 * @return True if Log4J has debug enables.
	 */
	public static boolean isDebugEnabled() {
		return LogManager.getRootLogger().isDebugEnabled();
	}

	// CHECKSTYLE:ON

	/**
	 * Configurations enum.
	 */
	public enum Flags {

		// =============== BASIC ============ //

		/** Indicates whether to check session (dataset access rights, etc) or not. */
		CHECK_SESSION(false, ConfType.BOOLEAN),
		/** Indicates whether to check namespace owner or not. */
		CHECK_NAMESPACE(false, ConfType.BOOLEAN),
		/** Expiration date for sessions when CHECK_SESSION=FALSE. */
		EXPIRATION_DATE_IF_NOCHECK_SESSION(Configuration.getSessionExpirationDate(), ConfType.DATE),

		/** Indicates if DataClay Classes must be registered or not. */
		REGISTER_DATACLAY_CLASSES(false, ConfType.BOOLEAN),
		/** Indicates notification manager is active or not. */
		NOTIFICATION_MANAGER_ACTIVE(false, ConfType.BOOLEAN),
		/** Indicates whether to enable memory gc or not. */
		MEMORY_GC_ENABLED(true, ConfType.BOOLEAN),
		/** Indicates whether to enable global gc or not. */
		GLOBAL_GC_ENABLED(true, ConfType.BOOLEAN),
		
		/**
		 * Indicates whether to check if object is read-only to accept replication or
		 * not.
		 */
		CHECK_READ_ONLY(false, ConfType.BOOLEAN),

		/** State file for healthcheck. */
		STATE_FILE_PATH("state.txt", ConfType.STRING),

		// =============== PREFETCHING_FLAGS ========= //

		/** Indicates if prefetching is enabled. */
		PREFETCHING_ENABLED(false, ConfType.BOOLEAN),
		/** Indicates if parallel prefetching should be used */
		PREFETCHING_PARALLEL_ENABLED(false, ConfType.BOOLEAN),
		/** Indicates if ROP prefetching should be used */
		PREFETCHING_ROP_ENABLED(false, ConfType.BOOLEAN),
		/** Indicates fetch depth of ROP prefetching (ignored if ROP is disabled) */
		PREFETCHING_ROP_DEPTH(1, ConfType.INTEGER),

		// =============== GRPC ========= //

		/**
		 * GRPC default is 4 MiB, dataClay default is unlimited while we do not have a
		 * message chunking mechanism
		 */
		MAX_MESSAGE_SIZE(Integer.MAX_VALUE, ConfType.INTEGER),

		/** Path to Trusted certificates for verifying the remote endpoint's certificate. */
		SSL_CLIENT_TRUSTED_CERTIFICATES(null, ConfType.STRING),

		/** Path to identifying certificate for this host. */
		SSL_CLIENT_CERTIFICATE(null, ConfType.STRING),

		/** Path to identifying certificate for this host. */
		SSL_CLIENT_KEY(null, ConfType.STRING),

		/** Custom header of service alias for calls to Logic module. Used in Traefik. **/
		SSL_TARGET_LM_ALIAS("11034", ConfType.STRING),

		/** Custom header of service alias for calls to DS. Used in Traefik. **/
		SSL_TARGET_DS_ALIAS("2127", ConfType.STRING),

		/** Override authority hostname in SSL calls. */
		SSL_TARGET_AUTHORITY("proxy", ConfType.STRING),

		/** Indicates if netty servers and clients should use fork join pool instead of pool executor. */
		GRPC_USE_FORK_JOIN_POOL(true, ConfType.BOOLEAN),

		/** Indicates to use EPoll thread poll if possible. */
		GRPC_USER_EPOLL_THREAD_POOL(false, ConfType.BOOLEAN),

		/** Indicates numer of threads in boss and worker loop groups in Netty. */
		GRPC_BOSS_NUM_THREADS(-1, ConfType.INTEGER),
		GRPC_WORKER_NUM_THREADS(-1, ConfType.INTEGER),

		/** Indicates SQLite should be executed in memory. */
		SQLITE_IN_MEMORY(false, ConfType.BOOLEAN),

		// ============== MISC ============== //

		/** Default value for DbHandler type for LOCAL data services. */
		DB_HANDLER_TYPE_FOR_DATASERVICES(DBHandlerType.SQLITE, ConfType.DBHANDLER),
		/** Default value for DbHandler type for LOCAL logic module. */
		DB_HANDLER_TYPE_FOR_LOGICMODULE(DBHandlerType.SQLITE, ConfType.DBHANDLER),

		/** Default read-write. */
		READONLY_BY_DEFAULT(false, ConfType.BOOLEAN),

		// ============== DB ============== //

		/** Maximum retries to connect to the database. */
		MAX_RETRY_DATABASE_CONN(30, ConfType.SHORT),
		/** Millis to wait to retry connection to the database. */
		RETRY_DATABASE_CONN_TIME(2000L, ConfType.LONG),

		/**
		 * Indicates if we ignore replication from StorageItf. In this case, hint will
		 * be the only location of an object.
		 */
		STORAGEITF_IGNORE_REPLICATION(true, ConfType.BOOLEAN),

		/** Experimental use of NIO streaming for array serialization. */
		MULTIDIM_ARRAY_STREAM_SERIALIZATION(false, ConfType.BOOLEAN),

		// =============== CACHING ============== //

		/** Defines the default load factor of the LRU caches. */
		LRU_LOAD_FACTOR(0.75f, ConfType.FLOAT),
		/** Defines the timeunit used for the LRU cache that uses expiration dates. */
		LRU_BYDATE_TIMEUNIT(TimeUnit.DAYS, null),
		/** Defines the value for the timeunit. */
		LRU_BYDATE_TIMEUNIT_VALUE(1, ConfType.INTEGER),
		/** Default value for Maximum entries in Data Service cache. */
		MAX_ENTRIES_DATASERVICE_CACHE(10000, ConfType.INTEGER),
		/** Default value for Maximum entries in Client cache. */
		MAX_ENTRIES_CLIENT_CACHE(10000, ConfType.INTEGER),
		/** Default value for Maximum entries in MetaDataService cache. */
		MAX_ENTRIES_METADATASERVICE_CACHE(10000, ConfType.INTEGER),
		/** Default value for Maximum entries in ClassManager cache. */
		MAX_ENTRIES_CLASS_MANAGER_CACHE(10000, ConfType.INTEGER),
		/** Default value for Maximum entries in AccountManager cache. */
		MAX_ENTRIES_ACCOUNT_MANAGER_CACHE(100, ConfType.INTEGER),
		/** Default value for Maximum entries in NamespaceManager cache. */
		MAX_ENTRIES_NAMESPACE_MANAGER_CACHE(100, ConfType.INTEGER),
		/** Default value for Maximum entries in ContractManager cache. */
		MAX_ENTRIES_CONTRACT_MANAGER_CACHE(100, ConfType.INTEGER),
		/** Default value for Maximum entries in DataSetManager cache. */
		MAX_ENTRIES_DATASET_MANAGER_CACHE(100, ConfType.INTEGER),
		/** Default value for Maximum entries in DataContractManager cache. */
		MAX_ENTRIES_DATACONTRACT_MANAGER_CACHE(100, ConfType.INTEGER),
		/** Default value for Maximum entries in InterfaceManager cache. */
		MAX_ENTRIES_INTERFACE_MANAGER_CACHE(100, ConfType.INTEGER),
		/** Default value for Maximum entries in SessionManager cache. */
		MAX_ENTRIES_SESSION_MANAGER_CACHE(100, ConfType.INTEGER),

		// ===== NOTIFICATION MANAGER ===== //

		/** Default value for Maximum entries in NotificationManager Event queue. */
		MAX_ENTRIES_NOTIFICATION_MANAGER_MSG_QUEUE(1000, ConfType.INTEGER),
		/** Default value for Maximum entries in NotificationManager session cache. */
		MAX_ENTRIES_NOTIFICATION_MANAGER_SESSION_CACHE(100, ConfType.INTEGER),
		/**
		 * Default value for time that should pass to consider a message to be old. In
		 * millis.
		 */
		MAX_TIME_OLD_EVENT_MSGS(6000L, ConfType.LONG),
		/** Defines the period to check event message queue at Notification Manager. */
		NOTIFICATION_MANAGER_INTERVAL(1000L, ConfType.LONG),

		// ===================== RETRIES AND TIME OUTS ===================== //


		// NEW DATA-SERVICE OR EXECUTION ENVIRONMENT: WARNING: for each retry there is max_retries_logicmodule
		/** Maximum autoregistration retries of a node. */
		MAX_RETRY_AUTOREGISTER(10, ConfType.SHORT), 
		/** Millis to wait to retry autoregistration of a node. */
		RETRY_AUTOREGISTER_TIME(3000L, ConfType.LONG),

		// ANY CALL TO LOGICMODULE 
		/** Default value for number of retries in connection to LogicModule. 
		 * NOTE: Clients can wait at init waitForBackends*/
		MAX_RETRIES_LOGICMODULE(600, ConfType.SHORT),
		/** Default value for sleeping before retrying in LM in millis. */
		SLEEP_RETRIES_LOGICMODULE(1000, ConfType.SHORT),

		// EXECUTION RETRIES

		/**
		 * Default value for number of retries IN EXECUTION
		 */
		MAX_EXECUTION_RETRIES(5, ConfType.SHORT),

		// WAIT FOR OBJECTS TO BE REGISTERED BY GC
		/** Number of millis of time to wait for object to be registered. Default: NO WAIT */
		TIMEOUT_WAIT_REGISTERED(0L, ConfType.LONG),
		/** Waiting milliseconds to check if object to be registered. */
		SLEEP_WAIT_REGISTERED(50L, ConfType.LONG),

		/** Waiting milliseconds to check if backends where shutted down. */
		SLEEP_WAIT_SHUTDOWN(200L, ConfType.LONG),
		
		// ============== MEMORY MANAGEMENT =================== //

		/** Minimum life time for an object to be flushed. */
		MEMMGMT_MIN_OBJECT_TIME(500L, ConfType.LONG),
		/** Fraction of memory to consider it is under pressure. */
		MEMMGMT_PRESSURE_FRACTION(0.7f, ConfType.FLOAT),
		/**
		 * Interval to check if objects may be flushed (depending on memory pressure).
		 */
		MEMMGMT_CHECK_TIME_INTERVAL(5000L, ConfType.LONG),
		/** Waiting millis to other MEMMGT threads to finish during shutdown process. */
		MEMMGMT_WAIT_TO_SHUTDOWN(200L, ConfType.LONG),

		// ============== GLOBAL GC ===================== //
		/** Interval to check and collect objects in disk. (IN MILLIS) */
		GLOBALGC_COLLECT_TIME_INTERVAL(24 * 60 * 60 * 1000L, ConfType.LONG),
		/** Interval to check and send remote reference countings. (IN MILLIS) */
		GLOBALGC_CHECK_REMOTE_PENDING(12000L, ConfType.LONG),
		/**
		 * Interval to process pending reference counters. The idea is not to use same
		 * collection interval to avoid retaining too much memory: one thing is the map
		 * of counters (which is not released till collection or sending references to
		 * other nodes) and other thing is the pending reference countings to process
		 * (which are maps of oid -> num refs per each object). (IN MILLIS)
		 */
		GLOBALGC_PROCESS_COUNTINGS_INTERVAL(8000L, ConfType.LONG),

		/**
		 * Maximum time an object can be in quarantine. WARNING: this time must ensure
		 * no race condition of wrong 0 references happen.
		 */
		GLOBALGC_MAX_TIME_QUARANTINE(16000L, ConfType.LONG),

		/** Waiting millis to other GC threads to finish during shutdown process. */
		GLOBALGC_WAIT_TO_SHUTDOWN(30000L, ConfType.LONG),

		/**
		 * Maximum number of objects to collect during an iteration, this allow us to avoid
		 * infinite cleaning thread.
		 */
		GLOBALGC_MAX_OBJECTS_TO_COLLECT_ITERATION(1000, ConfType.INTEGER),

		/**
		 * Initial delay for collector thread to start working in hours
		 */
		GLOBALGC_COLLECTOR_INITIAL_DELAY_HOURS(12L, ConfType.LONG),

		// ============= LAZY TASKS ==================== //

		/** Interval to check if tasks must be run. */
		PREFETCHING_TASKS_INTERVAL(500L, ConfType.LONG),

		// ============== DEPLOYMENT ============== //

		/** Defines the file where aspectlib is located. */
		ASPECTLIBPATH(
				System.getProperty("user.dir") + File.separatorChar + "lib" + File.separatorChar + "aspectjrt.jar",
				ConfType.STRING),
		/** Default value for path in which aspects are stored. */
		ASPECTS_PATH(System.getProperty("user.dir") + File.separatorChar + "execAspects", ConfType.STRING),
		/** Aspects Home dir. */
		ASPECTS_HOME("", ConfType.STRING),
		/** DB storage path. */
		STORAGE_PATH(File.separatorChar + "dataclay" + File.separatorChar + "storage", ConfType.STRING),
		/** Default path in which classes to install are stored. */
		DATACLAY_INSTALLED_CLASSES_SRC_PATH(
				System.getProperty("user.dir") + File.separatorChar + "install_classes" + File.separatorChar + "src",
				ConfType.STRING),
		/** Default path in which classes to install are compiled. */
		DATACLAY_INSTALLED_CLASSES_BIN_PATH(
				System.getProperty("user.dir") + File.separatorChar + "install_classes" + File.separatorChar + "bin",
				ConfType.STRING),
		/** Default value for path in which execution classes are stored. */
		EXECUTION_CLASSES_PATH(System.getProperty("user.dir") + File.separatorChar + "execClasses/", ConfType.STRING),
		/** Defines the file where dependencies is located. */
		INCLUDE_THIS_PROJECT(System.getProperty("user.dir") + File.separatorChar + "target/classes", ConfType.STRING),

		/** Indicates path to extrae wrapper library. */
		JAVACLAY_EXTRAE_WRAPPER_LIB(ProcessEnvironment.getInstance().get("JAVACLAY_EXTRAE_WRAPPER_LIB"), ConfType.STRING),
		
		/** Indicates if finish extrae must be called. */ 
		FINISH_EXTRAE_TRACING(true, ConfType.BOOLEAN),
		
		// =========== DEBUG ================ //

		/** Whether AspectJ info should be printed. */
		PRINT_ASPECTS_INFO(false, ConfType.BOOLEAN),
		/** Print bytes message. */
		PRETTY_PRINT_MESSAGES(false, ConfType.BOOLEAN),
		/** Add debug information on methods. */
		ADD_DEBUG_INFO_ON_METHODS(false, ConfType.BOOLEAN);

		// ============= END OF FLAGS ================== //

		/** Indicates if configuration was loaded. */
		private boolean loaded = false;

		/** Configuration value. */
		private Object value = null;

		/** Indicates type of configuration. */
		private ConfType valueType = null;

		/**
		 * Constructor with default value
		 * 
		 * @param defaultValue
		 *            Default value
		 * @param defaultValType
		 *            Default value type
		 */
		Flags(final Object defaultValue, final ConfType defaultValType) {
			value = defaultValue;
			valueType = defaultValType;
		}

		/**
		 * Initialize the testFlagsProps variable
		 * 
		 * This static method will typically be called either from the non-static
		 * init() or from the initializeGlobalProperties() --which may seem a little bit
		 * chicken-egg given that this is also calling the initializeGlobalProperties.
		 * 
		 * But certain dataClay initializations (e.g. PyCOMPSs runtime) will end up here,
		 * not through the init(), so this method is needed for those scenarios.
		 */
		private static void initTestFlagsProps() {
			testFlagsProps = new Properties();
			File globalFile = null;
			String globalPropsPath = ProcessEnvironment.getInstance().get(Configuration.GLOBALPROPS_ENV);
			boolean fileExists = false;
			if (globalPropsPath != null && !globalPropsPath.isEmpty()) {
				globalFile = new File(globalPropsPath);
				fileExists = globalFile.isFile() && globalFile.exists();
			}

			if (fileExists) {
				final Path path = Paths.get(globalPropsPath).normalize();
				globalPropsPath = path.toAbsolutePath().toString();
				globalFile = new File(globalPropsPath);

			} else {
				final Path path = Paths.get(GLOBALPROPS_PATH).normalize();
				globalPropsPath = path.toAbsolutePath().toString();
				globalFile = new File(globalPropsPath);
			}

			initializeGlobalProperties(globalFile);
		}

		/**
		 * @throws Exception
		 *             if some exception occurs Init properties.
		 */
		private void init() throws Exception {
			if (testFlagsProps == null) {
				initTestFlagsProps();
			}
			if (valueType == null) {
				if (value == null) {
					throw new Exception("Missing default value for configuration without Value type");
				}
			} else {
				String strVal = testFlagsProps.getProperty(this.name());
				if (strVal != null) {
					LOGGER.info("Found environment variable set in global.properties: {}={}", this.name(), strVal);
				} else {
					strVal = ProcessEnvironment.getInstance().get(this.name());
					if (strVal != null) {
						LOGGER.info("Found environment variable defined: {}={}", this.name(), strVal);
					}
				}
				
				if (strVal != null) {
					switch (valueType) {
					case BOOLEAN:
						value = Boolean.valueOf(strVal);
						break;
					case FLOAT:
						value = Float.valueOf(strVal);
						break;
					case INTEGER:
						value = Integer.valueOf(strVal);
						break;
					case SHORT:
						value = Short.valueOf(strVal);
						break;
					case LONG:
						value = Long.valueOf(strVal);
						break;
					case STRING:
						value = strVal;
						break;
					case DBHANDLER:
						if (strVal.equals("POSTGRES")) {
							value = DBHandlerType.POSTGRES;
						} else if (strVal.equals("SQLITE")) {
							value = DBHandlerType.SQLITE;
						} else if (strVal.equals("NVRAM")) {
							value = DBHandlerType.NVRAM;
						}
						break;
					case DATE:
						value = DATE_FORMAT.parse(strVal);
						break;
					default:
						throw new Exception("Wrong value Type: " + valueType);
					}
				}
			}

			loaded = true;
		}

		/**
		 * Reload global properties
		 * @param globalConfig Global properties file path
		 */
		public static void reloadGlobalConfiguration(final String globalConfig) {
			final String globalPropsPath = globalConfig;
			final File globalFile = new File(globalConfig);
			final boolean fileExists = globalFile.isFile() && globalFile.exists();
			if (!fileExists) {
				LOGGER.debug("Cannot find global properties with properties located at {}",
						globalPropsPath);
				return;
			} 
			initializeGlobalProperties(globalFile);
			for (final Configuration.Flags flag : Configuration.Flags.values()) { 
				// unload it
				flag.loaded = false;
			}
		}

		/**
		 * Read global properties file
		 * @param globalFile global properties file
		 */
		public static void initializeGlobalProperties(final File globalFile) {
			FileInputStream input = null;

			if (testFlagsProps == null) {
				// testFlagsProps will be already initialized if init() is the one calling this,
				// but maybe the code comes from another path (i.e. reloadGlobalProperties) and thus
				// this check is required.
				initTestFlagsProps();
			}

			try {

				input = new FileInputStream(globalFile);

				// load a properties file
				testFlagsProps.load(input);

				LOGGER.info("Using global.properties file found at {}", globalFile.getAbsolutePath());

			} catch (final IOException ex) {
				// No global properties file, exit initialization
				LOGGER.warn("No global.properties file found at {}. Using default values",
						globalFile.getAbsolutePath());
				return;
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (final IOException e) {
						LOGGER.warn("No global.properties file found at {}. Using default values",
								globalFile.getAbsolutePath());
						return;
					}
				}
			}
		}

		/**
		 * Get the Configurations::value
		 * 
		 * @return the value
		 */
		public Object getValue() {
			if (!loaded) {
				try {
					init();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
			return value;
		}

		/**
		 * Get Date value of the Configurations::value
		 * 
		 * @return the value
		 */
		public Date getDate() {
			if (!loaded) {
				try {
					init();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
			return (Date) value;
		}

		/**
		 * Get int value of the Configurations::value
		 * 
		 * @return the value
		 */
		public short getShortValue() {
			if (!loaded) {
				try {
					init();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
			if (value instanceof Short) { 
				return (Short) value;
			} else {
				return ((Integer) value).shortValue();
			}
		}

		/**
		 * Get int value of the Configurations::value
		 * 
		 * @return the value
		 */
		public int getIntValue() {
			if (!loaded) {
				try {
					init();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
			return (Integer) value;
		}

		/**
		 * Get long value of the Configurations::value
		 * 
		 * @return the value
		 */
		public long getLongValue() {
			if (!loaded) {
				try {
					init();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
			if (value instanceof Double) {
				return ((Double) value).longValue();
			}
			return (Long) value;
		}

		/**
		 * Get boolean value of the Configurations::value
		 * 
		 * @return the value
		 */
		public boolean getBooleanValue() {
			if (!loaded) {
				try {
					init();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
			return (Boolean) value;
		}

		/**
		 * Get String value of the Configurations::value
		 * 
		 * @return the value
		 */
		public String getStringValue() {
			if (!loaded) {
				try {
					init();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
			return (String) value;
		}

		/**
		 * Get Float value of the Configurations::value
		 * 
		 * @return the value
		 */
		public float getFloatValue() {
			if (!loaded) {
				try {
					init();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
			return (Float) value;
		}

		/**
		 * Set the Configurations::value (USED FOR TESTING PURPOSES)
		 * 
		 * @param newvalue
		 *            the value
		 */
		public void setValue(final Object newvalue) {
			this.value = newvalue;
		}

	}
}
