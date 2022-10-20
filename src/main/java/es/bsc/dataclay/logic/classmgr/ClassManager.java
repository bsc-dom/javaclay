
/**
 * @file ClassManager.java
 * @date Sep 6, 2012
 */

package es.bsc.dataclay.logic.classmgr;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import io.etcd.jetcd.Client;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.exceptions.DataClayRuntimeException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.exceptions.dbhandler.DbHandlerException;
import es.bsc.dataclay.exceptions.dbhandler.DbObjectNotExistException;
import es.bsc.dataclay.exceptions.logicmodule.classmgr.ClassInIncludesException;
import es.bsc.dataclay.exceptions.logicmodule.classmgr.ClassNotExistsException;
import es.bsc.dataclay.exceptions.logicmodule.classmgr.ImplementationNotExistException;
import es.bsc.dataclay.exceptions.logicmodule.classmgr.NoMoreImplementationsInOperationException;
import es.bsc.dataclay.exceptions.logicmodule.classmgr.OperationAlreadyInClassException;
import es.bsc.dataclay.exceptions.logicmodule.classmgr.OperationNotExistException;
import es.bsc.dataclay.exceptions.logicmodule.classmgr.OperationNotInClassException;
import es.bsc.dataclay.exceptions.logicmodule.classmgr.PropertyAlreadyInClassException;
import es.bsc.dataclay.exceptions.logicmodule.classmgr.PropertyNotExistException;
import es.bsc.dataclay.exceptions.logicmodule.classmgr.PropertyNotInClassException;
import es.bsc.dataclay.exceptions.logicmodule.classmgr.SetterOrGetterOperationsCannotBeRemoved;
import es.bsc.dataclay.logic.classmgr.bytecode.java.ByteCodeConstants;
import es.bsc.dataclay.logic.classmgr.bytecode.java.ExecutionByteCodeManager;
import es.bsc.dataclay.logic.classmgr.bytecode.java.StubByteCodeManager;
import es.bsc.dataclay.logic.classmgr.bytecode.java.merger.ByteCodeMerger;
import es.bsc.dataclay.logic.classmgr.bytecode.pysrc.StubPySourceManager;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.management.AbstractManager;
import es.bsc.dataclay.util.management.classmgr.AccessedImplementation;
import es.bsc.dataclay.util.management.classmgr.AccessedProperty;
import es.bsc.dataclay.util.management.classmgr.Implementation;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.classmgr.Operation;
import es.bsc.dataclay.util.management.classmgr.PrefetchingInformation;
import es.bsc.dataclay.util.management.classmgr.Property;
import es.bsc.dataclay.util.management.classmgr.Type;
import es.bsc.dataclay.util.management.classmgr.UserType;
import es.bsc.dataclay.util.management.classmgr.features.Feature;
import es.bsc.dataclay.util.management.classmgr.features.LanguageFeature;
import es.bsc.dataclay.util.management.classmgr.features.QualitativeFeature;
import es.bsc.dataclay.util.management.classmgr.features.QuantitativeFeature;
import es.bsc.dataclay.util.management.classmgr.features.Feature.FeatureType;
import es.bsc.dataclay.util.management.classmgr.java.JavaImplementation;
import es.bsc.dataclay.util.management.classmgr.java.JavaOperationInfo;
import es.bsc.dataclay.util.management.stubs.StubInfo;
import es.bsc.dataclay.util.reflection.Reflector;
import es.bsc.dataclay.util.structs.MemoryCache;
import es.bsc.dataclay.util.structs.Triple;
import es.bsc.dataclay.util.structs.Tuple;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteDataSource;

/**
 * This class is responsible to manage Classes, Operations, Properties and
 * Implementations, and Properties: add, remove and modify.
 * 
 * @version 2.0
 */
public final class ClassManager extends AbstractManager {
	private static final Logger logger = LogManager.getLogger("managers.ClassManager");

	/** Indicates if debug is enabled. */
	protected static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** Prefix of the getters created in a Stub. */
	public static final String GETTER_PREFIX = "$$get";
	/** Prefix of the setters created in a Stub. */
	public static final String SETTER_PREFIX = "$$set";

	/** Class cache. */
	private final MemoryCache<MetaClassID, MetaClass> classCache;
	/** Class cache by name and namespace id. */
	private final MemoryCache<Tuple<NamespaceID, String>, MetaClass> classCacheByName;
	/** Class cache by name and namespace. */
	private final MemoryCache<Tuple<String, String>, MetaClass> classCacheByNameAndNamespace;
	/** Operations cache. */
	private final MemoryCache<OperationID, Operation> operationsCache;
	/** Implementations cache. */
	private final MemoryCache<ImplementationID, Implementation> implementationsCache;
	/** Properties cache. */
	private final MemoryCache<PropertyID, Property> propertiesCache;

	/** DbHandler for the management of Database. */
	private final ClassManagerDB classDB;

	/** Cache of Stubs. */
	private final MemoryCache<StubInfo, Triple<String, byte[], byte[]>> stubsCache;

	/** ETCD */
	private final Client etcdClient;
	private final ETCDClassManagerDB ETCDclassDB;


	/**
	 * Instantiates an Class Manager that uses the Backend configuration
	 *        provided.
	 * @param dataSource data source
	 * @post Creates an Class manager and hash initializes the backend.
	 */
	public ClassManager(final SQLiteDataSource dataSource) {
		super(dataSource);
		classCache = new MemoryCache<>();
		classCacheByName = new MemoryCache<>();
		classCacheByNameAndNamespace = new MemoryCache<>();
		operationsCache = new MemoryCache<>();
		propertiesCache = new MemoryCache<>();
		implementationsCache = new MemoryCache<>();
		stubsCache = new MemoryCache<>();
		this.classDB = new ClassManagerDB(dataSource);
		this.classDB.createTables();

		// ETCD
		this.etcdClient = Client.builder().target("localhost:2379").build();
		ETCDclassDB = new ETCDClassManagerDB(etcdClient);
	}

	/**
	 * Empty caches of all references to the class with id provided or
	 *        operations and properties associated.
	 * @param classID
	 *            ID of class to analyze
	 */
	private void emptyClassInCaches(final MetaClassID classID) {
		// IMPORTANT: elements updated must be removed from Cache since references
		// obtained and modified
		// (like operations) are not same references than the ones in cache and CHANGES
		// ARE NOT REFLECTED.
		final MetaClass mClass = getClassFromDatabaseOrCache(classID);
		this.classCacheByName.remove(new Tuple<>(mClass.getNamespaceID(), mClass.getName()));
		classCache.remove(classID);
		for (final Operation op : mClass.getOperations()) {
			operationsCache.remove(op.getDataClayID());
			for (final Implementation impl : op.getImplementations()) {
				implementationsCache.remove(impl.getDataClayID());
			}
		}
		for (final Property prop : mClass.getProperties()) {
			propertiesCache.remove(prop.getDataClayID());
		}
	}

	/**
	 * Get a MetaClass from cache or database
	 * @param classID
	 *            ID of the class
	 * @return The MetaClass with ID provided
	 */
	private MetaClass getClassFromDatabaseOrCache(final MetaClassID classID) {
		MetaClass mClass = classCache.get(classID);
		if (mClass == null) {
			mClass = classDB.getMetaClassByID(classID);
			if (mClass == null) {
				throw new ClassNotExistsException(classID);
			} else {
				classCache.put(classID, mClass);
			}
		}
		return mClass;
	}

	/**
	 * Get a Operation from cache or database
	 * @param operationID
	 *            ID of the Operation
	 * @return The Operation with ID provided
	 */
	private Operation getOperationFromDatabaseOrCache(final OperationID operationID) {
		Operation op = operationsCache.get(operationID);
		if (op == null) {
			op = classDB.getOperationByID(operationID);
			if (op == null) {
				throw new OperationNotExistException(operationID);
			} else {
				operationsCache.put(operationID, op);
			}
		}

		return op;
	}

	/**
	 * Get a Implementation from cache or database
	 * @param implementationID
	 *            ID of the Implementation
	 * @return The Implementation with ID provided
	 */
	private Implementation getImplementationFromDatabaseOrCache(final ImplementationID implementationID) {
		Implementation impl = implementationsCache.get(implementationID);
		if (impl == null) {
			impl = classDB.getImplementationByID(implementationID);
			if (impl == null) {
				throw new ImplementationNotExistException(implementationID);
			} else {
				implementationsCache.put(implementationID, impl);
			}
		}
		return impl;
	}

	/**
	 * Get a Property from cache or database
	 * @param propertyID
	 *            ID of the Property
	 * @return The Property with ID provided
	 */
	private Property getPropertyFromDatabaseOrCache(final PropertyID propertyID) {
		Property prop = propertiesCache.get(propertyID);
		if (prop == null) {
			prop = classDB.getPropertyByID(propertyID);
			if (prop == null) {
				throw new PropertyNotExistException(propertyID);
			} else {
				propertiesCache.put(propertyID, prop);
			}
		}

		return prop;
	}

	// ============== Classes ==============//

	/**
	 * Creates a new class with specifications and IDs provided
	 * @param accountID
	 *            ID of the account registering the class to associate it with the
	 *            getters and setters implfaementations
	 * @param namespaceID
	 *            ID of the namespace of the new class
	 * @param namespace
	 *            Namespace of the class
	 * @param metaClass
	 *            ifications of the new class
	 * @param lang
	 *            Language of the class
	 * @return Information about the new class created with all the IDs.
	 */
	private MetaClass newClassInternal(final AccountID accountID, final NamespaceID namespaceID, final String namespace,
			final MetaClass metaClass, final Langs lang) {

		// If there are properties
		for (final Property prop : metaClass.getProperties()) {

			// if (lang.equals(Langs.LANG_JAVA)) {
			// We create the Getter Operation
			final Tuple<Operation, Implementation> newGetterOperationAndImpl = newGetterOperation(prop.getName(),
					metaClass, prop.getType(), prop.getDataClayID(), namespaceID, accountID, lang);
			final Operation newGetterOperation = newGetterOperationAndImpl.getFirst();
			final Implementation newGetterImplementation = newGetterOperationAndImpl.getSecond();
			// We create the Setter Operation
			final Tuple<Operation, Implementation> newSetterOperationAndImpl = newSetterOperation(prop.getName(),
					metaClass, prop.getType(), prop.getDataClayID(), namespaceID, accountID, lang);
			final Operation newSetterOperation = newSetterOperationAndImpl.getFirst();
			// We create the update Operation

			if (prop.isReplicated()) {
				final Tuple<Operation, Implementation> newupdateOperationAndImpl = newSetterOperation(
						ByteCodeConstants.DATACLAY_UPDATE + prop.getName(), metaClass, prop.getType(),
						prop.getDataClayID(), namespaceID, accountID, lang);
				prop.setUpdateImplementationID(newupdateOperationAndImpl.getSecond().getDataClayID());
				prop.setUpdateOperationID(newupdateOperationAndImpl.getFirst().getDataClayID());
				metaClass.addOperation(newupdateOperationAndImpl.getFirst());

			}

			prop.setGetterOperationID(newGetterOperation.getDataClayID());
			prop.setSetterOperationID(newSetterOperation.getDataClayID());
			prop.setGetterImplementationID(newGetterImplementation.getDataClayID());
			prop.setSetterImplementationID(newSetterOperationAndImpl.getSecond().getDataClayID());

			metaClass.addOperation(newGetterOperation);
			metaClass.addOperation(newSetterOperation);
			// }

		}

		// Set namespace
		metaClass.setNamespace(namespace);

		// Fill caches
		classCache.put(metaClass.getDataClayID(), metaClass);
		classCacheByName.put(new Tuple<>(metaClass.getNamespaceID(), metaClass.getName()), metaClass);
		for (final Property newProp : metaClass.getProperties()) {
			propertiesCache.put(newProp.getDataClayID(), newProp);
		}
		for (final Operation newOperation : metaClass.getOperations()) {
			// we create the Implementation in the System.
			int curImplPos = 0;
			for (final Implementation newImplementation : newOperation.getImplementations()) {
				newImplementation.setPosition(curImplPos);
				implementationsCache.put(newImplementation.getDataClayID(), newImplementation);
				curImplPos++;
			}
			operationsCache.put(newOperation.getDataClayID(), newOperation);
		}

		// We store now the new Metaclass on the Database
		classDB.storeMetaClass(metaClass);
		// ETCD
		ETCDclassDB.storeMetaClass(metaClass);

		logger.info("Registered class {} in namespace {} with ID {}", metaClass.getName(), namespace,
				metaClass.getDataClayID());

		return metaClass;

	}

	/**
	 * Apply new enrichment to class specified
	 * @param newEnrichmentClass
	 *            Enrichment metaclass information
	 */
	public void newJavaEnrichment(final MetaClass newEnrichmentClass) {

		// Get original class
		final MetaClass originalClass = getMetaClassByName(newEnrichmentClass.getNamespaceID(),
				newEnrichmentClass.getName());
		final byte[] originalByteCode = originalClass.getJavaClassInfo().getClassByteCode();
		final byte[] enrichmentByteCode = newEnrichmentClass.getJavaClassInfo().getClassByteCode();

		final byte[] newBytecode = ByteCodeMerger.mergeByteCodes(originalByteCode, enrichmentByteCode);
		originalClass.getJavaClassInfo().setClassByteCode(newBytecode);

		// Update it
		try {
			classDB.updateJavaClassByteCode(originalClass.getJavaClassInfo().getId(), newBytecode);
		} catch (final DbObjectNotExistException e) {
			throw new DbHandlerException(e.getMessage());

		}
	}

	/**
	 * This operation creates a new MetaClass in the Namespace specified by
	 *        namespaceID using the provided specifications.
	 * @param accountID
	 *            ID of the account registering the class for associating it with
	 *            implementations of setters and getters
	 * @param namespaceID
	 *            ID of the namespace within we will create the MetaClass
	 * @param namespace
	 *            Namespace of the class
	 * @param metaClass
	 *            New Class specifications
	 * @param lang
	 *            Language of the class
	 * @pre All IDs provided do not have object reference. The namespace identified
	 *      by namespaceID exists and is correct. The metaClassSpec provided does
	 *      not have more than one property with same name and no more than one
	 *      operation with same name, parameter type names and return type names.
	 * @post A MetaClass with the specification provided is created
	 * @return All information of the new MetaClass if it was successfully created.
	 */
	public MetaClass newClass(final AccountID accountID, final NamespaceID namespaceID, final String namespace,
			final MetaClass metaClass, final Langs lang) {
		return newClassInternal(accountID, namespaceID, namespace, metaClass, lang);
	}

	/**
	 * This function removes the MetaClass identified by metaClassID. It also
	 *        remove its associated operations, properties, implementations and
	 *        types.
	 * @param metaClassID
	 *            ID of the MetaClass of the Property to remove
	 * @pre All IDs provided do not have object reference.
	 * @pre The class exists
	 */
	public void removeClass(final MetaClassID metaClassID) {
		// First we must get the MetaClass
		final MetaClass metaclass = getClassFromDatabaseOrCache(metaClassID);
		// Now we can remove the MetaClass.
		classDB.deleteClass(metaclass.getDataClayID());
		// Remove from cache
		// this.metaClassCache.remove(metaClassID);
		/*
		 * for (Operation op : metaclass.getOperations()) {
		 * this.operationsCache.remove(op.getID()); for (Implementation impl :
		 * op.getImplementations()) { this.implementationsCache.remove(impl.getID()); }
		 * }
		 */
	}

	/**
	 * This operation verifies if the namespace provided has some class
	 *        associated in the System.
	 * @param namespaceID
	 *            ID of the namespace
	 * @pre All IDs provided do not have object reference.
	 * @return TRUE if the provided Namespace has no classes in the database.FALSE
	 *         otherwise.
	 * @throws Exception
	 *             if an internal error occurred while working with Database.
	 * @note When you delete a MetaClass its namespaceID associated is deleted. Keep
	 *       in mind that a namespaceID associated to the MetaClass has no object
	 *       reference and can be replicated.
	 */
	public boolean checkNamespaceHasNothing(final NamespaceID namespaceID) {
		final boolean exists = classDB.existsClassInNamespace(namespaceID);
		// If exists return false. True, otherwise.
		return !exists;
	}

	// ============== Properties =============//

	/**
	 * This operation creates a new Property in the System, its Types
	 *        specified and associates it to the MetaClass identified by the
	 *        metaClassID provided. It also creates new Getter and Setter Operations
	 *        for the Property provided.
	 * @pre All IDs provided do not have object reference. The propertySpec provided
	 *      has an associated TypeSpec. The TypeSpec indicates if it is primitive or
	 *      not, and must indicates its name.
	 * @param accountID
	 *            ID of the account registering the property to be associated with
	 *            the corresponding setter and getter implementations
	 * @param namespaceID
	 *            ID of the namespace in which the property is created
	 * @param newProperty
	 *            Specifications of the Property to add
	 * @param metaClassID
	 *            ID of the MetaClass that will contain the Property
	 * @post 1.- A Property with the specifications provided is created. <br>
	 *       2.- The Property is associated to the MetaClass provided. <br>
	 *       3.- The MetaClass is updated.
	 * @return information of the new Property.
	 */
	public Property newProperty(final AccountID accountID, final NamespaceID namespaceID, final MetaClassID metaClassID,
			final Property newProperty) {

		// We get the MetaClass in which to add the Property.
		final MetaClass clazz = getClassFromDatabaseOrCache(metaClassID);

		// verify that no other Property with same name exists
		if (clazz.existsPropertyInClass(newProperty.getName())) {
			throw new PropertyAlreadyInClassException(newProperty.getName(), clazz.getDataClayID());
		}

		// static final properties have no getter nor setter
		// We create the Getter Operation

		// TODO: ATTENTION! LANGUAGE OF GETTER IS ONLY JAVA BECAUSE NO ENRICHMENT IN
		// PYTHON
		final Tuple<Operation, Implementation> newGetterOperationAndImpl = newGetterOperation(newProperty.getName(),
				clazz, newProperty.getType(), newProperty.getDataClayID(), namespaceID, accountID, Langs.LANG_JAVA);
		final Operation newGetterOperation = newGetterOperationAndImpl.getFirst();
		final Implementation newGetterImplementation = newGetterOperationAndImpl.getSecond();

		// We create the Setter Operation
		final Tuple<Operation, Implementation> newSetterOperationAndImpl = newSetterOperation(newProperty.getName(),
				clazz, newProperty.getType(), newProperty.getDataClayID(), namespaceID, accountID, Langs.LANG_JAVA);
		final Operation newSetterOperation = newSetterOperationAndImpl.getFirst();

		// We create the update Operation

		newProperty.setSetterOperationID(newSetterOperation.getDataClayID());
		newProperty.setGetterOperationID(newGetterOperation.getDataClayID());
		newProperty.setGetterImplementationID(newGetterImplementation.getDataClayID());
		newProperty.setSetterImplementationID(newSetterOperationAndImpl.getSecond().getDataClayID());
		newProperty.setNamespaceID(namespaceID);

		// We add the PropertyID to the MetaClass obtained before
		clazz.addOperation(newGetterOperation);
		clazz.addOperation(newSetterOperation);
		clazz.addPropertyAsEnrichment(newProperty);

		Operation newUpdateOperation = null;
		if (newProperty.isReplicated()) {
			final Tuple<Operation, Implementation> newUpdateOperationAndImpl = newSetterOperation(
					ByteCodeConstants.DATACLAY_UPDATE + newProperty.getName(), clazz, newProperty.getType(),
					newProperty.getDataClayID(), namespaceID, accountID, Langs.LANG_JAVA);
			newUpdateOperation = newUpdateOperationAndImpl.getFirst();
			newProperty.setUpdateImplementationID(newUpdateOperationAndImpl.getSecond().getDataClayID());
			newProperty.setUpdateOperationID(newUpdateOperation.getDataClayID());
			clazz.addOperation(newUpdateOperation);
		}

		try {

			classDB.updateClassPropertiesAndOperations(clazz.getDataClayID(), newProperty, newSetterOperation,
					newGetterOperation, newUpdateOperation);

			classDB.updateJavaClassByteCode(clazz.getJavaClassInfo().getId(),
					clazz.getJavaClassInfo().getClassByteCode());
			emptyClassInCaches(clazz.getDataClayID());

		} catch (final DbObjectNotExistException e) {
			throw new DbHandlerException(e.getMessage());
		}

		return newProperty;

	}

	/**
	 * This function creates an implementation for the Getter property
	 *        operations.
	 * @param propName
	 *            Name of the property to get
	 * @param metaClass
	 *            MetaClass containing the Property
	 * @param returnType
	 *            Return Type of the getter.
	 * @param propertyID
	 *            Accessed propertyID
	 * @param originalNamespaceID
	 *            ID of the namespace in which the operation must be created
	 * @param responsibleOfProperty
	 *            ID of the account that registered the property
	 * @param lang
	 *            language of setter
	 * @return The Operation of the Getter Operation for the Property specified.
	 * 
	 */
	private Tuple<Operation, Implementation> newGetterOperation(final String propName, final MetaClass metaClass,
			final Type returnType, final PropertyID propertyID, final NamespaceID originalNamespaceID,
			final AccountID responsibleOfProperty, final Langs lang) {

		String opNameAndDescriptor = null;
		String getterDesc = null;
		String getterSignature = null;

		if (lang.equals(Langs.LANG_JAVA)) {
			// TODO: Fix Python side to use signatures
			opNameAndDescriptor = GETTER_PREFIX + propName + "()" + returnType.getDescriptor();
		} else {
			opNameAndDescriptor = GETTER_PREFIX + propName;
		}

		getterDesc = "()" + returnType.getDescriptor();
		if (returnType.getSignature() != null) {
			getterSignature = "()" + returnType.getSignature();
		}
		final Operation getter = new Operation(GETTER_PREFIX + propName, getterDesc, getterSignature,
				opNameAndDescriptor, metaClass.getNamespace(), metaClass.getName(), false);
		getter.setMetaClassID(metaClass.getDataClayID());
		getter.setDataClayID(new OperationID());
		getter.setNamespaceID(metaClass.getNamespaceID());
		final JavaOperationInfo javaOpInfo = new JavaOperationInfo(Modifier.PUBLIC);
		getter.addLanguageDepInfo(javaOpInfo);
		getter.setDataClayID(new OperationID());

		final List<AccessedProperty> accessedProperties = new ArrayList<>();
		final AccessedProperty accProp = new AccessedProperty(metaClass.getNamespace(), metaClass.getName(), propName);
		accProp.setPropertyID(propertyID);
		accessedProperties.add(accProp);

		final List<Type> includedTypes = new ArrayList<>();
		if (returnType instanceof UserType) {
			includedTypes.add(returnType);
		}

		// IMPORTANT: GETTERS AND SETTERS CODE IS GENERATED WHILE CREATING
		// EXECUTION/STUBS CLASSES
		final Implementation implementation = new JavaImplementation(0, accessedProperties,
				new ArrayList<AccessedImplementation>(), includedTypes, new PrefetchingInformation(),
				new HashMap<FeatureType, QuantitativeFeature>(), new HashMap<FeatureType, QualitativeFeature>(),
				metaClass.getNamespace(), metaClass.getName(), getter.getNameAndDescriptor());

		implementation.setDataClayID(new ImplementationID());
		implementation.setMetaClassID(metaClass.getDataClayID());
		implementation.setNamespaceID(metaClass.getNamespaceID());
		implementation.setOperationID(getter.getDataClayID());
		implementation.setResponsibleAccountID(responsibleOfProperty);

		// TODO: required feature for now in getter operations is Java 1.8
		implementation.getRequiredQualitativeFeatures().put(FeatureType.LANGUAGE, new LanguageFeature("Java", "1.8"));
		getter.addImplementation(implementation);
		getter.setReturnType(returnType);

		return new Tuple<>(getter, implementation);
	}

	/**
	 * This function creates an implementation for the Setter property
	 *        operations.
	 * @param propName
	 *            Name of the property to set
	 * @param metaClass
	 *            MetaClass containing the Property
	 * @param paramType
	 *            Param Type of the setter.
	 * @param propertyID
	 *            Accessed propertyID
	 * @param originalNamespaceID
	 *            ID of the namespace in which the operation must be created
	 * @param lang
	 *            language of setter
	 * @param responsibleOfProperty
	 *            ID of the account that registered the property
	 * @return The Operation of the Setter Operation for the Property specified.
	 */
	private Tuple<Operation, Implementation> newSetterOperation(final String propName, final MetaClass metaClass,
			final Type paramType, final PropertyID propertyID, final NamespaceID originalNamespaceID,
			final AccountID responsibleOfProperty, final Langs lang) {

		String opNameAndDescriptor = null;

		if (lang.equals(Langs.LANG_JAVA)) {
			// TODO: Fix Python side to use signatures
			if (propName.contains(ByteCodeConstants.DATACLAY_UPDATE)) {
				opNameAndDescriptor = SETTER_PREFIX + propName + "(" + paramType.getDescriptor()
						+ "Ljava/lang/Boolean;)V";
			} else {
				opNameAndDescriptor = SETTER_PREFIX + propName + "(" + paramType.getDescriptor() + ")V";
			}
		} else {
			opNameAndDescriptor = SETTER_PREFIX + propName;
		}

		final String setterDesc = "()" + "(" + paramType.getDescriptor() + ")V";
		String setterSignature = null;
		if (paramType.getSignature() != null) {
			if (propName.contains(ByteCodeConstants.DATACLAY_UPDATE)) {
				setterSignature = "(" + paramType.getSignature()
					+ "Ljava/lang/Boolean;)V";
			} else {
				setterSignature = "(" + paramType.getSignature() + ")V";
			}
		}
		final Operation setter = new Operation(SETTER_PREFIX + propName, setterDesc, setterSignature,
				opNameAndDescriptor, metaClass.getNamespace(), metaClass.getName(), false);
		setter.setMetaClassID(metaClass.getDataClayID());
		setter.setDataClayID(new OperationID());
		setter.setNamespaceID(metaClass.getNamespaceID());
		final JavaOperationInfo javaOpInfo = new JavaOperationInfo(Modifier.PUBLIC);
		setter.addLanguageDepInfo(javaOpInfo);
		setter.setDataClayID(new OperationID());

		final List<AccessedProperty> accessedProperties = new ArrayList<>();
		final AccessedProperty accProp = new AccessedProperty(metaClass.getNamespace(), metaClass.getName(), propName);
		accProp.setPropertyID(propertyID);
		accessedProperties.add(accProp);

		final List<Type> includedTypes = new ArrayList<>();
		if (paramType instanceof UserType) {
			includedTypes.add(paramType);
		}

		// IMPORTANT: GETTERS AND SETTERS CODE IS GENERATED WHILE CREATING
		// EXECUTION/STUBS CLASSES
		final Implementation implementation = new JavaImplementation(0, accessedProperties,
				new ArrayList<AccessedImplementation>(), includedTypes, new PrefetchingInformation(),
				new HashMap<FeatureType, QuantitativeFeature>(), new HashMap<FeatureType, QualitativeFeature>(),
				metaClass.getNamespace(), metaClass.getName(), setter.getNameAndDescriptor());

		implementation.setDataClayID(new ImplementationID());
		implementation.setMetaClassID(metaClass.getDataClayID());
		implementation.setNamespaceID(metaClass.getNamespaceID());
		implementation.setOperationID(setter.getDataClayID());
		implementation.setResponsibleAccountID(responsibleOfProperty);

		// TODO: required feature for now in getter operations is Java 1.8
		implementation.getRequiredQualitativeFeatures().put(FeatureType.LANGUAGE, new LanguageFeature("Java", "1.8"));
		setter.addImplementation(implementation);
		setter.addParam("param0", paramType);
		if (propName.contains(ByteCodeConstants.DATACLAY_UPDATE)) {
			setter.addParam("param1", 
					new Type("Ljava/lang/Boolean;", "Ljava/lang/Boolean;", Reflector.getTypeNameFromSignatureOrDescriptor("Ljava/lang/Boolean;")));
		}
		final Type returnType = new Type("V", null, "void");
		setter.setReturnType(returnType);
		return new Tuple<>(setter, implementation);
	}
	
	

	// ================== Operations ==================//

	/**
	 * This operation creates a new Operation in the System, its
	 *        Implementations and Types (arguments) specified and associates it to
	 *        the MetaClass identified by the metaClassID provided.
	 * @pre All IDs provided do not have object reference. OperationSpec provided
	 *      has N implementationSpec and K TypeSpec. The TypeSpecs must specify if
	 *      they are primitive or not and its name. The implementationSpec must have
	 *      the implementation code associated and not null.
	 * @param namespaceID
	 *            ID of the namespace in which the operation is created
	 * @param newOperation
	 *            Specification of the operation
	 * @param metaClassID
	 *            ID of the MetaClass that will contain the operation.
	 * @post 1.- An Operation with the provided specifications is created. <br>
	 *       2.- For each parameter, a new Type is created. <br>
	 *       3.- Each Type added to the System is associated to the Operation
	 *       created before. <br>
	 *       4.- The Operation is associated to the MetaClass identified by
	 *       metaClassID. <br>
	 *       5.- The MetaClass is updated.
	 * @return information of the new Operation.
	 */
	public Operation newOperation(final NamespaceID namespaceID, final MetaClassID metaClassID,
			final Operation newOperation) {

		// We get the MetaClass in which to add the Operation.
		final MetaClass clazz = getClassFromDatabaseOrCache(metaClassID);

		// We verify that there is not another Operation in the MetaClass with
		// the same name, params and return.
		for (final Operation operation : clazz.getOperations()) {
			if (operation.getNameAndDescriptor().equals(newOperation.getNameAndDescriptor())) {

				throw new OperationAlreadyInClassException(operation.getNameAndDescriptor(), operation.getNamespaceID(),
						clazz.getDataClayID());

			}

		}

		// Now we can add the Operation to the System
		clazz.addOperation(newOperation);

		if (DEBUG_ENABLED) {
			String log = "Added new operation " + newOperation.getName() + " with OperationID "
					+ newOperation.getDataClayID() + " and implementations: ";
			for (final Implementation newImplementation : newOperation.getImplementations()) {
				log += " ," + newImplementation.getDataClayID();
			}
			logger.debug(log);
		}

		// We update the MetaClass
		try {
			classDB.updateClassAddOperation(clazz.getDataClayID(), newOperation);
			emptyClassInCaches(clazz.getDataClayID());

		} catch (final DbObjectNotExistException e) {
			throw new DbHandlerException(e.getMessage());

		}

		return newOperation;
	}

	/**
	 * This function deletes an Operation identified by the operationID
	 *        provided, and its associated arguments and implementations. The
	 *        operation's MetaClass is updated.
	 * @param operationID
	 *            ID of the operation to remove
	 * @pre All IDs provided do not have object reference. Operation identified by
	 *      operationID has a MetaClassID associated which exists and has the
	 *      OperationID in its list of operation IDs. The Operation in database has
	 *      its parameter Types, return Types and implementations as object
	 *      references.
	 * @pre The class exists
	 * @post 1.- The Implementations of the Operation identified by operationID are
	 *       removed from the System. <br>
	 *       2.- The Types (arguments) of the Operation identified by operationID
	 *       are removed from the System. <br>
	 *       3.- The Operation identified by operationID is removed from the System.
	 *       <br>
	 *       4.- The MetaClass associated to the Operation removes the Property from
	 *       its list of Operation and is updated in the System.
	 * @return Info of the removed operation
	 */
	public Operation removeOperation(final OperationID operationID) {

		// First we get the Operation and check if it exists
		final Operation operation = getOperationFromDatabaseOrCache(operationID);

		// TODO Probably we should define setter/getter boolean flags to check this
		// better (28 Nov 2013 jmarti)
		if (operation.getName().startsWith(GETTER_PREFIX) || operation.getName().startsWith(SETTER_PREFIX)) {
			throw new SetterOrGetterOperationsCannotBeRemoved(operationID);
		}

		// Now we must get the MetaClass (we know it exists and has the
		// operationID associated)
		final MetaClass metaClass = getClassFromDatabaseOrCache(operation.getMetaClassID());
		metaClass.removeOperation(operationID);

		// We update the MetaClass and remove the Operation from database
		try {
			classDB.updateClassRemoveOperation(metaClass.getDataClayID(), operationID);
			// Remove return type, parameters and implementations of the operation.
			classDB.deleteOperation(operation.getDataClayID());

			emptyClassInCaches(metaClass.getDataClayID());

		} catch (final DbObjectNotExistException ex) {
			logger.debug("removeOperation error", ex);
			throw new DbHandlerException(ex.getMessage());

		}
		// classDB.deleteByID(operation.getID());
		return operation;

	}

	/**
	 * This function gets the list of Implementation IDs of an Operation
	 *        identified by the OperationID provided.
	 * @param operationID
	 *            ID of the Operation.
	 * @pre All IDs provided do not have object reference. Operation identified by
	 *      operationID has a MetaClassID associated which exists and has the
	 *      OperationID in its list of operation IDs. The Operation in database has
	 *      its parameter Types, return Types and implementations as object
	 *      references.
	 * @return The list of Implementation IDs of the Operation
	 * @throws Exception
	 *             If the Operation identified by operationID does not exist or if
	 *             an internal error occurred while working with Database.
	 */
	public LinkedList<ImplementationID> getImplementationsOfOperation(final OperationID operationID) {

		// First we get the Operation and check if it exists
		final Operation operation = getOperationFromDatabaseOrCache(operationID);

		// Since we cannot return object references we must create new iDs
		final LinkedList<ImplementationID> res = new LinkedList<>();
		final List<Implementation> impls = operation.getImplementations();
		for (final Implementation impl : impls) {
			res.add(impl.getDataClayID());
		}

		// Now we get the return the Implementations of the Operation
		return res;
	}

	// ================== Implementations ================== //

	/**
	 * This function creates a new Implementation in the System and
	 *        associates it to the Operation identified by the operationID provided.
	 * @param accountID
	 *            ID of the account responsible of the implementation
	 * @param namespaceID
	 *            ID of the namespace in which the implementation is created
	 * @param metaClassID
	 *            ID of the class containing the operation
	 * @param newImplementation
	 *            Specification of the Implementation
	 * @param operationID
	 *            ID of the Operation that will contain the Implementation.
	 * @param namespaceIDofOperation
	 *            the namespace ID of the operation
	 * @pre All IDs provided do not have object reference. The implementationSpec
	 *      provided must have implementation code different of null. Operation
	 *      identified by operationID has a MetaClassID associated which exists and
	 *      has the OperationID in its list of operation IDs. The Operation in
	 *      database has its parameter Types, return Types and implementations as
	 *      object references. does not belong to the class with ID provided (same
	 *      parameter type names and return type name) with the operation identified
	 *      by the ID provided.
	 * @post 1.- An Implementation with the provided specifications is created. <br>
	 *       2.- The Implementation is associated to the Operation identified by
	 *       operationID.<br>
	 *       3.- The Operation is updated with the new Implementation.
	 * @return information of the new Implementation.
	 * @throws Exception
	 *             If the Operation identified by operationID does not exist or does
	 *             not belong to the class with ID provided, If implementation is
	 *             not compatible or if an internal error occurred while working
	 *             with Database.
	 */
	public Implementation newImplementation(final AccountID accountID, final NamespaceID namespaceID,
			final MetaClassID metaClassID, final OperationID operationID, final NamespaceID namespaceIDofOperation,
			final Implementation newImplementation) {

		// We create a new Implementation and we get its generated
		// ImplementationID
		final MetaClass metaClass = getClassFromDatabaseOrCache(metaClassID);
		final Operation operation = metaClass.getOperation(operationID);

		// if the operation does not belong to the class with ID provided throw
		// an error
		if (operation == null || !operation.getMetaClassID().equals(metaClassID)
				|| !operation.getNamespaceID().equals(namespaceIDofOperation)) {
			throw new OperationNotExistException(operationID);
		}

		// We add the ImplementationID to the Operation
		operation.addImplementation(newImplementation);

		if (DEBUG_ENABLED) {
			String log = "Added new implementation with ImplementationID " + newImplementation.getDataClayID()
					+ " to operation " + operation.getName() + " with OperationID " + operation.getDataClayID();
			logger.debug(log);
			log = "Now operation " + operation.getName() + " with OperationID " + operation.getDataClayID()
					+ " has implementations: ";
			for (final Implementation impl : operation.getImplementations()) {
				log += " ," + impl.getDataClayID();
			}
			logger.debug(log);
		}

		// We update the Operation containing the implementation
		try {
			classDB.updateOperationAddImplementation(operation.getDataClayID(), newImplementation);
			emptyClassInCaches(metaClass.getDataClayID());

		} catch (final DbObjectNotExistException e) {
			throw new OperationNotExistException(operation.getDataClayID());
		}

		return newImplementation;
	}

	/**
	 * This function removes an Implementation with the implementationID
	 *        provided from the System and updates the associated Operation.
	 * @param implementationID
	 *            ID of the Implementation to remove
	 * @pre All IDs provided do not have object reference. Implementation identified
	 *      by implementationID has an OperationID associated which exists and has
	 *      the implementationID in its list of implementation IDs. It also has a
	 *      MetaClassID associated which exists and has the OperationID in its list
	 *      of operation IDs. The Operation in database has its parameter Types,
	 *      return Types and implementations as object references. only has this
	 *      implementation
	 * @post 1.- The Type of the Property identified by propertyID is removed from
	 *       the System. <br>
	 *       2.- The Property identified by propertyID is removed from the System.
	 *       <br>
	 *       3.- The MetaClass associated to the Property removes the Property from
	 *       its list of Properties and is updated in the System.
	 * @throws Exception
	 *             If the Implementation identified by implementationID does not
	 *             exist, If the Operation associated to the Implementation does not
	 *             exist, If the implementation cannot be removed because the
	 *             Operation only has this implementation or if an internal error
	 *             occurred while working with Database.
	 */
	public void removeImplementation(final ImplementationID implementationID) {

		// First we get the Implementation and check if it exists
		final Implementation implementation = getImplementationFromDatabaseOrCache(implementationID);

		// First we get the Operation (we know it exists and it is correct)
		final Operation operation = getOperationFromDatabaseOrCache(implementation.getOperationID());

		if (operation.getImplementations().size() == 1) {
			throw new NoMoreImplementationsInOperationException(implementationID, operation.getDataClayID());
		}

		final MetaClass metaClass = getClassFromDatabaseOrCache(operation.getMetaClassID());

		operation.removeImplementation(implementation.getDataClayID());

		// We update the Operation containing the implementation and remove the
		// implementation from database
		try {
			classDB.deleteImplementation(implementation);
			classDB.updateOperationRemoveImplementation(operation.getDataClayID(), implementation.getDataClayID());
			emptyClassInCaches(metaClass.getDataClayID());

		} catch (final DbObjectNotExistException e) {
			throw new OperationNotExistException(implementation.getOperationID());
		}
	}

	/**
	 * This function gets the list of Requirements of an Implementation
	 *        identified by the ImplementationID provided.
	 * @param implementationID
	 *            ID of the Implementation.
	 * @pre All IDs provided do not have object reference. Implementation identified
	 *      by implementationID has an OperationID associated which exists and has
	 *      the implementationID in its list of implementation IDs. It also has a
	 *      MetaClassID associated which exists and has the OperationID in its list
	 *      of operation IDs. The Operation in database has its parameter Types,
	 *      return Types and implementations as object references.
	 * @return The list of Features (requirements) of the Implementation
	 * @throws Exception
	 *             If the Implementation identified by implementationID does not
	 *             exist or if an internal error occurred while working with
	 *             Database.
	 */
	public List<Feature> getRequirementsOfMethod(final ImplementationID implementationID) {

		// First we get the Implementation and check if it exists
		final Implementation implementation = getImplementationFromDatabaseOrCache(implementationID);
		final List<Feature> allFeatures = new ArrayList<>();
		allFeatures.addAll(implementation.getRequiredQualitativeFeatures().values());
		allFeatures.addAll(implementation.getRequiredQuantitativeFeatures().values());
		return allFeatures;
	}

	/**
	 * This function retrieves the information of the given implementations
	 * @param implementationsIDs
	 *            IDs of the implementations
	 * @return the information about every implementation
	 * @throws Exception
	 *             if any of the implementations does not exist
	 */
	public Map<ImplementationID, Implementation> getInfoOfImplementations(
			final Set<ImplementationID> implementationsIDs) {
		final Map<ImplementationID, Implementation> result = new HashMap<>();
		for (final ImplementationID implementationID : implementationsIDs) {
			final Implementation implementation = getImplementationFromDatabaseOrCache(implementationID);
			result.put(implementationID, implementation);
		}
		return result;
	}

	/**
	 * This function gets the MetaClass with name className and namespaceID
	 *        provided
	 * @param namespaceID
	 *            Namespace ID of the MetaClass to look for
	 * @param className
	 *            Name of the class to look for
	 * @return theMetaClass with name and namespace ID provided or NULL if not
	 *         found.
	 */
	private MetaClass getMetaClassByName(final NamespaceID namespaceID, final String className) {
		final Tuple<NamespaceID, String> key = new Tuple<>(namespaceID, className);
		MetaClass metaclass = classCacheByName.get(new Tuple<>(namespaceID, className));
		if (metaclass == null) {
			metaclass = classDB.getClassByNameAndNamespaceID(className, namespaceID);
			classCacheByName.put(key, metaclass);
		}
		return metaclass;
	}

	/**
	 * This function gets the MetaClass with name className and Namespace
	 *        provided
	 * @param namespace
	 *            Namespace of the MetaClass to look for
	 * @param className
	 *            Name of the class to look for
	 * @return theMetaClass with name and namespace ID provided or NULL if not
	 *         found.
	 */
	public MetaClass getMetaClassByNameAndNamespace(final String namespace, final String className) {
		final Tuple<String, String> key = new Tuple<>(namespace, className);
		MetaClass metaclass = classCacheByNameAndNamespace.get(new Tuple<>(namespace, className));
		if (metaclass == null) {
			metaclass = classDB.getClassByNameAndNamespace(className, namespace);
			classCacheByNameAndNamespace.put(key, metaclass);
		}
		return metaclass;
	}

	/**
	 * Retrieves the ID of a class given its name
	 * @pre All IDs provided do not have object reference.
	 * @param namespaceID
	 *            ID of the namespace containing the class
	 * @param className
	 *            Name of the MetaClass
	 * @return The ID of the class or NULL if class does not exist
	 */
	public MetaClassID getMetaClassID(final NamespaceID namespaceID, final String className) {
		// TODO Unify caches and only use namespaceIDs!! (jmarti 5 Apr 2018)
		final MetaClass metaclass = classCacheByName.get(new Tuple<>(namespaceID, className));
		if (metaclass == null) {
			return classDB.getClassIDByNameAndNamespaceID(className, namespaceID);
		} else {
			return metaclass.getDataClayID();
		}
	}

	/**
	 * Retrieves the id of a property given its name, name of the class
	 *        containing the property and the namespace where it belongs to.
	 * @pre All IDs provided do not have object reference.
	 * @pre The class exists
	 * @param metaClassName
	 *            Name of the class
	 * @param namespace
	 *            Namespace of the class
	 * @param propertyName
	 *            the name of the property
	 * @return The ID of the property
	 */
	public PropertyID getPropertyID(final String metaClassName, final String namespace, final String propertyName) {
		final Property property = classDB.getPropertyByNames(propertyName, metaClassName, namespace);
		if (property == null) {
			throw new PropertyNotExistException(metaClassName, propertyName);
		}
		// We just return the ID and not the ref. object.
		return property.getDataClayID();
	}

	/**
	 * Retrieves the id of a property given its name, name of the class
	 *        containing the property and the namespace where it belongs to.
	 * @pre All IDs provided do not have object reference.
	 * @pre The class exists
	 * @param metaClassID
	 *            ID of the class
	 * @param propertyName
	 *            the name of the property
	 * @return The ID of the property
	 */
	public PropertyID getPropertyID(final MetaClassID metaClassID, final String propertyName) {

		// Get the class
		final MetaClass metaClass = getClassFromDatabaseOrCache(metaClassID);

		Property property = null;
		for (final Property prop : metaClass.getProperties()) {
			if (prop.getName().equals(propertyName)) {
				property = prop;
				break;
			}
		}
		if (property == null) {
			throw new PropertyNotExistException(metaClass.getName(), propertyName);
		}
		// We just return the ID and not the ref. object.
		return property.getDataClayID();
	}

	/**
	 * Retrieves the names of the properties with IDs provided.
	 * @param propertiesIDs
	 *            IDs of the properties
	 * @return Return the names of the properties with IDs provided
	 */
	public Map<PropertyID, String> getPropertiesNames(final Set<PropertyID> propertiesIDs) {

		final Map<PropertyID, String> result = new HashMap<>();
		for (final PropertyID propertyID : propertiesIDs) {
			final Property property = getPropertyFromDatabaseOrCache(propertyID);
			result.put(propertyID, property.getName());
		}
		return result;

	}

	/**
	 * Retrieves the id of an operation given its signature, id of the
	 *        namespace of the operation and id of the class containing the
	 *        operation and the namespace where it belongs to
	 * @param metaClassID
	 *            ID of the class
	 * @param operationSignature
	 *            the signature of the operation
	 * @pre All IDs provided do not have object reference.
	 * @return The ID of the operation
	 * @throws Exception
	 *             If operation does not exist
	 */
	public OperationID getOperationID(final MetaClassID metaClassID, final String operationSignature) {

		// Get the class
		final MetaClass metaClass = getClassFromDatabaseOrCache(metaClassID);
		OperationID operation = null;
		for (final Operation op : metaClass.getOperations()) {
			if (op.getNameAndDescriptor().equals(operationSignature)) {
				operation = op.getDataClayID();
				break;
			}

		}
		if (operation == null) {
			throw new OperationNotExistException(metaClass.getName(), operationSignature);
		}

		return operation;
	}

	/**
	 * Retrieves the id of an operation given its signature, id of the
	 *        namespace of the operation and id of the class containing the
	 *        operation and the namespace where it belongs to
	 * @param metaClassName
	 *            Name of the class
	 * @param namespace
	 *            Namespace of the class
	 * @param operationSignature
	 *            the signature of the operation
	 * @pre All IDs provided do not have object reference.
	 * @return The ID of the operation
	 * @throws Exception
	 *             If operation does not exist
	 */
	public OperationID getOperationID(final String metaClassName, final String namespace,
			final String operationSignature) {

		final Operation operation = classDB.getOperationByNames(operationSignature, metaClassName, namespace);
		if (operation == null) {
			throw new OperationNotExistException(metaClassName, operationSignature);
		}

		return operation.getDataClayID();
	}

	/**
	 * Returns the name of the MetaClass identified by ID provided
	 * @param metaClassID
	 *            ID of the MetaClass
	 * @return The name of the MetaClass identified by the ID provided
	 * @pre The class exists
	 */
	public String getClassname(final MetaClassID metaClassID) {
		return getClassNameInternal(metaClassID);
	}

	/**
	 * Returns the name and namespace of the MetaClass identified by ID
	 *        provided
	 * @param metaClassID
	 *            ID of the MetaClass
	 * @return The name and namespace of the MetaClass identified by the ID provided
	 * @pre The class exists
	 */
	public Tuple<String, String> getClassNameAndNamespace(final MetaClassID metaClassID) {
		final MetaClass metaClass = getClassFromDatabaseOrCache(metaClassID);
		return new Tuple<>(metaClass.getName(), metaClass.getNamespace());
	}

	/**
	 * Return the name of the class with Id provided
	 * @param metaClassID
	 *            ID of the class
	 * @return The name of the class with id proved
	 */
	private String getClassNameInternal(final MetaClassID metaClassID) {
		final MetaClass metaClass = getClassFromDatabaseOrCache(metaClassID);
		return metaClass.getName();
	}

	/**
	 * Generate a set of Java stubs with the provided informations
	 * @pre The class exists
	 * @param language
	 *            Language of the stub to generate
	 * @param stubInfos
	 *            Information associated to each stub (names, contract IDs,
	 *            interface IDs...)
	 * @return Byte code of the stubs of the classes with information provided and
	 *         its aspect
	 * @throws Exception
	 *             if an internal error occurred while working with Database.
	 */
	public Map<MetaClassID, Triple<String, byte[], byte[]>> generateStubs(final Langs language,
			final Map<MetaClassID, StubInfo> stubInfos) {

		final Map<MetaClassID, Triple<String, byte[], byte[]>> stubs = new HashMap<>();
		for (final Entry<MetaClassID, StubInfo> entry : stubInfos.entrySet()) {

			final MetaClassID metaClassID = entry.getKey();
			final StubInfo stubInfo = entry.getValue();
			// Get the metaclass
			final MetaClass metaClass = getClassFromDatabaseOrCache(metaClassID);

			Triple<String, byte[], byte[]> stubAndIncludes = stubsCache.get(stubInfo);
			if (stubAndIncludes == null) {
				// Get parent name
				String realParentName = null;
				if (metaClass.getParentType() == null) {
					realParentName = DataClayObject.class.getName();
				} else {
					final MetaClass parentClass = getClassFromDatabaseOrCache(metaClass.getParentType().getClassID());
					realParentName = parentClass.getName();
				}

				final List<MetaClass> accessedClasses = getAllIncludes(metaClass);

				final byte[] byteCode;
				byte[] aspectBytes = null;
				if (language == Langs.LANG_JAVA) {
					byteCode = StubByteCodeManager.generateJavaStub(metaClass, stubInfo, accessedClasses, this);
					aspectBytes = StubByteCodeManager.generateStubAspect(metaClass, false, this);
				} else if (language == Langs.LANG_PYTHON) {

					// TODO: MODIFY THIS FOR EMBEDDED
					byteCode = StubPySourceManager.generatePythonClientStub(metaClass, realParentName,
							stubInfos.get(metaClassID));
				} else {
					throw new DataClayRuntimeException(ERRORCODE.CLASS_UNSUPPORTED_LANGUAGE,
							"Unknown language, could not genearte stub", false);
				}
				stubAndIncludes = new Triple<>(metaClass.getName(), byteCode, aspectBytes);

				stubsCache.put(stubInfo, stubAndIncludes);
			}
			stubs.put(metaClassID, stubAndIncludes);
		}
		return stubs;
	}

	/**
	 * Generate a set of Java stubs for Enrichments with the provided
	 *        informations
	 * @param language
	 *            Language of the stub to generate
	 * @param stubInfos
	 *            Information associated to each stub (names, contract IDs,
	 *            interface IDs...)
	 * @throws Exception
	 *             if an internal error occurred while working with Database.
	 * @pre The class exists
	 * @return Byte code of the stubs of the stubs for enrichment with information
	 *         provided
	 */
	public Map<MetaClassID, byte[]> generateStubsForEnrichment(final Langs language,
			final Map<MetaClassID, StubInfo> stubInfos) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Checks that there is no operation, implementation or property that
	 *        includes the class with name and Namespace specified.
	 * @param newClassName
	 *            Name of the class
	 * @param newNamespaceID
	 *            ID of the namespace of the class
	 * @return The ID of the class with Name and Namespace specified. If no class
	 *         with this name and namespace exists NULL is returned and the check is
	 *         skipped.
	 * @throws Exception
	 *             if an internal error occurred while working with Database.
	 */
	public MetaClassID checkClassNotInIncludesAndGetID(final String newClassName, final NamespaceID newNamespaceID) {

		final MetaClass metaClass = getMetaClassByName(newNamespaceID, newClassName);
		final MetaClassID metaClassID = metaClass.getDataClayID();

		// Verify there is no type using the class id
		final boolean existsInIncludes = classDB.existsClassInSomeType(metaClassID);
		if (existsInIncludes) {
			throw new ClassInIncludesException(newNamespaceID, newClassName);
		}

		return metaClassID;
	}

	/**
	 * For each property and operation ID provided verify they belong to the
	 *        Namespace of the Class with ID provided. Return those that do not
	 *        belong to the Namespace.
	 * @param metaClassID
	 *            ID of the MetaClass
	 * @param propertyIDs
	 *            IDs of the properties
	 * @param operationIDs
	 *            IDs of the operations
	 * @return IDs of operations and IDs of properties that do not belong to the
	 *         MetaClass with ID provided.
	 * @throws Exception
	 *             if an internal error occurred while working with Database.
	 */
	public Tuple<Set<PropertyID>, Set<OperationID>> getOperationsAndPropertiesNotInNamespaceOfClass(
			final MetaClassID metaClassID, final Set<PropertyID> propertyIDs, final Set<OperationID> operationIDs) {
		// Get the metaClass
		final MetaClass metaClass = getClassFromDatabaseOrCache(metaClassID);
		final Set<PropertyID> propertiesNotInClassNamespace = new HashSet<>();
		for (final PropertyID propertyID : propertyIDs) {
			// Verify the property belong to the metaClass with ID provided
			boolean found = false;
			for (final Property property : metaClass.getProperties()) {
				final PropertyID propID = property.getDataClayID();
				if (propID.equals(propertyID)) {
					if (!property.getNamespaceID().equals(metaClass.getNamespaceID())) {
						propertiesNotInClassNamespace.add(propertyID);
					}
					found = true;
					break;
				}
			}
			if (!found) {
				throw new PropertyNotInClassException(propertyID, metaClassID);
			}
		}

		final Set<OperationID> operationsNotInClassNamespace = new HashSet<>();
		for (final OperationID operationID : operationIDs) {
			// Verify the operation belong to the metaClass with ID provided
			boolean found = false;
			for (final Operation operation : metaClass.getOperations()) {
				final OperationID opID = operation.getDataClayID();
				if (opID.equals(operationID)) {
					if (!operation.getNamespaceID().equals(metaClass.getNamespaceID())) {
						operationsNotInClassNamespace.add(operationID);
					}
					found = true;
					break;
				}
			}
			if (!found) {
				throw new OperationNotInClassException(operationID, metaClassID);
			}
		}

		return new Tuple<>(propertiesNotInClassNamespace, operationsNotInClassNamespace);
	}

	/**
	 * Verify if the type with ID provided is in namespace specified or not
	 *        and add it into one of the provided structures.
	 * @param includedTypeID
	 *            ID of the type to check
	 * @param namespaceID
	 *            ID of the namespace
	 * @param includesInNamespace
	 *            [out] Set of MetaClass informations of includes in same namespace
	 * @param includesNotInNamespace
	 *            [out] Set of MetaClass informations of includes not in same
	 *            namespace
	 */
	private void verifyTypeInclude(final MetaClassID includedTypeID, final NamespaceID namespaceID,
			final Set<MetaClass> includesInNamespace, final Set<MetaClass> includesNotInNamespace) {

		// Get the class
		final MetaClass includedClass = getClassFromDatabaseOrCache(includedTypeID);
		if (includedClass != null) {
			if (includedClass.getNamespaceID().equals(namespaceID)) {
				includesInNamespace.add(includedClass);
			} else {
				includesNotInNamespace.add(includedClass);
			}
		}

	}

	/**
	 * Verify the includes of the Implementation. Check if they are in
	 *        namespace specified or not and add it into one of the provided
	 *        structures.
	 * @param implementation
	 *            Implementation to check.
	 * @param namespaceID
	 *            ID of the namespace
	 * @param includesInNamespace
	 *            [out] Set of MetaClass informations of includes in same namespace
	 * @param includesNotInNamespace
	 *            [out] Set of MetaClass informations of includes not in same
	 *            namespace
	 * @param analyzedImplementations
	 *            [out] Set of implementations that have been already analyzed
	 */
	private void getClassIncludesOfImplementation(final Implementation implementation, final NamespaceID namespaceID,
			final Set<MetaClass> includesInNamespace, final Set<MetaClass> includesNotInNamespace,
			final Set<ImplementationID> analyzedImplementations) {

		final ImplementationID implIDwithoutRef = implementation.getDataClayID();
		if (analyzedImplementations.contains(implIDwithoutRef)) {
			return;
		} else {
			analyzedImplementations.add(implementation.getDataClayID());
		}

		// Verify implementation includes
		for (final Type implInclude : implementation.getIncludes()) {
			if (implInclude instanceof UserType) {
				final UserType implIncludeUType = (UserType) implInclude;
				verifyTypeInclude(implIncludeUType.getClassID(), namespaceID, includesInNamespace,
						includesNotInNamespace);
			}
		}
		// Verify properties accessed by the implementation
		final List<AccessedProperty> accessedProperties = implementation.getAccessedProperties();
		for (final AccessedProperty accProp : accessedProperties) {
			final PropertyID propertyID = accProp.getPropertyID();
			// TODO: accessed properties cannot be properties that do not belong
			// to metaclass so it can be
			// improved the call "getIncludesInDifferentNamespaces"
			final Property property = getPropertyFromDatabaseOrCache(propertyID);

			final Type propertyType = property.getType();
			if (propertyType instanceof UserType) {
				final UserType includeUType = (UserType) propertyType;
				verifyTypeInclude(includeUType.getClassID(), namespaceID, includesInNamespace, includesNotInNamespace);

			}

		}

		// Verify implementations accessed by the implementation
		final List<AccessedImplementation> accessedImplementations = implementation.getAccessedImplementations();
		for (final AccessedImplementation accImpl : accessedImplementations) {
			final ImplementationID implID = accImpl.getImplementationID();
			final Implementation accessedImplementation = getImplementationFromDatabaseOrCache(implID);
			final Operation accessedOperation = getOperationFromDatabaseOrCache(
					accessedImplementation.getOperationID());

			// Verify Operation arguments
			final Map<String, Type> params = accessedOperation.getParams();
			for (final Entry<String, Type> entry : params.entrySet()) {
				final Type type = entry.getValue();
				if (type instanceof UserType) {
					final UserType includeUType = (UserType) type;
					verifyTypeInclude(includeUType.getClassID(), namespaceID, includesInNamespace,
							includesNotInNamespace);
				}
			}

			// Verify operation return type
			final Type type = accessedOperation.getReturnType();
			if (type instanceof UserType) {
				final UserType includeUType = (UserType) type;
				verifyTypeInclude(includeUType.getClassID(), namespaceID, includesInNamespace, includesNotInNamespace);
			}

			// Recursive call
			getClassIncludesOfImplementation(accessedImplementation, namespaceID, includesInNamespace,
					includesNotInNamespace, analyzedImplementations);

		}
	}

	/**
	 * Get the includes of the operations and properties provided and for
	 *        those that belongs to the Namespace with ID provided return all its
	 *        information.
	 * @param operationsAndImpls
	 *            IDs of the operations and implementations
	 * @param propertyIDs
	 *            IDs of the properties
	 * @param namespaceID
	 *            ID of the namespace
	 * @param parentClassID
	 *            ID of the parent class
	 * @return A set of MetaClass information with only Class ID and Namespace ID
	 *         corresponding to imported classes, and a set of MetaClass information
	 *         with all information corresponding to the non imported classes.
	 * @throws Exception
	 *             if an internal error occurred while working with Database.
	 */
	public Tuple<Set<MetaClass>, Set<MetaClass>> getClassIncludesOfOperationsPropsAndImpls(
			final Map<OperationID, Set<ImplementationID>> operationsAndImpls, final Set<PropertyID> propertyIDs,
			final NamespaceID namespaceID, final MetaClassID parentClassID) {

		final Set<MetaClass> includesInNamespace = new HashSet<>();
		final Set<MetaClass> includesNotInNamespace = new HashSet<>();
		final Set<ImplementationID> alreadyAnalyzedImpls = new HashSet<>();

		// Analyze includes of properties
		for (final PropertyID propertyID : propertyIDs) {
			final Property property = getPropertyFromDatabaseOrCache(propertyID);
			final Type propertyType = property.getType();
			if (propertyType instanceof UserType) {
				final UserType includeUType = (UserType) propertyType;
				verifyTypeInclude(includeUType.getClassID(), namespaceID, includesInNamespace, includesNotInNamespace);
			}

		}
		// Analyze includes of operations and implementations
		for (final Entry<OperationID, Set<ImplementationID>> entry : operationsAndImpls.entrySet()) {

			final OperationID operationID = entry.getKey();
			final Operation operation = getOperationFromDatabaseOrCache(operationID);

			// Verify Operation arguments
			final Map<String, Type> params = operation.getParams();
			for (final Entry<String, Type> curParam : params.entrySet()) {
				final Type type = curParam.getValue();
				if (type instanceof UserType) {
					final UserType includeUType = (UserType) type;
					verifyTypeInclude(includeUType.getClassID(), namespaceID, includesInNamespace,
							includesNotInNamespace);
				}
			}

			// Verify operation return type
			final Type type = operation.getReturnType();
			if (type instanceof UserType) {
				final UserType includeUType = (UserType) type;
				verifyTypeInclude(includeUType.getClassID(), namespaceID, includesInNamespace, includesNotInNamespace);
			}

			// Now let's verify the implementation includes
			final Set<ImplementationID> implementationIDs = entry.getValue();
			for (final ImplementationID implementationID : implementationIDs) {
				final Implementation implementation = getImplementationFromDatabaseOrCache(implementationID);
				getClassIncludesOfImplementation(implementation, namespaceID, includesInNamespace,
						includesNotInNamespace, alreadyAnalyzedImpls);
			}

		}

		// Verify parent class (verify the ops and props of parent class is not
		// necessary since the implementations
		// will contain this information)
		if (parentClassID != null) {
			verifyTypeInclude(parentClassID, namespaceID, includesInNamespace, includesNotInNamespace);
		}

		return new Tuple<>(includesNotInNamespace, includesInNamespace);
	}

	/**
	 * Get the Class name, Namespace ID and the IDs of the included classes
	 *        in different Namespaces than the one specified of the class with ID
	 *        provided.
	 * @param metaClassID
	 *            ID of the class
	 * @param propertiesIDs
	 *            properties to be analyzed
	 * @param operationsIDsAndImplementationsIDs
	 *            operations and their implementations to be analyzed
	 * @param namespaceID
	 *            ID of the namespace
	 * @return The class name, Namespace ID of class and a set of included classes
	 *         that are not in the namespace provided.
	 * @throws Exception
	 *             if an internal error occurred while working with Database.
	 */
	public Triple<String, NamespaceID, Set<MetaClassID>> getIncludesInDifferentNamespaces(final MetaClassID metaClassID,
			final Set<PropertyID> propertiesIDs,
			final Map<OperationID, Set<ImplementationID>> operationsIDsAndImplementationsIDs,
			final NamespaceID namespaceID) {
		// Get the class
		final MetaClass metaClass = getClassFromDatabaseOrCache(metaClassID);

		final Set<MetaClass> includesInNamespace = new HashSet<>();
		final Set<MetaClass> includesNotInNamespace = new HashSet<>();
		final Set<ImplementationID> alreadyAnalyzedImpls = new HashSet<>();

		// Analyze includes of properties
		for (final Property property : metaClass.getProperties()) {
			if (propertiesIDs.contains(property.getDataClayID())) {
				final Type propertyType = property.getType();
				if (propertyType instanceof UserType) {
					final UserType includeUType = (UserType) propertyType;
					verifyTypeInclude(includeUType.getClassID(), namespaceID, includesInNamespace,
							includesNotInNamespace);
				}
			}

		}
		// Analyze includes of operations and implementations
		for (final Operation operation : metaClass.getOperations()) {
			if (operationsIDsAndImplementationsIDs.containsKey(operation.getDataClayID())) {
				final Set<ImplementationID> implsIDs = operationsIDsAndImplementationsIDs
						.get(operation.getDataClayID());

				// Verify Operation arguments
				final Map<String, Type> params = operation.getParams();
				for (final Entry<String, Type> curParam : params.entrySet()) {
					final Type type = curParam.getValue();
					if (type instanceof UserType) {
						final UserType includeUType = (UserType) type;
						verifyTypeInclude(includeUType.getClassID(), namespaceID, includesInNamespace,
								includesNotInNamespace);
					}
				}

				// Verify operation return type
				final Type type = operation.getReturnType();
				if (type instanceof UserType) {
					final UserType includeUType = (UserType) type;
					verifyTypeInclude(includeUType.getClassID(), namespaceID, includesInNamespace,
							includesNotInNamespace);
				}

				// Now let's verify the implementation includes
				for (final Implementation implementation : operation.getImplementations()) {
					if (implsIDs.contains(implementation.getDataClayID())) {
						getClassIncludesOfImplementation(implementation, namespaceID, includesInNamespace,
								includesNotInNamespace, alreadyAnalyzedImpls);
					}
				}
			}

		}

		// Verify parent class (verify the ops and props of parent class is not
		// necessary since the implementations
		// will contain this information)
		if (metaClass.getParentType() != null) {
			verifyTypeInclude(metaClass.getParentType().getClassID(), namespaceID, includesInNamespace,
					includesNotInNamespace);
		}

		final Set<MetaClassID> includesIDsNotInNamespace = new HashSet<>();

		for (final MetaClass metaClassInfo : includesNotInNamespace) {
			includesIDsNotInNamespace.add(metaClassInfo.getDataClayID());
		}

		return new Triple<>(metaClass.getName(), metaClass.getNamespaceID(), includesIDsNotInNamespace);
	}

	/**
	 * Verify if there is some implementation accessing the operation with ID
	 *        provided
	 * @param operationID
	 *            ID of the operation
	 * @return TRUE if there is some implementation accessing the operation. FALSE
	 *         otherwise.
	 * @throws Exception
	 *             if an internal error occurred while working with Database.
	 */
	public boolean existsImplementationAccessingOperation(final OperationID operationID) {

		final Operation op = classDB.getOperationByID(operationID);
		// If any implementation accesses some of the implementations of this operation,
		// return true.
		for (final Implementation impl : op.getImplementations()) {
			if (classDB.existsAccessedImplementationWithID(impl.getDataClayID())) {
				return true;
			}
		}
		return false;

	}

	/**
	 * Get the properties, operations and implementations created as
	 *        enrichments of the specified class in the given namespace.
	 * @param metaClassID
	 *            ID of the class
	 * @param namespaceIDofEnrichments
	 *            ID of the namespace of the enrichments
	 * @return the set of properties, operations and implementations that enrich the
	 *         class in the given namespace. If there is no enrichment in the
	 *         specified namespace, return the corresponding empty sets.
	 * @throws Exception
	 *             if an internal error occurred while working with Database.
	 */
	public Tuple<Set<PropertyID>, Map<OperationID, Set<ImplementationID>>> getEnrichmentsInNamespaceOfClass(
			final MetaClassID metaClassID, final NamespaceID namespaceIDofEnrichments) {

		// Get properties of the MetaClass with ID provided whose original
		// Namespace is the one specified
		final Set<PropertyID> propertyEnrichments = new HashSet<>();
		final List<Property> resultProp = classDB.getPropertiesByClassIDAndNamespaceID(metaClassID,
				namespaceIDofEnrichments);
		for (final Property property : resultProp) {
			propertyEnrichments.add(property.getDataClayID());
		}

		// Get operations of the MetaClass with ID provided whose original
		// Namespace is the one specified
		final Map<OperationID, Set<ImplementationID>> operationAndImplEnrichments = //
				new HashMap<>();
		final List<Operation> resultOps = classDB.getOperationsByClassIDAndNamespaceID(metaClassID,
				namespaceIDofEnrichments);
		for (final Operation operation : resultOps) {

			final Set<ImplementationID> implOfOperationEnrichments = new HashSet<>();

			for (final Implementation implementation : operation.getImplementations()) {
				if (implementation.getNamespaceID().equals(namespaceIDofEnrichments)) {
					implOfOperationEnrichments.add(implementation.getDataClayID());
				}
			}

			operationAndImplEnrichments.put(operation.getDataClayID(), implOfOperationEnrichments);

		}

		return new Tuple<>(propertyEnrichments, operationAndImplEnrichments);
	}

	/**
	 * Return all information of a class
	 * @param metaClassID
	 *            ID of the metaclass
	 * @return Information of the class
	 * @pre There is a class with ID provided
	 * @throws Exception
	 *             if an internal error occurred while working with Database.
	 */
	public MetaClass getClassInfo(final MetaClassID metaClassID) {
		// Get class
		final MetaClass metaClass = getClassFromDatabaseOrCache(metaClassID);
		return metaClass;

	}

	/**
	 * Return ID of class of the type of the property or NULL if type is not
	 *        user type.
	 * @param propertyID
	 *            ID of the property
	 * @return ID of class of the type or NULL if type is not user type.
	 * @pre There is a type with ID provided
	 * @throws Exception
	 *             if an internal error occurred while working with Database.
	 */
	public MetaClassID getClassIDFromProperty(final PropertyID propertyID) {
		final Property prop = this.getPropertyFromDatabaseOrCache(propertyID);
		if (prop.getType() instanceof UserType) {
			final UserType includeUType = (UserType) prop.getType();
			return includeUType.getClassID();
		} else {
			return null;
		}
	}

	/**
	 * Return information of classes in the given namespace.
	 * @param namespaceID
	 *            ID of the namespace of the classes to be retrieved
	 * @return the info of the classes in the namespace provided.
	 * @throws Exception
	 *             if an internal error occurred while working with Database.
	 */
	public Map<MetaClassID, MetaClass> getInfoOfClassesInNamespace(final NamespaceID namespaceID) {
		// Get classes
		final List<MetaClass> classesInNamespace = classDB.getClassesInNamespace(namespaceID);
		if (classesInNamespace == null) {
			return null;
		}

		final Map<MetaClassID, MetaClass> result = new HashMap<>();
		for (final MetaClass curClass : classesInNamespace) {
			result.put(curClass.getDataClayID(), curClass);
		}

		return result;
	}

	/**
	 * Return all information of an operation
	 * @param operationID
	 *            ID of the operation
	 * @return Information of the operation
	 * @throws Exception
	 *             if an internal error occurred while working with Database.
	 */
	public Operation getOperationInfo(final OperationID operationID) {
		// First we get the Operation and check if it exists
		final Operation operation = getOperationFromDatabaseOrCache(operationID);
		return operation;
	}

	/**
	 * Return all information of a property
	 * @param propertyID
	 *            ID of the property
	 * @return Information of the property
	 */
	public Property getPropertyInfo(final PropertyID propertyID) {
		// First we get the Property and check if it exists
		final Property property = getPropertyFromDatabaseOrCache(propertyID);
		return property;
	}

	/**
	 * For each property and operation ID of the metaclass with ID provided
	 *        return all those that belong to the namespace with ID specified
	 * @param metaClassID
	 *            ID of the MetaClass
	 * @param namespaceID
	 *            ID of the namespace
	 * @return IDs of operations and IDs of properties that belong to the Namespace
	 *         with ID provided and class with ID specified.
	 * @throws Exception
	 *             if an internal error occurred while working with Database.
	 */
	public Tuple<Set<PropertyID>, Map<OperationID, Set<ImplementationID>>> getOperationsAndPropertiesAndImplInNamespace(
			final MetaClassID metaClassID, final NamespaceID namespaceID) {

		// Get the class
		final MetaClass metaClass = getClassFromDatabaseOrCache(metaClassID);

		final Set<PropertyID> propertyIDs = new HashSet<>();
		final Map<OperationID, Set<ImplementationID>> operationIDs = //
				new HashMap<>();

		for (final Operation operation : metaClass.getOperations()) {
			if (operation.getNamespaceID().equals(namespaceID)) {

				final Set<ImplementationID> implementationIDs = new HashSet<>();
				for (final Implementation implementation : operation.getImplementations()) {
					if (implementation.getNamespaceID().equals(namespaceID)) {
						implementationIDs.add(implementation.getDataClayID());
					}
				}
				operationIDs.put(operation.getDataClayID(), implementationIDs);
			}
		}

		for (final Property property : metaClass.getProperties()) {
			if (property.getNamespaceID().equals(namespaceID)) {
				propertyIDs.add(property.getDataClayID());
			}

		}

		return new Tuple<>(propertyIDs, operationIDs);
	}

	/**
	 * Return the operations IDs of the default getters and setters of the
	 *        given properties IDs
	 * @param metaClassID
	 *            ID of the class of the properties
	 * @param propertiesIDs
	 *            IDs of the properties
	 * @return The set of operations IDs corresponding to the getters and setters of
	 *         the specified properties.
	 * @throws Exception
	 *             if an internal error occurred while working with Database.
	 */
	public Set<OperationID> getOperationsIDsOfGettersAndSetters(final MetaClassID metaClassID,
			final Set<PropertyID> propertiesIDs) {
		final Set<OperationID> gettersAndSetters = new HashSet<>();

		// Get the class
		final MetaClass metaClass = getClassFromDatabaseOrCache(metaClassID);
		for (final Property property : metaClass.getProperties()) {
			if (propertiesIDs.contains(property.getDataClayID())) {
				gettersAndSetters.add(property.getGetterOperationID());
				gettersAndSetters.add(property.getSetterOperationID());
			}
		}

		return gettersAndSetters;
	}

	/**
	 * Return the implementations of the operations of the getters and
	 *        setters of the given properties
	 * @param propertiesInClass
	 *            Properties which implementations of getters ands settes have to be
	 *            retrieved.
	 * @return The implementations of the operations of the getters and setters of
	 *         the given properties
	 * @throws Exception
	 *             if an internal error occurred while working with Database.
	 */
	public Map<PropertyID, Map<OperationID, ImplementationID>> getImplementationsOfGettersAndSetters(
			final Map<MetaClassID, Set<PropertyID>> propertiesInClass) {
		final Map<PropertyID, Map<OperationID, ImplementationID>> result = new HashMap<>();
		for (final Entry<MetaClassID, Set<PropertyID>> curProperties : propertiesInClass.entrySet()) {
			final MetaClass metaClass = getClassFromDatabaseOrCache(curProperties.getKey());
			for (final Property prop : metaClass.getProperties()) {
				if (curProperties.getValue().contains(prop.getDataClayID())) {
					final Map<OperationID, ImplementationID> implsOfGettersAndSetters = new HashMap<>();
					final OperationID getter = prop.getGetterOperationID();
					final OperationID setter = prop.getSetterOperationID();
					final Operation getterRef = this.getOperationFromDatabaseOrCache(getter);
					final Operation setterRef = this.getOperationFromDatabaseOrCache(setter);
					implsOfGettersAndSetters.put(getter,
							getterRef.getImplementations().iterator().next().getDataClayID());
					implsOfGettersAndSetters.put(setter,
							setterRef.getImplementations().iterator().next().getDataClayID());

					result.put(prop.getDataClayID(), implsOfGettersAndSetters);
				}
			}
		}
		return result;
	}

	/**
	 * Generate execution class for Java MetaClasses. This function should be
	 *        used to install a class in DataService.
	 * @param metaClass
	 *            ID of the class
	 * @return The bytes of the generated execution class and its aspects.
	 * @throws Exception
	 *             If some exception occurs.
	 */
	public Tuple<byte[], byte[]> generateJavaExecutionClass(final MetaClass metaClass) {


		// Get parent name
		String realParentName = null;
		if (metaClass.getParentType() == null) {
			realParentName = DataClayObject.class.getName();
		} else {
			final MetaClass parentClass = getClassFromDatabaseOrCache(metaClass.getParentType().getClassID());
			realParentName = parentClass.getNamespace() + "." + parentClass.getName();
		}
		MetaClass curClass = metaClass;
		while (curClass.getParentType() != null) {
			MetaClassID parentID = null;
			if (metaClass.getParentType() != null) {
				parentID = curClass.getParentType().getClassID();
			}
			curClass = getClassFromDatabaseOrCache(parentID);
		}

		// Once symbols were modified we must generate the execution class
		Map<MetaClassID, MetaClass> allNamespaceClasses = this.getInfoOfClassesInNamespace(metaClass.getNamespaceID());
		if (DEBUG_ENABLED) {
			List<String> classNamesToUse = new ArrayList<>();
			for (MetaClass curDepClass : allNamespaceClasses.values()) {
				classNamesToUse.add(curDepClass.getName());
			}
			logger.debug("Class dependencies used to replace: " + classNamesToUse);

		}
		final byte[] result = ExecutionByteCodeManager.generateExecutionClass(metaClass, realParentName, this,
				new ArrayList<>(allNamespaceClasses.values()));
		final byte[] aspects = StubByteCodeManager.generateStubAspect(metaClass, true, this);
		return new Tuple<>(result, aspects);
	}

	/**
	 * Get all includes of the class
	 * @param metaClass
	 *            Class to check
	 * @return All includes of type
	 */
	private List<MetaClass> getAllIncludes(final MetaClass metaClass) {

		final Set<MetaClassID> accClass = new HashSet<>();

		// ======================== FIND ALL ACCESSED CLASSES
		// ==================================== //
		MetaClass curMetaClass = metaClass;
		while (curMetaClass != null) {
			accClass.add(curMetaClass.getDataClayID());
			if (curMetaClass.getParentType() == null) {
				curMetaClass = null;
			} else {
				curMetaClass = this.getClassInfo(curMetaClass.getParentType().getClassID());
			}
		}
		
		if (metaClass.getJavaClassInfo() != null) {
			for (final Type ifaceInclude : metaClass.getJavaClassInfo().getIncludes()) {  
				if (ifaceInclude instanceof UserType) {
					final UserType utype = (UserType) ifaceInclude;
					accClass.add(utype.getClassID());
				}
			}
		}

		for (final Operation op : metaClass.getOperations()) {
			if (op.getReturnType() instanceof UserType) {
				final UserType utype = (UserType) op.getReturnType();
				accClass.add(utype.getClassID());
			}
			if (op.getReturnType().getIncludes() != null) {
				for (final Type subInclude : op.getReturnType().getIncludes()) {
					if (subInclude instanceof UserType) {
						final UserType utype = (UserType) subInclude;
						accClass.add(utype.getClassID());
					}
				}
			}
			for (final Type paramType : op.getParams().values()) {
				if (paramType instanceof UserType) {
					final UserType utype = (UserType) paramType;
					accClass.add(utype.getClassID());
				}
				if (paramType.getIncludes() != null) {
					for (final Type subInclude : paramType.getIncludes()) {
						if (subInclude instanceof UserType) {
							final UserType utype = (UserType) subInclude;
							accClass.add(utype.getClassID());
						}
					}
				}
			}

			if (op.getIsAbstract()) {
				continue;
			}
			for (final Implementation impl : op.getImplementations()) {
				// Get accessed implementation IDs
				final List<AccessedImplementation> accImplems = impl.getAccessedImplementations();
				for (final AccessedImplementation accImplS : accImplems) {
					final ImplementationID accImplID = accImplS.getImplementationID();
					final Implementation accImpl = getImplementationFromDatabaseOrCache(accImplID);
					final Operation accOp = getOperationFromDatabaseOrCache(accImpl.getOperationID());

					// Check accessed classes
					accClass.add(accImpl.getMetaClassID());
					if (accOp.getReturnType() instanceof UserType) {
						final UserType utype = (UserType) accOp.getReturnType();
						accClass.add(utype.getClassID());
					}
					if (accOp.getReturnType().getIncludes() != null) {
						for (final Type subInclude : accOp.getReturnType().getIncludes()) {
							if (subInclude instanceof UserType) {
								final UserType utype = (UserType) subInclude;
								accClass.add(utype.getClassID());
							}
						}
					}
					for (final Type paramType : accOp.getParams().values()) {
						if (paramType instanceof UserType) {
							final UserType utype = (UserType) paramType;
							accClass.add(utype.getClassID());
						}
						if (paramType.getIncludes() != null) {
							for (final Type subInclude : paramType.getIncludes()) {
								if (subInclude instanceof UserType) {
									final UserType utype = (UserType) subInclude;
									accClass.add(utype.getClassID());
								}
							}
						}
					}
				}

				for (final AccessedProperty accProp : impl.getAccessedProperties()) {
					final PropertyID accPropertyID = accProp.getPropertyID();
					final Property accProperty = getPropertyFromDatabaseOrCache(accPropertyID);
					// Check accessed classes
					accClass.add(accProperty.getMetaClassID());

					final Type accTypeProp = accProperty.getType();
					if (accTypeProp instanceof UserType) {

						final UserType utype = (UserType) accTypeProp;
						accClass.add(utype.getClassID());
					}
					if (accTypeProp.getIncludes() != null) {
						for (final Type subInclude : accTypeProp.getIncludes()) {
							if (subInclude instanceof UserType) {
								final UserType utype = (UserType) subInclude;
								accClass.add(utype.getClassID());
							}
						}
					}

				}
				for (final Type accInclude : impl.getIncludes()) {
					if (accInclude instanceof UserType) {
						final UserType utype = (UserType) accInclude;
						accClass.add(utype.getClassID());
					}
					if (accInclude.getIncludes() != null) {
						for (final Type subInclude : accInclude.getIncludes()) {
							if (subInclude instanceof UserType) {
								final UserType utype = (UserType) subInclude;
								accClass.add(utype.getClassID());
							}
						}
					}
				}

			}
		}
		for (final Property prop : metaClass.getProperties()) {
			final Type propType = prop.getType();
			if (propType instanceof UserType) {
				final UserType utype = (UserType) propType;
				accClass.add(utype.getClassID());
			}
			if (propType.getIncludes() != null) {
				for (final Type accInclude : propType.getIncludes()) {
					if (accInclude instanceof UserType) {
						final UserType utype = (UserType) accInclude;
						accClass.add(utype.getClassID());
					}
				}
			}
		}

		final List<MetaClass> accessedClasses = new ArrayList<>(); // Used later for replacing.
		for (final MetaClassID accClassID : accClass) {
			if (accClassID == null) {
				continue; // Can be null for java or DataCLay types
			}
			final MetaClass accessedClass = getClassFromDatabaseOrCache(accClassID);
			accessedClasses.add(accessedClass);
		}

		return accessedClasses;
	}

	// ============= OTHER =========== //

	/**
	 * Return the cache of classes
	 * @return The cache of classes
	 */
	public MemoryCache<MetaClassID, MetaClass> getClassCache() {
		return this.classCache;
	}

	/**
	 * Method for unit testing.
	 * @return the db handler reference of this manager.
	 */
	public ClassManagerDB getDbHandler() {
		return classDB;
	}

	@Override
	public void cleanCaches() {
		this.implementationsCache.clear();
		this.classCache.clear();
		this.operationsCache.clear();
		this.propertiesCache.clear();
	}

}
