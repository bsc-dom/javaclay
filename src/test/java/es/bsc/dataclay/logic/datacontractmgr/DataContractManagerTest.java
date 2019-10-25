
/**
 * @file DataContractManagerTest.java
 * @date Mar 3, 2014
 * @author jmarti
 */

package es.bsc.dataclay.logic.datacontractmgr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import es.bsc.dataclay.exceptions.logicmodule.datacontractmgr.AccountAlreadyHasADataContractWithProvider;
import es.bsc.dataclay.exceptions.logicmodule.datacontractmgr.AccountHasNoDataContractWithProvider;
import es.bsc.dataclay.logic.datacontractmgr.DataContractManager;
import es.bsc.dataclay.logic.datacontractmgr.DataContractManagerDB;
import es.bsc.dataclay.test.AbstractManagerTest;
import es.bsc.dataclay.test.TestUtils;
import es.bsc.dataclay.util.ProcessEnvironment;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.DataContractID;
import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.management.datacontractmgr.DataContract;

/**
 * @brief This class tests the functions of DataContract Manager
 * @author jmarti
 */
public final class DataContractManagerTest  extends AbstractManagerTest{

	private DataContractManager datacontractMgr;
	private static String DBFILESDIRNAME = System.getProperty("user.dir") + "/dbfiles/DataContractManagerTest";
	// !<@brief Path to the test databases.
	private static Set<AccountID> accountsIDsofTheApplicants;
	private static Set<String> applicantsNames;
	private static AccountID providerAccountID = new AccountID();
	private static DataSetID datasetIDofProvider;
	private static Calendar beginDate;
	private static Calendar endDate;
	private static Calendar beginGMTDate;
	private static Calendar endGMTDate;

	private DataContractManagerDB testdb; // !<@brief DbHandler used in tests.

	static {
		final ProcessEnvironment pe = ProcessEnvironment.getInstance();
		pe.put("POSTGRES_HOST", "127.0.0.1");
		pe.put("POSTGRES_PORT", "5432");
		pe.put("POSTGRES_USER", "postgres");
		pe.put("POSTGRES_PASSWORD", "postgres");
		pe.put("POSTGRES_DBNAME", "postgres");
	}

	/**
	 * @brief This method is executed before all tests. It is used to instantiate variables that are used in all the tests in order
	 *        to save code.
	 * @author jmarti
	 */
	@BeforeClass
	public static void beforeAll() throws RemoteException {
		TestUtils.createOrCleanDirectory(DBFILESDIRNAME);
		datasetIDofProvider = new DataSetID();
		beginDate = Calendar.getInstance();
		beginDate.roll(Calendar.YEAR, -1);
		endDate = Calendar.getInstance();
		endDate.roll(Calendar.YEAR, 1);

		final TimeZone tz = TimeZone.getTimeZone("GMT");
		beginGMTDate = Calendar.getInstance();
		beginGMTDate.roll(Calendar.YEAR, -1);
		beginGMTDate.setTimeZone(tz);
		endGMTDate = Calendar.getInstance();
		endGMTDate.roll(Calendar.YEAR, 1);
		endGMTDate.setTimeZone(tz);

		accountsIDsofTheApplicants = new HashSet<>();
		accountsIDsofTheApplicants.add(new AccountID());
		final String applicantName = "Applicant";
		applicantsNames = new HashSet<>();
		applicantsNames.add(applicantName);
	}

	/**
	 * @brief This method is executed before each test. It is used instantiate the DataContract Manager to test.
	 * @author jmarti
	 */
	@Override
	@Before
	public void before() {
		super.before();

		final DataContractManagerDB cdb = new DataContractManagerDB(dataSource);
		cdb.dropTables();

		datacontractMgr = new DataContractManager(dataSource);

		testdb = datacontractMgr.getDbHandler();

	}

	/**
	 * @brief This method is executed after each test. It is used to delete the test database since we are testing the creation of
	 *        DataContract on it, it is necessary to empty the database before.
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
	 * @brief Test method for DataContractManager::newPrivateDataContract(DataSetID, AccountID, Date, Date)
	 * @author jmarti
	 * @post The datacontract can be retrieved after registering it in the DB
	 * @note Currently we cannot check by example with booleans, since "false" means "whatever" (10-Oct-2012 jmarti)
	 */
	@Test
	public void testNewPrivateDataContract() throws Exception {
		// Use manager
		final DataContract datacontract = new DataContract(datasetIDofProvider, providerAccountID, beginDate, endDate);
		datacontract.setDataClayID(new DataContractID());
		datacontract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		datacontract.setProviderAccountID(providerAccountID);
		datacontract.setProviderDataSetID(datasetIDofProvider);

		final DataContractID newID = datacontractMgr.newPrivateDataContract(datacontract);

		// Check by ID (object reference)
		final DataContract curDataContract = testdb.getDataContractByID(newID);
		assertTrue(curDataContract != null);

		// Check the ID of the object
		assertTrue(newID.equals(curDataContract.getDataClayID()));

		// Check fields
		assertFalse(curDataContract.isPublicAvailable());
		assertTrue(curDataContract.getProviderDataSetID().equals(datasetIDofProvider));
		assertTrue(curDataContract.getApplicantsAccountsIDs().equals(accountsIDsofTheApplicants));
		assertTrue(curDataContract.getBeginDate().equals(beginDate));
		assertTrue(curDataContract.getEndDate().equals(endDate));

	}

	/**
	 * @brief Test method newPrivateDataContract including GMT beginDate and endData
	 * @author abarcelo
	 * @post The datacontract can be retrieved after registering it in the DB
	 */
	@Test
	public void testNewGMTPrivateDataContract() throws Exception {
		// Use manager
		final DataContract datacontract = new DataContract(datasetIDofProvider, providerAccountID, beginGMTDate, endGMTDate);
		datacontract.setDataClayID(new DataContractID());
		datacontract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		datacontract.setProviderAccountID(providerAccountID);
		datacontract.setProviderDataSetID(datasetIDofProvider);

		final DataContractID newID = datacontractMgr.newPrivateDataContract(datacontract);

		// Check by ID (object reference)
		final DataContract curDataContract = testdb.getDataContractByID(newID);
		assertTrue(curDataContract != null);

		// Check the ID of the object
		assertTrue(newID.equals(curDataContract.getDataClayID()));

		// Check fields
		assertFalse(curDataContract.isPublicAvailable());
		assertEquals(curDataContract.getProviderDataSetID(), datasetIDofProvider);
		assertEquals(curDataContract.getApplicantsAccountsIDs(), accountsIDsofTheApplicants);
		assertEquals(curDataContract.getBeginDate().compareTo(beginGMTDate), 0);
		assertEquals(curDataContract.getEndDate().compareTo(endGMTDate), 0);

		// Caution! This is environment dependant! It is not requirement per se,
		// but that is a state of the facts. The TimeZone has changed
		assertNotEquals(curDataContract.getBeginDate(), beginGMTDate);
		assertNotEquals(curDataContract.getEndDate(), endGMTDate);
	}

	/**
	 * @brief Test method for DataContractManager::newPublicDataContract(DataSetID, Date, Date)
	 * @author jmarti
	 * @post The datacontract can be retrieved after registering it in the DB
	 */
	@Test
	public void testNewPublicDataContract() throws Exception {
		// Use manager

		final DataContract datacontract = new DataContract(datasetIDofProvider, providerAccountID, accountsIDsofTheApplicants, beginDate, endDate);
		datacontract.setDataClayID(new DataContractID());
		datacontract.setProviderAccountID(providerAccountID);
		datacontract.setApplicantsAccountsIDs(new HashSet<AccountID>());
		datacontract.setProviderDataSetID(datasetIDofProvider);

		final DataContractID newID = datacontractMgr.newPublicDataContract(datacontract);

		// Check by ID (object reference)Check by ID
		final DataContract curDataContract = testdb.getDataContractByID(newID);
		assertTrue(curDataContract != null);

		// Check the ID of the object
		assertTrue(newID.equals(curDataContract.getDataClayID()));

		// Check fields
		assertTrue(curDataContract.isPublicAvailable());
		assertTrue(curDataContract.getProviderDataSetID().equals(datasetIDofProvider));
		assertTrue(curDataContract.getApplicantsAccountsIDs().size() == 0);
		assertTrue(curDataContract.getBeginDate().equals(beginDate));
		assertTrue(curDataContract.getEndDate().equals(endDate));
	}

	/**
	 * @brief Test method for DataContractManager::registerToPublicDataContract(AccountID, DataContractID)
	 */
	@Test
	public void testRegisterToPublicActiveDataContract() {
		final DataContract datacontract = new DataContract(datasetIDofProvider, providerAccountID, accountsIDsofTheApplicants, beginGMTDate, endGMTDate);
		datacontract.setDataClayID(new DataContractID());
		datacontract.setProviderAccountID(providerAccountID);
		datacontract.setApplicantsAccountsIDs(new HashSet<AccountID>());
		datacontract.setProviderDataSetID(datasetIDofProvider);

		testdb.store(datacontract);

		// Check object exists by its ID
		DataContract curDataContract = testdb.getDataContractByID(datacontract.getDataClayID());
		assertTrue(curDataContract != null);

		// use the manager
		final AccountID applicantAccountID = new AccountID();
		datacontractMgr.registerToPublicDataContract(applicantAccountID, datacontract.getDataClayID());

		// Check by ID
		curDataContract = testdb.getDataContractByID(datacontract.getDataClayID());
		assertTrue(curDataContract != null);

		// Check the updated datacontract has the applicantAccount
		assertTrue(curDataContract.getApplicantsAccountsIDs().contains(applicantAccountID));
	}

	/**
	 * @brief Test method for DataContractManager::checkDataSetHasNoDataContracts(DataSetID) when it has a datacontract.
	 */
	@Test
	public void testCheckDataSetHasNoDataContracts() {
		final DataContract datacontract = new DataContract(datasetIDofProvider, providerAccountID, accountsIDsofTheApplicants, beginGMTDate, endGMTDate);
		datacontract.setDataClayID(new DataContractID());
		datacontract.setProviderAccountID(providerAccountID);
		datacontract.setApplicantsAccountsIDs(new HashSet<AccountID>());
		datacontract.setProviderDataSetID(datasetIDofProvider);
		testdb.store(datacontract);

		// Check object exists by its ID
		assertTrue(testdb.getDataContractByID(datacontract.getDataClayID()) != null);

		// Query manager
		assertFalse(datacontractMgr.checkDataSetHasNoDataContracts(datasetIDofProvider));
		assertTrue(datacontractMgr.checkDataSetHasNoDataContracts(new DataSetID()));
	}

	/**
	 * @brief Test method for {@link DataContractManager#getDataContractIDsOfProvider(DataSetID)}
	 */
	@Test
	public void testGetDataContractIDsOfProvider() {
		final DataContract datacontract = new DataContract(datasetIDofProvider, providerAccountID, accountsIDsofTheApplicants, beginGMTDate, endGMTDate);
		datacontract.setDataClayID(new DataContractID());
		datacontract.setProviderAccountID(providerAccountID);
		datacontract.setApplicantsAccountsIDs(new HashSet<AccountID>());
		datacontract.setProviderDataSetID(datasetIDofProvider);
		testdb.store(datacontract);

		// Check object exists by its ID
		assertTrue(testdb.getDataContractByID(datacontract.getDataClayID()) != null);

		// Query manager
		Map<DataContractID, DataContract> curDataContracts = datacontractMgr
				.getDataContractIDsOfProvider(datasetIDofProvider);
		assertTrue(curDataContracts.size() == 1 && curDataContracts.containsKey(datacontract.getDataClayID()));
		curDataContracts = datacontractMgr.getDataContractIDsOfProvider(new DataSetID());
		assertTrue(curDataContracts.size() == 0);
	}

	/**
	 * @brief Test method for {@link DataContractManager#getDataContractIDsOfApplicant(AccountID)
	 * 
	 */
	@Test
	public void testGetDataContractIDsOfApplicant() {
		final AccountID applicantAccountID = accountsIDsofTheApplicants.iterator().next();
		final DataContract datacontract = new DataContract(datasetIDofProvider, providerAccountID, accountsIDsofTheApplicants, beginDate,
				endDate);
		datacontract.setDataClayID(new DataContractID());
		datacontract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		datacontract.setProviderAccountID(providerAccountID);
		datacontract.setProviderDataSetID(datasetIDofProvider);
		testdb.store(datacontract);

		// Check object exists by its ID
		assertTrue(testdb.getDataContractByID(datacontract.getDataClayID()) != null);

		// Query manager
		Map<DataContractID, DataContract> curDataContracts = datacontractMgr
				.getDataContractIDsOfApplicant(applicantAccountID);
		assertTrue(curDataContracts.size() == 1 && curDataContracts.containsKey(datacontract.getDataClayID()));
		curDataContracts = datacontractMgr.getDataContractIDsOfApplicant(new AccountID());
		assertTrue(curDataContracts.size() == 0);
	}

	/**
	 * @brief Test method for {@link DataContractManager#getDataContractOfApplicantWithProvider(AccountID, DataSetID)
	 * 
	 */
	@Test
	public void testGetDataContractOfApplicantWithProvider() {
		final AccountID applicantAccountID = accountsIDsofTheApplicants.iterator().next();
		final DataContract datacontract = new DataContract(datasetIDofProvider, providerAccountID, accountsIDsofTheApplicants, beginDate,
				endDate);
		datacontract.setDataClayID(new DataContractID());
		datacontract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		datacontract.setProviderAccountID(providerAccountID);
		datacontract.setProviderDataSetID(datasetIDofProvider);
		testdb.store(datacontract);

		// Check object exists by its ID
		assertTrue(testdb.getDataContractByID(datacontract.getDataClayID()) != null);

		// Query manager
		final DataContract curDataContract = datacontractMgr.getDataContractInfoOfApplicantWithProvider(applicantAccountID,
				datasetIDofProvider);
		assertTrue(curDataContract.getDataClayID().equals(datacontract.getDataClayID()));
	}

	/**
	 * @brief Test method for {@link DataContractManager#getInfoOfSomeActiveDataContractsForAccountWithProviders(AccountID, HashSet)
	 * 
	 */
	@Test
	public void testGetInfoOfSomeActiveDataContractForAccountWithProvider() {
		final AccountID applicantAccountID = accountsIDsofTheApplicants.iterator().next();
		final DataContract datacontract = new DataContract(datasetIDofProvider, providerAccountID, accountsIDsofTheApplicants, beginDate,
				endDate);
		datacontract.setDataClayID(new DataContractID());
		datacontract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		datacontract.setProviderAccountID(providerAccountID);
		datacontract.setProviderDataSetID(datasetIDofProvider);
		testdb.store(datacontract);

		// Check object exists by its ID
		assertTrue(testdb.getDataContractByID(datacontract.getDataClayID()) != null);

		// Query manager
		final HashSet<DataSetID> datasets = new HashSet<>();
		datasets.add(datasetIDofProvider);
		final Map<DataContractID, DataContract> datacontractsInfo = datacontractMgr
				.getInfoOfSomeActiveDataContractsForAccountWithProviders(applicantAccountID, datasets);
		assertTrue(datacontractsInfo != null);
		assertTrue(datacontractsInfo.size() == 1);
	}

	/**
	 * @brief Test method for {@link DataContractManager#getInfoOfSomeActiveDataContractsForAccountWithProviders(AccountID, HashSet)
	 * 
	 */
	@Test(expected = AccountHasNoDataContractWithProvider.class)
	public void testGetInfoOfSomeActiveDataContractForAccountWithoutContractsWithProvider() {
		final DataContract datacontract = new DataContract(datasetIDofProvider, providerAccountID, accountsIDsofTheApplicants, beginDate,
				endDate);
		datacontract.setDataClayID(new DataContractID());
		datacontract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		datacontract.setProviderAccountID(providerAccountID);
		datacontract.setProviderDataSetID(datasetIDofProvider);
		testdb.store(datacontract);

		// Check object exists by its ID
		assertTrue(testdb.getDataContractByID(datacontract.getDataClayID()) != null);

		// Query manager
		final HashSet<DataSetID> datasets = new HashSet<>();
		datasets.add(datasetIDofProvider);
		datacontractMgr.getInfoOfSomeActiveDataContractsForAccountWithProviders(new AccountID(), datasets);
	}

	/**
	 * @brief Test method for {@link DataContractManager#newPrivateDataContract(DataSetID, AccountID, Calendar, Calendar)
	 * 
	 */
	@Test(expected = AccountAlreadyHasADataContractWithProvider.class)
	public void testAccountWithOnlyOneDataContractPerProvider() throws Exception {
		final DataContract datacontract = new DataContract(datasetIDofProvider, providerAccountID, accountsIDsofTheApplicants, beginDate,
				endDate);
		datacontract.setDataClayID(new DataContractID());
		datacontract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		datacontract.setProviderAccountID(providerAccountID);
		datacontract.setProviderDataSetID(datasetIDofProvider);
		testdb.store(datacontract);

		// Check object exists by its ID
		assertTrue(testdb.getDataContractByID(datacontract.getDataClayID()) != null);

		final DataContract datacontract1 = new DataContract(datasetIDofProvider, providerAccountID, accountsIDsofTheApplicants, beginDate,
				endDate);
		datacontract1.setDataClayID(new DataContractID());
		datacontract1.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		datacontract1.setProviderAccountID(providerAccountID);
		datacontract1.setProviderDataSetID(datasetIDofProvider);
		datacontract1.setDataClayID(new DataContractID());
		datacontractMgr.newPrivateDataContract(datacontract1);
	}

	/**
	 * @brief Test method for {@link DataContractManager#registerToPublicDataContract(AccountID, DataContractID)
	 * 
	 */
	@Test(expected = AccountAlreadyHasADataContractWithProvider.class)
	public void testAccountTriesToRegisterToPublicContractAndAlreadyHasContractWithProvider() {
		final AccountID applicantAccountID = accountsIDsofTheApplicants.iterator().next();
		final DataContract datacontract = new DataContract(datasetIDofProvider, providerAccountID, accountsIDsofTheApplicants, beginDate,
				endDate);
		datacontract.setDataClayID(new DataContractID());
		datacontract.setApplicantsAccountsIDs(accountsIDsofTheApplicants);
		datacontract.setProviderAccountID(providerAccountID);
		datacontract.setProviderDataSetID(datasetIDofProvider);

		final DataContract publicdatacontract = new DataContract(datasetIDofProvider, providerAccountID, accountsIDsofTheApplicants, beginDate,
				endDate);
		publicdatacontract.setDataClayID(new DataContractID());
		publicdatacontract.setProviderAccountID(providerAccountID);
		publicdatacontract.setApplicantsAccountsIDs(new HashSet<AccountID>());
		publicdatacontract.setProviderDataSetID(datasetIDofProvider);

		testdb.store(datacontract);
		testdb.store(publicdatacontract);

		// Check object exists by its ID
		assertTrue(testdb.getDataContractByID(datacontract.getDataClayID()) != null);

		datacontractMgr.registerToPublicDataContract(applicantAccountID, publicdatacontract.getDataClayID());
	}

}
