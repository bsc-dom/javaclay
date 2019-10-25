
/**
 * @file InterfaceManagerTest.java
 * @date Sep 27, 2012
 * @author jmarti
 */

package es.bsc.dataclay.logic.interfacemgr;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import es.bsc.dataclay.exceptions.logicmodule.interfacemgr.InterfaceNotExistsException;
import es.bsc.dataclay.logic.interfacemgr.InterfaceManager;
import es.bsc.dataclay.logic.interfacemgr.InterfaceManagerDB;
import es.bsc.dataclay.test.AbstractManagerTest;
import es.bsc.dataclay.test.TestUtils;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.management.interfacemgr.Interface;

/**
 * @class InterfaceManagerTest
 * @brief This class tests the functions of Interface Manager
 * @author jmarti
 */
public final class InterfaceManagerTest extends AbstractManagerTest{

	private InterfaceManager ifaceMgr;
	private static String DBFILESDIRNAME = System.getProperty("user.dir") + "/dbfiles/InterfaceManagerTest";
	// !<@brief Path to the test databases.
	private static final int RANDMAX = 10;

	private static String providerAccountName = "Provider";
	private static AccountID providerAccount = new AccountID();
	private static String namespace = "Namespace";
	private static NamespaceID namespaceID = new NamespaceID();
	private static MetaClassID metaClassID;
	private static HashSet<String> ops;
	private static HashSet<String> props;
	private static HashSet<OperationID> opsIDs;
	private static HashSet<PropertyID> propsIDs;
	private InterfaceManagerDB testdb; // !<@brief DbHandler used in tests.

	/**
	 * @brief This method is executed before all tests. It is used to instantiate variables that are used in all the tests in order
	 *        to save code.
	 * @author jmarti
	 */
	@BeforeClass
	public static void beforeAll() {
		TestUtils.createOrCleanDirectory(DBFILESDIRNAME);
		metaClassID = new MetaClassID();
		opsIDs = new HashSet<>();
		propsIDs = new HashSet<>();
		ops = new HashSet<>();
		props = new HashSet<>();
	}

	/**
	 * @brief This method is executed before each test. It is used instantiate the Interface Manager to test.
	 * @author jmarti
	 * @throws RemoteException
	 */
	@Override
	@Before
	public void before() {
		super.before();

		final InterfaceManagerDB adb = new InterfaceManagerDB(dataSource);
		adb.dropTables();

		ifaceMgr = new InterfaceManager(dataSource);

		testdb = ifaceMgr.getDbHandler();
	}

	/**
	 * @brief This method is executed after each test. It is used to delete the test database since we are testing the creation of
	 *        Interface on it, it is necessary to empty the database before.
	 * @author jmarti
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
	 * @brief Test method for InterfaceManager::newInterface(NamespaceID, TypeID, ArrayList, ArrayList).
	 * @author jmarti
	 * @post The interface can be retrieved after registering it in the DB
	 */
	@Test
	public void testNewInterface() {
		final Interface iface = new Interface(providerAccountName, namespace, namespace, "testclass", props, ops);
		iface.setProviderAccountID(providerAccount);
		iface.setMetaClassID(metaClassID);
		iface.setNamespaceID(namespaceID);
		iface.setPropertiesIDs(propsIDs);
		iface.setOperationsIDs(opsIDs);
		iface.setClassNamespaceID(namespaceID);
		iface.setDataClayID(new InterfaceID());
		final InterfaceID newID = ifaceMgr.newInterface(iface);

		assertTrue(testdb.getInterfaceByID(newID) != null);
	}

	/**
	 * @brief Test method for InterfaceManager::removeInterface(InterfaceID).
	 * @author jmarti
	 * @post The interface is removed from the db.
	 */
	@Test
	public void testRemoveInterface() {
		// Store it with dbhandler
		final Interface iface = new Interface(providerAccountName, namespace, namespace, "testclass", props, ops);
		iface.setProviderAccountID(providerAccount);
		iface.setMetaClassID(metaClassID);
		iface.setNamespaceID(namespaceID);
		iface.setPropertiesIDs(propsIDs);
		iface.setOperationsIDs(opsIDs);
		iface.setClassNamespaceID(namespaceID);
		iface.setDataClayID(new InterfaceID());
		testdb.store(iface);

		// Check object exists by its ID
		assertTrue(testdb.getInterfaceByID(iface.getDataClayID()) != null);

		// Use manager
		ifaceMgr.removeInterface(namespaceID, iface.getDataClayID());

		// Check
		final boolean found = testdb.getInterfaceByID(iface.getDataClayID()) != null;
		assertFalse(found);
	}

	/**
	 * @brief Test method for InterfaceManager::removeInterface(InterfaceID).
	 * @author jmarti
	 * @post One of the interfaces is removed from the db but the other interface is kept with all its operations and properties
	 *       (although sharing some of them with the removed one).
	 */
	@Test
	public void testRemoveInterface2Interfaces() {
		// Store it with dbhandler
		final PropertyID propID = new PropertyID(UUID.randomUUID());
		final HashSet<PropertyID> propertiesIDs = new HashSet<>();
		propertiesIDs.add(propID);
		final HashSet<String> props2 = new HashSet<>();
		props2.add("prop");

		final Interface testInterface = new Interface(providerAccountName, namespace, namespace, "testclass", props, ops);
		testInterface.setProviderAccountID(providerAccount);
		testInterface.setMetaClassID(metaClassID);
		testInterface.setNamespaceID(namespaceID);
		testInterface.setPropertiesIDs(propsIDs);
		testInterface.setOperationsIDs(opsIDs);
		testInterface.setDataClayID(new InterfaceID());
		testInterface.setClassNamespaceID(namespaceID);

		final Interface testInterface2 = new Interface(providerAccountName, namespace, namespace, "testclass", props2, ops);
		testInterface2.setProviderAccountID(providerAccount);
		testInterface2.setMetaClassID(metaClassID);
		testInterface2.setNamespaceID(namespaceID);
		testInterface2.setPropertiesIDs(propertiesIDs);
		testInterface2.setOperationsIDs(opsIDs);
		testInterface2.setDataClayID(new InterfaceID());
		testInterface2.setClassNamespaceID(namespaceID);

		testdb.store(testInterface);
		testdb.store(testInterface2);

		// Check object exists by its ID
		assertTrue(testdb.existsObjectByID(testInterface.getDataClayID()));

		// Use manager
		ifaceMgr.removeInterface(namespaceID, testInterface.getDataClayID());

		// Check
		assertFalse(testdb.existsObjectByID(testInterface.getDataClayID()));

		// Check the second one is kept intact
		assertTrue(testdb.existsObjectByID(testInterface2.getDataClayID()));
		final Interface iface = testdb.getInterfaceByID(testInterface2.getDataClayID());
		assertTrue(iface.getPropertiesIDs().iterator().next().equals(propID));

		// Clean
		propsIDs.remove(propID);
	}

	/**
	 * @brief Test method for InterfaceManager::getInterfacesOfClass(NamespaceID, TypeID)
	 * @author jmarti
	 * @post After manually inserting an interface we can check there is one (itself) related with the metaclass
	 */
	@Test
	public void testGetInterfacesOfClass1() {
		// Store 1 interface into the db
		final Interface testInterface = new Interface(providerAccountName, namespace, namespace, "testclass", props, ops);
		testInterface.setProviderAccountID(providerAccount);
		testInterface.setMetaClassID(metaClassID);
		testInterface.setNamespaceID(namespaceID);
		testInterface.setPropertiesIDs(propsIDs);
		testInterface.setOperationsIDs(opsIDs);
		testInterface.setDataClayID(new InterfaceID());
		testInterface.setClassNamespaceID(namespaceID);

		testdb.store(testInterface);

		// Check it exists
		assertTrue(testdb.existsObjectByID(testInterface.getDataClayID()));

		// Use manager
		final HashSet<InterfaceID> interfaces = ifaceMgr.getInterfacesOfClass(namespaceID, metaClassID);
		assertTrue(interfaces.size() == 1 && interfaces.contains(testInterface.getDataClayID()));
	}

	/**
	 * @brief Test method for InterfaceManager::getInterfacesOfClass(NamespaceID, TypeID).
	 * @author jmarti
	 * @post After manually inserting N interfaces we can check that N (themselves) are related with the metaclass
	 */
	@Test
	public void testGetInterfacesOfClassN() {
		final Random rand = new Random();
		final int totalInterfaces = rand.nextInt(RANDMAX) + 1; // Avoid 0

		// Store N interfaces into the db
		for (int i = 0; i < totalInterfaces; i++) {
			final Interface testInterface = new Interface(providerAccountName, namespace, namespace, "testclass", props, ops);
			testInterface.setProviderAccountID(providerAccount);
			testInterface.setMetaClassID(metaClassID);
			testInterface.setNamespaceID(namespaceID);
			testInterface.setPropertiesIDs(propsIDs);
			testInterface.setOperationsIDs(opsIDs);
			testInterface.setDataClayID(new InterfaceID());
			testInterface.setClassNamespaceID(namespaceID);

			testdb.store(testInterface);

			// Check it exists
			assertTrue(testdb.existsObjectByID(testInterface.getDataClayID()));
		}
		// Check them by metaclass
		final HashSet<InterfaceID> interfaces = ifaceMgr.getInterfacesOfClass(namespaceID, metaClassID);
		assertTrue(interfaces.size() == totalInterfaces);
	}

	/**
	 * @brief Test method for InterfaceManager::getInterfacesOfClass(NamespaceID, TypeID)
	 * @author jmarti
	 * @post Get an exception when trying to get interfaces of specific metaclass for which there is no interface at all
	 */
	@Test
	public void testGetInterfacesOfClass0() {
		// No previous stores imply no interfaces
		final HashSet<InterfaceID> interfaces = ifaceMgr.getInterfacesOfClass(namespaceID, metaClassID);
		assertTrue(interfaces.size() == 0);
	}

	/**
	 * @brief Test method for InterfaceManager::getInterfacesAccessingOperation(NamespaceID, TypeID, OperationID)
	 * @author jmarti
	 * @post After manually inserting an interface with a specific operation, check we can retrieve it
	 */
	@Test
	public void testGetInterfacesAccessingOperation1() {
		// Create a specific operation ID
		final OperationID opId = new OperationID();
		final HashSet<OperationID> operationsIDs = new HashSet<>();
		operationsIDs.add(opId);
		final HashSet<String> newops = new HashSet<>();
		newops.add("op");

		// Store 1 interface into the db accessing such operation
		final Interface testInterface = new Interface(providerAccountName, namespace, namespace, "testclass", props, newops);
		testInterface.setProviderAccountID(providerAccount);
		testInterface.setMetaClassID(metaClassID);
		testInterface.setNamespaceID(namespaceID);
		testInterface.setPropertiesIDs(propsIDs);
		testInterface.setOperationsIDs(operationsIDs);
		testInterface.setDataClayID(new InterfaceID());
		testInterface.setClassNamespaceID(namespaceID);

		testdb.store(testInterface);

		// Check it exists
		assertTrue(testdb.existsObjectByID(testInterface.getDataClayID()));

		// Use manager
		final HashSet<InterfaceID> interfaces = ifaceMgr.getInterfacesAccessingOperation(namespaceID, metaClassID, opId);
		assertTrue(interfaces.size() == 1 && interfaces.contains(testInterface.getDataClayID()));
	}

	/**
	 * @brief Test method for InterfaceManager::getInterfacesAccessingOperation(NamespaceID, TypeID, OperationID)
	 * @author jmarti
	 * @post After inserting N interfaces with a specific operation, check we can retrieve them all given such op
	 */
	@Test
	public void testGetInterfacesAccessingOperationN() {
		final Random rand = new Random();
		final int totalInterfaces = rand.nextInt(RANDMAX) + 1; // Avoid 0

		// Create a specific operation
		final OperationID opId = new OperationID();
		final HashSet<OperationID> operationsIDs = new HashSet<>();
		operationsIDs.add(opId);
		final HashSet<String> newops = new HashSet<>();
		newops.add("op");

		// Store N interfaces into the db accessing such operation
		for (int i = 0; i < totalInterfaces; i++) {
			final Interface testInterface = new Interface(providerAccountName, namespace, namespace, "testclass", props, newops);
			testInterface.setProviderAccountID(providerAccount);
			testInterface.setMetaClassID(metaClassID);
			testInterface.setNamespaceID(namespaceID);
			testInterface.setPropertiesIDs(propsIDs);
			testInterface.setOperationsIDs(operationsIDs);
			testInterface.setDataClayID(new InterfaceID());
			testInterface.setClassNamespaceID(namespaceID);

			testdb.store(testInterface);

			// Check it exists
			assertTrue(testdb.existsObjectByID(testInterface.getDataClayID()));
		}

		// Check it by operation
		final HashSet<InterfaceID> interfaces = ifaceMgr.getInterfacesAccessingOperation(namespaceID, metaClassID, opId);
		assertTrue(interfaces.size() == totalInterfaces);
	}

	/**
	 * @brief Test method for InterfaceManager::getInterfacesAccessingOperation(NamespaceID, TypeID, OperationID)
	 * @author jmarti
	 * @post Get an exception when trying to retrieve an interface by its op, but there is no interface at all.
	 */
	@Test
	public void testGetInterfacesAccessingOperation0() {
		// Check it by operation
		final HashSet<InterfaceID> interfaces = ifaceMgr.getInterfacesAccessingOperation(namespaceID, metaClassID, new OperationID());
		assertTrue(interfaces.size() == 0);
	}

	/**
	 * @brief Test method for {@link InterfaceManager#getSubsetInterfacesOfClasses(HashSet, HashSet)}
	 * @author jmarti
	 * @post After manually inserting N interfaces we can check that N (themselves) are related with the metaclasses
	 */
	@Test
	public void testGetSubsetInterfacesOfClasses() {
		// Store 2 interfaces into the db

		final Interface testInterface = new Interface(providerAccountName, namespace, namespace, "testclass", props, ops);
		testInterface.setProviderAccountID(providerAccount);
		testInterface.setMetaClassID(metaClassID);
		testInterface.setNamespaceID(namespaceID);
		testInterface.setPropertiesIDs(propsIDs);
		testInterface.setOperationsIDs(opsIDs);
		testInterface.setDataClayID(new InterfaceID());
		testInterface.setClassNamespaceID(namespaceID);

		testdb.store(testInterface);
		final MetaClassID metaClassID2 = new MetaClassID();

		final Interface testInterface2 = new Interface(providerAccountName, namespace, namespace, "testclass2", props, ops);
		testInterface2.setProviderAccountID(providerAccount);
		testInterface2.setMetaClassID(metaClassID2);
		testInterface2.setNamespaceID(namespaceID);
		testInterface2.setPropertiesIDs(propsIDs);
		testInterface2.setOperationsIDs(opsIDs);
		testInterface2.setDataClayID(new InterfaceID());
		testInterface2.setClassNamespaceID(namespaceID);

		testdb.store(testInterface2);

		// Check it exists
		assertTrue(testdb.existsObjectByID(testInterface.getDataClayID()));

		// Use manager
		final HashSet<InterfaceID> interfacesIDs = new HashSet<>();
		interfacesIDs.add(testInterface.getDataClayID());
		interfacesIDs.add(testInterface2.getDataClayID());
		final HashSet<MetaClassID> metaClassesIDs = new HashSet<>();
		metaClassesIDs.add(metaClassID);
		metaClassesIDs.add(metaClassID2);

		Set<InterfaceID> interfaces = ifaceMgr.getSubsetInterfacesOfClasses(interfacesIDs, metaClassesIDs);
		assertTrue(interfaces.size() == 2 && interfaces.containsAll(interfacesIDs));

		metaClassesIDs.remove(metaClassID);
		interfaces = ifaceMgr.getSubsetInterfacesOfClasses(interfacesIDs, metaClassesIDs);
		assertTrue(interfaces.size() == 1 && interfaces.contains(testInterface2.getDataClayID()));
	}

	/**
	 * @brief Test method for InterfaceManager::getMetaClassOfInterface(InterfaceID)
	 * @author jmarti
	 * @post After inserting an interface, check we can retrieve its metaclass
	 */
	@Test
	public void testGetMetaClassOfInterface() {
		// Store 1 interface into the db
		final Interface testInterface = new Interface(providerAccountName, namespace, namespace, "testclass", props, ops);
		testInterface.setProviderAccountID(providerAccount);
		testInterface.setMetaClassID(metaClassID);
		testInterface.setNamespaceID(namespaceID);
		testInterface.setPropertiesIDs(propsIDs);
		testInterface.setOperationsIDs(opsIDs);
		testInterface.setClassNamespaceID(namespaceID);
		testInterface.setDataClayID(new InterfaceID());
		testdb.store(testInterface);

		// Check it exists
		assertTrue(testdb.existsObjectByID(testInterface.getDataClayID()));

		// Check obtained MetaClass
		final MetaClassID curTypeID = ifaceMgr.getMetaClassOfInterface(testInterface.getDataClayID());
		assertTrue(curTypeID.equals(metaClassID));
	}

	/**
	 * @brief Test method for {@link InterfaceManager#getInterfacesOfNamespaceInfo(NamespaceID, HashSet)
	 * @author jmarti
	 * @post After inserting two interfaces, check we can retrieve their info
	 */
	@Test
	public void testGetInterfacesOfNamespaceInfo() {
		// Create a property id
		final PropertyID propId = new PropertyID();
		final HashSet<PropertyID> propertiesIDs = new HashSet<>();
		propertiesIDs.add(propId);

		final HashSet<String> newprops = new HashSet<>();
		newprops.add("prop");

		// Create an operation id
		final OperationID opId = new OperationID();
		final HashSet<OperationID> operationsIDs = new HashSet<>();
		operationsIDs.add(opId);
		final HashSet<String> newops = new HashSet<>();
		newops.add("op");

		// Store 1 interface into the db with such an property
		final Interface testInterface = new Interface(providerAccountName, namespace, namespace, "testclass", newprops, newops);
		testInterface.setProviderAccountID(providerAccount);
		testInterface.setMetaClassID(metaClassID);
		testInterface.setNamespaceID(namespaceID);
		testInterface.setPropertiesIDs(propertiesIDs);
		testInterface.setOperationsIDs(operationsIDs);
		testInterface.setClassNamespaceID(namespaceID);
		testInterface.setDataClayID(new InterfaceID());
		testdb.store(testInterface);

		final Interface testInterface2 = new Interface(providerAccountName, namespace, namespace, "testclass", newprops, newops);
		testInterface2.setProviderAccountID(providerAccount);
		testInterface2.setMetaClassID(metaClassID);
		testInterface2.setNamespaceID(namespaceID);
		testInterface2.setPropertiesIDs(propertiesIDs);
		testInterface2.setOperationsIDs(operationsIDs);
		testInterface2.setDataClayID(new InterfaceID());
		testInterface2.setClassNamespaceID(namespaceID);

		testdb.store(testInterface2);

		// Check it exists
		assertTrue(testdb.existsObjectByID(testInterface.getDataClayID()));

		// Check obtained properties
		final HashSet<InterfaceID> interfacesIDs = new HashSet<>();
		interfacesIDs.add(testInterface.getDataClayID());
		interfacesIDs.add(testInterface2.getDataClayID());
		final Map<InterfaceID, Interface> curInterfaces = ifaceMgr.getInterfacesOfNamespaceInfo(namespaceID, interfacesIDs);
		assertTrue(curInterfaces.get(testInterface.getDataClayID()).getNamespaceID().equals(namespaceID)
				&& curInterfaces.get(testInterface.getDataClayID()).getMetaClassID().equals(metaClassID)
				&& curInterfaces.get(testInterface.getDataClayID()).getOperationsIDs().containsAll(operationsIDs)
				&& curInterfaces.get(testInterface.getDataClayID()).getPropertiesIDs().containsAll(propertiesIDs));
		assertTrue(curInterfaces.get(testInterface2.getDataClayID()).getNamespaceID().equals(namespaceID)
				&& curInterfaces.get(testInterface2.getDataClayID()).getMetaClassID().equals(metaClassID)
				&& curInterfaces.get(testInterface2.getDataClayID()).getOperationsIDs().containsAll(operationsIDs)
				&& curInterfaces.get(testInterface2.getDataClayID()).getPropertiesIDs().containsAll(propertiesIDs));
	}

	/**
	 * @brief Test method for {@link InterfaceManager#getInterfacesOfNamespaceInfo(NamespaceID, HashSet)
	 * @author jmarti
	 * @post After inserting two interfaces, check we can retrieve their info
	 */
	@Test
	public void testGetInterfacesInfo() {
		// Create a property id
		final PropertyID propId = new PropertyID();
		final HashSet<PropertyID> propertiesIDs = new HashSet<>();
		propertiesIDs.add(propId);

		final HashSet<String> newprops = new HashSet<>();
		newprops.add("prop");

		// Create an operation id
		final OperationID opId = new OperationID();
		final HashSet<OperationID> operationsIDs = new HashSet<>();
		operationsIDs.add(opId);
		final HashSet<String> newops = new HashSet<>();
		newops.add("op");

		// Store 1 interface into the db with such an property
		final Interface testInterface = new Interface(providerAccountName, namespace, namespace, "testclass", newprops, newops);
		testInterface.setProviderAccountID(providerAccount);
		testInterface.setMetaClassID(metaClassID);
		testInterface.setNamespaceID(namespaceID);
		testInterface.setPropertiesIDs(propertiesIDs);
		testInterface.setOperationsIDs(operationsIDs);
		testInterface.setDataClayID(new InterfaceID());
		testInterface.setClassNamespaceID(namespaceID);

		testdb.store(testInterface);

		final Interface testInterface2 = new Interface(providerAccountName, namespace, namespace, "testclass", newprops, newops);
		testInterface2.setProviderAccountID(providerAccount);
		testInterface2.setMetaClassID(metaClassID);
		testInterface2.setNamespaceID(namespaceID);
		testInterface2.setPropertiesIDs(propertiesIDs);
		testInterface2.setOperationsIDs(operationsIDs);
		testInterface2.setDataClayID(new InterfaceID());
		testInterface2.setClassNamespaceID(namespaceID);

		testdb.store(testInterface2);

		// Check it exists
		assertTrue(testdb.existsObjectByID(testInterface.getDataClayID()));

		// Check obtained properties
		final HashSet<InterfaceID> interfacesIDs = new HashSet<>();
		interfacesIDs.add(testInterface.getDataClayID());
		interfacesIDs.add(testInterface2.getDataClayID());
		final Map<InterfaceID, Interface> curInterfaces = ifaceMgr.getInterfacesInfo(interfacesIDs);
		assertTrue(curInterfaces.get(testInterface.getDataClayID()).getNamespaceID().equals(namespaceID)
				&& curInterfaces.get(testInterface.getDataClayID()).getMetaClassID().equals(metaClassID)
				&& curInterfaces.get(testInterface.getDataClayID()).getOperationsIDs().containsAll(operationsIDs)
				&& curInterfaces.get(testInterface.getDataClayID()).getPropertiesIDs().containsAll(propertiesIDs));
		assertTrue(curInterfaces.get(testInterface2.getDataClayID()).getNamespaceID().equals(namespaceID)
				&& curInterfaces.get(testInterface2.getDataClayID()).getMetaClassID().equals(metaClassID)
				&& curInterfaces.get(testInterface2.getDataClayID()).getOperationsIDs().containsAll(operationsIDs)
				&& curInterfaces.get(testInterface2.getDataClayID()).getPropertiesIDs().containsAll(propertiesIDs));
	}

	/******************************************** EXCEPTIONS ********************************************/

	@Test(expected = InterfaceNotExistsException.class)
	public void testRemoveInterfaceNotInNamespace() {
		// Store it with dbhandler
		final Interface testInterface = new Interface(providerAccountName, namespace, namespace, "testclass", props, ops);
		testInterface.setProviderAccountID(providerAccount);
		testInterface.setMetaClassID(metaClassID);
		testInterface.setNamespaceID(namespaceID);
		testInterface.setPropertiesIDs(propsIDs);
		testInterface.setOperationsIDs(opsIDs);
		testInterface.setDataClayID(new InterfaceID());
		testInterface.setClassNamespaceID(namespaceID);

		testdb.store(testInterface);

		// Check object exists by its ID
		assertTrue(testdb.existsObjectByID(testInterface.getDataClayID()));

		// Use manager
		ifaceMgr.removeInterface(new NamespaceID(), testInterface.getDataClayID());

		// Check
		final boolean found = testdb.existsObjectByID(testInterface.getDataClayID());
		assertFalse(found);
	}
}
