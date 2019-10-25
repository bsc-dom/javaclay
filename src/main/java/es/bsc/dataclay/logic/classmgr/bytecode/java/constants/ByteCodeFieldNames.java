
package es.bsc.dataclay.logic.classmgr.bytecode.java.constants;

/**
 * This class contains names of stub fields as Stub's constants.
 */
public final class ByteCodeFieldNames {

	/**
	 * Utility classes should have private constructor.
	 */
	private ByteCodeFieldNames() {

	}

	/** Object ID field name in stub. */
	public static final String IS_STUB_FIELDNAME = "$$IsStub";

	/** Object ID field name in stub. */
	public static final String OBJECTID_FIELDNAME = "objectID";

	/** threadsUsingObject field name in stub. */
	public static final String THREADSUSINGOBJ_FIELDNAME = "threadsUsingObject";

	/** Python dictionary for method names, containing operation IDs. */
	public static final String METHOD_NAME_DICT = "methodDict";

	/** DEBUG_ENABLED field name in stub. */
	public static final String DEBUG_ENABLED_FIELDNAME = "DEBUG_ENABLED";

	/** Client Lib field name in stub. */
	public static final String CLIENT_LIB_FIELDNAME = "clientLib";

}
