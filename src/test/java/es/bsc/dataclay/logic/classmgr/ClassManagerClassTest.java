
/**
 * @file ClassManagerClassTest.java
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
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import org.junit.*;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.yaml.snakeyaml.Yaml;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.exceptions.logicmodule.classmgr.OperationNotInClassException;
import es.bsc.dataclay.exceptions.logicmodule.classmgr.PropertyNotInClassException;
import es.bsc.dataclay.logic.classmgr.ClassManager;
import es.bsc.dataclay.logic.classmgr.ClassManagerDB;
import es.bsc.dataclay.test.AbstractManagerTest;
import es.bsc.dataclay.test.TestUtils;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.management.classmgr.AccessedImplementation;
import es.bsc.dataclay.util.management.classmgr.AccessedProperty;
import es.bsc.dataclay.util.management.classmgr.Implementation;
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
import es.bsc.dataclay.util.management.classmgr.java.JavaPropertyInfo;
import es.bsc.dataclay.util.structs.Tuple;
import es.bsc.dataclay.util.yaml.CommonYAML;

/**
 * @author dgasull
 * @class ClassManagerClassTest
 * @brief This class tests the ClassManager focusing in MetaClass functions.
 * @note This is a parameterized test.
 */
@Ignore
@RunWith(Parameterized.class)
public final class ClassManagerClassTest extends AbstractManagerTest {

	private ClassManager cman; // !<@brief Class Manager instance tested.

	private static String DBFILESDIRNAME = System.getProperty("user.dir") + "/dbfiles/ClassManagerClassTest";
	// !<@brief Path to the test databases.

	private static final String NAMESPACENAME = "testNamespace";
	private static final NamespaceID NAMESPACEID = new NamespaceID(); // !<@brief NamespaceID used in tests
	private static final AccountID NAMESPACERESP = new AccountID(); // !<@brief AccountID used in tests

	private static final int RANDMAX = 3; // !<@brief Seed of Random numbers generated in tests

	private final MetaClass globalMetaClass;
	private MetaClass metaClass; // !<@brief MetaClass used in tests

	private MetaClassID complexTypeClassID;
	private ClassManagerDB testdb; // !<@brief DbHandler used in tests.

	/**
	 * @brief This method is executed before all tests. It is used to instantiate variables that are used in all the tests in
	 *        order to save code.
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

		// Clone metaClass
		final Yaml yaml = CommonYAML.getYamlObject();
		final String strYaml = yaml.dump(globalMetaClass);
		this.metaClass = (MetaClass) CommonYAML.getYamlObject().load(strYaml);
		setIDs(metaClass);

		cman = new ClassManager(dataSource);
		testdb = cman.getDbHandler();
		testdb.dropTables();
		testdb.createTables();

		// Store the complex type for user return types, param types and property user types...
		final MetaClass complexTypeClass = new MetaClass(NAMESPACENAME, "testMetaClassType", null, false);
		final JavaClassInfo classInfo = new JavaClassInfo("LtestMetaClassType;", null);
		complexTypeClass.addLanguageDepInfo(classInfo);
		complexTypeClassID = new MetaClassID();
		complexTypeClass.setDataClayID(complexTypeClassID);
		complexTypeClass.setNamespaceID(NAMESPACEID);
		setIDs(complexTypeClass);
		testdb.storeMetaClass(complexTypeClass);

		// Modify actual metaClass to use the MetaClassID obtained
		setAssocClassID("testMetaClassType", metaClass.getDataClayID(), metaClass);

	}

	private void setAssocClassID(final String assocClassName, final MetaClassID assocClassID, final MetaClass actualMetaClass) {
		final MetaClassID newmetaClassID = new MetaClassID();
		actualMetaClass.setDataClayID(newmetaClassID);
		actualMetaClass.setNamespaceID(NAMESPACEID);

		// No includes of properties and operations and accessed properties and accessed operations in this test
		if (actualMetaClass.getParentType() != null) {
			final UserType parentType = actualMetaClass.getParentType();
			if (parentType.getTypeName().equals(assocClassName)) {
				actualMetaClass.getParentType().setClassID(assocClassID);
			}
		}

		if (actualMetaClass.getOperations() != null) {
			for (final Operation op : actualMetaClass.getOperations()) {
				for (final Type param : op.getParams().values()) {
					if (param instanceof UserType) {
						final UserType utype = (UserType) param;
						if (utype.getTypeName().equals(assocClassName)) {
							utype.setClassID(assocClassID);
						}
					}
					if (param.getIncludes() != null) {
						for (final Type subinclude : param.getIncludes()) {
							if (subinclude instanceof UserType) {
								final UserType utype = (UserType) subinclude;
								if (utype.getTypeName().equals(assocClassName)) {
									utype.setClassID(assocClassID);
								}
							}
						}
					}
				}
				final OperationID newoperationID = new OperationID();
				op.setDataClayID(newoperationID);
				op.setNamespaceID(NAMESPACEID);
				op.setMetaClassID(newmetaClassID);

				if (op.getReturnType() instanceof UserType) {
					final UserType utype = (UserType) op.getReturnType();
					if (utype.getTypeName().equals(assocClassName)) {
						utype.setClassID(assocClassID);
					}
					if (op.getReturnType().getIncludes() != null) {
						for (final Type subinclude : op.getReturnType().getIncludes()) {
							if (subinclude instanceof UserType) {
								final UserType subutype = (UserType) subinclude;
								if (subutype.getTypeName().equals(assocClassName)) {
									subutype.setClassID(assocClassID);
								}
							}
						}
					}
				}
				for (final Implementation imp : op.getImplementations()) {
					for (final Type include : imp.getIncludes()) {
						if (include instanceof UserType) {
							final UserType utype = (UserType) include;
							if (utype.getTypeName().equals(assocClassName)) {
								utype.setClassID(assocClassID);
							}
						}
						if (include.getIncludes() != null) {
							for (final Type subinclude : include.getIncludes()) {
								if (subinclude instanceof UserType) {
									final UserType subutype = (UserType) subinclude;
									if (subutype.getTypeName().equals(assocClassName)) {
										subutype.setClassID(assocClassID);
									}
								}
							}
						}
					}
					imp.setDataClayID(new ImplementationID());
					imp.setOperationID(newoperationID);
					imp.setNamespaceID(NAMESPACEID);
				}
			}
		}
		if (actualMetaClass.getProperties() != null) {
			for (final Property prop : actualMetaClass.getProperties()) {
				if (prop.getType() instanceof UserType) {
					final UserType utype = (UserType) prop.getType();
					if (utype.getTypeName().equals(assocClassName)) {
						utype.setClassID(assocClassID);
					}
				}
				if (prop.getType().getIncludes() != null) {
					for (final Type subinclude : prop.getType().getIncludes()) {
						if (subinclude instanceof UserType) {
							final UserType subutype = (UserType) subinclude;
							if (subutype.getTypeName().equals(assocClassName)) {
								subutype.setClassID(assocClassID);
							}
						}
					}
				}

				prop.setDataClayID(new PropertyID());
				prop.setMetaClassID(newmetaClassID);
				prop.setNamespaceID(NAMESPACEID);
			}
		}
	}

	/**
	 * @brief This method is executed after each test. It is used to delete the test database since we are testing the creation
	 *        of Metaclasses on it, it is necessary to empty the database before.
	 * @author dgasull
	 * @throws Exception
	 */
	@Override
	@After
	public void after() throws Exception {
		testdb.close();
		super.after();
		TestUtils.cleanDirectory(DBFILESDIRNAME);
		cman = null;
		testdb = null;
	}

	@AfterClass
	public static void afterAll() throws Exception {
		TestUtils.deleteDirectory(DBFILESDIRNAME);
	}

	/**
	 * @brief Creates a new ClassManagerClassTest
	 * @author dgasull
	 * @param metaClassCase
	 *            MetaClass specification of this set of tests
	 * @note This function is necessary for the Parameterized Test.
	 */
	public ClassManagerClassTest(final MetaClass metaClassCase) {
		this.globalMetaClass = metaClassCase;
	}

	/*************************** TEST PARAMETERS ***************************/

	/**
	 * @brief This method is executed before all tests. <br>
	 *        It is used to prepare all the possible Parameters of this Parameterized Test.<br>
	 *        Each array of Objects obtained in the collection represents the Parameters of the Constructor function. So for
	 *        each array in the Collection all tests in this class are run. In our case we prepare all possible Class
	 *        ifications: <br>
	 *        <br>
	 *        SPECIFICATION CLASS TESTED CASES: One ification can have different options, we must combine them in order to do a
	 *        Complete Test. The structure of the combinational table and an example are:<br>
	 *        <br>
	 *        <table>
	 *        <tr>
	 *        <td>HAS PROPERTIES</td>
	 *        <td>1 (1 MEANS N PROPERTIES, 0 MEANS 0 PROPERTIES)</td>
	 *        </tr>
	 *        <tr>
	 *        <td>HAS OPERATIONS</td>
	 *        <td>1 (1 MEANS N OPERATIONS, 0 MEANS 0 OPERATIONS)</td>
	 *        </tr>
	 *        <tr>
	 *        <td>+AREPRIMITIVE PROPERTIES</td>
	 *        <td>0 (0 MEANS METACLASS PROPERTY)</td>
	 *        </tr>
	 *        <tr>
	 *        <td>+OPERATION HAS RETURN</td>
	 *        <td>0 (0 MEANS NO RETURN TYPE)</td>
	 *        </tr>
	 *        <tr>
	 *        <td>+OPERATION HAS N PARAMETERS</td>
	 *        <td>1 (1 MEANS N PARAMETERS)</td>
	 *        </tr>
	 *        <tr>
	 *        <td>+OPERATION HAS N IMPL.</td>
	 *        <td>1 (1 MEANS N IMPLEMENTATIONS)</td>
	 *        </tr>
	 *        <tr>
	 *        <td>++OPERATION HAVE PRIMITIVE PAR</td>
	 *        <td>1 (1 MEANS PRIMITIVE PARAMETERS)</td>
	 *        </tr>
	 *        <tr>
	 *        <td>++OPERATION HAS PRIMITIVE RETURN</td>
	 *        <td>0 (0 MEANS METACLASS RETURN TYPE)</td>
	 *        </tr>
	 *        </table>
	 *        <br>
	 *        The total number of combinations is: <br>
	 *        1 case without properties and operations <br>
	 *        2 cases without operations (primitive and metaclass properties) <br>
	 *        2 cases without properties and with operations without parameters nor return <br>
	 *        2^2 cases without properties and with operations without parameters but with return <br>
	 *        2^2 cases without properties and with operations with parameters but without return <br>
	 *        2^3 cases without properties and with operations with parameters and return <br>
	 *        (2^3+2^2+2^2+2)*2 cases with operations and properties <br>
	 *        TOTAL: 57 combinations. <br>
	 *        <br>
	 *        A combination table snapshot can be the following: <br>
	 *        <br>
	 *        <table>
	 *        <tr>
	 *        <td>nProp</td>
	 *        <td>nOp</td>
	 *        <td>prProp</td>
	 *        <td>hasRet</td>
	 *        <td>nPar</td>
	 *        <td>nImpl</td>
	 *        <td>prPara</td>
	 *        <td>prRet</td>
	 *        </tr>
	 *        <tr>
	 *        <td>0</td>
	 *        <td>0</td>
	 *        <td>X</td>
	 *        <td>X</td>
	 *        <td>X</td>
	 *        <td>X</td>
	 *        <td>X</td>
	 *        <td>X</td>
	 *        </tr>
	 *        <tr>
	 *        <td>0</td>
	 *        <td>1</td>
	 *        <td>X</td>
	 *        <td>0</td>
	 *        <td>0</td>
	 *        <td>0</td>
	 *        <td>0</td>
	 *        <td>X</td>
	 *        </tr>
	 *        <tr>
	 *        <td>0</td>
	 *        <td>1</td>
	 *        <td>X</td>
	 *        <td>1</td>
	 *        <td>0</td>
	 *        <td>0</td>
	 *        <td>0</td>
	 *        <td>0</td>
	 *        </tr>
	 *        <tr>
	 *        <td>0</td>
	 *        <td>1</td>
	 *        <td>X</td>
	 *        <td>1</td>
	 *        <td>0</td>
	 *        <td>0</td>
	 *        <td>0</td>
	 *        <td>1</td>
	 *        </tr>
	 *        <tr>
	 *        <td>...</td>
	 *        <td>...</td>
	 *        <td>...</td>
	 *        <td>...</td>
	 *        <td>...</td>
	 *        <td>...</td>
	 *        <td>...</td>
	 *        <td>...</td>
	 *        </tr>
	 *        <tr>
	 *        <td>1</td>
	 *        <td>0</td>
	 *        <td>0</td>
	 *        <td>X</td>
	 *        <td>X</td>
	 *        <td>X</td>
	 *        <td>X</td>
	 *        <td>X</td>
	 *        </tr>
	 *        <tr>
	 *        <td>1</td>
	 *        <td>0</td>
	 *        <td>1</td>
	 *        <td>X</td>
	 *        <td>X</td>
	 *        <td>X</td>
	 *        <td>X</td>
	 *        <td>X</td>
	 *        </tr>
	 *        </table>
	 *        <br>
	 * @return Array of Object List. Each element of the array is an array of objects. In our case, this array of objects is
	 *         only one object: the MetaClass ification case. It is used for our Parameterized test.
	 * @author dgasull
	 * @throws RemoteException
	 */
	@Parameters
	public static Collection<Object[]> generates() throws RemoteException {

		// First we prepare the Collection to return.
		ArrayList<Object[]> metaClasss = new ArrayList<>();

		// Now, we iterate all the Combinations possible.
		/*
		 * hasProp: --> 0 means 0 properties --> 1 means N properties
		 */
		for (int hasProp = 0; hasProp < 2; ++hasProp) {
			/*
			 * hasOperations: --> 0 means 0 operations --> 1 means N operations
			 */
			for (int hasOperations = 0; hasOperations < 2; ++hasOperations) {

				/*
				 * In order to save some useless checks, for example if we know that we do nothave properties, why do we need to
				 * check the case that the Properties are Primitive or not? Therefore, we verify it here.
				 */

				SortedSet<Property> propertys = new TreeSet<>();
				// Useful sneak while programming combinations

				if (hasProp > 0) { // Prune of Tree
					// If we have N properties we check if they will be Primitive or not.
					/*
					 * isPrimitiveProp: Type of the Properties to add --> 0 means metaclass property (Property of MetaClass
					 * class) --> 1 means primitive property (Property of Type class)
					 */
					for (int isPrimitiveProp = 0; isPrimitiveProp < 2; ++isPrimitiveProp) {

						// We call the function to generate N primitive/complex properties.
						propertys = generatePropertys(isPrimitiveProp, "testMetaClass" + metaClasss.size());
						metaClasss = generatesPrune(hasOperations, propertys, metaClasss);

					}
				} else {
					metaClasss = generatesPrune(hasOperations, propertys, metaClasss);
				}
			}
		}
		return metaClasss;
	}

	/**
	 * @brief This function is used by the function {@link #generates()} in order to prune the Combinational tree and generate
	 *        new MetaClass ifications.
	 * @param hasOperations
	 *            Indicates if the MetaClass ification has Operations to generate.
	 * @param propertys
	 *            Property ifications to add to each MetaClass ification. All MetaClass ifications will have the same Property
	 *            ification
	 * @param metaClasss
	 *            Array of Object list which each one contains only a MetaClass (Paremeterized test) Since we are generating a
	 *            Matrix of combinations, we should pass this as a parameter, like in/out parameter.
	 * @param classID
	 *            ID of the class containig the properties
	 * @return Array of Object List. Each element of the array is an array of objects. In our case, this array of objects is
	 *         only one object: the MetaClass ification case. It is used for our Parameterized test.
	 * @author dgasull
	 */
	private static ArrayList<Object[]> generatesPrune(final int hasOperations,
			final SortedSet<Property> propertys, final ArrayList<Object[]> metaClasss) {

		ArrayList<Object[]> pruneMetaClasss = metaClasss;

		// Since we must provide an array of objects for each element in the Collection
		// we prepare it with only one element because the constructor function of this class
		// has only one parameter.
		final Object[] testParam = new Object[1];

		// Now we check if we have N operations.
		if (hasOperations > 0) { // Prune of Tree
			// If we have N operations we call function to generate them.
			// This function will modify metaClasss (add N operation and
			// Property cases) so we provide Propertys.
			pruneMetaClasss = generatesWithOperations(propertys, pruneMetaClasss);
		} else {
			// If we do not have Operations we just add a new MetaClass without Operations
			// and the property ifications generated before.
			final MetaClass curClass = new MetaClass(NAMESPACENAME, "testMetaClassType" + metaClasss.size(), null, false);
			final JavaClassInfo classInfo = new JavaClassInfo("LtestMetaClassType" + metaClasss.size() + ";", null);
			curClass.addLanguageDepInfo(classInfo);
			curClass.setProperties(propertys);

			testParam[0] = curClass;
			pruneMetaClasss.add(testParam);
		}

		return pruneMetaClasss;
	}

	/**
	 * @brief This function is responsible to generate N property specifications of type primitive or complex (MetaClass)
	 * @param nOperations
	 *            Number of Operations to add to each MetaClass ification generated.
	 * @param IsPrimitiveProp
	 *            Indicates if the Properties to generate are Primitive (Type class) or not (MetaClass)
	 * @param classID
	 *            ID of the class containig the properties
	 * @return Set of Property ifications.
	 * @note We use a HashSet to control Property repetition.
	 * @author dgasull
	 */
	private static SortedSet<Property> generatePropertys(final int isPrimitive,
			final String className) {

		// We calculate how many properties we will have in this case.
		final Random rand1 = new Random();
		final int nprops = rand1.nextInt(RANDMAX) + 1; // Avoid 0

		final SortedSet<Property> propertys = new TreeSet<>();
		for (int j = 0; j < nprops; j++) {
			// For each property
			if (isPrimitive == 1) {
				// if it is primitive, we just create a Primitive Type with an Unique Name (we know that we cannot have
				// two properties with same name)

				// We create the Primitive Type.
				final String primTypeSignature = getPrimitiveType();
				final Type primitiveTypeN = new Type(primTypeSignature, primTypeSignature, primTypeSignature, null);

				// We create a Property with this Type.
				final Property primitivePropertyN = new Property(j, "testPrimitiveProperty"
						+ UUID.randomUUID(), primitiveTypeN, NAMESPACENAME, className);

				final JavaPropertyInfo javaPropertyInfo = new JavaPropertyInfo(Modifier.PUBLIC);
				primitivePropertyN.addLanguageDepInfo(javaPropertyInfo);
				primitivePropertyN.setDataClayID(new PropertyID());
				// We add it to the result list of Properties.
				propertys.add(primitivePropertyN);

			} else {
				// We create the MetaClass Type.
				final Type metaClassTypeN = new UserType(NAMESPACENAME, "testMetaClassType",
						"LtestMetaClassType;", "LtestMetaClassType;", null);
				// We create a Property with this Type.
				final Property metaClassPropertyN = new Property(j, "testMetaClassProperty"
						+ UUID.randomUUID(), metaClassTypeN, NAMESPACENAME, className);
				final JavaPropertyInfo javaPropertyInfo = new JavaPropertyInfo(Modifier.PUBLIC);
				metaClassPropertyN.addLanguageDepInfo(javaPropertyInfo);
				metaClassPropertyN.setDataClayID(new PropertyID());

				// We add it to the result list of Properties.
				propertys.add(metaClassPropertyN);
			}
		}
		return propertys;
	}

	/**
	 * @brief This function is responsible to add to the metaClasss provided those MetaClass ifications with Operations. This
	 *        means that this function is called when we are sure that there will be n operations in our MetaClass ifications.
	 *        Each MetaClass ification in the result collection will have the same number of operations (N) but with different
	 *        cases (no parameters, no return types...).
	 * @param propertys
	 *            Property ifications to add to each MetaClass ification. All MetaClass ifications will have the same Property
	 *            ification (see {@link #generates()}.
	 * @param metaClasss
	 *            Array of Object list which each one contains only a MetaClass (Paremeterized test) Since we are generating a
	 *            Matrix of combinations, we should pass this as a parameter, like in/out parameter.
	 * @param classID
	 *            ID of the class containing operations
	 * @return Array of Object List. Each element of the array is an array of objects. In our case, this array of objects is
	 *         only one object: the MetaClass ification case. It is used for our Parameterized test.
	 * @author dgasull
	 */
	private static ArrayList<Object[]> generatesWithOperations(final SortedSet<Property> propertys,
			final ArrayList<Object[]> metaClasss) {

		ArrayList<Object[]> newMetaClasss = metaClasss;
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
						 * opPrimitiveParam: Type of parameter. --> 0 means metaclass parameter (parameter of MetaClass class)
						 * --> 1 means primitive parameter (parameter of Type class)
						 */
						for (int opPrimitiveParam = 0; opPrimitiveParam < 2; ++opPrimitiveParam) {
							newMetaClasss = generatesWithOperationsPrune(hasReturn, hasParams, hasImpl,
									opPrimitiveParam, propertys, newMetaClasss);
						}
					} else {
						newMetaClasss = generatesWithOperationsPrune(hasReturn, hasParams, hasImpl, -1, propertys,
								newMetaClasss);
					}
				}
			}
		}
		return newMetaClasss;
	}

	/**
	 * @brief This function is used by the function {@link #generatesWithOperations(HashSet, ArrayList)} in order to prune the
	 *        Combinational tree generated.
	 * @param hasParams
	 *            Indicates if the operations to generate must have parameters.
	 * @param hasReturn
	 *            Indicates if the operations to generate must have return.
	 * @param hasImplementations
	 *            Indicates if the operations to generate must have Implementations.
	 * @param arePrimitiveParams
	 *            Indicates if the parameters of the operations to generate are Primitive (Type class) or not (MetaClass)
	 * @param propertys
	 *            Property ifications to add to each MetaClass ification. All MetaClass ifications will have the same Property
	 *            ification (see {@link #generates()}.
	 * @param metaClasss
	 *            Array of Object list which each one contains only a MetaClass (Paremeterized test) Since we are generating a
	 *            Matrix of combinations, we should pass this as a parameter, like in/out parameter.
	 * @param classID
	 *            ID of class containing the operations
	 * @return Array of Object List. Each element of the array is an array of objects. In our case, this array of objects is
	 *         only one object: the MetaClass ification case. It is used for our Parameterized test.
	 * @author dgasull
	 */
	private static ArrayList<Object[]> generatesWithOperationsPrune(final int hasReturn, final int hasParams,
			final int hasImpl, final int arePrimitiveParams, final SortedSet<Property> propertys,
			final ArrayList<Object[]> metaClasss) {

		final Object[] testParam = new Object[1];

		if (hasReturn > 0) {
			/*
			 * opPrimitiveRet: Type of return. --> 0 means return parameter (return of MetaClass class) --> 1 means return
			 * parameter (return of Type class)
			 */
			for (int opPrimitiveRet = 0; opPrimitiveRet < 2; ++opPrimitiveRet) {
				// This function will modify operations (add N operations with return)

				final HashSet<Operation> operations = generateOperations("testMetaClass" + metaClasss.size(),
						hasParams, hasReturn, hasImpl, arePrimitiveParams,
						opPrimitiveRet);
				final MetaClass curClass = new MetaClass(NAMESPACENAME,
						"testMetaClassType" + metaClasss.size(), null, false);
				final JavaClassInfo javaClassInfo = new JavaClassInfo("LtestMetaClassType"
						+ metaClasss.size() + ";", null);
				curClass.addLanguageDepInfo(javaClassInfo);
				curClass.setProperties(propertys);
				curClass.setOperations(operations);
				testParam[0] = curClass;
				metaClasss.add(testParam);
			}
		} else {
			// This function will modify operations (add N operations with return)
			final HashSet<Operation> operations = generateOperations("testMetaClass" + metaClasss.size(),
					hasParams, hasReturn, hasImpl, arePrimitiveParams, -1);

			final MetaClass curClass = new MetaClass(NAMESPACENAME,
					"testMetaClassType" + metaClasss.size(), null, false);
			final JavaClassInfo javaClassInfo = new JavaClassInfo("LtestMetaClassType"
					+ metaClasss.size() + ";", null);
			curClass.addLanguageDepInfo(javaClassInfo);
			curClass.setProperties(propertys);
			curClass.setOperations(operations);
			testParam[0] = curClass;
			metaClasss.add(testParam);
		}

		return metaClasss;

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
	private static HashSet<Operation> generateOperations(final String metaclassName,
			final int hasParams,
			final int hasReturn, final int hasImplementations,
			final int arePrimitiveParams, final int isPrimitiveReturn) {

		// We calculate the number of operations to generate
		Random rand = new Random();
		final int numops = rand.nextInt(RANDMAX) + 1; // Avoid 0

		final HashSet<Operation> operations = new HashSet<>();

		for (int i = 0; i < numops; i++) {

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
			final Operation op = new Operation("testOperation" + i, signature, signature,
					"testOperation" + i + signature,
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
						NAMESPACENAME, metaclassName, "testOperation" + i + signature);
				impl.setDataClayID(new ImplementationID());
				impls.add(impl);
			}

			op.setImplementations(impls);
			op.setParams(parameters);
			op.setParamsOrder(parameterOrder);
			op.setReturnType(returnType);

			// We add the operation ification calculated to the list of ifications of operations.
			operations.add(op);

		}

		return operations;
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
	 * @brief Verify the existence and relationship of a Property
	 * @param propertyID
	 *            ID of the Property to verify
	 * @param metaClassID
	 *            ID of the MetaClass containing the Property
	 * @throws Exception
	 */
	private void verifyProperty(final PropertyID propertyID,
			final MetaClassID metaClassID) throws Exception {
		// Verify that the Property exists in the database.
		final Property property = testdb.getPropertyByID(propertyID);
		assertTrue(property != null);

		// Verify Property<->ID relationship
		assertTrue(property.getDataClayID().equals(propertyID));

		// Check Metaclass->Property association.
		final MetaClass updatedMetaClass = testdb.getMetaClassByID(metaClassID);
		assertTrue(updatedMetaClass != null);
		assertTrue(updatedMetaClass.existsPropertyInClass(propertyID));

		// Check Property->MetaClass association.
		assertTrue(property.getMetaClassID().equals(metaClassID));

		// TYPE: Type must exist
		assertTrue(property.getType() != null);
		final Type propertyType = testdb.getTypeByID(property.getType().getId());
		assertTrue(propertyType != null);

		// GETTER: Getter must exist
		final Operation getter = testdb.getOperationByID(property.getGetterOperationID());
		final OperationID getterID = getter.getDataClayID();
		assertTrue(getter != null);
		assertTrue(getterID.equals(property.getGetterOperationID()));

		// SETTER: Getter must exist
		final Operation setter = testdb.getOperationByID(property.getSetterOperationID());
		final OperationID setterID = setter.getDataClayID();
		assertTrue(setter != null);
		assertTrue(setterID.equals(property.getSetterOperationID()));
	}

	/**
	 * @brief Verify the existence and relationship of an Operation
	 * @param operationID
	 *            ID of the Operation to verify
	 * @param metaClassID
	 *            ID of the MetaClass containing the Operation
	 */
	private void verifyOperation(final OperationID operationID,
			final MetaClassID metaClassID) throws Exception {
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
		if (!implementations.isEmpty()) {
			final Iterator<Implementation> iterator = implementations.iterator();
			while (iterator.hasNext()) {
				final Implementation actImpl = iterator.next();
				final Implementation impl = testdb.getImplementationByID(actImpl.getDataClayID());
				assertTrue(impl != null);
				assertTrue(impl.getDataClayID().equals(actImpl.getDataClayID()));
			}
		}
	}

	/**
	 * @brief Verify the existence and relationship of a MetaClass
	 * @param metaClassID
	 *            ID of the MetaClass
	 */
	private MetaClass verifyMetaClass(final MetaClassID metaClassID)
			throws Exception {

		final MetaClass metaclass = testdb.getMetaClassByID(metaClassID);

		assertTrue(metaclass != null);
		// Verify Class<->ID relationship
		assertTrue(metaclass.getDataClayID().equals(metaClassID));

		// Verify that all new Properties and its Type exist in the database and are properly associated
		final List<Property> properties = metaclass.getProperties();
		if (properties != null) {
			final Iterator<Property> iteratorProperty = properties.iterator();
			while (iteratorProperty.hasNext()) {
				// PROPERTIES
				verifyProperty(iteratorProperty.next().getDataClayID(), metaClassID);
			}
		}

		// Verify that all new Operations and its Properties and Types (including return type) exist
		final List<Operation> operations = metaclass.getOperations();
		if (!operations.isEmpty()) {
			for (final Operation op : operations) {
				verifyOperation(op.getDataClayID(), metaClassID);
			}
		}

		return metaclass;
	}

	/*************************** FUNCTIONAL TESTS ***************************/

	private void setIDs(final MetaClass metaClass) {

		final MetaClassID classID = new MetaClassID();
		metaClass.setDataClayID(classID);

		for (final Operation op : metaClass.getOperations()) {
			op.setMetaClassID(classID);
			op.setNamespaceID(NAMESPACEID);

			if (op.getReturnType() instanceof UserType) {
				final UserType uType = (UserType) op.getReturnType();
				uType.setClassID(classID);
				uType.setNamespace(NAMESPACENAME);
			}

			for (final Type paramType : op.getParams().values()) {
				if (paramType instanceof UserType) {
					final UserType uType = (UserType) paramType;
					uType.setClassID(classID);
					uType.setNamespace(NAMESPACENAME);
				}
			}

			for (final Implementation impl : op.getImplementations()) {
				impl.setMetaClassID(classID);
				impl.setNamespaceID(NAMESPACEID);
				impl.setResponsibleAccountID(new AccountID());

				for (final Type includeType : impl.getIncludes()) {
					if (includeType instanceof UserType) {
						final UserType uType = (UserType) includeType;
						uType.setClassID(classID);
						uType.setNamespace(NAMESPACENAME);
					}
				}
			}
		}

	}

	/**
	 * @brief Test method for ClassManager::newClass(NamespaceID, MetaClass)
	 * @author dgasull
	 * @pre The functions DbHandler::existsObjectByID(ID) and DbHandler::queryByExampleID(util.ids.ID) must be tested and
	 *      correct.
	 * @post Test the creation and storage of a new MetaClass using the a MetaClass ification: <br>
	 *       If the function throws an error the test fails. <br>
	 *       If after the creation and storage we query the database for the MetaClass and we obtain that it does not exist, the
	 *       test fails. We also verify that its Properties, Operations, Types and Implementations exist in the database. <br>
	 *       Otherwise the test is passed.
	 */
	@Test
	public void testNewClass() throws Exception {

		final MetaClass metaClassInfo = cman.newClass(NAMESPACERESP, NAMESPACEID, NAMESPACENAME,
				metaClass, Langs.LANG_JAVA);

		// Verify the Class
		verifyMetaClass(metaClassInfo.getDataClayID());

	}

	/**
	 * @brief Test method for ClassManager::checkNamespaceHasNoClasses(NamespaceID)
	 * @author dgasull
	 * @pre The function DbHandler::store(Object) and ClassManager::newClass(NamespaceID, MetaClass) must be tested and correct.
	 * @post Test the function that checks if a Namespace has classes (in this case the namespace has classes)
	 */
	@Test
	public void testcheckNamespaceHasNothing() throws Exception {

		final MetaClass metaClassInfo = cman.newClass(NAMESPACERESP, NAMESPACEID, NAMESPACENAME, metaClass,
				Langs.LANG_JAVA);
		verifyMetaClass(metaClassInfo.getDataClayID());

		// Now we test the function
		assertFalse(cman.checkNamespaceHasNothing(NAMESPACEID));
		assertTrue(cman.checkNamespaceHasNothing(new NamespaceID()));

	}

	/**
	 * @brief Test method for ClassManager::checkNamespaceHasNoClasses(NamespaceID)
	 * @author dgasull
	 * @pre The function DbHandler::store(Object) and ClassManager::newClass(NamespaceID, MetaClass) must be tested and correct.
	 * @post Test the function that checks if a Namespace has classes (in this case the namespace does not have classes).
	 */
	@Test
	public void testCheckNamespaceHasNoClassesNotExists() {

		assertTrue(cman.checkNamespaceHasNothing(new NamespaceID()));

	}

	/**
	 * @brief Test method for ClassManager::removeClass(TypeID)
	 * @author dgasull
	 * @pre The functions DbHandler::queryByExampleID(util.ids.ID), and DbHandler::existsObjectByID(util.ids.ID) and
	 *      ClassManager::newClass(NamespaceID, MetaClass) must be tested and correct.
	 * @post Test the remove function of a MetaClass. <br>
	 *       If the function throws an error the test fails. <br>
	 *       If after the remove process we query the database for the MetaClass and we obtain that it exists, the test fails
	 *       <br>
	 *       Otherwise the test is passed.
	 */
	@Test
	public void testRemoveClass() throws Exception {

		// First we create the Class.
		final MetaClass metaClassInfo = cman.newClass(NAMESPACERESP, NAMESPACEID, NAMESPACENAME, metaClass,
				Langs.LANG_JAVA);
		final MetaClass metaclass = verifyMetaClass(metaClassInfo.getDataClayID());

		// Save Properties, Types and Parents
		final List<Property> properties = metaclass.getProperties();
		final List<Operation> operations = metaclass.getOperations();
		MetaClassID parentID = null;
		if (metaclass.getParentType() != null) {
			parentID = metaclass.getParentType().getClassID();
		}

		// Now we call the function in order to remove the MetaClass, its Operations, Properties,
		// associated Types and Implementations
		cman.removeClass(metaClassInfo.getDataClayID());

		// Verify that the Class does not exist in the database.
		assertFalse(testdb.getMetaClassByID(metaClassInfo.getDataClayID()) != null);

		// VERIFY PROPERTIES
		if (properties != null && !properties.isEmpty()) {
			final Iterator<Property> itProperty = properties.iterator();
			while (itProperty.hasNext()) {

				final Property actualProp = itProperty.next();
				// Verify that the Property and its ID does not exist in the system
				assertFalse(testdb.getPropertyByID(actualProp.getDataClayID()) != null);

				// Verify that the Property Type does not exist
				assertFalse(testdb.getTypeByID(actualProp.getType().getId()) != null);

			}
		}

		// VERIFY OPERATIONS
		if (operations != null && !operations.isEmpty()) {
			final Iterator<Operation> itOperation = operations.iterator();
			while (itOperation.hasNext()) {
				final Operation operation = itOperation.next();
				// Verify that the Parameters do not exist.
				final Map<String, Type> params = operation.getParams();
				if (params != null && !params.isEmpty()) {
					for (final Type type : params.values()) {
						assertFalse(testdb.getTypeByID(
								type.getId()) != null);
					}
				}

				// Verify that the Implementations do not exist.
				final List<Implementation> implementations = operation.getImplementations();
				for (final Implementation implementation : implementations) {
					assertFalse(testdb.getImplementationByID(implementation.getDataClayID()) != null);

				}

				// Verify the Return Type does not exist.
				if (operation.getReturnType() != null) {
					assertFalse(testdb.getTypeByID(operation.getReturnType().getId()) != null);

				}
			}

		}

		// VERIFY PARENT
		if (parentID != null) {
			assertFalse(testdb.getTypeByID(metaclass.getParentType().getId()) != null);
		}

	}

	/**
	 * @brief Test method of ClassManager::getMetaClassID(NamespaceID, String)
	 * @pre The functions DbHandler::store(Object), ClassManager::newClass(NamespaceID, MetaClass) and
	 *      DbHandler::queryByExampleID(util.ids.ID) must be tested an correct.
	 * @post Test the function that gets the ID of a MetaClass given its name. <br>
	 *       If the function throws an error the test fails. <br>
	 *       If after the get process we verify that it is not the same ID, the test fails <br>
	 *       Otherwise the test is passed.
	 */
	@Test
	public void testGetMetaClassID() {

		final MetaClass metaClassInfo = cman.newClass(NAMESPACERESP, NAMESPACEID, NAMESPACENAME, metaClass,
				Langs.LANG_JAVA);

		final MetaClassID newMetaClassID = cman.getMetaClassID(NAMESPACEID, metaClass.getName());
		assertTrue(newMetaClassID.equals(metaClassInfo.getDataClayID()));

	}

	@Test
	public void testGetOperationsAndPropertiesNotInNamespaceOfClass() throws RemoteException {

		final MetaClass metaClassInfo = cman.newClass(NAMESPACERESP, NAMESPACEID, NAMESPACENAME, metaClass,
				Langs.LANG_JAVA);

		final MetaClass metaClass = testdb.getMetaClassByID(metaClassInfo.getDataClayID());
		final Type propType = new Type("I", "I", "int");
		final Property propertyNew = new Property(0, "newProperty",
				propType, NAMESPACENAME, metaClass.getName());
		final JavaPropertyInfo javaPropInfo = new JavaPropertyInfo(0);
		propertyNew.addLanguageDepInfo(javaPropInfo);
		propertyNew.setDataClayID(new PropertyID());
		propertyNew.setNamespaceID(new NamespaceID());
		propertyNew.setMetaClassID(metaClassInfo.getDataClayID());
		final Operation newGetter = new Operation("$$getNewProperty",
				"()I", "$$getNewProperty()I",
				"$$getNewProperty()I",
				NAMESPACENAME, metaClass.getName(),
				false);
		newGetter.setDataClayID(new OperationID());
		newGetter.setMetaClassID(complexTypeClassID);
		newGetter.setReturnType(new Type("I", "I", "int"));
		newGetter.setNamespaceID(NAMESPACEID);

		final Operation newSetter = new Operation("$$setNewProperty",
				"(I)V", "$$setNewProperty(I)V",
				"$$setNewProperty(I)V",
				NAMESPACENAME, metaClass.getName(),
				false);
		newSetter.setDataClayID(new OperationID());
		newSetter.setReturnType(new Type("V", "V", "void"));
		newSetter.setMetaClassID(complexTypeClassID);
		newSetter.setNamespaceID(NAMESPACEID);

		final Operation newUpdate = new Operation("$$setUpdate$$NewProperty",
				"(I)V", "$$setUpdate$$NewProperty(I)V",
				"$$setUpdate$$NewProperty(I)V",
				NAMESPACENAME, metaClass.getName(),
				false);
		newSetter.setDataClayID(new OperationID());
		newSetter.setReturnType(new Type("V", "V", "void"));
		newSetter.setMetaClassID(complexTypeClassID);
		newSetter.setNamespaceID(NAMESPACEID);

		newUpdate.setReturnType(new Type("V", "V", "void"));
		newUpdate.setDataClayID(new OperationID());
		newUpdate.setMetaClassID(complexTypeClassID);
		newUpdate.setNamespaceID(NAMESPACEID);

		propertyNew.setGetterImplementationID(new ImplementationID());
		propertyNew.setGetterOperationID(newGetter.getDataClayID());
		propertyNew.setSetterOperationID(newSetter.getDataClayID());
		propertyNew.setUpdateOperationID(newUpdate.getDataClayID());
		propertyNew.setSetterImplementationID(new ImplementationID());
		propertyNew.setUpdateImplementationID(new ImplementationID());

		metaClass.addPropertyAsEnrichment(propertyNew);
		testdb.updateClassPropertiesAndOperations(metaClassInfo.getDataClayID(),
				propertyNew,
				newSetter, newGetter, newUpdate);

		// Update cache manually since the enrichment has been forced
		cman.getClassCache().put(metaClass.getDataClayID(), metaClass);

		final HashSet<PropertyID> propertiesNotInClassNamespace = new HashSet<>();
		propertiesNotInClassNamespace.add(propertyNew.getDataClayID());

		final HashSet<OperationID> operationsNotInClass = new HashSet<>();

		final Tuple<Set<PropertyID>, Set<OperationID>> result = cman.getOperationsAndPropertiesNotInNamespaceOfClass(
				metaClassInfo.getDataClayID(), propertiesNotInClassNamespace, operationsNotInClass);

		assertTrue(result.getFirst().size() == 1);
		assertTrue(result.getFirst().iterator().next().equals(propertyNew.getDataClayID()));
		assertTrue(result.getSecond().size() == 0);

	}

	/*************************** EXCEPTION TEST ***************************/

	@Test(expected = PropertyNotInClassException.class)
	public void testGetOperationsAndPropertiesNotInNamespaceOfClassPropertyNotInClass() throws RemoteException {

		final MetaClass metaClassInfo = cman.newClass(NAMESPACERESP, NAMESPACEID, NAMESPACENAME, metaClass,
				Langs.LANG_JAVA);

		final HashSet<PropertyID> propertiesNotInClass = new HashSet<>();
		propertiesNotInClass.add(new PropertyID());

		final HashSet<OperationID> operationsNotInClass = new HashSet<>();

		cman.getOperationsAndPropertiesNotInNamespaceOfClass(metaClassInfo.getDataClayID(), propertiesNotInClass,
				operationsNotInClass);
	}

	@Test(expected = OperationNotInClassException.class)
	public void testGetOperationsAndPropertiesNotInNamespaceOfClassOperationNotInClass() throws RemoteException {

		final MetaClass metaClassInfo = cman.newClass(NAMESPACERESP, NAMESPACEID, NAMESPACENAME, metaClass,
				Langs.LANG_JAVA);

		final HashSet<PropertyID> propertiesNotInClass = new HashSet<>();
		final HashSet<OperationID> operationsNotInClass = new HashSet<>();
		operationsNotInClass.add(new OperationID());
		cman.getOperationsAndPropertiesNotInNamespaceOfClass(metaClassInfo.getDataClayID(), propertiesNotInClass,
				operationsNotInClass);
	}
}
