
package es.bsc.dataclay.util.management.classmgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.yaml.snakeyaml.Yaml;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.exceptions.logicmodule.classmgr.PropertyDepInfoAlreadyRegisteredException;
import es.bsc.dataclay.util.MgrObject;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.management.classmgr.java.JavaPropertyInfo;
import es.bsc.dataclay.util.management.classmgr.python.PythonPropertyInfo;
import es.bsc.dataclay.util.replication.Replication;
import es.bsc.dataclay.util.yaml.CommonYAML;

/**
 * This class represents a Property.
 * 
 */
public final class Property extends MgrObject<PropertyID> implements Comparable<Property> {

	// === YAML SPECIFICATION === //
	// Properties must be public for YAML parsing.
	// CHECKSTYLE:OFF
	/** Name of namespace of the class of the property. */
	private String namespace;
	/** Name of the class of the property. */
	private String className;
	/** Name of the property. */
	private String name;
	/** Position of the property in class. */
	private int position;
	/** Type of the Property. */
	private Type type;

	// CHECKSTYLE:ON
	// ==== DYNAMIC FIELDS ==== //
	/** ID of the Getter operation of the Property. */
	private OperationID getterOperationID;
	/** ID of the Getter Implementation of the Property. */
	private ImplementationID getterImplementationID;
	/** ID of the Setter Implementation of the Property. */
	private ImplementationID setterImplementationID;
	/** ID of the Setter operation of the Property. */
	private OperationID setterOperationID;
	/** ID of the namespace in which the property has been created. */
	private NamespaceID namespaceID;
	/** ID of the MetaClass containing the Property. */
	private MetaClassID metaClassID;
	/** Language dependant property information. */
	private Map<Langs, LanguageDependantPropertyInfo> languageDepInfos;
	/** Property's annotations */
	private List<Annotation> annotations;

	/** ID of the Setter Implementation of the Property. */
	private ImplementationID updateImplementationID;
	/** ID of the Setter operation of the Property. */
	private OperationID updateOperationID;

	private String beforeUpdate, afterUpdate;

	private boolean inMaster;

	private boolean isReplicated;

	/**
	 * Creates an empty Property
	 */
	public Property() {

	}

	/**
	 * Property constructor
	 * @param newposition
	 *            Position of the property
	 * @param newname
	 *            Name of the property
	 * @param newtype
	 *            Type of the property
	 * @param newclassName
	 *            Name of the MetaClass containing the Property
	 * @param newnamespace
	 *            Name of the namespace in which the property has been created
	 * @post Creates a new Property with provided name and type and generates a new PropertyID.
	 */
	public Property(final int newposition, final String newname, final Type newtype,
			final String newnamespace, final String newclassName) {
		this.setNamespace(newnamespace);
		this.setClassName(newclassName);
		this.setPosition(newposition);
		this.setName(newname);
		this.setType(newtype);
		this.setLanguageDepInfos(new HashMap<Langs, LanguageDependantPropertyInfo>());
		this.setAnnotations(new ArrayList<Annotation>());
	}

	/**
	 * Get the name of this Property
	 * @return Property::name of container Property.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set the Property::name of this Property
	 * @param newname
	 *            New name to be set
	 */
	public void setName(final String newname) {
		this.name = newname;
	}

	/**
	 * Get the TypeID of this Property
	 * @return Property::typeID of container Property.
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * Set the Property::type
	 * @param newtype
	 *            New Type to set of this Property.
	 */
	public void setType(final Type newtype) {
		this.type = newtype;
	}

	/**
	 * Get the Property::getterOperationID
	 * @return the getterOperationID
	 */

	public OperationID getGetterOperationID() {
		return getterOperationID;
	}

	/**
	 * Set the Property::getterOperationID
	 * @param newgetterOperationID
	 *            the getterOperationID to set
	 */
	public void setGetterOperationID(final OperationID newgetterOperationID) {
		this.getterOperationID = newgetterOperationID;
	}

	/**
	 * Get the Property::setterOperationID
	 * @return the setterOperationID
	 */

	public OperationID getSetterOperationID() {
		return setterOperationID;
	}

	/**
	 * Set the Property::setterOperationID
	 * @param newsetterOperationID
	 *            the setterOperationID to set
	 */
	public void setSetterOperationID(final OperationID newsetterOperationID) {
		this.setterOperationID = newsetterOperationID;
	}

	/********** RTOUMA: GETTER IMPLEMENTATION ID **********/
	/**
	 * Get the Property::getterImplementationID
	 * @return the getterImplementationID
	 */

	public ImplementationID getGetterImplementationID() {
		return getterImplementationID;
	}

	/**
	 * Set the Property::getterImplementationID
	 * @param newGetterImplementationID
	 *            the getterImplementationID to set
	 */
	public void setGetterImplementationID(final ImplementationID newGetterImplementationID) {
		this.getterImplementationID = newGetterImplementationID;
	}

	/******************************************************/

	/**
	 * Get the Property::metaClassID
	 * @return the metaClassID
	 */

	public MetaClassID getMetaClassID() {
		return metaClassID;
	}

	/**
	 * Set the Property::metaClassID
	 * @param newmetaClassID
	 *            the metaClassID to set
	 */
	public void setMetaClassID(final MetaClassID newmetaClassID) {
		this.metaClassID = newmetaClassID;
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
	public boolean equals(final Object t) {
		if (t instanceof Property) {
			final Property other = (Property) t;
			return this.name.equals(other.getName())
					&& this.className.equals(other.getClassName())
					&& this.namespace.equals(other.getNamespace());
		}
		return false;
	}

	/**
	 * Get the Property::position
	 * @return The position of the property
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Sets the Property::position
	 * @param newPosition
	 *            the position to be set
	 */
	public void setPosition(final int newPosition) {
		this.position = newPosition;
	}

	@Override
	public int compareTo(final Property other) {
		return this.getPosition() - other.getPosition();
	}

	/**
	 * Get the Property::languageDepInfos
	 * @return the languageDepInfos
	 */
	public Map<Langs, LanguageDependantPropertyInfo> getLanguageDepInfos() {
		return languageDepInfos;
	}

	/**
	 * Get Java language-dependant information of the property
	 * @return Java language-dependant information of the property
	 */
	public JavaPropertyInfo getJavaPropertyInfo() {
		return (JavaPropertyInfo) languageDepInfos.get(Langs.LANG_JAVA);
	}

	/**
	 * Get Python language-dependant information of the property
	 * @return Python language-dependant information of the property
	 */
	public PythonPropertyInfo getPythonPropertyInfo() {
		return (PythonPropertyInfo) languageDepInfos.get(Langs.LANG_PYTHON);
	}

	/**
	 * Set the Property::languageDepInfos
	 * @param newlanguageDepInfos
	 *            the languageDepInfos to set
	 */
	public void setLanguageDepInfos(final Map<Langs, LanguageDependantPropertyInfo> newlanguageDepInfos) {
		this.languageDepInfos = newlanguageDepInfos;
	}

	/**
	 * Add language dependant information
	 * @param langInfo
	 *            Language information
	 */
	public void addLanguageDepInfo(final LanguageDependantPropertyInfo langInfo) {
		if (langInfo instanceof JavaPropertyInfo) {
			if (this.languageDepInfos.get(Langs.LANG_JAVA) != null) {
				throw new PropertyDepInfoAlreadyRegisteredException(Langs.LANG_JAVA.name());
			} else {
				this.languageDepInfos.put(Langs.LANG_JAVA, langInfo);
			}
		} else if (langInfo instanceof PythonPropertyInfo) {
			if (this.languageDepInfos.get(Langs.LANG_PYTHON) != null) {
				throw new PropertyDepInfoAlreadyRegisteredException(Langs.LANG_PYTHON.name());
			} else {
				this.languageDepInfos.put(Langs.LANG_PYTHON, langInfo);
			}
		}
	}

	/**
	 * Get the Property::namespaceID
	 * @return the namespaceID
	 */
	public NamespaceID getNamespaceID() {
		return namespaceID;
	}

	/**
	 * Set the Property::namespaceID
	 * @param newnamespaceID
	 *            the namespaceID to set
	 */
	public void setNamespaceID(final NamespaceID newnamespaceID) {
		this.namespaceID = newnamespaceID;
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
	 * Get className
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Set className
	 * @param newclassName
	 *            the className to set
	 */
	public void setClassName(final String newclassName) {
		this.className = newclassName;
	}

	/**
	 * Get setterImplementationID
	 * @return the setterImplementationID
	 */
	public ImplementationID getSetterImplementationID() {
		return setterImplementationID;
	}

	/**
	 * Set setterImplementationID
	 * @param newsetterImplementationID
	 *            the setterImplementationID to set
	 */
	public void setSetterImplementationID(final ImplementationID newsetterImplementationID) {
		this.setterImplementationID = newsetterImplementationID;
	}

	/**
	 * Get the Property::updateOperationID
	 * @return the updateOperationID
	 */

	public OperationID getUpdateOperationID() {
		return updateOperationID;
	}

	/**
	 * Set the Property::updateOperationID
	 * @param newUpdateOperationID
	 *            the setterOperationID to set
	 */
	public void setUpdateOperationID(final OperationID newUpdateOperationID) {
		this.updateOperationID = newUpdateOperationID;
	}

	/**
	 * Get updateImplementationID
	 * @return the updateImplementationID
	 */
	public ImplementationID getUpdateImplementationID() {
		return updateImplementationID;
	}

	/**
	 * Set updateImplementationID
	 * @param newUpdateImplementationID
	 *            the setterImplementationID to set
	 */
	public void setUpdateImplementationID(final ImplementationID newUpdateImplementationID) {
		this.updateImplementationID = newUpdateImplementationID;
	}

	/**
	 * Get annotations
	 * @return List of property's annotations
	 */
	public List<Annotation> getAnnotations() {
		return annotations;
	}

	/**
	 * Set annotations
	 * @param annotations
	 *            the list of annotations to set
	 */
	public void setAnnotations(final List<Annotation> annotations) {
		this.annotations = annotations;
		for(Annotation a: annotations) {
			if(a.getDescr().contains(Replication.class.getCanonicalName().replace('.', '/'))) {
				isReplicated = true;
				break;
			}
		}
	}

	/**
	 * Add an annotation to the property
	 * @param annotation
	 *            the annotation to add
	 */
	public void addAnnotation(final Annotation annotation) {
		annotations.add(annotation);
		if(annotation.getDescr().contains(Replication.class.getCanonicalName().replace('.', '/'))) {
			isReplicated = true;
			if(annotation.getDescr().contains("InMaster")) {
				inMaster = true;
			}
		}
	}

	public String getAfterUpdate() {
		return afterUpdate;
	}

	public void setAfterUpdate(String afterUpdate) {
		this.afterUpdate = afterUpdate;
	}

	public String getBeforeUpdate() {
		return beforeUpdate;
	}

	public void setBeforeUpdate(String beforeUpdate) {
		this.beforeUpdate = beforeUpdate;
	}

	public boolean getInMaster() {
		return inMaster;
	}

	public void setInMaster(boolean inMaster) {
		this.inMaster = inMaster;
	}

	public boolean isReplicated() {
		return this.isReplicated;
	}
}
