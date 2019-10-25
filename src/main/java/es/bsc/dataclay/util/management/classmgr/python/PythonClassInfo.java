
package es.bsc.dataclay.util.management.classmgr.python;

import java.util.List;
import java.util.UUID;

import es.bsc.dataclay.util.management.classmgr.LanguageDependantClassInfo;

/**
 * This class represents Python language dependant information for a MetaClass.
 */
public final class PythonClassInfo implements LanguageDependantClassInfo {

	/** ID. */
	private UUID id;

	/** Imports. */
	private List<String> imports;

	/**
	 * Creates an empty PythonClassInfo
	 */
	public PythonClassInfo() {
		super();
	}

	/**
	 * PythonClassInfoconstructor with provided specifications and IDs
	 * @param newimports
	 *            Import information of the Python class
	 * 
	 */
	public PythonClassInfo(final List<String> newimports) {
		this.setImports(newimports);
	}

	/**
	 * Get the MetaClass::imports
	 * @return the imports
	 */
	public List<String> getImports() {
		return imports;
	}

	/**
	 * Set the MetaClass::imports
	 * @param newimports
	 *            the imports to set
	 */
	public void setImports(final List<String> newimports) {
		if (newimports == null) {
			throw new IllegalArgumentException("imports cannot be null");
		}
		this.imports = newimports;
	}

	/**
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * @param theid
	 *            the id to set
	 */
	public void setId(final UUID theid) {
		this.id = theid;
	}

}
