
/**
 * @file ContractManagerTest.java
 * @date Oct 3, 2012
 * @author jmarti
 */

package es.bsc.dataclay.logic.contractmgr;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import es.bsc.dataclay.exceptions.logicmodule.contractmgr.AccountAlreadyRegisteredInContract;
import es.bsc.dataclay.exceptions.logicmodule.contractmgr.AccountNotRegisteredInContract;
import es.bsc.dataclay.exceptions.logicmodule.contractmgr.ContractNotActiveException;
import es.bsc.dataclay.exceptions.logicmodule.contractmgr.ContractNotExistException;
import es.bsc.dataclay.exceptions.logicmodule.contractmgr.ContractNotPublicException;
import es.bsc.dataclay.exceptions.logicmodule.contractmgr.InterfaceNotInContractException;
import es.bsc.dataclay.logic.contractmgr.ContractManager;
import es.bsc.dataclay.logic.contractmgr.ContractManagerDB;
import es.bsc.dataclay.logic.interfacemgr.InterfaceManagerDB;
import es.bsc.dataclay.test.AbstractManagerTest;
import es.bsc.dataclay.test.TestUtils;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.management.contractmgr.Contract;
import es.bsc.dataclay.util.management.contractmgr.InterfaceInContract;
import es.bsc.dataclay.util.management.contractmgr.OpImplementations;
import es.bsc.dataclay.util.management.interfacemgr.Interface;
import es.bsc.dataclay.util.structs.Tuple;

/**
 * This class tests the functions of Contract Manager
 * @author jmarti
 */
public final class ContractManagerTest extends AbstractManagerTest{

	private ContractManager contractMgr;
	private static String DBFILESDIRNAME = System.getProperty("user.dir") + "/dbfiles/ContractManagerTest";
	// !<@brief Path to the test databases.

	private static NamespaceID namespaceIDOfProvider = new NamespaceID();
	private static String namespaceofProvider = "Namespace";
	private static String providerAccount = "ProviderAccount";
	private static List<InterfaceInContract> interfacesInContract;
	private static Set<AccountID> accountsIDsofTheApplicants;
	private static Set<String> applicantsNames;
	private static AccountID providerAccountID = new AccountID();
	private static Map<InterfaceID, InterfaceInContract> actualInterfacesInContract;
	private static Calendar beginDate;
	private static Calendar endDate;

	private ContractManagerDB testdb; // !<@brief DbHandler used in tests.
	private InterfaceManagerDB ifaceManagerDB; // !<@brief DbHandler used in tests.

	/**
	 * @brief This method is executed before all tests. It is used to instantiate variables that are used in all the tests in order
	 *        to save code.
	 * @author jmarti
	 */
	@BeforeClass
	public static void beforeAll() throws RemoteException {
		TestUtils.createOrCleanDirectory(DBFILESDIRNAME);
		interfacesInContract = new ArrayList<>();
		beginDate = Calendar.getInstance();
		beginDate.roll(Calendar.YEAR, -1);
		endDate = Calendar.getInstance();
		endDate.roll(Calendar.YEAR, 1);
		accountsIDsofTheApplicants = new HashSet<>();
		accountsIDsofTheApplicants.add(new AccountID());
		actualInterfacesInContract = new HashMap<>();
		final String applicantName = "Applicant";
		applicantsNames = new HashSet<>();
		applicantsNames.add(applicantName);
	}

	/**
	 * @brief This method is executed before each test. It is used instantiate the Contract Manager to test.
	 * @author jmarti
	 */
	@Override
	@Before
	public void before() {
		super.before();

		final ContractManagerDB cdb = new ContractManagerDB(dataSource);
		cdb.dropTables();

		ifaceManagerDB = new InterfaceManagerDB(dataSource);
		ifaceManagerDB.dropTables();
		ifaceManagerDB.createTables();

		contractMgr = new ContractManager(dataSource);
		testdb = contractMgr.getDbHandler();

	}

	/**
	 * @brief This method is executed after each test. It is used to delete the test database since we are testing the creation of
	 *        Contract on it, it is necessary to empty the database before.
	 * @author jmarti
	 * @throws Exception
	 */
	@Override
	@After
	public void after() throws Exception {
		testdb.close();
		super.after();
		TestUtils.cleanDirectory(DBFILESDIRNAME);
		super.after();
	}

	@AfterClass
	public static void afterAll() throws Exception {
		TestUtils.deleteDirectory(DBFILESDIRNAME);
	}

	/**
	 * @brief Test method for ContractManager::newPrivateContract(AccountID, AccountID, NamespaceID, HashSet, Date, Date)
	 * @author jmarti
	 * @post The contract can be retrieved after registering it in the DB
	 * @note Currently we cannot check by example with booleans, since "false" means "whatever" (10-Oct-2012 jmarti)
	 */
	@Test
	public void testNewPrivateContract() throws Exception {
		final Contract contract = new Contract(namespaceofProvider, providerAccountID, accountsIDsofTheApplicants, interfacesInContract,
				beginDate, endDate);
		final ContractID newID = new ContractID();
		contract.setDataClayID(newID);
		contract.setNamespaceID(namespaceIDOfProvider);
		contract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		contract.setInterfacesInContract(actualInterfacesInContract);
		contract.setProviderAccountID(providerAccountID);

		contractMgr.newPrivateContract(contract);

		// Check by ID (object reference)
		final Contract curContract = testdb.getContractByID(newID);
		assertTrue(curContract != null);

		// Check the ID of the object
		assertTrue(newID.equals(curContract.getDataClayID()));

		// Check fields
		assertFalse(curContract.isPublicAvailable());
		assertTrue(curContract.getNamespaceID().equals(namespaceIDOfProvider));
		assertTrue(curContract.getInterfacesInContract().equals(actualInterfacesInContract));
		assertTrue(curContract.getApplicantsAccountsIDs().equals(accountsIDsofTheApplicants));
		assertTrue(curContract.getBeginDate().equals(beginDate));
		assertTrue(curContract.getEndDate().equals(endDate));

	}

	/**
	 * @brief Test method for ContractManager::newPublicContract(AccountID, NamespaceID, HashSet, Date, Date)
	 * @author jmarti
	 * @post The contract can be retrieved after registering it in the DB
	 */
	@Test
	public void testNewPublicContract() throws Exception {
		// Use manager

		final Contract contract = new Contract(namespaceofProvider, providerAccount,
				interfacesInContract, beginDate, endDate);
		contract.setDataClayID(new ContractID());
		contract.setNamespaceID(namespaceIDOfProvider);
		contract.setApplicantsAccountsIDs(new HashSet<AccountID>());
		contract.setInterfacesInContract(actualInterfacesInContract);
		contract.setProviderAccountID(providerAccountID);
		final ContractID newID = contractMgr.newPublicContract(contract);

		// Check by ID (object reference)Check by ID
		final Contract curContract = testdb.getContractByID(newID);
		assertTrue(curContract != null);

		// Check the ID of the object
		assertTrue(newID.equals(curContract.getDataClayID()));

		// Check fields
		assertTrue(curContract.isPublicAvailable());
		assertTrue(curContract.getNamespaceID().equals(namespaceIDOfProvider));
		assertTrue(curContract.getInterfacesInContract().equals(actualInterfacesInContract));
		assertTrue(curContract.getApplicantsAccountsIDs().size() == 0);
		assertTrue(curContract.getBeginDate().equals(beginDate));
		assertTrue(curContract.getEndDate().equals(endDate));
	}

	/**
	 * @brief Test method for ContractManager::registerToPublicContract(AccountID, ContractID)
	 */
	@Test
	public void testRegisterToPublicActiveContract() {
		final Contract contract = new Contract(namespaceofProvider, providerAccount, interfacesInContract, beginDate, endDate);
		contract.setDataClayID(new ContractID());
		contract.setNamespaceID(namespaceIDOfProvider);
		contract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		contract.setInterfacesInContract(actualInterfacesInContract);
		contract.setProviderAccountID(providerAccountID);
		testdb.store(contract);

		// Check object exists by its ID
		Contract curContract = testdb.getContractByID(contract.getDataClayID());
		assertTrue(curContract != null);

		// use the manager
		final AccountID applicantAccountID = new AccountID();
		contractMgr.registerToPublicContract(applicantAccountID, contract.getDataClayID());

		// Check by ID
		curContract = testdb.getContractByID(contract.getDataClayID());
		assertTrue(curContract != null);

		// Check the updated contract has the applicantAccount
		assertTrue(curContract.getApplicantsAccountsIDs().contains(applicantAccountID));
	}

	/**
	 * @brief Test method for ContractManager::checkNamespaceHasNoContracts(NamespaceID) when it has a contract.
	 */
	@Test
	public void testCheckNamespaceHasNoContracts() {
		final Contract contract = new Contract(namespaceofProvider, providerAccount, interfacesInContract, beginDate, endDate);
		contract.setDataClayID(new ContractID());
		contract.setNamespaceID(namespaceIDOfProvider);
		contract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		contract.setInterfacesInContract(actualInterfacesInContract);
		contract.setProviderAccountID(providerAccountID);
		testdb.store(contract);

		// Check object exists by its ID
		assertTrue(testdb.getContractByID(contract.getDataClayID()) != null);

		// Query manager
		assertFalse(contractMgr.checkNamespaceHasNoContracts(namespaceIDOfProvider));
		assertTrue(contractMgr.checkNamespaceHasNoContracts(new NamespaceID()));
	}

	/**
	 * @brief Test method for ContractManager::checkInterfaceHasNoContracts(InterfaceID) when there is a contract associated with
	 *        the interface
	 */
	@Test
	public void testCheckInterfaceHasNoContracts() {
		final InterfaceID ifaceID = new InterfaceID();
		final InterfaceInContract interfaceInContract = new InterfaceInContract();
		interfaceInContract.setInterfaceID(ifaceID);

		interfaceInContract.setIface(new Interface(providerAccount, namespaceofProvider,
				namespaceofProvider, "class", new HashSet<String>(), new HashSet<String>()));
		interfaceInContract.getIface().setNamespaceID(namespaceIDOfProvider);
		interfaceInContract.getIface().setClassNamespaceID(namespaceIDOfProvider);
		interfaceInContract.getIface().setMetaClassID(new MetaClassID());
		interfaceInContract.getIface().setProviderAccountID(providerAccountID);
		interfaceInContract.setAccessibleImplementations(new HashMap<OperationID, OpImplementations>());
		interfaceInContract.getIface().setDataClayID(ifaceID);
		ifaceManagerDB.store(interfaceInContract.getIface());

		final List<InterfaceInContract> newIfacesInContract = new ArrayList<>();
		newIfacesInContract.add(interfaceInContract);
		final Hashtable<InterfaceID, InterfaceInContract> ifacesInContract = new Hashtable<>();
		ifacesInContract.put(ifaceID, interfaceInContract);
		final Contract contract = new Contract(namespaceofProvider, providerAccount, newIfacesInContract, beginDate, endDate);
		contract.setNamespaceID(namespaceIDOfProvider);
		contract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		contract.setInterfacesInContract(ifacesInContract);
		contract.setProviderAccountID(providerAccountID);
		contract.setDataClayID(new ContractID());

		testdb.store(contract);

		// Check object exists by its ID
		assertTrue(testdb.getContractByID(contract.getDataClayID()) != null);

		// Query manager
		assertFalse(contractMgr.checkInterfaceHasNoContracts(ifaceID));
		assertTrue(contractMgr.checkInterfaceHasNoContracts(new InterfaceID()));
	}

	/**
	 * @brief Test method for ContractManager::checkImplementationHasNoContracts(ImplementationID) when there is a contract
	 *        associated with implementation.
	 */
	@Test
	public void testCheckImplementationHasNoContracts() {
		// Generate an ImplementationID
		final OperationID opID = new OperationID();
		final ImplementationID localImplID = new ImplementationID();
		final ImplementationID remoteImplID = new ImplementationID();
		final OpImplementations opImpls = new OpImplementations("opSignature", 0, 0);
		opImpls.setLocalImplementationID(localImplID);
		opImpls.setRemoteImplementationID(remoteImplID);
		final Hashtable<OperationID, OpImplementations> accessibleImplementations = new Hashtable<>();
		accessibleImplementations.put(opID, opImpls);

		// Add it to the accessible implementations for the interface in contract
		final InterfaceInContract interfaceInContract = new InterfaceInContract();
		interfaceInContract.setInterfaceID(new InterfaceID());
		interfaceInContract.setIface(new Interface(providerAccount, namespaceofProvider,
				namespaceofProvider, "class", new HashSet<String>(), new HashSet<String>()));
		interfaceInContract.getIface().setNamespaceID(namespaceIDOfProvider);
		interfaceInContract.getIface().setClassNamespaceID(namespaceIDOfProvider);
		interfaceInContract.getIface().setMetaClassID(new MetaClassID());
		interfaceInContract.getIface().setProviderAccountID(providerAccountID);
		interfaceInContract.getIface().setDataClayID(interfaceInContract.getInterfaceID());
		ifaceManagerDB.store(interfaceInContract.getIface());

		interfaceInContract.setAccessibleImplementations(accessibleImplementations);

		// Add the interface in contract
		final List<InterfaceInContract> newIfacesInContract = new ArrayList<>();
		newIfacesInContract.add(interfaceInContract);
		final Hashtable<InterfaceID, InterfaceInContract> ifacesInContract = new Hashtable<>();
		ifacesInContract.put(interfaceInContract.getInterfaceID(), interfaceInContract);

		// Create a new contract
		final Contract contract = new Contract(namespaceofProvider, providerAccount, newIfacesInContract, beginDate, endDate);
		contract.setNamespaceID(namespaceIDOfProvider);
		contract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		contract.setInterfacesInContract(ifacesInContract);
		contract.setProviderAccountID(providerAccountID);
		contract.setDataClayID(new ContractID());
		testdb.store(contract);

		// Check object exists by its ID
		assertTrue(testdb.getContractByID(contract.getDataClayID()) != null);

		// Query manager
		assertFalse(contractMgr.checkImplementationHasNoContracts(localImplID));
		assertFalse(contractMgr.checkImplementationHasNoContracts(remoteImplID));
		assertTrue(contractMgr.checkImplementationHasNoContracts(new ImplementationID()));
	}

	@Test
	public void testCheckInterfacesInActiveContractsForAccount() {

		final InterfaceID ifaceID = new InterfaceID();
		final InterfaceInContract interfaceInContract = new InterfaceInContract();
		interfaceInContract.setIface(new Interface(providerAccount, namespaceofProvider,
				namespaceofProvider, "class", new HashSet<String>(), new HashSet<String>()));
		interfaceInContract.setInterfaceID(ifaceID);
		interfaceInContract.getIface().setNamespaceID(namespaceIDOfProvider);
		interfaceInContract.getIface().setClassNamespaceID(namespaceIDOfProvider);
		interfaceInContract.getIface().setMetaClassID(new MetaClassID());
		interfaceInContract.getIface().setProviderAccountID(providerAccountID);
		interfaceInContract.getIface().setDataClayID(interfaceInContract.getInterfaceID());
		ifaceManagerDB.store(interfaceInContract.getIface());

		interfaceInContract.setAccessibleImplementations(new HashMap<OperationID, OpImplementations>());

		// Add the interface in contract
		final List<InterfaceInContract> newIfacesInContract = new ArrayList<>();
		newIfacesInContract.add(interfaceInContract);
		final Hashtable<InterfaceID, InterfaceInContract> ifacesInContract = new Hashtable<>();
		ifacesInContract.put(interfaceInContract.getInterfaceID(), interfaceInContract);

		// Create a new contract
		final Contract contract = new Contract(namespaceofProvider, providerAccount, newIfacesInContract, beginDate, endDate);
		contract.setNamespaceID(namespaceIDOfProvider);
		contract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		contract.setInterfacesInContract(ifacesInContract);
		contract.setProviderAccountID(providerAccountID);
		contract.setDataClayID(new ContractID());
		contract.setPublicAvailable(false);

		testdb.store(contract);

		// Check object exists by its ID
		assertTrue(testdb.getContractByID(contract.getDataClayID()) != null);

		// Query manager
		final Hashtable<ContractID, InterfaceID> interfacesInContractTable = new Hashtable<>();
		interfacesInContractTable.put(contract.getDataClayID(), ifaceID);
		assertTrue(contractMgr.checkInterfacesInActiveContractsForAccount(accountsIDsofTheApplicants.iterator().next(),
				interfacesInContractTable));
	}

	@Test
	public void testCheckInterfacesInActiveContractsForNotValidAccount() {
		final InterfaceID ifaceID = new InterfaceID();
		final InterfaceInContract interfaceInContract = new InterfaceInContract();
		interfaceInContract.setIface(new Interface(providerAccount, namespaceofProvider,
				namespaceofProvider, "class", new HashSet<String>(), new HashSet<String>()));
		interfaceInContract.getIface().setProviderAccountID(providerAccountID);
		interfaceInContract.setInterfaceID(ifaceID);
		interfaceInContract.getIface().setNamespaceID(namespaceIDOfProvider);
		interfaceInContract.getIface().setClassNamespaceID(namespaceIDOfProvider);
		interfaceInContract.getIface().setMetaClassID(new MetaClassID());
		interfaceInContract.getIface().setDataClayID(interfaceInContract.getInterfaceID());
		interfaceInContract.setAccessibleImplementations(new HashMap<OperationID, OpImplementations>());
		ifaceManagerDB.store(interfaceInContract.getIface());

		// Add the interface in contract
		final List<InterfaceInContract> newIfacesInContract = new ArrayList<>();
		newIfacesInContract.add(interfaceInContract);
		final Hashtable<InterfaceID, InterfaceInContract> ifacesInContract = new Hashtable<>();
		ifacesInContract.put(interfaceInContract.getInterfaceID(), interfaceInContract);

		// Create a new contract
		final Contract contract = new Contract(namespaceofProvider, providerAccount, newIfacesInContract, beginDate, endDate);
		contract.setNamespaceID(namespaceIDOfProvider);
		contract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		contract.setInterfacesInContract(ifacesInContract);
		contract.setProviderAccountID(providerAccountID);
		contract.setPublicAvailable(false);
		contract.setDataClayID(new ContractID());

		testdb.store(contract);

		// Check object exists by its ID
		assertTrue(testdb.getContractByID(contract.getDataClayID()) != null);

		// Query manager
		final Hashtable<ContractID, InterfaceID> interfacesInContractTable = new Hashtable<>();
		interfacesInContractTable.put(contract.getDataClayID(), ifacesInContract.keySet().iterator().next());
		assertFalse(contractMgr.checkInterfacesInActiveContractsForAccount(new AccountID(), interfacesInContractTable));
	}

	@Test
	public void testCheckInterfacesInNonActiveContractsForAccount() {
		final InterfaceID ifaceID = new InterfaceID();
		final InterfaceInContract interfaceInContract = new InterfaceInContract();
		interfaceInContract.setInterfaceID(ifaceID);
		interfaceInContract.setIface(new Interface(providerAccount, namespaceofProvider, namespaceofProvider,
				"class", new HashSet<String>(), new HashSet<String>()));
		interfaceInContract.getIface().setProviderAccountID(providerAccountID);
		interfaceInContract.getIface().setNamespaceID(namespaceIDOfProvider);
		interfaceInContract.getIface().setClassNamespaceID(namespaceIDOfProvider);
		interfaceInContract.getIface().setMetaClassID(new MetaClassID());
		interfaceInContract.getIface().setDataClayID(interfaceInContract.getInterfaceID());
		interfaceInContract.setAccessibleImplementations(new HashMap<OperationID, OpImplementations>());
		ifaceManagerDB.store(interfaceInContract.getIface());

		// Add the interface in contract
		final List<InterfaceInContract> newIfacesInContract = new ArrayList<>();
		newIfacesInContract.add(interfaceInContract);
		final Hashtable<InterfaceID, InterfaceInContract> ifacesInContract = new Hashtable<>();
		ifacesInContract.put(interfaceInContract.getInterfaceID(), interfaceInContract);

		// Create a new contract
		final Contract contract = new Contract(namespaceofProvider, providerAccount, newIfacesInContract, beginDate, endDate);
		contract.setNamespaceID(namespaceIDOfProvider);
		contract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		contract.setInterfacesInContract(ifacesInContract);
		contract.setProviderAccountID(providerAccountID);
		contract.setPublicAvailable(false);
		contract.setDataClayID(new ContractID());

		testdb.store(contract);

		// Check object exists by its ID
		assertTrue(testdb.getContractByID(contract.getDataClayID()) != null);

		// Query manager
		final Hashtable<ContractID, InterfaceID> interfacesInContractTable = new Hashtable<>();
		interfacesInContractTable.put(contract.getDataClayID(), ifacesInContract.keySet().iterator().next());
		assertFalse(contractMgr.checkInterfacesInActiveContractsForAccount(new AccountID(), interfacesInContractTable));
	}

	@Test
	public void testCheckNotValidInterfacesInActiveContractsForAccount() {

		// Create a new contract
		final Contract contract = new Contract(namespaceofProvider, providerAccount, interfacesInContract, beginDate, endDate);
		contract.setNamespaceID(namespaceIDOfProvider);
		contract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		contract.setInterfacesInContract(actualInterfacesInContract);
		contract.setProviderAccountID(providerAccountID);
		contract.setPublicAvailable(false);
		contract.setDataClayID(new ContractID());
		testdb.store(contract);

		// Check object exists by its ID
		assertTrue(testdb.getContractByID(contract.getDataClayID()) != null);

		// Query manager
		final Hashtable<ContractID, InterfaceID> interfacesInContractTable = new Hashtable<>();
		interfacesInContractTable.put(contract.getDataClayID(), new InterfaceID());
		assertFalse(contractMgr.checkInterfacesInActiveContractsForAccount(new AccountID(), interfacesInContractTable));
	}

	/**
	 * @brief Test method for {@link ContractManager#getContractIDsOfProvider(NamespaceID)}
	 */
	@Test
	public void testGetContractIDsOfProvider() {
		// Create a new contract
		final Contract contract = new Contract(namespaceofProvider, providerAccount, interfacesInContract, beginDate, endDate);
		contract.setNamespaceID(namespaceIDOfProvider);
		contract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		contract.setInterfacesInContract(actualInterfacesInContract);
		contract.setProviderAccountID(providerAccountID);
		contract.setDataClayID(new ContractID());

		testdb.store(contract);

		// Check object exists by its ID
		assertTrue(testdb.getContractByID(contract.getDataClayID()) != null);

		// Query manager
		Map<ContractID, Contract> curContracts = contractMgr.getContractIDsOfProvider(namespaceIDOfProvider);
		assertTrue(curContracts.size() == 1 && curContracts.containsKey(contract.getDataClayID()));
		curContracts = contractMgr.getContractIDsOfProvider(new NamespaceID());
		assertTrue(curContracts.size() == 0);
	}

	/**
	 * @brief Test method for {@link ContractManager#getContractIDsOfProvider(NamespaceID)}
	 */
	@Test
	public void testGetContractIDsOfApplicant() {
		// Create a new contract
		final Contract contract = new Contract(namespaceofProvider, providerAccount, interfacesInContract, beginDate, endDate);
		contract.setNamespaceID(namespaceIDOfProvider);
		contract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		contract.setInterfacesInContract(actualInterfacesInContract);
		contract.setProviderAccountID(providerAccountID);
		contract.setDataClayID(new ContractID());

		testdb.store(contract);

		// Check object exists by its ID
		assertTrue(testdb.getContractByID(contract.getDataClayID()) != null);

		// Query manager
		Map<ContractID, Contract> curContracts = contractMgr.getContractIDsOfApplicant(accountsIDsofTheApplicants.iterator().next());
		assertTrue(curContracts.size() == 1 && curContracts.containsKey(contract.getDataClayID()));
		curContracts = contractMgr.getContractIDsOfApplicant(new AccountID());
		assertTrue(curContracts.size() == 0);
	}

	/**
	 * @brief Test method for {@link ContractManager#getContractIDsOfProvider(NamespaceID)}
	 */
	@Test
	public void testGetContractIDsOfApplicantWithProvider() {
		final AccountID applicantAccountID = accountsIDsofTheApplicants.iterator().next();
		final Contract contract = new Contract(namespaceofProvider, providerAccount, interfacesInContract, beginDate, endDate);
		contract.setNamespaceID(namespaceIDOfProvider);
		contract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		contract.setInterfacesInContract(actualInterfacesInContract);
		contract.setProviderAccountID(providerAccountID);
		contract.setDataClayID(new ContractID());

		testdb.store(contract);

		// Check object exists by its ID
		assertTrue(testdb.getContractByID(contract.getDataClayID()) != null);

		// Query manager
		Map<ContractID, Contract> curContracts = contractMgr.getContractIDsOfApplicantWithProvider(
				applicantAccountID, namespaceIDOfProvider);
		assertTrue(curContracts.size() == 1 && curContracts.containsKey(contract.getDataClayID()));
		curContracts = contractMgr.getContractIDsOfApplicantWithProvider(new AccountID(), namespaceIDOfProvider);
		assertTrue(curContracts.size() == 0);
		curContracts = contractMgr.getContractIDsOfApplicantWithProvider(applicantAccountID, new NamespaceID());
		assertTrue(curContracts.size() == 0);
	}

	/**
	 * @brief Test method for {@link ContractManager#getContract(ContractID)
	 * 
	 */
	@Test
	public void testGetContract() {
		final InterfaceID ifaceID = new InterfaceID();
		final InterfaceInContract interfaceInContract = new InterfaceInContract();
		interfaceInContract.setInterfaceID(ifaceID);
		interfaceInContract.setAccessibleImplementations(new HashMap<OperationID, OpImplementations>());
		interfaceInContract.setIface(new Interface(providerAccount, namespaceofProvider, namespaceofProvider,
				"class", new HashSet<String>(), new HashSet<String>()));
		interfaceInContract.getIface().setProviderAccountID(providerAccountID);
		interfaceInContract.getIface().setNamespaceID(namespaceIDOfProvider);
		interfaceInContract.getIface().setClassNamespaceID(namespaceIDOfProvider);
		interfaceInContract.getIface().setMetaClassID(new MetaClassID());
		interfaceInContract.getIface().setDataClayID(interfaceInContract.getInterfaceID());
		ifaceManagerDB.store(interfaceInContract.getIface());
		final List<InterfaceInContract> newIfacesInContract = new ArrayList<>();
		newIfacesInContract.add(interfaceInContract);
		final Hashtable<InterfaceID, InterfaceInContract> ifacesInContract = new Hashtable<>();
		ifacesInContract.put(interfaceInContract.getInterfaceID(), interfaceInContract);

		// Create a new contract
		final Contract contract = new Contract(namespaceofProvider, providerAccount, newIfacesInContract, beginDate, endDate);
		contract.setNamespaceID(namespaceIDOfProvider);
		contract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		contract.setInterfacesInContract(ifacesInContract);
		contract.setProviderAccountID(providerAccountID);

		contract.setDataClayID(new ContractID());
		testdb.store(contract);

		// Check object exists by its ID
		assertTrue(testdb.getContractByID(contract.getDataClayID()) != null);

		// Query manager
		final Contract contractInfo = contractMgr.getContractInfo(contract.getDataClayID());
		assertTrue(contractInfo != null);
		assertTrue(contractInfo.getInterfacesInContract().size() == 1
				&& contractInfo.getInterfacesInContract().containsValue(interfaceInContract));
	}

	/**
	 * @brief Test method for {@link ContractManager#getInfoOfActiveContractForAccount(ContractID, AccountID)
	 * 
	 */
	@Test
	public void testGetInfoOfActiveContractForAccount() {
		final AccountID accountID = accountsIDsofTheApplicants.iterator().next();
		final InterfaceID ifaceID = new InterfaceID();
		final InterfaceInContract interfaceInContract = new InterfaceInContract();
		interfaceInContract.setInterfaceID(ifaceID);
		interfaceInContract.setAccessibleImplementations(new HashMap<OperationID, OpImplementations>());
		interfaceInContract.setIface(new Interface(providerAccount, namespaceofProvider, namespaceofProvider,
				"class", new HashSet<String>(), new HashSet<String>()));
		interfaceInContract.getIface().setProviderAccountID(providerAccountID);
		interfaceInContract.getIface().setNamespaceID(namespaceIDOfProvider);
		interfaceInContract.getIface().setClassNamespaceID(namespaceIDOfProvider);
		interfaceInContract.getIface().setMetaClassID(new MetaClassID());
		interfaceInContract.getIface().setDataClayID(interfaceInContract.getInterfaceID());
		ifaceManagerDB.store(interfaceInContract.getIface());

		final List<InterfaceInContract> newIfacesInContract = new ArrayList<>();
		newIfacesInContract.add(interfaceInContract);
		final Hashtable<InterfaceID, InterfaceInContract> ifacesInContract = new Hashtable<>();
		ifacesInContract.put(interfaceInContract.getInterfaceID(), interfaceInContract);

		// Create a new contract
		final Contract contract = new Contract(namespaceofProvider, providerAccount, newIfacesInContract, beginDate, endDate);
		contract.setNamespaceID(namespaceIDOfProvider);
		contract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		contract.setInterfacesInContract(ifacesInContract);
		contract.setProviderAccountID(providerAccountID);
		contract.setDataClayID(new ContractID());

		testdb.store(contract);

		// Check object exists by its ID
		assertTrue(testdb.getContractByID(contract.getDataClayID()) != null);

		// Query manager
		final LinkedList<ContractID> contractsIDs = new LinkedList<>();
		contractsIDs.add(contract.getDataClayID());
		final LinkedHashMap<ContractID, Contract> contractsInfo = contractMgr.getInfoOfSomeActiveContractsForAccount(
				contractsIDs, accountID);
		assertTrue(contractsInfo != null);
		assertTrue(contractsInfo.size() == 1);
		final Map<InterfaceID, InterfaceInContract> curInterfacesInContract = contractsInfo.get(contract.getDataClayID()).getInterfacesInContract();
		assertTrue(curInterfacesInContract.size() == 1 &&
				curInterfacesInContract.containsValue(interfaceInContract));
	}

	@Test
	public void testGetInfoOfInerfacesInActiveContractsForAccount() {
		final Set<String> operations = new HashSet<>();
		operations.add("op1");
		final HashSet<OperationID> operationsIDs = new HashSet<>();
		final OperationID operationID = new OperationID();
		operationsIDs.add(operationID);
		final HashSet<PropertyID> propertiesIDs = new HashSet<>();
		final HashSet<String> properties = new HashSet<>();
		final Interface interfaceForContract = new Interface(providerAccount, namespaceofProvider, namespaceofProvider, "testclass",
				operations, properties);
		interfaceForContract.setNamespaceID(namespaceIDOfProvider);
		interfaceForContract.setClassNamespaceID(namespaceIDOfProvider);
		interfaceForContract.setMetaClassID(new MetaClassID());
		interfaceForContract.setProviderAccountID(providerAccountID);
		interfaceForContract.setOperationsIDs(operationsIDs);
		interfaceForContract.setPropertiesIDs(propertiesIDs);
		interfaceForContract.setDataClayID(new InterfaceID());
		ifaceManagerDB.store(interfaceForContract);
		final InterfaceID ifaceID = interfaceForContract.getDataClayID();

		final Set<OpImplementations> opImpls = new HashSet<>();

		final ImplementationID localImplID = new ImplementationID();
		final ImplementationID remoteImplID = new ImplementationID();
		final OpImplementations implsForOps = new OpImplementations("op1", 0, 0);
		implsForOps.setLocalImplementationID(localImplID);
		implsForOps.setRemoteImplementationID(remoteImplID);
		opImpls.add(implsForOps);
		final Map<OperationID, OpImplementations> finalOpImpls = new HashMap<>();
		finalOpImpls.put(operationID, implsForOps);
		final InterfaceInContract interfaceInContract = new InterfaceInContract(interfaceForContract, opImpls);
		interfaceInContract.setAccessibleImplementations(finalOpImpls);
		interfaceInContract.setInterfaceID(ifaceID);

		final List<InterfaceInContract> newIfacesInContract = new ArrayList<>();
		newIfacesInContract.add(interfaceInContract);
		final Hashtable<InterfaceID, InterfaceInContract> ifacesInContract = new Hashtable<>();
		ifacesInContract.put(interfaceInContract.getInterfaceID(), interfaceInContract);

		final Contract contract = new Contract(namespaceofProvider, providerAccountID, accountsIDsofTheApplicants, newIfacesInContract, beginDate, endDate);
		contract.setNamespaceID(namespaceIDOfProvider);
		contract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		contract.setInterfacesInContract(ifacesInContract);
		contract.setProviderAccountID(providerAccountID);
		contract.setDataClayID(new ContractID());

		final AccountID accountID = accountsIDsofTheApplicants.iterator().next();

		testdb.store(contract);

		// Check object exists by its ID
		assertTrue(testdb.getContractByID(contract.getDataClayID()) != null);

		// Check operation
		final LinkedList<ContractID> contractsIDs = new LinkedList<>();
		contractsIDs.add(contract.getDataClayID());
		final LinkedHashMap<ContractID, Contract> result = contractMgr.getInfoOfSomeActiveContractsForAccount(contractsIDs,
				accountID);

		assertTrue(result.size() == 1);
		final Contract contractInfo = result.get(contract.getDataClayID());
		assertTrue(contractInfo.getApplicantsAccountsIDs().contains(accountID));
		assertTrue(contractInfo.getBeginDate().equals(beginDate));
		assertTrue(contractInfo.getEndDate().equals(endDate));
		assertFalse(contractInfo.isPublicAvailable());

		final Map<InterfaceID, InterfaceInContract> resIfacesInContract = contractInfo.getInterfacesInContract();
		assertTrue(resIfacesInContract.size() == 1);
		final InterfaceInContract ifaceInContract = resIfacesInContract.get(ifaceID);
		ifaceInContract.getInterfaceID().equals(ifaceID);

		final Map<OperationID, OpImplementations> resOpImpls = ifaceInContract.getAccessibleImplementations();
		assertTrue(resOpImpls.size() == 1);
		final OpImplementations resImplsForOps = resOpImpls.get(operationID);
		assertTrue(resImplsForOps.getLocalImplementationID().equals(localImplID));
		assertTrue(resImplsForOps.getRemoteImplementationID().equals(remoteImplID));
	}

	@Test
	public void testGetInfoOfMultipleInterfacesPerActiveContractsForAccount() {
		final Set<String> operations = new HashSet<>();
		operations.add("op1");
		final HashSet<OperationID> operationsIDs = new HashSet<>();
		final OperationID operationID = new OperationID();
		operationsIDs.add(operationID);
		final HashSet<PropertyID> propertiesIDs = new HashSet<>();
		final HashSet<String> properties = new HashSet<>();
		final Interface interfaceForContract = new Interface(providerAccount, namespaceofProvider, namespaceofProvider, "testclass",
				operations, properties);
		interfaceForContract.setNamespaceID(namespaceIDOfProvider);
		interfaceForContract.setClassNamespaceID(namespaceIDOfProvider);
		interfaceForContract.setMetaClassID(new MetaClassID());
		interfaceForContract.setProviderAccountID(providerAccountID);
		interfaceForContract.setOperationsIDs(operationsIDs);
		interfaceForContract.setPropertiesIDs(propertiesIDs);
		interfaceForContract.setDataClayID(new InterfaceID());
		ifaceManagerDB.store(interfaceForContract);
		final InterfaceID ifaceID = interfaceForContract.getDataClayID();

		final Set<OpImplementations> opImpls = new HashSet<>();

		final ImplementationID localImplID = new ImplementationID();
		final ImplementationID remoteImplID = new ImplementationID();
		final OpImplementations implsForOps = new OpImplementations("op1", 0, 0);
		implsForOps.setLocalImplementationID(localImplID);
		implsForOps.setRemoteImplementationID(remoteImplID);
		opImpls.add(implsForOps);
		final Map<OperationID, OpImplementations> finalOpImpls = new HashMap<>();
		finalOpImpls.put(operationID, implsForOps);
		final InterfaceInContract interfaceInContract = new InterfaceInContract(interfaceForContract, opImpls);
		interfaceInContract.setAccessibleImplementations(finalOpImpls);
		interfaceInContract.setInterfaceID(ifaceID);

		final List<InterfaceInContract> newIfacesInContract = new ArrayList<>();
		newIfacesInContract.add(interfaceInContract);
		final Hashtable<InterfaceID, InterfaceInContract> ifacesInContract = new Hashtable<>();
		ifacesInContract.put(interfaceInContract.getInterfaceID(), interfaceInContract);

		final Contract contract = new Contract(namespaceofProvider, providerAccountID, accountsIDsofTheApplicants, newIfacesInContract, beginDate, endDate);
		contract.setNamespaceID(namespaceIDOfProvider);
		contract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		contract.setInterfacesInContract(ifacesInContract);
		contract.setProviderAccountID(providerAccountID);
		contract.setDataClayID(new ContractID());

		final AccountID accountID = accountsIDsofTheApplicants.iterator().next();

		testdb.store(contract);

		// Check object exists by its ID
		assertTrue(testdb.getContractByID(contract.getDataClayID()) != null);

		// Check operation
		final HashSet<InterfaceID> ifacesIDs = new HashSet<>();
		ifacesIDs.add(ifaceID);
		final Hashtable<ContractID, HashSet<InterfaceID>> interfacesInContracts = new Hashtable<>();
		interfacesInContracts.put(contract.getDataClayID(), ifacesIDs);
		final Map<ContractID, Tuple<Map<InterfaceID, InterfaceInContract>, Calendar>> result = contractMgr
				.getInfoOfMultipleInterfacesPerActiveContractsForAccount(accountID, interfacesInContracts);

		assertTrue(result.size() == 1);
		final Tuple<Map<InterfaceID, InterfaceInContract>, Calendar> contractInfo = result.get(contract.getDataClayID());
		assertTrue(contractInfo.getSecond().equals(endDate));

		final Map<InterfaceID, InterfaceInContract> resIfacesInContract = contractInfo.getFirst();
		assertTrue(resIfacesInContract.size() == 1);
		final InterfaceInContract ifaceInContract = resIfacesInContract.get(ifaceID);
		ifaceInContract.getInterfaceID().equals(ifaceID);

		final Map<OperationID, OpImplementations> resOpImpls = ifaceInContract.getAccessibleImplementations();
		assertTrue(resOpImpls.size() == 1);
		final OpImplementations resImplsForOps = resOpImpls.get(operationID);
		assertTrue(resImplsForOps.getLocalImplementationID().equals(localImplID));
		assertTrue(resImplsForOps.getRemoteImplementationID().equals(remoteImplID));
	}

	@Test
	public void testGetInfoOfMultipleContractsPerActiveContractsForAccount() throws RemoteException {
		final Set<String> operations = new HashSet<>();
		operations.add("op1");
		final HashSet<OperationID> operationsIDs = new HashSet<>();
		final OperationID operationID = new OperationID();
		operationsIDs.add(operationID);
		final HashSet<PropertyID> propertiesIDs = new HashSet<>();
		final HashSet<String> properties = new HashSet<>();
		final Interface interfaceForContract = new Interface(providerAccount, namespaceofProvider, namespaceofProvider, "testclass",
				operations, properties);
		interfaceForContract.setNamespaceID(namespaceIDOfProvider);
		interfaceForContract.setClassNamespaceID(namespaceIDOfProvider);
		interfaceForContract.setMetaClassID(new MetaClassID());
		interfaceForContract.setProviderAccountID(providerAccountID);
		interfaceForContract.setOperationsIDs(operationsIDs);
		interfaceForContract.setPropertiesIDs(propertiesIDs);
		interfaceForContract.setDataClayID(new InterfaceID());
		ifaceManagerDB.store(interfaceForContract);

		final InterfaceID ifaceID = interfaceForContract.getDataClayID();

		final Set<OpImplementations> opImpls = new HashSet<>();

		final ImplementationID localImplID = new ImplementationID();
		final ImplementationID remoteImplID = new ImplementationID();
		final OpImplementations implsForOps = new OpImplementations("op1", 0, 0);
		implsForOps.setLocalImplementationID(localImplID);
		implsForOps.setRemoteImplementationID(remoteImplID);
		opImpls.add(implsForOps);
		final Map<OperationID, OpImplementations> finalOpImpls = new HashMap<>();
		finalOpImpls.put(operationID, implsForOps);
		final InterfaceInContract interfaceInContract = new InterfaceInContract(interfaceForContract, opImpls);
		interfaceInContract.setAccessibleImplementations(finalOpImpls);
		interfaceInContract.setInterfaceID(ifaceID);

		final List<InterfaceInContract> newIfacesInContract = new ArrayList<>();
		newIfacesInContract.add(interfaceInContract);
		final Hashtable<InterfaceID, InterfaceInContract> ifacesInContract = new Hashtable<>();
		ifacesInContract.put(interfaceInContract.getInterfaceID(), interfaceInContract);

		final Contract contract = new Contract(namespaceofProvider, providerAccountID, accountsIDsofTheApplicants, newIfacesInContract, beginDate, endDate);
		contract.setNamespaceID(namespaceIDOfProvider);
		contract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		contract.setInterfacesInContract(ifacesInContract);
		contract.setProviderAccountID(providerAccountID);
		contract.setDataClayID(new ContractID());

		final AccountID accountID = accountsIDsofTheApplicants.iterator().next();

		testdb.store(contract);

		// Check object exists by its ID
		assertTrue(testdb.getContractByID(contract.getDataClayID()) != null);

		// Check operation
		final HashSet<InterfaceID> ifacesIDs = new HashSet<>();
		ifacesIDs.add(ifaceID);
		final HashSet<ContractID> contracts = new HashSet<>();
		contracts.add(contract.getDataClayID());
		final Map<ContractID, Tuple<Map<InterfaceID, InterfaceInContract>, Calendar>> result = contractMgr
				.getInfoOfMultipleContractsPerActiveContractsForAccount(accountID, contracts);

		assertTrue(result.size() == 1);
		final Tuple<Map<InterfaceID, InterfaceInContract>, Calendar> contractInfo = result.get(contract.getDataClayID());
		assertTrue(contractInfo.getSecond().equals(endDate));

		final Map<InterfaceID, InterfaceInContract> resIfacesInContract = contractInfo.getFirst();
		assertTrue(resIfacesInContract.size() == 1);
		final InterfaceInContract ifaceInContract = resIfacesInContract.get(ifaceID);
		ifaceInContract.getInterfaceID().equals(ifaceID);

		final Map<OperationID, OpImplementations> resOpImpls = ifaceInContract.getAccessibleImplementations();
		assertTrue(resOpImpls.size() == 1);
		final OpImplementations resImplsForOps = resOpImpls.get(operationID);
		assertTrue(resImplsForOps.getLocalImplementationID().equals(localImplID));
		assertTrue(resImplsForOps.getRemoteImplementationID().equals(remoteImplID));
	}

	// =========== EXCEPTIONS ============ //

	@Test(expected = ContractNotExistException.class)
	public void testContractNotExist() {
		// use the manager
		final AccountID applicantAccountID = new AccountID();
		contractMgr.registerToPublicContract(applicantAccountID, new ContractID());
	}

	@Test(expected = AccountAlreadyRegisteredInContract.class)
	public void testAccountAlreadyRegisteredInContract() {
		final Contract contract = new Contract(namespaceofProvider, providerAccount, interfacesInContract, beginDate, endDate);
		contract.setNamespaceID(namespaceIDOfProvider);
		contract.setInterfacesInContract(actualInterfacesInContract);
		contract.setProviderAccountID(providerAccountID);
		contract.setDataClayID(new ContractID());

		testdb.store(contract);

		// Check object exists by its ID
		final ContractID contractID = contract.getDataClayID();
		final Contract curContract = testdb.getContractByID(contractID);
		assertTrue(curContract != null);

		// use the manager
		final AccountID applicantAccountID = new AccountID();
		contractMgr.registerToPublicContract(applicantAccountID, contractID);
		contractMgr.registerToPublicContract(applicantAccountID, contractID);
	}

	@Test(expected = AccountNotRegisteredInContract.class)
	public void testAccountNotRegistered() {
		final Contract contract = new Contract(namespaceofProvider, providerAccountID, accountsIDsofTheApplicants, interfacesInContract, beginDate, endDate);
		contract.setNamespaceID(namespaceIDOfProvider);
		contract.setInterfacesInContract(actualInterfacesInContract);
		contract.setProviderAccountID(providerAccountID);
		contract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		contract.setDataClayID(new ContractID());

		testdb.store(contract);

		// Check object exists by its ID
		final ContractID contractID = contract.getDataClayID();
		final Contract curContract = testdb.getContractByID(contractID);
		assertTrue(curContract != null);

		// use the manager
		final AccountID applicantAccountID = new AccountID();
		final LinkedList<ContractID> contractsIDs = new LinkedList<>();
		contractsIDs.add(contractID);
		contractMgr.getInfoOfSomeActiveContractsForAccount(contractsIDs, applicantAccountID);
	}

	@Test(expected = ContractNotActiveException.class)
	public void testContractNotActive() {
		final Calendar pastEndDate = Calendar.getInstance();
		pastEndDate.roll(Calendar.MONTH, -1);
		pastEndDate.roll(Calendar.YEAR, -1);
		final AccountID applicantAccountID = accountsIDsofTheApplicants.iterator().next();
		final Contract contract = new Contract(namespaceofProvider, providerAccountID, accountsIDsofTheApplicants,
				interfacesInContract, beginDate, pastEndDate);
		contract.setNamespaceID(namespaceIDOfProvider);
		contract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		contract.setInterfacesInContract(actualInterfacesInContract);
		contract.setProviderAccountID(providerAccountID);
		contract.setDataClayID(new ContractID());
		testdb.store(contract);

		// Check object exists by its ID
		final ContractID contractID = contract.getDataClayID();
		final Contract curContract = testdb.getContractByID(contractID);
		assertTrue(curContract != null);

		// use the manager
		final LinkedList<ContractID> contractsIDs = new LinkedList<>();
		contractsIDs.add(contractID);
		contractMgr.getInfoOfSomeActiveContractsForAccount(contractsIDs, applicantAccountID);
	}

	@Test(expected = ContractNotPublicException.class)
	public void testContractNotPublic() {
		final Calendar pastEndDate = Calendar.getInstance();
		pastEndDate.roll(Calendar.MONTH, -1);
		final Contract contract = new Contract(namespaceofProvider, providerAccountID, accountsIDsofTheApplicants,
				interfacesInContract, beginDate, endDate);
		contract.setNamespaceID(namespaceIDOfProvider);
		contract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		contract.setInterfacesInContract(actualInterfacesInContract);
		contract.setProviderAccountID(providerAccountID);
		contract.setDataClayID(new ContractID());

		testdb.store(contract);

		// Check object exists by its ID
		final ContractID contractID = contract.getDataClayID();
		final Contract curContract = testdb.getContractByID(contractID);
		assertTrue(curContract != null);

		// use the manager
		contractMgr.registerToPublicContract(new AccountID(), contractID);
	}

	@Test(expected = InterfaceNotInContractException.class)
	public void testInterfaceNotInContract() {
		final AccountID applicantAccountID = accountsIDsofTheApplicants.iterator().next();
		final Contract contract = new Contract(namespaceofProvider, providerAccountID, accountsIDsofTheApplicants,
				interfacesInContract, beginDate, endDate);
		contract.setNamespaceID(namespaceIDOfProvider);
		contract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		contract.setInterfacesInContract(actualInterfacesInContract);
		contract.setProviderAccountID(providerAccountID);
		contract.setDataClayID(new ContractID());

		testdb.store(contract);

		// Check object exists by its ID
		final ContractID contractID = contract.getDataClayID();
		final Contract curContract = testdb.getContractByID(contractID);
		assertTrue(curContract != null);

		// use the manager
		final Hashtable<ContractID, HashSet<InterfaceID>> fakeInterfacesInContract = new Hashtable<>();
		final HashSet<InterfaceID> interfacesIDs = new HashSet<>();
		interfacesIDs.add(new InterfaceID());
		fakeInterfacesInContract.put(contractID, interfacesIDs);
		contractMgr.getInfoOfMultipleInterfacesPerActiveContractsForAccount(applicantAccountID, fakeInterfacesInContract);
	}
}
