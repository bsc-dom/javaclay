
/**
 * @file ClassManagerPropertyTest.java
 * @date Sep 18, 2012
 * @author dgasull
 */
package es.bsc.dataclay.logic.classmgr;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Modifier;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.exceptions.logicmodule.classmgr.PropertyAlreadyInClassException;
import es.bsc.dataclay.exceptions.logicmodule.classmgr.PropertyNotExistException;
import es.bsc.dataclay.logic.classmgr.ClassManager;
import es.bsc.dataclay.logic.classmgr.ClassManagerDB;
import es.bsc.dataclay.test.AbstractManagerTest;
import es.bsc.dataclay.test.TestUtils;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.management.classmgr.LanguageDependantClassInfo;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.classmgr.Operation;
import es.bsc.dataclay.util.management.classmgr.Property;
import es.bsc.dataclay.util.management.classmgr.Type;
import es.bsc.dataclay.util.management.classmgr.UserType;
import es.bsc.dataclay.util.management.classmgr.java.JavaClassInfo;
import es.bsc.dataclay.util.management.classmgr.java.JavaPropertyInfo;
import es.bsc.dataclay.util.yaml.CommonYAML;

/**
 * @author dgasull
 * @class ClassManagerPropertyTest
 * @brief This class tests the ClassManager focusing in Property functions.
 * @note This is a parameterized test.
 */
@RunWith(Parameterized.class)
public final class ClassManagerPropertyTest extends AbstractManagerTest{

	private ClassManager cman;
	// !<@brief Class Manager instance tested.
	private static String DBFILESDIRNAME = System.getProperty("user.dir") + "/dbfiles/ClassManagerPropertyTest";
	// !<@brief Path to the test databases.

	private static final String NAMESPACENAME = "testNamespace"; // !<@brief Namespace name used in tests
	private static final String CLASSNAME = "testClass"; // !<@brief Namespace name used in tests

	private static final NamespaceID NAMESPACEID = new NamespaceID(); // !<@brief NamespaceID used in tests
	private Property property; // !<@brief Property used in tests
	private final Property globalProperty; // !<@brief Property used in tests
	private ClassManagerDB testdb; // !<@brief DbHandler used in tests.
	private static final AccountID NAMESPACERESP = new AccountID(); // !<@brief AccountID used in tests
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

		final String yamlStr = CommonYAML.getYamlObject().dump(globalProperty);
		this.property = (Property) CommonYAML.getYamlObject().load(yamlStr);
	}

	/**
	 * @brief This method is executed after each test. It is used to deleteByID the test database since we are testing the creation
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
	}

	@AfterClass
	public static void afterAll() throws Exception {
		TestUtils.deleteDirectory(DBFILESDIRNAME);
	}

	/**
	 * @brief Creates a new ClassManagerPropertyTest
	 * @author dgasull
	 * @param propertyCase
	 *            Property specification of this set of tests
	 * @note This function is necessary for the Parameterized Test.
	 */
	public ClassManagerPropertyTest(final Property propertyCase) {
		this.globalProperty = propertyCase;
	}

	/*************************** PARAMETERS ***************************/

	/**
	 * @brief This method is executed before all tests. <br>
	 *        It is used to prepare all the possible Parameters of this Parameterized Test.<br>
	 *        Each array of Objects obtained in the collection represents the Parameters of the Constructor function. So for each
	 *        array in the Collection all tests in this class are run. In our case we prepare all possible Property ifications: <br>
	 *        <br>
	 *        PROPERTY SPECIFICATION TESTED CASES: One ification can have different options, we must combine them in order to do a
	 *        Complete Test. The structure of the combinational table and an example are:<br>
	 *        NOTE that in this case we are using Combinational tables for scalability. <br>
	 *        <table>
	 *        <tr>
	 *        <td>+AREPRIMITIVE PROPERTIES</td>
	 *        <td>0 (0 MEANS METACLASS PROPERTY)</td>
	 *        </tr>
	 *        </table>
	 *        <br>
	 *        The total number of combinations is: <br>
	 *        1 case with primitive properties <br>
	 *        1 case with MetaClass properties <br>
	 *        TOTAL: 2 combinations. <br>
	 *        <br>
	 * 
	 * @return Array of Object List. Each element of the array is an array of objects. In our case, this array of objects is only
	 *         one object: the MetaClass ification case. It is used for our Parameterized test.
	 * @author dgasull
	 * @throws RemoteException
	 */
	@Parameters
	public static Collection<Object[]> prepareParameters() throws RemoteException {

		// First we prepare the Collection to return. o
		final ArrayList<Object[]> propertys = new ArrayList<>();

		// Since we must provide an array of objects for each element in the Collection
		// we prepare it with only one element because the constructor function of this class
		// has only one parameter.

		// Now we iterate all the possible combinations

		/*
		 * PRIMITIVE PROPERTIES
		 */

		// if it is primitive, we just create a Primitive Type with an Unique Name (we know that we cannot have
		// two properties with same name)

		final String[] primitives = { "Ljava/lang/String;", "Z", "B", "C", "D", "F", "I", "J", "S" };
		int i = 0;
		for (i = 0; i < primitives.length; ++i) {
			// We create the Primitive Type.
			final Type primitiveTypeN = new Type(primitives[i], primitives[i], primitives[i], null);
			// We create a Property with this Type.
			final Object[] testPrimitive = new Object[1];
			final Property primProp = new Property(i,
					"testPrimitiveProperty", primitiveTypeN, NAMESPACENAME, CLASSNAME);
			final JavaPropertyInfo javaPropInfo = new JavaPropertyInfo(Modifier.PUBLIC);
			primProp.addLanguageDepInfo(javaPropInfo);
			testPrimitive[0] = primProp;
			// We add it to the result list of Properties.
			propertys.add(testPrimitive);
		}

		/*
		 * METACLASS PROPERTIES
		 */

		// We create the MetaClass Type. l
		final Type metaClassTypeN = new UserType(NAMESPACENAME, "testMetaClassType", "LtestMetaClassType;",
				"LtestMetaClassType;", null);
		// We create a Property with this Type.
		final Object[] testParam2 = new Object[1];

		final Property prop = new Property(i + 1,
				"testMetaClassProperty", metaClassTypeN,
				NAMESPACENAME, CLASSNAME);
		final JavaPropertyInfo javaPropInfo = new JavaPropertyInfo(Modifier.PUBLIC);
		prop.addLanguageDepInfo(javaPropInfo);
		testParam2[0] = prop;
		// We add it to the result list of Properties.
		propertys.add(testParam2);

		return propertys;
	}

	/**
	 * @brief Create a new empty metaClass
	 * @return The ID of the created Metaclass
	 * @note This function is not always necessary so that's why is not in a before function.
	 */

	private MetaClass createMetaClass() {

		// We will use an empty MetaClass
		final MetaClass newMetaClass = new MetaClass();
		newMetaClass.setName(CLASSNAME);
		newMetaClass.setNamespaceID(NAMESPACEID);
		newMetaClass.setProperties(new TreeSet<Property>());
		newMetaClass.setOperations(new HashSet<Operation>());

		final JavaClassInfo javaClassInfo = new JavaClassInfo();
		newMetaClass.setLanguageDepInfos(new HashMap<Langs, LanguageDependantClassInfo>());
		newMetaClass.addLanguageDepInfo(javaClassInfo);
		final MetaClassID metaClassID = new MetaClassID();
		newMetaClass.setDataClayID(metaClassID);
		newMetaClass.setNamespace(NAMESPACENAME);

		// First we add a new Class
		testdb.storeMetaClass(newMetaClass);

		// If the type of the property is user type
		// First add the type of the property if user type
		final MetaClass typeMetaclass = new MetaClass(NAMESPACENAME,
				"testMetaClassType", null, false);

		final JavaClassInfo typeClassInfo = new JavaClassInfo("LtestMetaClassType;", null);
		typeMetaclass.addLanguageDepInfo(typeClassInfo);
		typeMetaclass.setNamespaceID(NAMESPACEID);
		typeMetaclass.setDataClayID(TESTMETACLASSTYPEID);
		testdb.storeMetaClass(typeMetaclass);

		// Check the MetaClass exists in the database.
		final MetaClass updatedMetaclass = testdb.getMetaClassByID(metaClassID);
		assertTrue(updatedMetaclass != null);
		final MetaClassID updatedMetaClassID = updatedMetaclass.getDataClayID();
		assertTrue(updatedMetaClassID.equals(metaClassID));

		// Return the result of the query if we want to update using db4o
		return updatedMetaclass;
	}

	/**
	 * @brief Creates a new Property in the MetaClass identified by the ID provided and verify the relationships and references.
	 * @param metaClassID
	 *            ID of the MetaClass containing the Property
	 */
	private Property newProperty(final MetaClassID metaClassID) throws Exception {

		property.setDataClayID(new PropertyID());
		if (property.getType() instanceof UserType) {
			final UserType uType = (UserType) property.getType();
			if (uType.getTypeName().equals(CLASSNAME)) {
				uType.setClassID(metaClassID);
			} else if (uType.getTypeName().equals("testMetaClassType")) {
				uType.setClassID(TESTMETACLASSTYPEID);
			}
		}
		if (property.getType().getIncludes() != null) {
			for (final Type subInclude : property.getType().getIncludes()) {
				if (subInclude instanceof UserType) {
					final UserType uType = (UserType) subInclude;
					if (uType.getTypeName().equals(CLASSNAME)) {
						uType.setClassID(metaClassID);
					} else if (uType.getTypeName().equals("testMetaClassType")) {
						uType.setClassID(TESTMETACLASSTYPEID);
					}
				}
			}
		}
		property.setMetaClassID(metaClassID);

		final Property propertyInfo = cman.newProperty(NAMESPACERESP, NAMESPACEID, metaClassID, property);
		final PropertyID propertyID = propertyInfo.getDataClayID();

		// Verify that the Property exists in the database.
		final Property property = testdb.getPropertyByID(propertyID);
		assertTrue(property != null);
		final PropertyID queriedPropertyID = property.getDataClayID();

		// Verify Property<->ID relationship
		assertTrue(queriedPropertyID.equals(propertyID));

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
		assertTrue(getter != null);
		final OperationID getterID = getter.getDataClayID();
		assertTrue(getterID.equals(property.getGetterOperationID()));

		// SETTER: Setter must exist
		final Operation setter = testdb.getOperationByID(property.getSetterOperationID());
		assertTrue(setter != null);
		final OperationID setterID = setter.getDataClayID();
		assertTrue(setterID.equals(property.getSetterOperationID()));

		return property;
	}

	/*************************** FUNCTIONAL TESTS ***************************/

	/**
	 * @brief Test method for ClassManager::newProperty(TypeID, Property)
	 * @author dgasull
	 * @pre The functions DbHandler::existsObjectByID(util.ids.ID), DbHandler::queryByExampleID(util.ids.ID) must be tested and
	 *      correct.
	 * @post Test the creation and storage of a new Property. <br>
	 *       If the function throws an error the test fails. <br>
	 *       If after the creation and storage we query the database for the Property and we obtain that it does not exists, the
	 *       test fails <br>
	 *       Otherwise the test is passed.
	 */
	@Test
	public void testNewProperty() throws Exception {

		final MetaClass metaClass = createMetaClass();
		final MetaClassID metaClassID = metaClass.getDataClayID();

		// CALL FUNCTION TO TEST
		newProperty(metaClassID);

	}

	/**
	 * @brief Test method of ClassManager::getPropertyID(NamespaceID, String, String)
	 * @pre The functions DbHandler::store(Object), ClassManager::newProperty(TypeID, Property) and
	 *      DbHandler::queryByExampleID(util.ids.ID) must be tested an correct.
	 * @post Test the function that gets the ID of a Property given its name and metaclass name. <br>
	 *       If the function throws an error the test fails. <br>
	 *       If after the get process we verify that it is not the same ID, the test fails <br>
	 *       Otherwise the test is passed.
	 */
	@Test
	public void testGetPropertyID() throws Exception {

		final MetaClass metaClass = createMetaClass();
		final MetaClassID metaClassID = metaClass.getDataClayID();

		// We add now the new Property by calling the newProperty function
		// It is important to use this function since our test is parameterized
		final Property property = newProperty(metaClassID);
		final PropertyID propertyID = property.getDataClayID();

		final PropertyID newPropertyID = cman.getPropertyID(metaClass.getDataClayID(), property.getName());
		assertTrue(newPropertyID.equals(propertyID));

	}

	/*************************** EXCEPTION TEST ***************************/

	/**
	 * @brief Test method for ClassManager::newProperty(TypeID, Property)
	 * @author dgasull
	 * @post Test that if the Class in which to add the Property already has the Property an error is thrown.
	 */
	@Test(expected = PropertyAlreadyInClassException.class)
	public void testNewPropertyAlreadyInClassException() throws Exception {

		final MetaClass metaClass = createMetaClass();
		final MetaClassID metaClassID = metaClass.getDataClayID();

		// We add now the new Property by calling the newProperty function
		// It is important to use this function since our test is parameterized
		newProperty(metaClassID);

		// since the property was already added it must provide an exception
		property.setDataClayID(new PropertyID());

		if (property.getType() instanceof UserType) {
			final UserType uType = (UserType) property.getType();
			if (uType.getTypeName().equals(CLASSNAME)) {
				uType.setClassID(metaClassID);
			}
		}
		if (property.getType().getIncludes() != null) {
			for (final Type subInclude : property.getType().getIncludes()) {
				if (subInclude instanceof UserType) {
					final UserType uType = (UserType) subInclude;
					if (uType.getTypeName().equals(CLASSNAME)) {
						uType.setClassID(metaClassID);
					}
				}
			}
		}

		property.setMetaClassID(metaClassID);
		cman.newProperty(NAMESPACERESP, NAMESPACEID, metaClassID, property);

	}

	/**
	 * @brief Test method of ClassManager::getPropertyID(NamespaceID, String, String)
	 * @post Test that if the Property does not exist an error is thrown.
	 */
	@Test(expected = PropertyNotExistException.class)
	public void testGetPropertyIDNotExistException() {

		final MetaClass metaClass = createMetaClass();
		// Call the function with a name of a property that does not exist
		cman.getPropertyID(metaClass.getDataClayID(), "unexistantProperty");

	}

}
