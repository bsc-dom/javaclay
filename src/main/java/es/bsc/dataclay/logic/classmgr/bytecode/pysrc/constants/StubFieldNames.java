
package es.bsc.dataclay.logic.classmgr.bytecode.pysrc.constants;

/**
 * This class contains the Python names of stub fields as Stub's constants.
 */
public final class StubFieldNames {

	/**
	 * Utility classes should have private constructor.
	 */
	private StubFieldNames() {

	}

	/** Object ID field name in stub. */
	public static final String OBJECTID_FIELDNAME = "_object_ID";

	/** executeRemoteImpl field name in stub. */
	public static final String EXECREMOTE_FIELDNAME = "__execute_remote_impl";

	/** Object ID field name in stub. */
	public static final String MCLASSID_FIELDNAME = "_metaclass_ID";

	/** SessionID field name in stub. */
	public static final String SESSID_FIELDNAME = "_session_ID";

	/** origSessionID field name in stub. */
	public static final String ORIGSESSID_FIELDNAME = "_orig_session_ID";

	/** StubInfo field name in stub. */
	public static final String STUBINF_FIELDNAME = "_stubinfo";
	
	/** Method arguments. List of pairs (argument_names, argument_types). */
	public static final String ARGUMENTINFO_FIELDNAME = "_argumentinfo";

	/** Python dictionary for method names, containing operation IDs. */
	public static final String METHOD_NAME_DICT = "_method_to_op";
	
	/** Iternal List with the fields (properties) of the stub. */
	public static final String PROPERTYLIST_NAME = "_dclay_fields";
	
	/** Internal List with the types for the properties (_dclay_fields). */
	public static final String PROPERTYTYPES_NAME = "_dclay_types";
	
	/** Python dictionary for field names, containing properties IDs. */
	public static final String FIELD_NAME_DICT = "_field_to_prop";
	
	/** Counter to know the number of nullable references. */
	public static final String NULLABLE_COUNTER_NAME = "_nullable_counter";
}
