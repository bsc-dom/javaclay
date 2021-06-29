
/**
 * @file ClassManagerOperationTest.java
 * @date Sep 18, 2012
 * @author dgasull
 */
package es.bsc.dataclay.logic.classmgr;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Modifier;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.*;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.exceptions.logicmodule.classmgr.OperationAlreadyInClassException;
import es.bsc.dataclay.exceptions.logicmodule.classmgr.OperationNotExistException;
import es.bsc.dataclay.logic.classmgr.ClassManager;
import es.bsc.dataclay.logic.classmgr.ClassManagerDB;
import es.bsc.dataclay.test.AbstractManagerTest;
import es.bsc.dataclay.test.TestUtils;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.management.classmgr.AccessedImplementation;
import es.bsc.dataclay.util.management.classmgr.AccessedProperty;
import es.bsc.dataclay.util.management.classmgr.Implementation;
import es.bsc.dataclay.util.management.classmgr.LanguageDependantClassInfo;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.classmgr.Operation;
import es.bsc.dataclay.util.management.classmgr.Property;
import es.bsc.dataclay.util.management.classmgr.Type;
import es.bsc.dataclay.util.management.classmgr.UserType;
import es.bsc.dataclay.util.management.classmgr.features.QualitativeFeature;
import es.bsc.dataclay.util.management.classmgr.features.QuantitativeFeature;
import es.bsc.dataclay.util.management.classmgr.features.Feature.FeatureType;
import es.bsc.dataclay.util.management.classmgr.java.JavaClassInfo;
import es.bsc.dataclay.util.management.classmgr.java.JavaImplementation;
import es.bsc.dataclay.util.management.classmgr.java.JavaOperationInfo;
import es.bsc.dataclay.util.yaml.CommonYAML;

/**
 * @author dgasull
 * @class ClassManagerOperationTest
 * @brief This class tests the ClassManager focusing in Operation functions.
 * @note This is a parameterized test.
 */
@RunWith(Parameterized.class)
public final class ClassManagerOperationTest extends AbstractManagerTest{

	private ClassManager cman;
	// !<@brief Class Manager instance tested.
	private static String DBFILESDIRNAME = System.getProperty("user.dir") + "/dbfiles/ClassManagerOperationTest";
	// !<@brief Path to the test databases.

	private static final String NAMESPACENAME = "testNamespace"; // !<@brief Namespace name used in tests

	private static final NamespaceID NAMESPACEID = new NamespaceID(); // !<@brief NamespaceID used in tests

	private static final int RANDMAX = 10;
	private Operation operation; // !<@brief Operation used in tests
	// private Operation operationSameTypes; // !<@brief Operation used in tests, same types than `operation`

	private final Operation globalOperation; // !<@brief Operation used in tests
	private ClassManagerDB testdb; // !<@brief DbHandler used in tests.
	private static final MetaClassID TESTMETACLASSTYPEID = new MetaClassID();

	/**
	 * @brief This method is executed before all tests. It is used to instantiate variables that are used in all the tests in order
	 *        to save code.
	 * @author dgasull
	 * @throws RemoteException
	 */
	@BeforeClass
	public static void beforeAll() {
		TestUtils.createOrCleanDirectory(DBFILESDIRNAME);
	}

	/**
	 * @brief This method is executed before each test. It is used create the test database.
	 * @author dgasull
	 * @throws Exception
	 */
	@Override
	@Before
	public void before() {
		super.before();

		final ClassManagerDB cdb = new ClassManagerDB(dataSource);
		cdb.dropTables();
		cdb.createTables();

		cman = new ClassManager(dataSource);
		testdb = cman.getDbHandler();

		final String yamlStr = CommonYAML.getYamlObject().dump(globalOperation);
		operation = (Operation) CommonYAML.getYamlObject().load(yamlStr);

	}

	/**
	 * @brief This method is executed after each test. It is used to delete the test database since we are testing the creation of
	 *        Metaclasses on it, it is necessary to empty the database before.
	 * @author dgasull
	 * @throws Exception
	 */
	@Override
	@After
	public void after() throws Exception {
		testdb.close();
		TestUtils.cleanDirectory(DBFILESDIRNAME);
		super.after();
	}

	@AfterClass
	public static void afterAll() throws Exception {
		TestUtils.deleteDirectory(DBFILESDIRNAME);
	}

	/**
	 * @brief Creates a new ClassManagerOperationTest
	 * @author dgasull
	 * @param operationCase
	 *            Operation specification of this set of tests
	 * @note This function is necessary for the Parameterized Test.
	 */
	public ClassManagerOperationTest(final Operation operationCase) {
		this.globalOperation = operationCase;
	}

	/*************************** PARAMETERS ***************************/

	/**
	 * @brief This method is executed before all tests. <br>
	 *        It is used to prepare all the possible Parameters of this Parameterized Test.<br>
	 *        Each array of Objects obtained in the collection represents the Parameters of the Constructor function. So for each
	 *        array in the Collection all tests in this class are run. In our case we prepare all possible Operation ifications:
	 *        <br>
	 *        <br>
	 *        OPERATION SPECIFICATION TESTED CASES: One ification can have different options, we must combine them in order to do a
	 *        Complete Test. The structure of the combinational table and an example are:<br>
	 *        <br>
	 *        <table>
	 *        <tr>
	 *        <td>HAS RETURN</td>
	 *        <td>0 (0 MEANS NO RETURN TYPE)</td>
	 *        </tr>
	 *        <tr>
	 *        <td>HAS N PARAMETERS</td>
	 *        <td>1 (1 MEANS N PARAMETERS)</td>
	 *        </tr>
	 *        <tr>
	 *        <td>HAS N IMPLEMENTATIONS</td>
	 *        <td>1 (1 MEANS N IMPLEMENTATIONS)</td>
	 *        </tr>
	 *        <tr>
	 *        <td>+OPERATION HAVE PRIMITIVE PAR</td>
	 *        <td>1 (1 MEANS PRIMITIVE PARAMETERS)</td>
	 *        </tr>
	 *        <tr>
	 *        <td>+OPERATION HAS PRIMITIVE RETURN</td>
	 *        <td>0 (0 MEANS METACLASS RETURN TYPE)</td>
	 *        </tr>
	 *        </table>
	 *        <br>
	 *        The total number of combinations is: <br>
	 *        2 cases with operations without parameters nor return <br>
	 *        2^2 cases with operations without parameters but with return <br>
	 *        2^2 cases with operations with parameters but without return <br>
	 *        2^3 cases with operations with parameters and return <br>
	 *        TOTAL: 18 combinations. <br>
	 *        <br>
	 *        The combination table is the following: <br>
	 *        <br>
	 *        <table>
	 *        <tr>
	 *        <td>hasRet</td>
	 *        <td>nPar</td>
	 *        <td>nImpl</td>
	 *        <td>prPara</td>
	 *        <td>prRet</td>
	 *        </tr>
	 *        <tr>
	 *        <td>0</td>
	 *        <td>0</td>
	 *        <td>0</td>
	 *        <td>X</td>
	 *        <td>X</td>
	 *        </tr>
	 *        <tr>
	 *        <td>0</td>
	 *        <td>0</td>
	 *        <td>1</td>
	 *        <td>X</td>
	 *        <td>X</td>
	 *        </tr>
	 *        <tr>
	 *        <td>0</td>
	 *        <td>1</td>
	 *        <td>0</td>
	 *        <td>0</td>
	 *        <td>X</td>
	 *        </tr>
	 *        <tr>
	 *        <td>0</td>
	 *        <td>1</td>
	 *        <td>0</td>
	 *        <td>1</td>
	 *        <td>X</td>
	 *        </tr>
	 *        <tr>
	 *        <td>0</td>
	 *        <td>1</td>
	 *        <td>1</td>
	 *        <td>0</td>
	 *        <td>X</td>
	 *        </tr>
	 *        <tr>
	 *        <td>0</td>
	 *        <td>1</td>
	 *        <td>1</td>
	 *        <td>1</td>
	 *        <td>X</td>
	 *        </tr>
	 *        <tr>
	 *        <td>1</td>
	 *        <td>0</td>
	 *        <td>0</td>
	 *        <td>X</td>
	 *        <td>0</td>
	 *        </tr>
	 *        <tr>
	 *        <td>1</td>
	 *        <td>0</td>
	 *        <td>0</td>
	 *        <td>X</td>
	 *        <td>1</td>
	 *        </tr>
	 *        <tr>
	 *        <td>1</td>
	 *        <td>0</td>
	 *        <td>1</td>
	 *        <td>X</td>
	 *        <td>0</td>
	 *        </tr>
	 *        <tr>
	 *        <td>1</td>
	 *        <td>0</td>
	 *        <td>1</td>
	 *        <td>X</td>
	 *        <td>1</td>
	 *        </tr>
	 *        <tr>
	 *        <td>1</td>
	 *        <td>1</td>
	 *        <td>0</td>
	 *        <td>0</td>
	 *        <td>0</td>
	 *        </tr>
	 *        <tr>
	 *        <td>1</td>
	 *        <td>1</td>
	 *        <td>0</td>
	 *        <td>0</td>
	 *        <td>1</td>
	 *        </tr>
	 *        <tr>
	 *        <td>1</td>
	 *        <td>1</td>
	 *        <td>0</td>
	 *        <td>1</td>
	 *        <td>0</td>
	 *        </tr>
	 *        <tr>
	 *        <td>1</td>
	 *        <td>1</td>
	 *        <td>0</td>
	 *        <td>1</td>
	 *        <td>1</td>
	 *        </tr>
	 *        <tr>
	 *        <td>1</td>
	 *        <td>1</td>
	 *        <td>1</td>
	 *        <td>0</td>
	 *        <td>0</td>
	 *        </tr>
	 *        <tr>
	 *        <td>1</td>
	 *        <td>1</td>
	 *        <td>1</td>
	 *        <td>0</td>
	 *        <td>1</td>
	 *        </tr>
	 *        <tr>
	 *        <td>1</td>
	 *        <td>1</td>
	 *        <td>1</td>
	 *        <td>1</td>
	 *        <td>0</td>
	 *        </tr>
	 *        <tr>
	 *        <td>1</td>
	 *        <td>1</td>
	 *        <td>1</td>
	 *        <td>1</td>
	 *        <td>1</td>
	 *        </tr>
	 *        </table>
	 *        <br>
	 * @return Array of Object List. Each element of the array is an array of objects. In our case, this array of objects is only
	 *         one object: the MetaClass ification case. It is used for our Parameterized test.
	 * @author dgasull
	 * @throws RemoteException
	 */
	@Parameters
	public static Collection<Object[]> generates() throws RemoteException {

		// First we prepare the Collection to return.
		ArrayList<Object[]> operations = new ArrayList<>();

		// Now we iterate all the possible combinations

		/*
		 * hasReturn: --> 0 means No return --> 1 means return
		 */
		for (int hasReturn = 0; hasReturn < 2; ++hasReturn) {
			/*
			 * hasParams: --> 0 means 0 Parameters --> 1 means N Parameters
			 */
			for (int hasParams = 0; hasParams < 2; ++hasParams) {
				/*
				 * hasImpl: --> 0 means 0 Implementations --> 1 means N Implementations
				 */
				for (int hasImpl = 0; hasImpl < 2; ++hasImpl) {

					// Prune of the Combination Tree if no Paramateres were found.
					if (hasParams > 0) {
						/*
						 * opPrimitiveParam: Type of parameter. --> 0 means metaclass parameter (parameter of MetaClass class) --> 1 means primitive
						 * parameter (parameter of Type class)
						 */
						for (int opPrimitiveParam = 0; opPrimitiveParam < 2; ++opPrimitiveParam) {
							operations = generatesPrune(hasReturn, hasParams, hasImpl, opPrimitiveParam, operations);
						}
					} else {
						operations = generatesPrune(hasReturn, hasParams, hasImpl, -1, operations);
					}
				}
			}
		}
		return operations;
	}

	/**
	 * @brief This function is used by the function ClassManagerOperationTest::generates() in order to prune the Combinational tree
	 *        and generate new Operation ifications.
	 * @param hasParams
	 *            Indicates if the operations to generate must have parameters.
	 * @param hasReturn
	 *            Indicates if the operations to generate must have return.
	 * @param hasImpl
	 *            Indicates if the operations to generate must have Implementations.
	 * @param arePrimitiveParams
	 *            Indicates if the parameters of the operations to generate are Primitive (Type class) or not (MetaClass)
	 * @param operations
	 *            Array of Object list which each one contains only a Operation (Paremeterized test) Since we are generating a
	 *            Matrix of combinations, we should pass this as a parameter, like in/out parameter.
	 * @return Array of Object List. Each element of the array is an array of objects. In our case, this array of objects is only
	 *         one object: the MetaClass ification case. It is used for our Parameterized test.
	 * @author dgasull
	 */
	private static ArrayList<Object[]> generatesPrune(final int hasReturn, final int hasParams, final int hasImpl,
			final int arePrimitiveParams, final ArrayList<Object[]> operations) {

		final ArrayList<Object[]> pruneOperations = operations;

		if (hasReturn > 0) {
			/*
			 * opPrimitiveRet: Type of return. --> 0 means return parameter (return of MetaClass class) --> 1 means return parameter (return
			 * of Type class)
			 */
			for (int opPrimitiveRet = 0; opPrimitiveRet < 2; ++opPrimitiveRet) {
				// This function will modify operations (add N operations with return)

				generateOperations("TestClass",
						hasParams, hasReturn, hasImpl, arePrimitiveParams, opPrimitiveRet,
						pruneOperations);
			}

		} else {
			// This function will modify operations (add N operations with return)
			generateOperations("TestClass",
					hasParams, hasReturn, hasImpl, arePrimitiveParams, -1,
					pruneOperations);

		}

		return pruneOperations;

	}

	/**
	 * @brief This function is responsible to generate N operation specifications combining different cases provided by the
	 *        parameters. For instance N operations without implementations, without return, but primitive Parameters.
	 * @param metaclassName
	 *            Name of the metaclass in which to generate operations
	 * @param hasParams
	 *            Indicates if the operations to generate must have parameters.
	 * @param hasReturn
	 *            Indicates if the operations to generate must have return.
	 * @param hasImplementations
	 *            Indicates if the operations to generate must have Implementations.
	 * @param arePrimitiveParams
	 *            Indicates if the parameters of the operations to generate are Primitive (Type class) or not (MetaClass)
	 * @param isPrimitiveReturn
	 *            Indicates if the return type of the operations to generate is Primitive (Type class) or not (MetaClass)
	 * @param classID
	 *            ID of the class containing the operations
	 * @return Set of Operation ifications.
	 * @note We use a HashSet to control Operation repetition.
	 * @author dgasull
	 */
	private static void generateOperations(final String metaclassName,
			final int hasParams,
			final int hasReturn, final int hasImplementations,
			final int arePrimitiveParams, final int isPrimitiveReturn,
			final ArrayList<Object[]> generatedOperations) {

		final Object[] testParam = new Object[1];

		Random rand = new Random();
		// We calculate the number of parameters to generate if the operation must have parameters
		int numparams = 0;
		if (hasParams > 0) {
			rand = new Random();
			numparams = rand.nextInt(RANDMAX) + 1; // Avoid 0
		}

		// We calculate the number of implementations to generate if the operation must have implementations
		int numimpl = 1;
		if (hasImplementations > 0) {
			rand = new Random();
			numimpl = rand.nextInt(RANDMAX) + 1; // Avoid 0
		}

		/*
		 * Parameters
		 */

		// We add N parameters to the Operation ification with the types and returns specified.
		final Map<String, Type> parameters = new LinkedHashMap<>();
		final List<String> parameterOrder = new LinkedList<>();
		for (int j = 0; j < numparams; j++) {
			if (arePrimitiveParams == 1) {
				// If the parameters are primitive we create a new 'Type' and add it to the operation
				final String primTypeSignature = getPrimitiveType();
				final Type primitiveTypeN = new Type(primTypeSignature, primTypeSignature, primTypeSignature, null);
				parameters.put("param" + j, primitiveTypeN);
			} else {
				// If the parameters are not primitive we create a new 'MetaClass' and add it to the operation
				final Type metaClassTypeN = new UserType(NAMESPACENAME, "testMetaClassType", "LtestMetaClassType;",
						"LtestMetaClassType;", null);
				parameters.put("param" + j, metaClassTypeN);
			}

			parameterOrder.add("param" + j);
		}

		/*
		 * Return
		 */

		Type returnType = new Type("V", "V", "V", null);
		if (hasReturn > 0) {
			// If the Operation has return.
			if (isPrimitiveReturn == 1) {
				// If the return type is primitive we add a new Type.
				final String primTypeSignature = getPrimitiveType();
				final Type primitiveTypeN = new Type(primTypeSignature, primTypeSignature, primTypeSignature, null);
				returnType = primitiveTypeN;

			} else {
				// Otherwise we add a new MetaClass.
				final Type metaClassTypeN = new UserType(NAMESPACENAME, "testMetaClassType", "LtestMetaClassType;",
						"LtestMetaClassType;", null);
				returnType = metaClassTypeN;
			}
		}

		/*
		 * Implementations
		 */

		// We prepare the operation specification.
		String signature = "(";
		if (parameters.size() > 0) {
			signature += parameters.get("param0").getSignature();
			for (int j = 1; j < parameters.size(); ++j) {
				signature += parameters.get("param" + j).getSignature();
			}
		}
		final SortedSet<Implementation> impls = new TreeSet<>();

		signature += ")" + returnType.getSignature();
		final Operation op = new Operation("testOperation", signature, signature,
				"testOperation" + signature,
				NAMESPACENAME, metaclassName, false);
		final JavaOperationInfo javaOpInfo = new JavaOperationInfo(Modifier.PUBLIC);
		op.addLanguageDepInfo(javaOpInfo);

		final OperationID opID = new OperationID();
		op.setDataClayID(opID);

		// We add N implementations to the Operation ification with the types and returns specified.
		for (int j = 0; j < numimpl; j++) {

			// Create an empty Method with same attributes and return type

			final Implementation impl = new JavaImplementation(j,
					new ArrayList<AccessedProperty>(),
					new ArrayList<AccessedImplementation>(), new ArrayList<Type>(),
					null,
					new Hashtable<FeatureType, QuantitativeFeature>(),
					new Hashtable<FeatureType, QualitativeFeature>(),
					NAMESPACENAME, metaclassName, "testOperation" + signature);

			impl.setDataClayID(new ImplementationID());
			impls.add(impl);
		}

		op.setImplementations(impls);
		op.setParams(parameters);
		op.setParamsOrder(parameterOrder);
		op.setReturnType(returnType);
		testParam[0] = op;
		generatedOperations.add(testParam);
	}

	/**
	 * @brief Get a Primitive Type randomnly
	 * @return A String representing a Primitive Type (int, float...)
	 */
	private static String getPrimitiveType() {
		final Random rand = new Random();
		final String[] primitives = { "Ljava/lang/String;", "Z", "B", "C", "D", "F", "I", "J", "S" };
		final int random = rand.nextInt(primitives.length);
		return primitives[random];
	}

	/**
	 * @brief Create a new empty metaClass
	 * @return The ID of the created Metaclass
	 * @note This function is not always necessary so that's why is not in a before function.
	 */

	private MetaClass createMetaClass() {

		// Create the Type for complex params and return types
		final MetaClass typeMetaclass = new MetaClass(NAMESPACENAME, "testMetaClassType",
				null, false);

		final JavaClassInfo javaClassInfo = new JavaClassInfo("LtestMetaClassType;", null);
		typeMetaclass.addLanguageDepInfo(javaClassInfo);

		typeMetaclass.setDataClayID(TESTMETACLASSTYPEID);
		typeMetaclass.setNamespaceID(NAMESPACEID);

		testdb.storeMetaClass(typeMetaclass);

		// We will use an empty MetaClass
		final MetaClass newMetaClass = new MetaClass();
		newMetaClass.setName("TestMetaClass");

		newMetaClass.setNamespaceID(NAMESPACEID);
		newMetaClass.setProperties(new TreeSet<Property>());
		newMetaClass.setOperations(new HashSet<Operation>());
		final JavaClassInfo newjavaClassInfo = new JavaClassInfo();
		newMetaClass.setLanguageDepInfos(new HashMap<Langs, LanguageDependantClassInfo>());
		newMetaClass.addLanguageDepInfo(newjavaClassInfo);
		final MetaClassID metaClassIDwithRef = new MetaClassID();
		newMetaClass.setDataClayID(metaClassIDwithRef);
		newMetaClass.setNamespace(NAMESPACENAME);

		// First we add a new Class
		testdb.storeMetaClass(newMetaClass);

		final MetaClassID metaClassID = metaClassIDwithRef;

		// Check the MetaClass exists in the database.
		final MetaClass updatedMetaclass = testdb.getMetaClassByID(metaClassID);
		assertTrue(updatedMetaclass != null);
		final MetaClassID updatedMetaclassID = updatedMetaclass.getDataClayID();
		assertTrue(updatedMetaclassID.equals(metaClassID));

		// Return the result of the query if we want to update using db4o
		return updatedMetaclass;
	}

	/**
	 * @brief Creates a new Operation in the MetaClass identified by the ID provided and verify the relationships and references.
	 * @param metaClassIDwithRef
	 *            ID of the MetaClass containing the Operation
	 */
	private Operation newOperation(final String className,
			final MetaClassID metaClassIDwithRef) throws Exception {

		final MetaClassID metaClassID = metaClassIDwithRef;

		// Create Operation
		final Operation testOperation = operation;

		// Analyze type of params of operation spec
		if (testOperation.getParams() != null && !testOperation.getParams().isEmpty()) {
			for (final Type param : testOperation.getParams().values()) {
				if (param instanceof UserType) {
					final UserType uType = (UserType) param;
					if (uType.getTypeName().equals(className)) {
						uType.setClassID(metaClassID);
					} else if (uType.getTypeName().equals("testMetaClassType")) {
						uType.setClassID(TESTMETACLASSTYPEID);
					}
				}
				if (param.getIncludes() != null) {
					for (final Type subInclude : param.getIncludes()) {
						if (subInclude instanceof UserType) {
							final UserType uType = (UserType) subInclude;
							if (uType.getTypeName().equals(className)) {
								uType.setClassID(metaClassID);
							} else if (uType.getTypeName().equals("testMetaClassType")) {
								uType.setClassID(TESTMETACLASSTYPEID);
							}
						}
					}
				}
			}

		}
		// And return type
		if (testOperation.getReturnType() instanceof UserType) {
			final UserType uType = (UserType) testOperation.getReturnType();
			if (uType.getTypeName().equals(className)) {
				uType.setClassID(metaClassID);
			} else if (uType.getTypeName().equals("testMetaClassType")) {
				uType.setClassID(TESTMETACLASSTYPEID);
			}
		}
		if (testOperation.getReturnType().getIncludes() != null) {
			for (final Type subInclude : testOperation.getReturnType().getIncludes()) {
				if (subInclude instanceof UserType) {
					final UserType uType = (UserType) subInclude;
					if (uType.getTypeName().equals(className)) {
						uType.setClassID(metaClassID);
					} else if (uType.getTypeName().equals("testMetaClassType")) {
						uType.setClassID(TESTMETACLASSTYPEID);
					}
				}
			}
		}

		for (final Implementation impl : testOperation.getImplementations()) {
			impl.setOperationID(operation.getDataClayID());
			impl.setMetaClassID(metaClassID);
			impl.setResponsibleAccountID(new AccountID());
			impl.setNamespaceID(NAMESPACEID);
		}

		testOperation.setMetaClassID(metaClassID);
		testOperation.setNamespaceID(NAMESPACEID);

		final Operation operationInfo = cman.newOperation(NAMESPACEID, metaClassID, testOperation);
		final OperationID operationID = operationInfo.getDataClayID();

		// Verify that the Operation exists in the database.
		final Operation operation = testdb.getOperationByID(operationID);
		assertTrue(operation != null);

		// Verify operation<->ID relationship
		assertTrue(operation.getDataClayID().equals(operationID));

		// Check Metaclass->operation association.
		final MetaClass updatedMetaClass = testdb.getMetaClassByID(metaClassID);
		assertTrue(updatedMetaClass != null);
		assertTrue(updatedMetaClass.existsOperationInClass(operationID));

		// Check operation->MetaClass association.
		assertTrue(operation.getMetaClassID().equals(metaClassID));

		// PARAMETERS
		if (operation.getParams() != null && !operation.getParams().isEmpty()) {
			final Map<String, Type> params = operation.getParams();
			for (final Entry<String, Type> curParam : params.entrySet()) {
				final Type paramType = testdb.getTypeByID(curParam.getValue().getId());
				assertTrue(paramType != null);
			}
		}

		// RETURN TYPE
		if (operation.getReturnType() != null) {
			final Type retType = testdb.getTypeByID(operation.getReturnType().getId());
			assertTrue(retType != null);
		}

		// IMPLEMENTATIONS
		final List<Implementation> implementations = operation.getImplementations();
		if (operation.getImplementations() != null && !operation.getImplementations().isEmpty()) {
			final Iterator<Implementation> iterator = implementations.iterator();
			while (iterator.hasNext()) {
				final Implementation actImpl = iterator.next();
				final ImplementationID actImplID = actImpl.getDataClayID();
				final Implementation impl = testdb.getImplementationByID(actImplID);
				assertTrue(impl != null);
				assertTrue(impl.getDataClayID().equals(actImplID));
			}
		}

		return operation;
	}

	/*************************** FUNCTIONAL TESTS ***************************/

	/**
	 * @brief Test method for ClassManager::newOperation(NamespaceID, Operation, MetaClassID)
	 * @author dgasull
	 * @pre The functions DbHandler::existsObjectByID(util.ids.ID), DbHandler::queryByExampleID(util.ids.ID) must be tested and
	 *      correct.
	 * @post Test the creation and storage of a new Operation. <br>
	 *       If the function throws an error the test fails. <br>
	 *       If after the creation and storage we query the database for the Operation and we obtain that it does not exists, the
	 *       test fails <br>
	 *       Otherwise the test is passed.
	 */
	@Test
	public void testNewOperation() throws Exception {

		final MetaClass metaClass = createMetaClass();
		final MetaClassID metaClassID = metaClass.getDataClayID();

		// CALL FUNCTION TO TEST
		newOperation(metaClass.getName(), metaClassID);
	}

	/**
	 * @brief Test method for ClassManager::getImplementationsOfOperation(OperationID)
	 * @author dgasull
	 * @pre The function DbHandler::store(Object) and ClassManager::newOperation(TypeID, Operation) must be tested and correct.
	 * @post Test the function that gets the Implementations of an Operation.
	 */
	@Test
	public void testGetImplementationsOfOperation() throws Exception {

		final MetaClass metaClass = createMetaClass();
		final MetaClassID metaClassIDwithRef = metaClass.getDataClayID();
		final MetaClassID metaClassID = metaClassIDwithRef;

		// Now we add the operation in order to remove it.
		final Operation operation = newOperation(metaClass.getName(), metaClassID);
		final OperationID operationID = operation.getDataClayID();

		// Now we get the implementations
		final LinkedList<ImplementationID> implIDs = cman.getImplementationsOfOperation(operationID);

		for (final ImplementationID implID : implIDs) {
			assertTrue(operation.existsImplementationInOperation(implID));
		}

	}

	/**
	 * @brief Test method for ClassManager::removeOperation(OperationID)
	 * @author dgasull
	 * @pre The functions DbHandler::queryByExampleID(util.ids.ID), and DbHandler::existsObjectByID(util.ids.ID) and
	 *      ClassManager::newOperation(TypeID, Operation) must be tested and correct.
	 * @post Test the remove function of an Operation. <br>
	 *       If the function throws an error the test fails. <br>
	 *       If after the remove process we query the database for the Operation and we obtain that it exists, the test fails <br>
	 *       Otherwise the test is passed.
	 */
	@Test
	public void testRemoveOperation() throws Exception {

		final MetaClass metaClass = createMetaClass();
		final MetaClassID metaClassID = metaClass.getDataClayID();

		// Now we add the operation in order to remove it.
		final Operation operation = newOperation(metaClass.getName(), metaClassID);
		final OperationID operationIDwithRef = operation.getDataClayID();
		final OperationID operationID = operationIDwithRef;

		// Return type
		final Type protoRetType = operation.getReturnType();

		// Now we call the function in order to remove the Operation, its Parameters and Implementations
		cman.removeOperation(operationID);

		// Verify that the Operation does not exist in the database.
		assertFalse(testdb.getOperationByID(operationID) != null);

		// Verify that the class does not have the operation associated
		final MetaClass updatedClass = testdb.getMetaClassByID(metaClassID);
		assertTrue(updatedClass != null);
		assertFalse(updatedClass.existsOperationInClass(operationID));
		assertFalse(updatedClass.getOperations() == null);

		// Verify that the Parameters do not exist.
		if (operation.getParams() != null && !operation.getParams().isEmpty()) {
			final Map<String, Type> params = operation.getParams();
			for (final Entry<String, Type> curParam : params.entrySet()) {
				assertFalse(testdb.getTypeByID(curParam.getValue().getId()) != null);

			}
		}

		// Verify that the Implementations do not exist.
		if (operation.getImplementations() != null && !operation.getImplementations().isEmpty()) {
			final List<Implementation> implementations = operation.getImplementations();
			final Iterator<Implementation> iterator = implementations.iterator();
			while (iterator.hasNext()) {
				assertFalse(testdb.getImplementationByID(iterator.next().getDataClayID()) != null);

			}
		}

		// Verify the Return Type does not exist.
		if (operation.getReturnType() != null) {
			assertFalse(testdb.getTypeByID(protoRetType.getId()) != null);

		}

	}

	/**
	 * @brief Test method of ClassManager::getOperationID(NamespaceID, String, String, String[], String)
	 * @pre The functions DbHandler::store(Object), ClassManager::newOperation(MetaClassID, Operation) and
	 *      DbHandler::queryByExampleID(util.ids.ID) must be tested an correct.
	 * @post Test the function that gets the ID of an Operation given its name, param type names, return type name and metaclass
	 *       name. <br>
	 *       If the function throws an error the test fails. <br>
	 *       If after the get process we verify that it is not the same ID, the test fails <br>
	 *       Otherwise the test is passed.
	 */
	@Test
	public void testGetOperationID() throws Exception {

		final MetaClass metaClass = createMetaClass();
		final MetaClassID metaClassID = metaClass.getDataClayID();

		// We add now the new Operation by calling the newOperation function
		// It is important to use this function since our test is parameterized
		final Operation operation = newOperation(metaClass.getName(), metaClassID);
		final OperationID operationID = operation.getDataClayID();

		final OperationID newOperationID = cman.getOperationID(metaClassID, operation.getNameAndDescriptor());
		assertTrue(newOperationID.equals(operationID));

	}

	/*************************** EXCEPTION TEST ***************************/

	/**
	 * @brief Test method for ClassManager::newOperation(NamespaceID, Operation, MetaClassID)
	 * @author dgasull
	 * @post Test that if the Class in which to add the Operation already has the operation an error is thrown.
	 */
	@Test(expected = OperationAlreadyInClassException.class)
	public void testNewOperationAlreadyInClassException() throws Exception {

		final MetaClass metaClass = createMetaClass();
		final MetaClassID metaClassID = metaClass.getDataClayID();

		// Now we add the operation in order to remove it.
		newOperation(metaClass.getName(), metaClassID);

		final Operation testOperation = operation;
		cman.newOperation(NAMESPACEID, metaClassID, testOperation);
	}

	/**
	 * @brief Test method for ClassManager::newOperation(NamespaceID, Operation, MetaClassID)
	 * @author dgasull
	 * @post Test that if the Class in which to add the Operation already has a matching-type operation, an error is thrown.
	 */
	@Test(expected = OperationAlreadyInClassException.class)
	public void testSameTypeOperationAlreadyInClassException() throws Exception {

		final MetaClass metaClass = createMetaClass();
		final MetaClassID metaClassID = metaClass.getDataClayID();

		// Now we add the operation in order to remove it.
		newOperation(metaClass.getName(), metaClassID);

		final Operation testOperation = new Operation(operation.getName(),
				operation.getDescriptor(), operation.getSignature(),
				operation.getNameAndDescriptor(),
				operation.getNamespace(), operation.getClassName(),
				operation.getIsAbstract());

		testOperation.setImplementations(operation.getImplementations());

		final Map<String, Type> params = new LinkedHashMap<>();
		final Map<String, Type> oldParams = operation.getParams();
		final List<String> paramOrder = new LinkedList<>();
		for (final String p : operation.getParamsOrder()) {
			paramOrder.add(p + "bis");
			params.put(p + "bis", oldParams.get(p));
		}
		testOperation.setParams(params);
		testOperation.setParamsOrder(paramOrder);
		testOperation.setReturnType(operation.getReturnType());
		testOperation.setLanguageDepInfos(operation.getLanguageDepInfos());

		cman.newOperation(NAMESPACEID, metaClassID, testOperation);
	}

	/**
	 * @brief Test method for ClassManager::removeOperation(OperationID)
	 * @author dgasull
	 * @post Test the remove process of Operation in the database. In this case we will try to remove an Operation that does not
	 *       exist in the System and see if an error is thrown. If not, the test fails. <br>
	 */
	@Test(expected = OperationNotExistException.class)
	public void testRemoveOperationNotExistException() {
		// We just call the function with a new OperationID. Since the database is empty, we are
		// sure that there is no Operation with the OperationID provided.
		cman.removeOperation(new OperationID());

	}

	/**
	 * @brief Test method of ClassManager::getImplementationsOfOperation(OperationID)
	 * @post Test that if the Operation does not exist and we try to get its Implementations an error is thrown.
	 */
	@Test(expected = OperationNotExistException.class)
	public void testGetImplementationsOfOperationNotExistException() {
		cman.getImplementationsOfOperation(new OperationID());

	}

	/**
	 * @brief Test method of ClassManager::getOperationID(NamespaceID, String, String, String[], String)
	 * @post Test that if the no Operation with name provided exist an error is thrown.
	 */
	@Test(expected = OperationNotExistException.class)
	public void testGetOperationNameNotExistException() {

		final MetaClass metaClass = createMetaClass();
		// Call the function with a name of a operation that does not exist
		cman.getOperationID(metaClass.getDataClayID(), "unexistantOperation");

	}

}
