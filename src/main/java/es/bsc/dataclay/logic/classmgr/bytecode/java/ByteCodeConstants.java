
package es.bsc.dataclay.logic.classmgr.bytecode.java;

/** Constants of names of classes, internal names... */
public final class ByteCodeConstants {

	/** Mark used to modify method's names, properties... */
	public static final String DATACLAY_PREFIX_MARK = "$$";
	public static final String DATACLAY_UPDATE = "Update$$";
	public static final String DATACLAY_SET_UPDATE_PREFIX = DATACLAY_PREFIX_MARK + "set" + DATACLAY_UPDATE;
	

	/**
	 * Utility classes should have a private constructor.
	 */
	private ByteCodeConstants() { 
		
	}
	

}
