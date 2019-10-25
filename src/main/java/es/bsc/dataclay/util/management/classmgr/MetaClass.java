
package es.bsc.dataclay.util.management.classmgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.yaml.snakeyaml.Yaml;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.exceptions.logicmodule.classmgr.ClassDepInfoAlreadyRegisteredException;
import es.bsc.dataclay.exceptions.logicmodule.classmgr.ImplementationNotExistException;
import es.bsc.dataclay.exceptions.logicmodule.classmgr.OperationNotExistException;
import es.bsc.dataclay.exceptions.logicmodule.classmgr.PropertyNotExistException;
import es.bsc.dataclay.util.MgrObject;
import es.bsc.dataclay.util.events.listeners.ECA;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.management.classmgr.java.JavaClassInfo;
import es.bsc.dataclay.util.management.classmgr.python.PythonClassInfo;
import es.bsc.dataclay.util.structs.Tuple;
import es.bsc.dataclay.util.yaml.CommonYAML;

/**
 * This class represents a Metaclass.
 * 
 */
public final class MetaClass extends MgrObject<MetaClassID> {

	// === YAML SPECIFICATION === //
	// Properties must be public for YAML parsing.
	// CHECKSTYLE:OFF
	/** Namespace of the class. */
	private String namespace;
	/** Name of the class. */
	private String name;
	/** Information of parent MetaClass (if exists). */
	private UserType parentType;
	/** List of Properties of the metaclass . */
	private SortedSet<Property> properties;
	/** List of Operations of the metaclass . */
	private Set<Operation> operations;
	/** Indicates class is abstract. */
	// @abarcelo: note that the *property name* is simply abstract,
	// because of is<Abstract> and get<Abstract> getter and setter.
	private boolean isAbstract;
	/** Language dependant information of the class. */
	private Map<Langs, LanguageDependantClassInfo> languageDepInfos;

	/** ECAs defined in class. */
	private List<ECA> ecas = new ArrayList<>();

	// CHECKSTYLE:ON
	// ==== DYNAMIC FIELDS ==== //
	/** ID of the namespace containing the Type. */
	private NamespaceID namespaceID;

	/**
	 * Creates an empty Metaclass
	 */
	public MetaClass() {

	}

	/**
	 * MetaClass constructor with provided specifications and IDs
	 * @param newnamespace
	 *            Namespace to set
	 * @param newname
	 *            Name to be set
	 * @param newparentType
	 *            Type of the parent class (can be null)
	 * @param newisAbstract
	 *            Indicates class is abstract
	 *
	 * @post Creates a new MetaClass with provided specifications. Generates a new ID for the MetaClass.
	 * @see ClassManager::newMetaClass(NamespaceID, util.MetaClassSpec)
	 * 
	 */
	public MetaClass(final String newnamespace, final String newname,
			final UserType newparentType, final boolean newisAbstract) {
		this.setNamespace(newnamespace);
		this.setParentType(newparentType);
		this.setName(newname);
		this.setProperties(new TreeSet<Property>());
		this.setOperations(new HashSet<Operation>());
		this.setLanguageDepInfos(new HashMap<Langs, LanguageDependantClassInfo>());
		this.setIsAbstract(newisAbstract);
	}

	/**
	 * Get the MetaClass::name
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the MetaClass::name
	 * @param newname
	 *            the name to set
	 */
	public void setName(final String newname) {
		this.name = newname;
	}

	/**
	 * Get the MetaClass::parentType
	 * @return the parentType
	 */
	public UserType getParentType() {
		return this.parentType;
	}

	/**
	 * Set the MetaClass::parentType
	 * @param newparentType
	 *            the parentType
	 */
	public void setParentType(final UserType newparentType) {
		this.parentType = newparentType;
	}

	/**
	 * Get the MetaClass::properties
	 * @return the properties
	 */

	public List<Property> getProperties() {
		if (properties == null) {
			return null;
		}
		return new ArrayList<>(properties);
	}

	/**
	 * Set the MetaClass::properties
	 * @param newproperties
	 *            the properties
	 */
	public void setProperties(final SortedSet<Property> newproperties) {
		this.properties = newproperties;
	}

	/**
	 * Set the MetaClass::properties
	 * @param newproperties
	 *            the properties
	 * 
	 *         This is YAML friendly for lists --instead of sets which Python doesn't like because they contain non-hashable
	 *         fields.
	 */
	public void setProperties(final List<Property> newproperties) {
		if (newproperties == null) {
			this.properties = null;
		} else {
			this.properties = new TreeSet<>(newproperties);
		}
	}

	/**
	 * Get the MetaClass::operations
	 * @return the operations
	 */

	public List<Operation> getOperations() {
		if (operations == null) {
			return null;
		}
		return new ArrayList<>(operations);
	}

	/**
	 * Set the MetaClass::operations
	 * @param newoperations
	 *            the operations
	 */

	public void setOperations(final Set<Operation> newoperations) {
		this.operations = newoperations;
	}

	/**
	 * Set the MetaClass::operations
	 * @param newoperations
	 *            the operations
	 * 
	 *         This is YAML friendly for lists --instead of sets which Python doesn't like because they contain non-hashable
	 *         fields.
	 */

	public void setOperations(final List<Operation> newoperations) {
		if (newoperations == null) {
			this.operations = null;
		} else {
			this.operations = new HashSet<>(newoperations);
		}
	}

	/**
	 * Add a new property to the list of properties
	 * @param newproperty
	 *            property to add
	 * @return TRUE if property was not found and added. FALSE, otherwise.
	 * @post A new property is added to MetaClass::properties
	 */
	public boolean addPropertyAsEnrichment(final Property newproperty) {
		if (this.properties == null) {
			this.properties = new TreeSet<>();
		}
		newproperty.setPosition(this.properties.size());
		return this.properties.add(newproperty);
	}

	/**
	 * Add a new property to the list of properties
	 * @param newproperty
	 *            property to add
	 * @param position
	 *            position of the property
	 * @return TRUE if property was not found and added. FALSE, otherwise.
	 * @post A new property is added to MetaClass::properties
	 */
	public boolean addProperty(final Property newproperty, final int position) {
		if (this.properties == null) {
			this.properties = new TreeSet<>();
		}
		newproperty.setPosition(position);
		return this.properties.add(newproperty);
	}

	/**
	 * Add a new operation to the list of operations
	 * @param newoperation
	 *            Operation to add
	 * @return TRUE if Operation was not found and added. FALSE, otherwise.
	 * @post A new Operation is added to MetaClass::operations
	 */
	public boolean addOperation(final Operation newoperation) {
		if (this.operations == null) {
			this.operations = new HashSet<>();
		}
		return this.operations.add(newoperation);
	}

	/**
	 * This operation verifies if a Property identified by propertyID exists
	 * @param propertyID
	 *            ID of the property to query
	 * @return TRUE if the provided property exists in the MetaClass. FALSE otherwise.
	 */
	public boolean existsPropertyInClass(final PropertyID propertyID) {
		if (properties == null) {
			return false;
		}
		for (final Iterator<Property> it = properties.iterator(); it.hasNext();) {
			final Property actualProp = it.next();
			if (propertyID.getId().equals(actualProp.getDataClayID().getId())) {
				return true;
			}
		}

		return false;

	}

	/**
	 * This operation verifies if a Property with the name provided exists
	 * @param propertyName
	 *            Name of the property to query
	 * @return TRUE if the provided property exists in the MetaClass. FALSE otherwise.
	 */
	public boolean existsPropertyInClass(final String propertyName) {
		if (properties == null) {
			return false;
		}
		final Iterator<Property> iterator = properties.iterator();
		while (iterator.hasNext()) {
			final Property actual = iterator.next();
			if (actual.getName().equals(propertyName)) {
				return true;
			}
		}
		return false;

	}

	/**
	 * This operation verifies if a Operation identified by operationID exists
	 * @param operationID
	 *            ID of the operation to query
	 * @return TRUE if the provided operation exists in the MetaClass. FALSE otherwise.
	 */
	public boolean existsOperationInClass(final OperationID operationID) {
		if (operations == null) {
			return false;
		}
		for (final Iterator<Operation> it = operations.iterator(); it.hasNext();) {
			final Operation actualOp = it.next();
			if (operationID.getId().equals(actualOp.getDataClayID().getId())) {
				return true;
			}
		}
		return false;

	}

	/**
	 * This operation remove a Operation identified by operationID in the list of operations.
	 * @param operationID
	 *            ID of the operation to remove
	 * @return TRUE if the provided Operation was successfully removed from system. FALSE otherwise.
	 */
	public boolean removeOperation(final OperationID operationID) {
		if (operations == null) {
			return false;
		}

		for (final Operation op : operations) {
			if (op.getDataClayID().equals(operationID)) {
				operations.remove(op);
				return true;
			}
		}

		return false;
	}

	/**
	 * Get the MetaClass::namespace
	 * @return the namespace
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * Set the MetaClass::namespace
	 * @param newnamespace
	 *            the namespace to set
	 */
	public void setNamespace(final String newnamespace) {
		this.namespace = newnamespace;
	}

	/**
	 * Get operation identified by id provided
	 * @param operationID
	 *            Operation ID
	 * @return Operation or NULL if not exists.
	 */
	public Operation getOperation(final OperationID operationID) {
		for (final Operation curOperation : operations) {
			if (curOperation.getDataClayID().equals(operationID)) {
				return curOperation;
			}
		}
		throw new OperationNotExistException(operationID);
	}

	/**
	 * Get property identified by id provided
	 * @param propertyID
	 *            property ID
	 * @return property or NULL if not exists.
	 */
	public Property getProperty(final PropertyID propertyID) {
		for (final Property curProperty : this.properties) {
			if (curProperty.getDataClayID().equals(propertyID)) {
				return curProperty;
			}
		}
		throw new PropertyNotExistException(propertyID);
	}

	/**
	 * Get Implementation identified by id provided
	 * @param implID
	 *            Implementation ID
	 * @return Implementation or NULL if not exists.
	 */
	public Implementation getImplementation(final ImplementationID implID) {
		for (final Operation curOperation : operations) {
			final Implementation impl = curOperation.getImplementationInOperation(implID);
			if (impl != null) {
				return impl;
			}
		}
		throw new ImplementationNotExistException(implID);
	}

	/**
	 * Get the implementation with ID provided and the associated operation or NULL if not found.
	 * @param implementationID
	 *            ID of the implementation to seek
	 * @return implementation with ID provided and the associated operation or NULL if not found.
	 */
	public Tuple<Operation, Implementation> getImplementationAndOperation(final ImplementationID implementationID) {
		for (final Operation curOperation : operations) {
			final Implementation impl = curOperation.getImplementationInOperation(implementationID);
			if (impl != null) {
				return new Tuple<>(curOperation, impl);
			}
		}
		throw new ImplementationNotExistException(implementationID);
	}

	/**
	 * Get the MetaClass::languageDepInfos
	 * @return the languageDepInfos
	 */
	public Map<Langs, LanguageDependantClassInfo> getLanguageDepInfos() {
		return languageDepInfos;
	}

	/**
	 * Get Java language-dependant information of the class
	 * @return Java language-dependant information of the class
	 */
	public JavaClassInfo getJavaClassInfo() {
		return (JavaClassInfo) languageDepInfos.get(Langs.LANG_JAVA);
	}

	/**
	 * Get Python language-dependant information of the class
	 * @return Python language-dependant information of the class
	 */
	public PythonClassInfo getPythonClassInfo() {
		return (PythonClassInfo) languageDepInfos.get(Langs.LANG_PYTHON);
	}

	/**
	 * Set the MetaClass::languageDepInfos
	 * @param newlanguageDepInfos
	 *            the languageDepInfos to set
	 */
	public void setLanguageDepInfos(final Map<Langs, LanguageDependantClassInfo> newlanguageDepInfos) {
		this.languageDepInfos = newlanguageDepInfos;
	}

	/**
	 * Add language dependant class information
	 * @param langClassInfo
	 *            Language class information
	 */
	public void addLanguageDepInfo(final LanguageDependantClassInfo langClassInfo) {
		if (langClassInfo instanceof JavaClassInfo) {
			if (this.languageDepInfos.get(Langs.LANG_JAVA) != null) {
				throw new ClassDepInfoAlreadyRegisteredException(Langs.LANG_JAVA.name());
			} else {
				this.languageDepInfos.put(Langs.LANG_JAVA, langClassInfo);
			}
		} else if (langClassInfo instanceof PythonClassInfo) {
			if (this.languageDepInfos.get(Langs.LANG_PYTHON) != null) {
				throw new ClassDepInfoAlreadyRegisteredException(Langs.LANG_PYTHON.name());
			} else {
				this.languageDepInfos.put(Langs.LANG_PYTHON, langClassInfo);
			}
		}
	}

	/**
	 * Get the MetaClass::isAbstract
	 * @return the isAbstract
	 */
	public boolean getIsAbstract() {
		return isAbstract;
	}

	/**
	 * Set the MetaClass::isAbstract
	 * @param newisAbstract
	 *            the isAbstract to set
	 */
	public void setIsAbstract(final boolean newisAbstract) {
		this.isAbstract = newisAbstract;
	}

	/**
	 * Get the MetaClass::namespaceID
	 * @return the namespaceID
	 */
	public NamespaceID getNamespaceID() {
		return namespaceID;
	}

	/**
	 * Set the MetaClass::namespaceID
	 * @param newnamespaceID
	 *            the namespaceID to set
	 */
	public void setNamespaceID(final NamespaceID newnamespaceID) {
		this.namespaceID = newnamespaceID;
	}

	/**
	 * @return the ecas
	 */
	public List<ECA> getEcas() {
		return ecas;
	}

	/**
	 * @param newEcas
	 *            the ecas to set
	 */
	public void setEcas(final List<ECA> newEcas) {
		this.ecas = newEcas;
	}

	@Override
	public String toString() {
		final Yaml yaml = CommonYAML.getYamlObject();
		return yaml.dump(this);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.namespace, this.name);

	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof MetaClass) {
			final MetaClass other = (MetaClass) object;
			if (this.namespace != null) {
				if (!this.namespace.equals(other.getNamespace())) {
					return false;
				}
			} else {
				if (other.getNamespace() != null) {
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
			return true;

		}
		return false;
	}

	/**
	 * Get operation with method name and descriptor provided
	 * @param opNameAndDescriptor
	 *            method name and descriptor of the operation
	 * @return Operation with name and descriptor provided or null if not present.
	 */
	public Operation getOperation(final String opNameAndDescriptor) {
		for (final Operation op : operations) {
			if (op.getNameAndDescriptor().equals(opNameAndDescriptor)) {
				return op;
			}
		}
		return null;

	}

}
