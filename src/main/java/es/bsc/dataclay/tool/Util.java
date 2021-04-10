
package es.bsc.dataclay.tool;

import java.util.HashSet;
import java.util.Set;

import es.bsc.dataclay.commonruntime.ClientManagementLib;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;

/**
 * Class with util methods for Tool operations
 */
public class Util {
		

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	public static final String ANSI_BOLD = "\u001B[1m";

	/**
	 * Types of datasets
	 */
	public enum DATASETTYPES {
		PUBLIC("public"), PRIVATE("private");

		String type;

		/**
		 * Constructor
		 * 
		 * @param dsType
		 *            current dataset type
		 */
		DATASETTYPES(final String dsType) {
			type = dsType;
		}

		/**
		 * Get supported types of datasets.
		 * 
		 * @return a set of types of datasets
		 */
		static Set<String> getDatasetTypes() {
			final Set<String> result = new HashSet<>();
			for (final DATASETTYPES dt : DATASETTYPES.values()) {
				result.add(dt.type);
			}
			return result;
		}

		/**
		 * Get type of dataset from String
		 * 
		 * @param dsType
		 *            type of dataset in String format
		 * @return type of dataset in Enum format
		 */
		static DATASETTYPES getType(final String dsType) {
			if (dsType.equalsIgnoreCase(PUBLIC.type)) {
				return PUBLIC;
			}
			if (dsType.equalsIgnoreCase(PRIVATE.type)) {
				return PRIVATE;
			}
			return null;
		}
	}

	/**
	 * Enum of supported Languages
	 */
	public enum LANGS {
		JAVA("java"), PYTHON("python");

		String base;

		/**
		 * Base code name of the language
		 * 
		 * @param basename
		 *            code name of the language
		 */
		LANGS(final String basename) {
			base = basename;
		}

		/**
		 * Get language from its String code name representation
		 * 
		 * @param lang
		 *            language code name
		 * @return enum language type
		 */
		static Langs getLang(final String lang) {
			if (lang.equalsIgnoreCase(JAVA.base)) {
				return Langs.LANG_JAVA;
			}
			if (lang.equalsIgnoreCase(PYTHON.base)) {
				return Langs.LANG_PYTHON;
			}
			return null;
		}

		/**
		 * Get base code name from enum language
		 * 
		 * @param lang
		 *            enum language
		 * @return string code name of the language
		 */
		static LANGS getBase(final Langs lang) {
			if (lang.equals(Langs.LANG_JAVA)) {
				return JAVA;
			}
			if (lang.equals(Langs.LANG_PYTHON)) {
				return PYTHON;
			}
			return null;
		}

		/**
		 * Get all supported languages
		 * 
		 * @return the set of supported languages (string code names)
		 */
		static Set<String> getSupported() {
			final Set<String> result = new HashSet<>();
			for (final LANGS l : LANGS.values()) {
				result.add(l.base);
			}
			return result;
		}
	}

	/**
	 * Error types
	 */
	public enum ERRCODE {
		NOERROR(0, ""), WARNING(1, " *WARNING* "), ERROR(-1, " *ERROR* ");

		public String prio;
		public int code;

		/**
		 * Constructor
		 * 
		 * @param newcode
		 *            error code
		 * @param newpref
		 *            error priority
		 */
		ERRCODE(final int newcode, final String newpref) {
			code = newcode;
			prio = newpref;
		}
	}

	public static boolean initialized = false;

	/**
	 * Init dataClay connection
	 * 
	 * @throws Exception
	 *             if an unexpected error occurs
	 */
	public static void init() throws Exception {
		if (!ClientManagementLib.initializeCMLib(null)) {
			finishErr("Unable to connect with dataClay", ERRCODE.ERROR);
		}
		initialized = true;
	}

	/**
	 * Finalize dataClay connection and prints message
	 * 
	 * @param msg
	 *            message to be printed
	 * @throws Exception
	 *             if an unexpected error occurs
	 */
	public static void finishOut(final String msg) throws Exception {
		if (initialized) {
			ClientManagementLib.finishConnections();
			initialized = false;
		}
		System.out.println(ANSI_BOLD + " " + ANSI_BLUE + "[dataClay] " + msg + ANSI_RESET);
	}

	/**
	 * Finalize dataClay connection and prints error message
	 * 
	 * @param msg
	 *            error message
	 * @param exitcode
	 *            error code
	 * @throws Exception
	 *             if an unexpected error occurs
	 */
	public static void finishErr(final String msg, final ERRCODE exitcode) throws Exception {
		System.err.println(ANSI_BOLD + " " + ANSI_RED + "[dataClay] " + exitcode.prio + msg + ANSI_RESET);
		if (initialized) {
			ClientManagementLib.finishConnections();
			initialized = false;
		}
	}
}
