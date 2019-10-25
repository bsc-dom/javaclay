
package es.bsc.dataclay.logic.classmgr.bytecode.pysrc.constants;

/**
 * This class contains names of methods as Stub's constants.
 */
public final class StubMethodsNames {

	/**
	 * Utility classes should have private constructor.
	 */
	private StubMethodsNames() { 
		
	}
		
	/** Python function for UUID instance generation. */
	public static final String UUID_MTHD = "uuid.UUID";

	/** Stub make persistent method. */
	public static final String MKPERST_MTHD = "make_persistent";

	/** Stub query by example method. */
	public static final String QBE_MTHD = "query_by_example";

	/** Stub copy to local method. */
	public static final String COPYTOLOCAL_MTHD = "copy_to_local";

	/** Stub delete persistent method. */
	public static final String DELPERST_MTHD = "delete_persistent";

	/** Stub new replica method. */
	public static final String NEWREP_MTHD = "new_replica";

	/** Stub move replica method. */
	public static final String MOVREP_MTHD = "move_replica";
}
