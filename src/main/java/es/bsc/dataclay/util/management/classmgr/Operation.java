
package es.bsc.dataclay.util.management.classmgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

import org.yaml.snakeyaml.Yaml;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.exceptions.logicmodule.classmgr.OperationDepInfoAlreadyRegisteredException;
import es.bsc.dataclay.util.MgrObject;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.management.classmgr.java.JavaOperationInfo;
import es.bsc.dataclay.util.management.classmgr.python.PythonOperationInfo;
import es.bsc.dataclay.util.yaml.CommonYAML;

/**
 * This class represents an Operation.
 *
 */
public final class Operation extends MgrObject<OperationID> {

	// === YAML SPECIFICATION === //
	// Properties must be public for YAML parsing.
	// CHECKSTYLE:OFF
	/** Name of namespace of the class of the operation. */
	private String namespace;
	/** Name of the class of the operation. */
	private String className;
	/** Descriptor of the operation (signature without subtypes). */
	private String descriptor;
	/** Signature of the operation. */
	private String signature;
	/** Name of the operation. */
	private String name;
	/** Name and descriptor. */
	private String nameAndDescriptor;
	/** List of arguments of the Operation. */
	private Map<String, Type> params;
	/** Order of the parameters. */
	private List<String> paramsOrder;
	/** Type of the returned element of the Operation (if exists). */
	private Type returnType;
	/** Ordered list of implementations implementing the Operation. */
	private SortedSet<Implementation> implementations;
	/** Indicates operation is abstract. */
	// @abarcelo: note that the *property name* is simply abstract,
	// because of is<Abstract> and get<Abstract> getter and setter.
	private boolean isAbstract;

	// See comments on isAbstract property
	/** Indicates operation is static constructor. */
	private boolean isStaticConstructor;

	// CHECKSTYLE:ON
	// ==== DYNAMIC FIELDS ==== //
	/** ID of the MetaClass containing the operation. */
	private MetaClassID metaClassID;
	/** ID of the namespace in which the operation has been created. */
	private NamespaceID namespaceID;
	/** Language dependant information. */
	private Map<Langs, LanguageDependantOperationInfo> languageDepInfos;
	/** Operation's annotations */
	private List<Annotation> annotations;

	/**
	 * Creates an empty Operation
	 */
	public Operation() {

	}

	/**
	 * Operation constructor with provided specifications
	 * @param newname
	 *            Name of the operation
	 * @param newdescriptor
	 *            Descriptor of the operation (signature without subtypes)
	 * @param newsignature
	 *            Signature of the operation
	 * @param newnameAndDescriptor
	 *            Name and descriptor of the operation
	 * @param newnamespace
	 *            Namespace of the class containing operation
	 * @param newclassName
	 *            Name of class of the operation
	 * @param newisAbstract
	 *            Indicates operation is abstract
	 * @post Creates a new Operation with provided name and generates a new OperationID.
	 */
	public Operation(final String newname, final String newdescriptor, final String newsignature,
			final String newnameAndDescriptor,
			final String newnamespace, final String newclassName,
			final boolean newisAbstract) {
		this.setNamespace(newnamespace);
		this.setSignature(newsignature);
		this.setNameAndDescriptor(newnameAndDescriptor);
		this.setDescriptor(newdescriptor);
		this.setClassName(newclassName);
		this.setName(newname);
		this.setImplementations(new TreeSet<Implementation>());
		this.setParams(new LinkedHashMap<String, Type>());
		this.setParamsOrder(new LinkedList<String>());
		this.setLanguageDepInfos(new HashMap<Langs, LanguageDependantOperationInfo>());
		this.setIsAbstract(newisAbstract);
		this.setIsStaticConstructor(newname.equals("<clinit>"));
		this.setAnnotations(new ArrayList<Annotation>());

	}

	/**
	 * Get the name of this Operation
	 * @return Operation::name of container Operation.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set the Operation::name of this Operation
	 * @param newname
	 *            New name to be set
	 */
	public void setName(final String newname) {
		this.name = newname;
	}

	/**
	 * Get the Parameters of this Operation
	 * @return Operation::params of container Operation.
	 */
	public Map<String, Type> getParams() {
		return this.params;
	}

	/**
	 * Get the Parameters types of this Operation
	 * @return Operation::params of container Operation.
	 */
	public List<Type> getParamsTypesInOrder() {
		List<Type> paramTypes = new ArrayList<>();
		for (String paramNum : paramsOrder) {
			paramTypes.add(this.params.get(paramNum));
		}
		return paramTypes;
	}

	/**
	 * Set the Operation::params
	 * @param newparams
	 *            Parameter to set of this Operation.
	 */
	public void setParams(final Map<String, Type> newparams) {
		this.params = newparams;
	}

	/**
	 * Get the Parameter order of this Operation
	 * @return Operation::paramOrder of container Operation.
	 */
	public List<String> getParamsOrder() {
		return this.paramsOrder;
	}

	/**
	 * Set the Operation::paramOrder
	 * @param newparamOrder
	 *            The list of name of parameters, in the proper order.
	 */
	public void setParamsOrder(final List<String> newparamOrder) {
		this.paramsOrder = newparamOrder;
	}

	/**
	 * Add a new Params to the map of Params
	 * @param paramName
	 *            Name of the parameter
	 * @param newparam
	 *            Param to add
	 * @post A new Param is added to Operation::params
	 */
	public void addParam(final String paramName, final Type newparam) {
		if (newparam == null) {
			throw new IllegalArgumentException("Params cannot be null");
		}
		this.params.put(paramName, newparam);
		this.paramsOrder.add(paramName);
	}

	/**
	 * @return the implementations
	 */
	public List<Implementation> getImplementations() {
		if (implementations == null) {
			return null;
		}
		return new ArrayList<>(implementations);
	}

	/**
	 * @param newimplementations
	 *            the implementations to set
	 */
	public void setImplementations(final SortedSet<Implementation> newimplementations) {
		this.implementations = newimplementations;
	}

	/**
	 * @param newimplementations
	 *            the implementations to set
	 *
	 *         This is YAML friendly for lists --instead of sets which Python doesn't like because they contain non-hashable fields.
	 */
	public void setImplementations(final List<Implementation> newimplementations) {
		if (newimplementations == null) {
			this.implementations = null;
		} else {
			this.implementations = new TreeSet<>(newimplementations);
		}
	}

	/**
	 * Add a new Implementation to the list of Implementations
	 * @param newimplementation
	 *            Implementation to add
	 * @return TRUE if Implementation was not found and added. FALSE, otherwise.
	 * @post A new Implementation is added to Operation::implementations
	 */
	public boolean addImplementation(final Implementation newimplementation) {
		if (this.implementations == null) {
			this.implementations = new TreeSet<>();
		}
		newimplementation.setPosition(this.implementations.size());
		return this.implementations.add(newimplementation);
	}

	/**
	 * This operation verifies if a Implementation identified by implementationID exists in this Operation.
	 * @param implementationID
	 *            ID of the Implementation to query
	 * @return TRUE if the provided Implementation exists in the Operation. FALSE otherwise.
	 */
	public boolean existsImplementationInOperation(final ImplementationID implementationID) {
		if (implementations == null) {
			return false;
		}
		for (final Implementation impl : implementations) {
			if (impl.getDataClayID().equals(implementationID)) {
				return true;
			}
		}
		return false;

	}

	/**
	 * Get the implementation with ID provided in this operation or NULL if it does not exist.
	 * @param implementationID
	 *            ID of the implementation to get
	 * @return The Implementation in the operation or NULL if it does not exist.
	 */
	public Implementation getImplementationInOperation(final ImplementationID implementationID) {
		final Iterator<Implementation> itImpls = implementations.iterator();
		while (itImpls.hasNext()) {
			final Implementation actualImpl = itImpls.next();
			if (actualImpl.getDataClayID().equals(implementationID)) {
				return actualImpl;
			}
		}
		return null;
	}

	/**
	 * This operation remove a Implementation identified by implementationID in the list of implementations.
	 * @param implementationID
	 *            ID of the implementation to remove
	 * @return TRUE if the provided Implementation was successfully removed from system. FALSE otherwise.
	 */
	public boolean removeImplementation(final ImplementationID implementationID) {
		if (implementations == null) {
			return false;
		}

		final Iterator<Implementation> itImpls = implementations.iterator();
		while (itImpls.hasNext()) {
			final Implementation actualImpl = itImpls.next();
			if (actualImpl.getDataClayID().equals(implementationID)) {
				return implementations.remove(actualImpl);
			}
		}

		return false;
	}

	/**
	 * Get the Operation::returnType
	 * @return the returnType
	 */

	public Type getReturnType() {
		return this.returnType;
	}

	/**
	 * Set the Operation::returnType
	 * @param newreturnType
	 *            the returnType
	 */

	public void setReturnType(final Type newreturnType) {
		this.returnType = newreturnType;
	}

	/**
	 * Get the Operation::metaClassID
	 * @return the metaClassID
	 */

	public MetaClassID getMetaClassID() {
		return metaClassID;
	}

	/**
	 * Set the Operation::metaClassID
	 * @param newmetaClassID
	 *            the metaClassID to set
	 */
	public void setMetaClassID(final MetaClassID newmetaClassID) {
		this.metaClassID = newmetaClassID;
	}

	/**
	 * Get the Operation::languageDepInfos
	 * @return the languageDepInfos
	 */
	public Map<Langs, LanguageDependantOperationInfo> getLanguageDepInfos() {
		return languageDepInfos;
	}

	/**
	 * Get Java language-dependant information of the operation
	 * @return Java language-dependant information of the operation
	 */
	public JavaOperationInfo getJavaOperationInfo() {
		return (JavaOperationInfo) languageDepInfos.get(Langs.LANG_JAVA);
	}

	/**
	 * Get Python language-dependant information of the operation
	 * @return Python language-dependant information of the operation
	 */
	public PythonOperationInfo getPythonOperationInfo() {
		return (PythonOperationInfo) languageDepInfos.get(Langs.LANG_PYTHON);
	}

	/**
	 * Set the Operation::languageDepInfos
	 * @param newlanguageDepInfos
	 *            the languageDepInfos to set
	 */
	public void setLanguageDepInfos(final Map<Langs, LanguageDependantOperationInfo> newlanguageDepInfos) {
		this.languageDepInfos = newlanguageDepInfos;
	}

	/**
	 * Add language dependant information
	 * @param langInfo
	 *            Language information
	 */
	public void addLanguageDepInfo(final LanguageDependantOperationInfo langInfo) {
		if (langInfo instanceof JavaOperationInfo) {
			if (this.languageDepInfos.get(Langs.LANG_JAVA) != null) {
				throw new OperationDepInfoAlreadyRegisteredException(Langs.LANG_JAVA.name());
			} else {
				this.languageDepInfos.put(Langs.LANG_JAVA, langInfo);
			}
		} else if (langInfo instanceof PythonOperationInfo) {
			if (this.languageDepInfos.get(Langs.LANG_PYTHON) != null) {
				throw new OperationDepInfoAlreadyRegisteredException(Langs.LANG_PYTHON.name());
			} else {
				this.languageDepInfos.put(Langs.LANG_PYTHON, langInfo);
			}
		}
	}

	@Override
	public String toString() {
		final Yaml yaml = CommonYAML.getYamlObject();
		return yaml.dump(this);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name, this.nameAndDescriptor,
				this.namespace, this.className,
				this.params, this.returnType, this.isAbstract);

	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof Operation) {
			final Operation other = (Operation) object;
			return this.name.equals(other.getName())
					&& this.className.equals(other.getClassName())
					&& this.namespace.equals(other.getNamespace())
					&& this.nameAndDescriptor.equals(other.getNameAndDescriptor());

		}
		return false;
	}

	/**
	 * Get the Operation::isAbstract
	 * @return the isAbstract
	 */
	public boolean getIsAbstract() {
		return isAbstract;
	}

	/**
	 * Set the Operation::isAbstract
	 * @param newisAbstract
	 *            the isAbstract to set
	 */
	public void setIsAbstract(final boolean newisAbstract) {
		this.isAbstract = newisAbstract;
	}

	/**
	 * Get the Operation::isStaticConstructor
	 * @return the isStaticConstructor
	 */
	public boolean getIsStaticConstructor() {
		return isStaticConstructor;
	}

	/**
	 * Set the Operation::isStaticConstructor
	 * @param newisStaticConstructor
	 *            the isStaticConstructor to set
	 */
	public void setIsStaticConstructor(final boolean newisStaticConstructor) {
		this.isStaticConstructor = newisStaticConstructor;
	}

	/**
	 * Get the Operation::namespaceID
	 * @return the namespaceID
	 */
	public NamespaceID getNamespaceID() {
		return namespaceID;
	}

	/**
	 * Set the Operation::namespaceID
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
	 * Generate operation signature
	 * @return Signature of the operation
	 */
	public String getSignature() {
		return this.signature;
	}

	/**
	 * Set the Operation::signature
	 * @param newsignature
	 *            the signature to set
	 */
	public void setSignature(final String newsignature) {
		this.signature = newsignature;
	}

	/**
	 * Get descriptor
	 * @return the descriptor
	 */
	public String getDescriptor() {
		return descriptor;
	}

	/**
	 * Set descriptor
	 * @param newdescriptor
	 *            the descriptor to set
	 */
	public void setDescriptor(final String newdescriptor) {
		this.descriptor = newdescriptor;
	}

	/**
	 * Get nameAndDescriptor
	 * @return the nameAndDescriptor
	 */
	public String getNameAndDescriptor() {
		return nameAndDescriptor;
	}

	/**
	 * Set nameAndDescriptor
	 * @param newnameAndDescriptor
	 *            the nameAndDescriptor to set
	 */
	public void setNameAndDescriptor(final String newnameAndDescriptor) {
		this.nameAndDescriptor = newnameAndDescriptor;
	}

	/**
	 * Get annotations
	 * @return List of operation's annotations
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
	}

	/**
	 * Add an annotation to the operation
	 * @param annotation
	 *            the annotation to add
	 */
	public void addAnnotation(final Annotation annotation) {
		annotations.add(annotation);
	}
}
