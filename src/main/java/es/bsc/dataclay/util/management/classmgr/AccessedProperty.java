
/**
 * @file AccessedPropertySpec.java
 * 
 * @date Jul 23, 2013
 */
package es.bsc.dataclay.util.management.classmgr;

import java.util.Objects;
import java.util.UUID;

import org.yaml.snakeyaml.Yaml;

import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.yaml.CommonYAML;

/**
 * This class represents the specification for an accessed Property for Implementations.
 */
public final class AccessedProperty {

	/** ID. */
	private UUID id;
	// === YAML SPECIFICATION === //
	// CHECKSTYLE:OFF
	// Properties must be public for YAML parsing.
	/** Namespace containing the property. */
	private String namespace;
	/** Class name containing the property. */
	private String className;
	/** Name of the property. */
	private String name;

	// CHECKSTYLE:ON
	// ==== DYNAMIC FIELDS ==== //
	/** ID of the accessed property. */
	private PropertyID propertyID;

	/**
	 * Empty constructor used for deserialization.
	 */
	public AccessedProperty() {

	}

	/**
	 * AccessedProperty constructor
	 * @param accessedNamespace
	 *            Namespace of accessed property
	 * @param newclassName
	 *            Name of the class containing the property
	 * @param newname
	 *            Name of the property
	 */
	public AccessedProperty(final String accessedNamespace, final String newclassName, final String newname) {
		this.setNamespace(accessedNamespace);
		this.setClassName(newclassName);
		this.setName(newname);
	}

	/**
	 * Get the AccessedPropertySpec::propertyID
	 * @return the propertyID
	 */

	public PropertyID getPropertyID() {
		return propertyID;
	}

	/**
	 * Set the AccessedPropertySpec::propertyID
	 * @param newpropertyID
	 *            the propertyID to set
	 */
	public void setPropertyID(final PropertyID newpropertyID) {
		this.propertyID = newpropertyID;
	}

	/**
	 * Get the AccessedProperty::className
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Set the AccessedProperty::className
	 * @param newclassName
	 *            the className to set
	 */
	public void setClassName(final String newclassName) {
		this.className = newclassName;
	}

	/**
	 * Get the AccessedProperty::name
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the AccessedProperty::name
	 * @param newname
	 *            the name to set
	 */
	public void setName(final String newname) {
		this.name = newname;
	}

	@Override
	public String toString() {
		final Yaml yaml = CommonYAML.getYamlObject();
		return yaml.dump(this);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.namespace, this.className, this.name);

	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof AccessedProperty) {
			final AccessedProperty other = (AccessedProperty) object;
			if (this.className != null) {
				if (!this.className.equals(other.getClassName())) {
					return false;
				}
			} else {
				if (other.getClassName() != null) {
					return false;
				}
			}
			if (this.name != null) {
				if (!this.name.equals(other.getName())) {
					return false;
				}
			} else {
				if (other.getName() != null) {
					return false;
				}
			}
			if (this.namespace != null) {
				if (!this.namespace.equals(other.getNamespace())) {
					return false;
				}
			} else {
				if (other.getNamespace() != null) {
					return false;
				}
			}
			return true;
		}
		return false;
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
