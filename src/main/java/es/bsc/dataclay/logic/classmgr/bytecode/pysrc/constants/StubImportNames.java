
package es.bsc.dataclay.logic.classmgr.bytecode.pysrc.constants;

/**
 * This class contains names of includes as Stub's constants.
 */
public final class StubImportNames {
	
	/**
	 * Utility classes should have private constructor.
	 */
	private StubImportNames() { 
		
	}

	/** UUID class name. */
	public static final String UUID_MODULE = "uuid";
	
	/** Wasabi public import name. */
	public static final String STUB_MODULE = "_dataclay.stubs";
	
	/** Name of the Python Stub class in the wasabi module. */
	public static final String DCLAY_BASECLASS = "DataClayObject";
	
	/** Name of the Python Stub class in the wasabi module. */
	public static final String DCLAY_OBJECT_CLSNAME = STUB_MODULE + ".DCLAYObject";
}
