
/**
 * @file AccessedImplementation.java
 *
 * @date Jun 14, 2013
 */
package es.bsc.dataclay.util.management.classmgr;

import java.util.Objects;
import java.util.UUID;

import org.yaml.snakeyaml.Yaml;

import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.yaml.CommonYAML;

/**
 * This class represents the specification for an accessed implementation for Implementations.
 */
public final class AccessedImplementation {

	/** ID. */
	private UUID id;
	// === YAML SPECIFICATION === //
	// CHECKSTYLE:OFF
	// Properties must be public for YAML parsing.
	/** Namespace containing the operation. */
	private String namespace;
	/** Class name containing the operation. */
	private String className;
	/** Signature of the operation . */
	private String opSignature;
	/** Position of implementation inside the operation. */
	private int implPosition;
	// CHECKSTYLE:ON
	// ==== DYNAMIC FIELDS ==== //
	/** ID of the accessed implementation. */
	private ImplementationID implementationID;

	/**
	 * Empty constructor used for deserialization.
	 */
	public AccessedImplementation() {

	}

	/**
	 * AccessedImplementation constructor
	 * @param newnamespace
	 *            Namespace containing operation
	 * @param newclassName
	 *            Class name containing operation
	 * @param newopSignature
	 *            Signature containing operation
	 * @param newimplPosition
	 *            Position of implementation inside the operation
	 */
	public AccessedImplementation(final String newnamespace, final String newclassName,
			final String newopSignature, final int newimplPosition) {
		this.setNamespace(newnamespace);
		this.setClassName(newclassName);
		this.setOpSignature(newopSignature);
		this.setImplPosition(newimplPosition);
	}

	/**
	 * Get the AccessedImplementationSpec::implementationID
	 * @return the implementationID
	 */
	public ImplementationID getImplementationID() {
		return implementationID;
	}

	/**
	 * Set the AccessedImplementationSpec::implementationID
	 * @param newimplementationID
	 *            the implementationID to set
	 */
	public void setImplementationID(final ImplementationID newimplementationID) {
		this.implementationID = newimplementationID;
	}

	/**
	 * Get the AccessedImplementation::className
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Set the AccessedImplementation::className
	 * @param newclassName
	 *            the className to set
	 */
	public void setClassName(final String newclassName) {
		this.className = newclassName;
	}

	/**
	 * Get the AccessedImplementation::opSignature
	 * @return the opSignature
	 */
	public String getOpSignature() {
		return opSignature;
	}

	/**
	 * Set the AccessedImplementation::opSignature
	 * @param newopSignature
	 *            the opSignature to set
	 */
	public void setOpSignature(final String newopSignature) {
		this.opSignature = newopSignature;
	}

	/**
	 * Get namespace
	 * @return the namespace
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * Set namespace
	 * @param newnamespace
	 *            the namespace to set
	 */
	public void setNamespace(final String newnamespace) {
		this.namespace = newnamespace;
	}

	@Override
	public String toString() {
		final Yaml yaml = CommonYAML.getYamlObject();
		return yaml.dump(this);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.namespace, this.className, this.opSignature, this.implPosition);
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof AccessedImplementation) {
			final AccessedImplementation other = (AccessedImplementation) object;
			return this.getNamespace().equals(other.getNamespace())
					&& this.getClassName().equals(other.getClassName())
					&& this.getOpSignature().equals(other.getOpSignature())
					&& this.getImplPosition() == other.getImplPosition();
		}
		return false;
	}

	/**
	 * Get the AccessedImplementation::implPosition
	 * @return the implPosition
	 */
	public int getImplPosition() {
		return this.implPosition;
	}

	/**
	 * Set the AccessedImplementation::implPosition
	 * @param newimplPosition
	 *            the implPosition to set
	 */
	public void setImplPosition(final int newimplPosition) {
		this.implPosition = newimplPosition;
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
