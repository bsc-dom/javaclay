
/**
 * @file NamespaceManagerTest.java
 * @date Sep 25, 2012
 * @author aqueralt
 */

package es.bsc.dataclay.logic.namespacemgr;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.exceptions.logicmodule.namespacemgr.AccountNotResponsibleOfNamespace;
import es.bsc.dataclay.exceptions.logicmodule.namespacemgr.ImportedInterfaceInUseException;
import es.bsc.dataclay.exceptions.logicmodule.namespacemgr.InterfaceAlreadyImportedException;
import es.bsc.dataclay.exceptions.logicmodule.namespacemgr.InterfaceNotImportedException;
import es.bsc.dataclay.exceptions.logicmodule.namespacemgr.NamespaceDoesNotExistException;
import es.bsc.dataclay.exceptions.logicmodule.namespacemgr.NamespaceExistsException;
import es.bsc.dataclay.logic.namespacemgr.NamespaceManager;
import es.bsc.dataclay.logic.namespacemgr.NamespaceManagerDB;
import es.bsc.dataclay.test.AbstractManagerTest;
import es.bsc.dataclay.test.TestUtils;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.management.namespacemgr.ImportedInterface;
import es.bsc.dataclay.util.management.namespacemgr.Namespace;

/**
 * @author aqueralt
 * @class NamespaceManagerTest
 * @brief This class tests the functions of the Namespace Manager
 */
public final class NamespaceManagerTest extends AbstractManagerTest{

	private NamespaceManager dman;
	// !<@brief Namespace Manager instance tested.
	private NamespaceManagerDB db;
	// !<@brief DbHandler to insert the responsible account when required in the tests
	private AccountID respAccountID;
	// !<@brief DbHandler to insert the responsible account when required in the tests
	private String respAccountName;
	// !<@brief An Account for the responsible of the new namespace.
	private static String DBFILESDIRNAME = System.getProperty("user.dir") + "/dbfiles/NamespaceManagerTest";
	// !<@brief Path to the test databases.
	private static final String NAMESPACENAME = "TestNamespace"; // !<@brief Name of the namespace

	private static final String IMPORTED_INTERFACE_NAME = "ImportedIface"; // !<@brief Name of imported interface
	private static final int RANDMAX = 10; // !<@brief Maximum of Random numbers generated in tests

	/**
	 * @brief This method is executed before all tests. It is used to instantiate the NamespaceManager.
	 * @author aqueralt
	 */
	@BeforeClass
	public static void beforeAll() {
		TestUtils.createOrCleanDirectory(DBFILESDIRNAME);
	}

	/**
	 * @brief This method is executed before each test. It is used to create an account for the namespace responsible and store it
	 *        in the DB.
	 * @author aqueralt
	 * @throws RemoteException
	 */
	@Override
	@Before
	public void before() {
		super.before();

		final NamespaceManagerDB cdb = new NamespaceManagerDB(dataSource);
		cdb.dropTables();

		// we create and store a new Account for the responsible.
		dman = new NamespaceManager(dataSource);
		db = dman.getDbHandler();
		respAccountID = new AccountID();
		respAccountName = "ResponsibleAccount";
	}

	/**
	 * @brief This method is executed after each test. It is used to delete the test database
	 * @author aqueralt
	 * @throws Exception
	 */
	@Override
	@After
	public void after() throws Exception {
		db.close();
		super.after();
		TestUtils.cleanDirectory(DBFILESDIRNAME);
	}

	@AfterClass
	public static void afterAll() throws Exception {
		TestUtils.deleteDirectory(DBFILESDIRNAME);
	}

	/**
	 * @brief Test method for
	 * 
	 *        NamespaceManager::newNamespace(String name, AccountID responsible)
	 * @author aqueralt
	 * @pre The functions dbhandler.DbHandler#existsObjectByID(java.lang.Object)} and dbhandler.DbHandler#store(java.lang.Object)}
	 *      must be tested and correct.
	 * @post Test the creation and storage of a new Namespace, associated to an existing responsible. \n
	 */
	@Test
	public void testNewNamespace() {

		// If an exception is thrown, the test fails
		final Namespace newNamespace = new Namespace("newNamespace", respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		final NamespaceID newID = dman.newNamespace(newNamespace);

		// Since we use the same database we need to open it again
		// to verify that the Namespace exists in the database by
		// calling the DbHandler directly.
		final Namespace curNamespace = db.getNamespaceByID(newID);
		assertTrue(curNamespace != null);

		// Check ID
		assertTrue(curNamespace.getDataClayID().equals(newID));

		// Check resp
		assertTrue(curNamespace.getProviderAccountID().equals(respAccountID));

	}

	/**
	 * @brief Test method for
	 * 
	 *        NamespaceManager::getNamespacesNames()
	 * @author jmarti
	 * @pre The functions dbhandler.DbHandler#existsObjectByID(java.lang.Object)} and dbhandler.DbHandler#store(java.lang.Object)}
	 *      must be tested and correct.
	 * @post Test the creation and storage of a new Namespace, associated to an existing responsible. \n
	 */
	@Test
	public void testGetNamespacesNames() {

		// If an exception is thrown, the test fails
		final String namespaceName1 = "newNamespace";
		final String namespaceName2 = "newNamespace2";
		final Namespace newNamespace1 = new Namespace(namespaceName1, respAccountName, Langs.LANG_JAVA);
		final Namespace newNamespace2 = new Namespace(namespaceName2, respAccountName, Langs.LANG_JAVA);
		newNamespace1.setDataClayID(new NamespaceID());
		newNamespace2.setDataClayID(new NamespaceID());
		newNamespace1.setProviderAccountID(respAccountID);
		newNamespace2.setProviderAccountID(respAccountID);
		dman.newNamespace(newNamespace1);
		dman.newNamespace(newNamespace2);

		// Since we use the same database we need to open it again
		// to verify that the Namespace exists in the database by
		// calling the DbHandler directly.
		Set<String> curNamespaces = dman.getNamespacesNames();
		assertTrue(curNamespaces.size() == 2);
		assertTrue(curNamespaces.contains(namespaceName1));
		assertTrue(curNamespaces.contains(namespaceName2));

		db.deleteNamespaceByID(newNamespace1.getDataClayID());
		curNamespaces = dman.getNamespacesNames();
		assertTrue(curNamespaces.size() == 1);
		assertTrue(curNamespaces.contains(namespaceName2));
	}

	/**
	 * @brief Test method for NamespaceManager::newNamespace(String name, AccountID responsible)
	 * @author aqueralt
	 * @pre The functions dbhandler.DbHandler#existsObjectByID(java.lang.Object)} and dbhandler.DbHandler#store(java.lang.Object)}
	 *      must be tested and correct.
	 * @post Test the creation and storage of a new Namespace, associated to an existing responsible. \n
	 */
	@Test(expected = NamespaceExistsException.class)
	public void testNewNamespaceAlreadyExists() {
		// We create a namespace and store it in the DB
		final Namespace newNamespace = new Namespace("newNamespace", respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);

		// If an exception is thrown, the test succeeds
		final Namespace newNamespace2 = new Namespace("newNamespace", respAccountName, Langs.LANG_JAVA);
		newNamespace2.setProviderAccountID(respAccountID);
		dman.newNamespace(newNamespace2);
	}

	/**
	 * @brief Test method for NamespaceManager::getNamespaceID(String)
	 * @author jmarti
	 * @post Test that the function "getNamespaceID" returns the ID of the stored namespace.
	 */
	@Test
	public void testGetNamespace() {
		final String namespaceName = "newNamespace";
		final Namespace newNamespace = new Namespace(namespaceName, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);

		// Check it exists
		assertTrue(db.existsObjectByID(newNamespace.getDataClayID()));

		// Use manager
		assertTrue(dman.getNamespaceID(namespaceName).equals(newNamespace.getDataClayID()));
	}

	/**
	 * @brief Test method for NamespaceManager::getNamespacesInfo(HashSet<NamespaceID>)
	 * @author jmarti
	 * @post Test that the function "getNamespacesInfo" returns the info of the stored namespaces.
	 */
	@Test
	public void testGetNamespacesInfo() {
		final String namespaceName = "newNamespace";
		// We create a namespace and store it in the DB
		final Namespace newNamespace = new Namespace(namespaceName + "1", respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);

		// We create a second namespace and store it in the DB
		final AccountID testAccountID = new AccountID();
		final Namespace newNamespace2 = new Namespace(namespaceName + "2", respAccountName, Langs.LANG_JAVA);
		newNamespace2.setProviderAccountID(testAccountID);
		newNamespace2.setDataClayID(new NamespaceID());
		db.store(newNamespace2);

		// Check it exists
		final NamespaceID namespaceID = newNamespace.getDataClayID();
		assertTrue(db.existsObjectByID(namespaceID));
		final NamespaceID namespaceID2 = newNamespace2.getDataClayID();
		assertTrue(db.existsObjectByID(namespaceID2));

		// Use manager
		final HashSet<NamespaceID> namespacesIDs = new HashSet<>();
		namespacesIDs.add(namespaceID);
		namespacesIDs.add(namespaceID2);
		final Map<NamespaceID, Namespace> namespacesInfo = dman.getNamespacesInfo(namespacesIDs);
		for (final Entry<NamespaceID, Namespace> curNamespace : namespacesInfo.entrySet()) {
			final NamespaceID curNamespaceID = curNamespace.getKey();
			final Namespace curNamespaceInfo = curNamespace.getValue();
			assertTrue(curNamespaceID.equals(namespaceID) && curNamespaceInfo.getProviderAccountID().equals(respAccountID)
					|| curNamespaceID.equals(namespaceID2) && curNamespaceInfo.getProviderAccountID().equals(testAccountID));
		}
	}

	/**
	 * @brief Test method for
	 * 
	 *        NamespaceManager::checkNamespaceResponsibleAndGetInfo(AccountID responsible, NamespaceID namespaceID)
	 * @author aqueralt
	 * @post Test that "checkNamespaceResponsible" throws exception when the namespace does not exist
	 */
	@Test(expected = NamespaceDoesNotExistException.class)
	public void testCheckNamespaceResponsibleWrongNamespace() {
		// we check first with a non-existing namespace and a non-existing responsible
		Configuration.Flags.CHECK_NAMESPACE.setValue(true);
		final AccountID resp = new AccountID();
		final NamespaceID dom = new NamespaceID();
		dman.checkNamespaceResponsibleAndGetInfo(dom, resp);
	}

	/**
	 * @brief Test method for
	 * 
	 *        NamespaceManager::checkNamespaceResponsibleAndGetInfo(AccountID responsible, NamespaceID namespaceID)
	 * @author aqueralt
	 * @post Test that "checkNamespaceResponsible" throws exception when the namespace does not exist
	 */
	@Test(expected = AccountNotResponsibleOfNamespace.class)
	public void testCheckNamespaceResponsibleWrongResponsible() {
		// we check first with a non-existing namespace and a non-existing responsible

		final Namespace newNamespace = new Namespace("newNamespace", respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		final NamespaceID newDom = dman.newNamespace(newNamespace);
		dman.checkNamespaceResponsibleAndGetInfo(newDom, new AccountID());
	}

	/**
	 * @brief Test method for NamespaceManager::checkNamespaceResponsible(AccountID responsible, NamespaceID namespaceID)
	 * @pre The function namespacemgr.NamespaceManager::newNamespace} and must be tested and correct.
	 * @post Test that "checkNamespaceResponsible" returns namespace info when the given AccountID is responsible for an existing
	 *       namespaceID, and false when it is not
	 */
	@Test
	public void testCheckNamespaceResponsibleCorrectNamespace() {
		// an existing namespace with a non-existing responsible
		final Namespace newNamespace = new Namespace("newNamespace", respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		final NamespaceID newDom = dman.newNamespace(newNamespace);
		final Namespace result = dman.checkNamespaceResponsibleAndGetInfo(newDom, respAccountID);
		assertTrue(result.getDataClayID().equals(newDom));
	}

	/**
	 * @brief Test method for NamespaceManager::removeNamespace(NamespaceID namespaceID)
	 * @author aqueralt
	 * @pre The function namespacemgr.NamespaceManager::newNamespace} namespacemgr.NamespaceManager::existsNamespace} must be tested
	 *      and correct.
	 * @post Test that "removeNamespace" succeeds when an existing namespace is provided.
	 */
	@Test
	public void testRemoveNamespaceCorrectNamespace() {
		// We create an initial namespace
		final Namespace newNamespace = new Namespace("newNamespace", respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);

		// Check it exists
		assertTrue(db.existsObjectByID(newNamespace.getDataClayID()));

		// We delete it
		dman.removeNamespace(newNamespace.getDataClayID());

		// we check that it does not exist
		assertFalse(db.existsObjectByID(newNamespace.getDataClayID()));
	}

	/**
	 * @brief Test method for NamespaceManager::removeNamespace(NamespaceID namespaceID)
	 * @author aqueralt
	 * @pre The function namespacemgr.NamespaceManager::newNamespace, namespacemgr.NamespaceManager::existsNamespace must be tested
	 *      and correct.
	 * @post Test that "removeNamespace" succeeds when an existing namespace is provided, deleting the namespace but not its
	 *       responsible account (thus another namespace with same responsible is ok)
	 */
	@Test
	public void testRemoveNamespace2NamespacesWithSameResponsible() {
		// We create an initial namespace
		final Namespace newNamespace1 = new Namespace("Namespace 1", respAccountName, Langs.LANG_JAVA);
		newNamespace1.setProviderAccountID(respAccountID);
		newNamespace1.setDataClayID(new NamespaceID());
		final String namespaceName2 = "Namespace 2";
		final Namespace newNamespace2 = new Namespace(namespaceName2, respAccountName, Langs.LANG_JAVA);
		newNamespace2.setProviderAccountID(respAccountID);
		newNamespace2.setDataClayID(new NamespaceID());
		db.store(newNamespace1);
		db.store(newNamespace2);

		// Check both exist
		assertTrue(db.existsObjectByID(newNamespace1.getDataClayID()));
		assertTrue(db.existsObjectByID(newNamespace2.getDataClayID()));

		// We delete namespace1
		dman.removeNamespace(newNamespace1.getDataClayID());

		// we check that namespace1 does not exist
		assertFalse(db.existsObjectByID(newNamespace1.getDataClayID()));

		// but the other namespace must be ok
		final Namespace curNamespace = db.getNamespaceByID(newNamespace2.getDataClayID());
		assertTrue(curNamespace != null);
		assertTrue(curNamespace.getProviderAccountID().equals(respAccountID));
		assertTrue(curNamespace.getName().equals(namespaceName2));
		assertTrue(curNamespace.getDataClayID().equals(newNamespace2.getDataClayID()));
	}

	@Test
	public void testImportInterfaces() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Randomly choose the number of interfaces to import
		final Random random = new Random();
		final int numImports = random.nextInt(RANDMAX) + 1; // exclude 0

		final HashSet<ImportedInterface> interfaces = new HashSet<>();
		for (int i = 0; i < numImports; ++i) {
			final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME + i,
					new InterfaceID(), new ContractID(), new MetaClassID(), new NamespaceID());
			interfaces.add(importedInterface);

		}

		dman.importInterfaces(namespaceID, interfaces);

		// Verify
		final Namespace updatedNamespace = db.getNamespaceByID(namespaceID);
		assertTrue(updatedNamespace != null);

		// Verify the objects exists and are correct
		for (final Iterator<ImportedInterface> it = interfaces.iterator(); it.hasNext();) {
			final ImportedInterface curInterface = it.next();

			assertTrue(updatedNamespace.existsImportedInterface(curInterface.getInterfaceID(), curInterface.getContractID()));

			assertTrue(updatedNamespace.getImportedInterface(curInterface.getInterfaceID(), curInterface.getContractID())
					.equals(curInterface));
		}
	}

	@Test
	public void testRemoveImportedInterfaces() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		final InterfaceID importedInterfaceID = new InterfaceID();
		final ContractID importedContractID = new ContractID();
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, importedInterfaceID,
				importedContractID, new MetaClassID(), new NamespaceID());

		newNamespace.addImportedInterface(importedInterface);

		db.updateNamespaceAddImport(namespaceID, importedInterface);

		final HashSet<ImportedInterface> importedIfaces = new HashSet<>();
		importedIfaces.add(importedInterface);
		dman.removeImportedInterfaces(namespaceID, importedIfaces);

		// Verify
		final Namespace updatedNamespace = db.getNamespaceByID(namespaceID);
		assertTrue(updatedNamespace != null);

		assertTrue(!updatedNamespace.existsImportedInterface(importedInterfaceID, importedContractID));

	}

	@Test
	public void testGetImportedInterfacesFromClassName() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Randomly choose the number of classes to import
		final Random random = new Random();
		final int numImports = random.nextInt(RANDMAX) + 1; // exclude 0

		final HashSet<ImportedInterface> importedInterfaces = new HashSet<>();
		for (int i = 0; i < numImports; ++i) {
			final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
					new ContractID(), new MetaClassID(), new NamespaceID());

			importedInterfaces.add(importedInterface);
			newNamespace.addImportedInterface(importedInterface);

			db.updateNamespaceAddImport(namespaceID, importedInterface);

		}

		// Get the interface specs
		final HashSet<ImportedInterface> resultIfaces = dman.getImportedInterfaces(namespaceID, IMPORTED_INTERFACE_NAME);

		// Verify
		for (final Iterator<ImportedInterface> it = resultIfaces.iterator(); it.hasNext();) {
			assertTrue(importedInterfaces.contains(it.next()));
		}
	}

	@Test
	public void testGetImportedInterfacesForMetaClasses() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Randomly choose the number of classes to import
		final Random random = new Random();
		final int numImports = random.nextInt(RANDMAX) + 1; // exclude 0

		final Map<MetaClassID, Set<ImportedInterface>> importedInterfaces = new HashMap<>();
		final Set<MetaClassID> metaClassIDs = new HashSet<>();
		for (int i = 0; i < numImports; ++i) {
			final MetaClassID metaClassID = new MetaClassID();
			metaClassIDs.add(metaClassID);
			final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
					new ContractID(), metaClassID, new NamespaceID());

			final Set<ImportedInterface> importedInterfacessOfMetaClass = new HashSet<>();
			importedInterfacessOfMetaClass.add(importedInterface);
			importedInterfaces.put(metaClassID, importedInterfacessOfMetaClass);
			newNamespace.addImportedInterface(importedInterface);

			db.updateNamespaceAddImport(namespaceID, importedInterface);
		}

		// Get the interface specs
		final Map<MetaClassID, Set<ImportedInterface>> resultIfaces = dman.getImportedInterfacesForMetaclasses(
				namespaceID, metaClassIDs);

		// Verify
		assertTrue(resultIfaces.keySet().equals(importedInterfaces.keySet()));
		for (final MetaClassID metaClassID : metaClassIDs) {
			assertTrue(resultIfaces.get(metaClassID).equals(importedInterfaces.get(metaClassID)));
		}
	}

	@Test
	public void testRegisterPropertyUsesImport() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Prepare imported interface spec
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
				new ContractID(), new MetaClassID(), new NamespaceID());

		// Add imported interface
		newNamespace.addImportedInterface(importedInterface);
		db.updateNamespaceAddImport(namespaceID, importedInterface);

		// Test
		final PropertyID propertyID = new PropertyID();
		final HashSet<PropertyID> propertiesUsingImport = new HashSet<>();
		propertiesUsingImport.add(propertyID);
		final Map<ContractID, InterfaceID> interfacesInContract = new HashMap<>();
		interfacesInContract.put(importedInterface.getContractID(), importedInterface.getInterfaceID());
		dman.registerPropertiesUsingImportedClass(namespaceID, propertiesUsingImport, interfacesInContract);

		// Verify
		final Namespace updatedNamespace = db.getNamespaceByID(namespaceID);
		assertTrue(updatedNamespace != null);
		assertTrue(updatedNamespace
				.getImportedInterface(importedInterface.getInterfaceID(), importedInterface.getContractID())
				.getPropertiesUsingImports().contains(propertyID));

	}

	@Test
	public void testRegisterOperationUsesImport() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Prepare imported interface spec
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
				new ContractID(), new MetaClassID(), new NamespaceID());

		// Add imported interface
		newNamespace.addImportedInterface(importedInterface);
		db.updateNamespaceAddImport(namespaceID, importedInterface);

		// Test
		final OperationID operationID = new OperationID();
		final HashSet<OperationID> usingImport = new HashSet<>();
		usingImport.add(operationID);
		final Map<ContractID, InterfaceID> interfacesInContract = new HashMap<>();
		interfacesInContract.put(importedInterface.getContractID(), importedInterface.getInterfaceID());
		dman.registerOperationsUsingImportedClass(namespaceID, usingImport, interfacesInContract);

		// Verify
		final Namespace updatedNamespace = db.getNamespaceByID(namespaceID);
		assertTrue(updatedNamespace != null);
		assertTrue(updatedNamespace
				.getImportedInterface(importedInterface.getInterfaceID(), importedInterface.getContractID())
				.getOperationsUsingImports().contains(operationID));

	}

	@Test
	public void testRegisterImplementationUsesImport() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Prepare imported interface spec
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
				new ContractID(), new MetaClassID(), new NamespaceID());

		// Add imported interface
		newNamespace.addImportedInterface(importedInterface);
		db.updateNamespaceAddImport(namespaceID, importedInterface);

		// Test
		final ImplementationID implementationID = new ImplementationID();
		final HashSet<ImplementationID> usingImport = new HashSet<>();
		usingImport.add(implementationID);
		final Map<ContractID, InterfaceID> interfacesInContract = new HashMap<>();
		interfacesInContract.put(importedInterface.getContractID(), importedInterface.getInterfaceID());
		dman.registerImplementationsUsingImportedClass(namespaceID, usingImport, interfacesInContract);

		// Verify
		final Namespace updatedNamespace = db.getNamespaceByID(namespaceID);
		assertTrue(updatedNamespace != null);
		assertTrue(updatedNamespace
				.getImportedInterface(importedInterface.getInterfaceID(), importedInterface.getContractID())
				.getImplementationsUsingImports().contains(implementationID));

	}

	@Test
	public void testRegisterSubClassUsesImport() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Prepare imported interface spec
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
				new ContractID(), new MetaClassID(), new NamespaceID());

		// Add imported interface
		newNamespace.addImportedInterface(importedInterface);
		db.updateNamespaceAddImport(namespaceID, importedInterface);

		// Test
		final MetaClassID metaClassID = new MetaClassID();
		final HashSet<MetaClassID> usingImport = new HashSet<>();
		usingImport.add(metaClassID);
		final Map<ContractID, InterfaceID> interfacesInContract = new HashMap<>();
		interfacesInContract.put(importedInterface.getContractID(), importedInterface.getInterfaceID());
		dman.registerSubClassesUsingImportedClass(namespaceID, usingImport, interfacesInContract);

		// Verify
		final Namespace updatedNamespace = db.getNamespaceByID(namespaceID);
		assertTrue(updatedNamespace != null);
		assertTrue(updatedNamespace
				.getImportedInterface(importedInterface.getInterfaceID(), importedInterface.getContractID())
				.getSubClassesOfImport().contains(metaClassID));

	}

	@Test
	public void testUnregisterOperationUsesImport() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Prepare imported interface spec
		final InterfaceID interfaceID = new InterfaceID();
		final ContractID contractID = new ContractID();
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, interfaceID,
				contractID, new MetaClassID(), new NamespaceID());

		// Add imported interface
		final OperationID operationID = new OperationID();
		newNamespace.addImportedInterface(importedInterface);
		newNamespace.getImportedInterface(interfaceID, contractID).addOperationUsingImport(operationID);
		db.updateNamespaceAddImport(namespaceID, importedInterface);

		// Test
		final HashSet<OperationID> usingImport = new HashSet<>();
		usingImport.add(operationID);
		final Map<ContractID, InterfaceID> interfacesInContract = new HashMap<>();
		interfacesInContract.put(importedInterface.getContractID(), importedInterface.getInterfaceID());
		dman.unregisterOperationsFromUsingAnyImportedClassInNamespace(namespaceID, usingImport);

		// Verify
		final Namespace updatedNamespace = db.getNamespaceByID(namespaceID);
		assertTrue(updatedNamespace != null);
		assertFalse(updatedNamespace
				.getImportedInterface(importedInterface.getInterfaceID(), importedInterface.getContractID())
				.getOperationsUsingImports().contains(operationID));

	}

	@Test
	public void testUnregisterImplementationUsesImport() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Prepare imported interface spec
		final InterfaceID interfaceID = new InterfaceID();
		final ContractID contractID = new ContractID();
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, interfaceID,
				contractID, new MetaClassID(), new NamespaceID());

		// Add imported interface
		final ImplementationID implementationID = new ImplementationID();
		newNamespace.addImportedInterface(importedInterface);
		newNamespace.getImportedInterface(interfaceID, contractID).addImplementationUsingImport(implementationID);
		db.updateNamespaceAddImport(namespaceID, importedInterface);

		// Test
		final HashSet<ImplementationID> usingImport = new HashSet<>();
		usingImport.add(implementationID);
		final Map<ContractID, InterfaceID> interfacesInContract = new HashMap<>();
		interfacesInContract.put(importedInterface.getContractID(), importedInterface.getInterfaceID());
		dman.unregisterImplementationsFromUsingAnyImportedClassInNamespace(namespaceID, usingImport);

		// Verify
		final Namespace updatedNamespace = db.getNamespaceByID(namespaceID);
		assertTrue(updatedNamespace != null);
		assertFalse(updatedNamespace
				.getImportedInterface(importedInterface.getInterfaceID(), importedInterface.getContractID())
				.getImplementationsUsingImports().contains(implementationID));

	}

	@Test
	public void testUnregisterSubClassUsesImport() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Prepare imported interface spec
		final InterfaceID interfaceID = new InterfaceID();
		final ContractID contractID = new ContractID();
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, interfaceID,
				contractID, new MetaClassID(), new NamespaceID());

		// Add imported interface
		final MetaClassID metaClassID = new MetaClassID();
		newNamespace.addImportedInterface(importedInterface);
		newNamespace.getImportedInterface(interfaceID, contractID).addSubClassUsingImport(metaClassID);
		db.updateNamespaceAddImport(namespaceID, importedInterface);

		// Test
		final HashSet<MetaClassID> usingImport = new HashSet<>();
		usingImport.add(metaClassID);
		final Map<ContractID, InterfaceID> interfacesInContract = new HashMap<>();
		interfacesInContract.put(importedInterface.getContractID(), importedInterface.getInterfaceID());
		dman.unregisterSubClassesFromUsingAnyImportedClassInNamespace(namespaceID, usingImport);

		// Verify
		final Namespace updatedNamespace = db.getNamespaceByID(namespaceID);
		assertTrue(updatedNamespace != null);
		assertFalse(updatedNamespace
				.getImportedInterface(importedInterface.getInterfaceID(), importedInterface.getContractID())
				.getSubClassesOfImport().contains(metaClassID));

	}

	@Test
	public void testCheckImportsNotUsedAndGet() {
		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Prepare imported interface spec
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
				new ContractID(), new MetaClassID(), new NamespaceID());

		// Add imported interface
		newNamespace.addImportedInterface(importedInterface);
		db.updateNamespaceAddImport(namespaceID, importedInterface);

		// Test
		final HashSet<ImportedInterface> imports = dman.checkImportsOfClassAreNotUsedAndGet(namespaceID, IMPORTED_INTERFACE_NAME);

		// Verify
		assertTrue(imports.size() == 1);
		final ImportedInterface theImport = imports.iterator().next();
		assertTrue(theImport.getInterfaceID().equals(importedInterface.getInterfaceID()));
		assertTrue(theImport.getContractID().equals(importedInterface.getContractID()));

	}

	@Test
	public void testGetImportsOfAccount() {

		// Create two namespaces with same responsible
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());

		final Namespace newNamespaceOther = new Namespace(NAMESPACENAME + 1, respAccountName, Langs.LANG_JAVA);
		newNamespaceOther.setProviderAccountID(respAccountID);
		newNamespaceOther.setDataClayID(new NamespaceID());

		db.store(newNamespace);
		db.store(newNamespaceOther);

		final NamespaceID namespaceID = newNamespace.getDataClayID();
		final NamespaceID namespaceIDother = newNamespaceOther.getDataClayID();

		// Add same import in both namespaces and a different one
		// Prepare imported interface spec
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
				new ContractID(), new MetaClassID(), new NamespaceID());

		final ImportedInterface importedInterfaceOther = new ImportedInterface(IMPORTED_INTERFACE_NAME + "Other",
				new InterfaceID(), new ContractID(), new MetaClassID(), new NamespaceID());

		// Add imported interface
		newNamespace.addImportedInterface(importedInterface);
		newNamespaceOther.addImportedInterface(importedInterface);
		newNamespaceOther.addImportedInterface(importedInterfaceOther);

		db.updateNamespaceAddImport(namespaceID, importedInterface);
		db.updateNamespaceAddImport(namespaceIDother, importedInterface);
		db.updateNamespaceAddImport(namespaceIDother, importedInterfaceOther);

		// Now get the imports of the account
		final HashSet<ImportedInterface> importsOfAccount = dman.getImportsOfAccount(respAccountID);

		// Verify it
		assertTrue(importsOfAccount.contains(importedInterface));
		assertTrue(importsOfAccount.contains(importedInterfaceOther));
		assertTrue(importsOfAccount.size() == 2);
	}

	@Test(expected = NamespaceDoesNotExistException.class)
	public void testGetWrongNamespacesResponsibles() {
		final String namespaceName = "newNamespace";
		// We create a namespace and store it in the DB
		final Namespace newNamespace = new Namespace(namespaceName + "1", respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);

		// We create a second namespace and store it in the DB
		final AccountID testAccountID = new AccountID();
		final Namespace newNamespace2 = new Namespace(namespaceName + "", "OtherAccount", Langs.LANG_JAVA);
		newNamespace2.setProviderAccountID(testAccountID);
		newNamespace2.setDataClayID(new NamespaceID());
		db.store(newNamespace2);

		// Check it exists
		final NamespaceID namespaceID = newNamespace.getDataClayID();
		assertTrue(db.existsObjectByID(namespaceID));
		final NamespaceID namespaceID2 = newNamespace2.getDataClayID();
		assertTrue(db.existsObjectByID(namespaceID2));

		// Use manager
		final HashSet<NamespaceID> namespacesIDs = new HashSet<>();
		namespacesIDs.add(new NamespaceID());
		namespacesIDs.add(namespaceID2);
		dman.getNamespacesInfo(namespacesIDs);
	}

	/**
	 * @brief Test method for NamespaceManager::removeNamespace(NamespaceID namespaceID)
	 * @author aqueralt
	 * @post Test that "removeNamespace" returns an exception when a non existing namespace is provided.
	 */
	@Test(expected = NamespaceDoesNotExistException.class)
	public void testRemoveNamespaceWrongNamespace() {
		// We try to delete a non-existing namespace
		dman.removeNamespace(new NamespaceID());
	}

	@Test(expected = NamespaceDoesNotExistException.class)
	public void testImportInterfacesNamespaceNotExistException() {

		// Prepare imported interface spec
		final InterfaceID importedInterfaceID = new InterfaceID();

		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, importedInterfaceID,
				new ContractID(), new MetaClassID(), new NamespaceID());

		final HashSet<ImportedInterface> importedIfaces = new HashSet<>();
		importedIfaces.add(importedInterface);
		dman.importInterfaces(new NamespaceID(), importedIfaces);
	}

	@Test(expected = InterfaceAlreadyImportedException.class)
	public void testImportInterfacesAlreadyExistsException() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Prepare imported interface spec
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
				new ContractID(), new MetaClassID(), new NamespaceID());

		final HashSet<ImportedInterface> importedIfaces = new HashSet<>();
		importedIfaces.add(importedInterface);
		dman.importInterfaces(namespaceID, importedIfaces);
		dman.importInterfaces(namespaceID, importedIfaces);

	}

	@Test(expected = NamespaceDoesNotExistException.class)
	public void testRemoveInterfacesNamespaceNotExistException() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Prepare imported interface spec
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
				new ContractID(), new MetaClassID(), new NamespaceID());

		// Add imported interface
		newNamespace.addImportedInterface(importedInterface);
		db.updateNamespaceAddImport(namespaceID, importedInterface);

		final HashSet<ImportedInterface> importedIfaces = new HashSet<>();
		importedIfaces.add(importedInterface);
		dman.removeImportedInterfaces(new NamespaceID(), importedIfaces);
	}

	@Test(expected = InterfaceNotImportedException.class)
	public void testRemoveInterfaceNotExistsException() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Prepare imported interface spec
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
				new ContractID(), new MetaClassID(), new NamespaceID());

		// Add imported interface
		newNamespace.addImportedInterface(importedInterface);
		db.updateNamespaceAddImport(namespaceID, importedInterface);

		importedInterface.setInterfaceID(new InterfaceID());
		final HashSet<ImportedInterface> importedIfaces = new HashSet<>();
		importedIfaces.add(importedInterface);
		dman.removeImportedInterfaces(namespaceID, importedIfaces);

	}

	@Test(expected = InterfaceNotImportedException.class)
	public void testRemoveInterfaceContractNotExistsException() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Prepare imported interface spec
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
				new ContractID(), new MetaClassID(), new NamespaceID());

		// Add imported interface
		newNamespace.addImportedInterface(importedInterface);
		db.updateNamespaceAddImport(namespaceID, importedInterface);

		importedInterface.setContractID(new ContractID());
		final HashSet<ImportedInterface> importedIfaces = new HashSet<>();
		importedIfaces.add(importedInterface);
		dman.removeImportedInterfaces(namespaceID, importedIfaces);

	}

	@Test(expected = NamespaceDoesNotExistException.class)
	public void testCheckImportNotUsedNamespaceNotExistException() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Prepare imported interface spec
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
				new ContractID(), new MetaClassID(), new NamespaceID());

		// Add imported interface
		newNamespace.addImportedInterface(importedInterface);
		db.updateNamespaceAddImport(namespaceID, importedInterface);

		dman.checkImportsOfClassAreNotUsedAndGet(new NamespaceID(), IMPORTED_INTERFACE_NAME);
	}

	@Test(expected = ImportedInterfaceInUseException.class)
	public void testCheckImportNotUsedFail() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Prepare imported interface spec
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
				new ContractID(), new MetaClassID(), new NamespaceID());

		// Add imported interface
		newNamespace.addImportedInterface(importedInterface);
		newNamespace.registerImplementationUsingImport(new ImplementationID(), importedInterface.getInterfaceID(),
				importedInterface.getContractID());
		db.updateNamespaceAddImport(namespaceID, importedInterface);

		dman.checkImportsOfClassAreNotUsedAndGet(namespaceID, IMPORTED_INTERFACE_NAME);
	}

	@Test(expected = NamespaceDoesNotExistException.class)
	public void testGetImportedInterfacesFromClassNameNamespaceDoesNotExistException() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Randomly choose the number of classes to import
		final Random random = new Random();
		final int numImports = random.nextInt(RANDMAX) + 1; // exclude 0

		final HashSet<ImportedInterface> importedInterfaces = new HashSet<>();
		for (int i = 0; i < numImports; ++i) {
			final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
					new ContractID(), new MetaClassID(), new NamespaceID());

			importedInterfaces.add(importedInterface);
			newNamespace.addImportedInterface(importedInterface);

			db.updateNamespaceAddImport(namespaceID, importedInterface);
		}

		// Get the interface specs
		dman.getImportedInterfaces(new NamespaceID(), IMPORTED_INTERFACE_NAME);
	}

	@Test
	public void testGetImportedInterfacesFromClassNotImported() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Randomly choose the number of classes to import
		final Random random = new Random();
		final int numImports = random.nextInt(RANDMAX) + 1; // exclude 0

		final HashSet<ImportedInterface> importedInterfaces = new HashSet<>();
		for (int i = 0; i < numImports; ++i) {
			final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
					new ContractID(), new MetaClassID(), new NamespaceID());

			importedInterfaces.add(importedInterface);
			newNamespace.addImportedInterface(importedInterface);

			db.updateNamespaceAddImport(namespaceID, importedInterface);
		}

		// Get the interface specs
		assertTrue(dman.getImportedInterfaces(namespaceID, IMPORTED_INTERFACE_NAME + "_badclass").size() == 0);
	}

	@Test(expected = NamespaceDoesNotExistException.class)
	public void testRegisterPropertyNamespaceNotExistException() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Prepare imported interface spec
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
				new ContractID(), new MetaClassID(), new NamespaceID());

		// Add imported interface
		newNamespace.addImportedInterface(importedInterface);
		db.updateNamespaceAddImport(namespaceID, importedInterface);

		final HashSet<PropertyID> usingImport = new HashSet<>();
		usingImport.add(new PropertyID());
		final Map<ContractID, InterfaceID> interfacesInContract = new HashMap<>();
		interfacesInContract.put(importedInterface.getContractID(), importedInterface.getInterfaceID());
		dman.registerPropertiesUsingImportedClass(new NamespaceID(), usingImport, interfacesInContract);
	}

	@Test(expected = InterfaceNotImportedException.class)
	public void testRegisterPropertyInterfaceNotExistsException() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Prepare imported interface spec
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
				new ContractID(), new MetaClassID(), new NamespaceID());

		// Add imported interface
		newNamespace.addImportedInterface(importedInterface);
		db.updateNamespaceAddImport(namespaceID, importedInterface);

		final HashSet<PropertyID> usingImport = new HashSet<>();
		usingImport.add(new PropertyID());
		final Map<ContractID, InterfaceID> interfacesInContract = new HashMap<>();
		interfacesInContract.put(importedInterface.getContractID(), new InterfaceID());
		dman.registerPropertiesUsingImportedClass(namespaceID, usingImport, interfacesInContract);

	}

	@Test(expected = InterfaceNotImportedException.class)
	public void testRegisterPropertyInterfaceContractNotExistsException() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Prepare imported interface spec
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
				new ContractID(), new MetaClassID(), new NamespaceID());

		// Add imported interface
		newNamespace.addImportedInterface(importedInterface);
		db.updateNamespaceAddImport(namespaceID, importedInterface);

		final HashSet<PropertyID> usingImport = new HashSet<>();
		usingImport.add(new PropertyID());
		final Map<ContractID, InterfaceID> interfacesInContract = new HashMap<>();
		interfacesInContract.put(new ContractID(), importedInterface.getInterfaceID());
		dman.registerPropertiesUsingImportedClass(namespaceID, usingImport, interfacesInContract);

	}

	@Test(expected = NamespaceDoesNotExistException.class)
	public void testRegisterOperationNamespaceNotExistException() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Prepare imported interface spec
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
				new ContractID(), new MetaClassID(), new NamespaceID());

		// Add imported interface
		newNamespace.addImportedInterface(importedInterface);
		db.updateNamespaceAddImport(namespaceID, importedInterface);

		final HashSet<OperationID> usingImport = new HashSet<>();
		usingImport.add(new OperationID());
		final Map<ContractID, InterfaceID> interfacesInContract = new HashMap<>();
		interfacesInContract.put(importedInterface.getContractID(), importedInterface.getInterfaceID());
		dman.registerOperationsUsingImportedClass(new NamespaceID(), usingImport, interfacesInContract);
	}

	@Test(expected = InterfaceNotImportedException.class)
	public void testRegisterOperationInterfaceNotExistsException() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Prepare imported interface spec
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
				new ContractID(), new MetaClassID(), new NamespaceID());

		// Add imported interface
		newNamespace.addImportedInterface(importedInterface);
		db.updateNamespaceAddImport(namespaceID, importedInterface);

		final HashSet<OperationID> usingImport = new HashSet<>();
		usingImport.add(new OperationID());
		final Map<ContractID, InterfaceID> interfacesInContract = new HashMap<>();
		interfacesInContract.put(importedInterface.getContractID(), new InterfaceID());
		dman.registerOperationsUsingImportedClass(namespaceID, usingImport, interfacesInContract);

	}

	@Test(expected = InterfaceNotImportedException.class)
	public void testRegisterOperationInterfaceContractNotExistsException() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Prepare imported interface spec
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
				new ContractID(), new MetaClassID(), new NamespaceID());

		// Add imported interface
		newNamespace.addImportedInterface(importedInterface);
		db.updateNamespaceAddImport(namespaceID, importedInterface);

		final HashSet<OperationID> usingImport = new HashSet<>();
		usingImport.add(new OperationID());
		final Map<ContractID, InterfaceID> interfacesInContract = new HashMap<>();
		interfacesInContract.put(new ContractID(), importedInterface.getInterfaceID());
		dman.registerOperationsUsingImportedClass(namespaceID, usingImport, interfacesInContract);

	}

	@Test(expected = NamespaceDoesNotExistException.class)
	public void testRegisterImplementationNamespaceNotExistException() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Prepare imported interface spec
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
				new ContractID(), new MetaClassID(), new NamespaceID());

		// Add imported interface
		newNamespace.addImportedInterface(importedInterface);
		db.updateNamespaceAddImport(namespaceID, importedInterface);

		final HashSet<ImplementationID> usingImport = new HashSet<>();
		usingImport.add(new ImplementationID());
		final Map<ContractID, InterfaceID> interfacesInContract = new HashMap<>();
		interfacesInContract.put(importedInterface.getContractID(), importedInterface.getInterfaceID());
		dman.registerImplementationsUsingImportedClass(new NamespaceID(), usingImport, interfacesInContract);
	}

	@Test(expected = InterfaceNotImportedException.class)
	public void testRegisterImplementationInterfaceNotExistsException() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Prepare imported interface spec
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
				new ContractID(), new MetaClassID(), new NamespaceID());

		// Add imported interface
		newNamespace.addImportedInterface(importedInterface);
		db.updateNamespaceAddImport(namespaceID, importedInterface);

		final HashSet<ImplementationID> usingImport = new HashSet<>();
		usingImport.add(new ImplementationID());
		final Map<ContractID, InterfaceID> interfacesInContract = new HashMap<>();
		interfacesInContract.put(importedInterface.getContractID(), new InterfaceID());
		dman.registerImplementationsUsingImportedClass(namespaceID, usingImport, interfacesInContract);

	}

	@Test(expected = InterfaceNotImportedException.class)
	public void testRegisterImplementationInterfaceContractNotExistsException() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Prepare imported interface spec
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
				new ContractID(), new MetaClassID(), new NamespaceID());

		// Add imported interface
		newNamespace.addImportedInterface(importedInterface);
		db.updateNamespaceAddImport(namespaceID, importedInterface);

		final HashSet<ImplementationID> usingImport = new HashSet<>();
		usingImport.add(new ImplementationID());
		final Map<ContractID, InterfaceID> interfacesInContract = new HashMap<>();
		interfacesInContract.put(new ContractID(), importedInterface.getInterfaceID());
		dman.registerImplementationsUsingImportedClass(namespaceID, usingImport, interfacesInContract);

	}

	@Test(expected = NamespaceDoesNotExistException.class)
	public void testRegisterSubClassNamespaceNotExistException() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Prepare imported interface spec
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
				new ContractID(), new MetaClassID(), new NamespaceID());

		// Add imported interface
		newNamespace.addImportedInterface(importedInterface);
		db.updateNamespaceAddImport(namespaceID, importedInterface);

		final HashSet<MetaClassID> usingImport = new HashSet<>();
		usingImport.add(new MetaClassID());
		final Map<ContractID, InterfaceID> interfacesInContract = new HashMap<>();
		interfacesInContract.put(importedInterface.getContractID(), importedInterface.getInterfaceID());
		dman.registerSubClassesUsingImportedClass(new NamespaceID(), usingImport, interfacesInContract);
	}

	@Test(expected = InterfaceNotImportedException.class)
	public void testRegisterSubClassInterfaceNotExistsException() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Prepare imported interface spec
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
				new ContractID(), new MetaClassID(), new NamespaceID());

		// Add imported interface
		newNamespace.addImportedInterface(importedInterface);
		db.updateNamespaceAddImport(namespaceID, importedInterface);

		final HashSet<MetaClassID> usingImport = new HashSet<>();
		usingImport.add(new MetaClassID());
		final Map<ContractID, InterfaceID> interfacesInContract = new HashMap<>();
		interfacesInContract.put(importedInterface.getContractID(), new InterfaceID());
		dman.registerSubClassesUsingImportedClass(namespaceID, usingImport, interfacesInContract);

	}

	@Test(expected = InterfaceNotImportedException.class)
	public void testRegisterSubClassInterfaceContractNotExistsException() {

		// Create namespace
		final Namespace newNamespace = new Namespace(NAMESPACENAME, respAccountName, Langs.LANG_JAVA);
		newNamespace.setProviderAccountID(respAccountID);
		newNamespace.setDataClayID(new NamespaceID());
		db.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Prepare imported interface spec
		final ImportedInterface importedInterface = new ImportedInterface(IMPORTED_INTERFACE_NAME, new InterfaceID(),
				new ContractID(), new MetaClassID(), new NamespaceID());

		// Add imported interface
		newNamespace.addImportedInterface(importedInterface);
		db.updateNamespaceAddImport(namespaceID, importedInterface);

		final HashSet<MetaClassID> usingImport = new HashSet<>();
		usingImport.add(new MetaClassID());
		final Map<ContractID, InterfaceID> interfacesInContract = new HashMap<>();
		interfacesInContract.put(new ContractID(), importedInterface.getInterfaceID());
		dman.registerSubClassesUsingImportedClass(namespaceID, usingImport, interfacesInContract);

	}

}
