
/**
 * @file DataSetManagerTest.java
 * @date Mar 3, 2014
 * @author jmarti
 */

package es.bsc.dataclay.logic.datasetmgr;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import es.bsc.dataclay.exceptions.logicmodule.datasetmgr.DataSetDoesNotExistException;
import es.bsc.dataclay.exceptions.logicmodule.datasetmgr.DataSetExistsException;
import es.bsc.dataclay.logic.datasetmgr.DataSetManager;
import es.bsc.dataclay.logic.datasetmgr.DataSetManagerDB;
import es.bsc.dataclay.test.AbstractManagerTest;
import es.bsc.dataclay.test.TestUtils;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.management.datasetmgr.DataSet;

/**
 * @class DataSetManagerTest
 * @brief This class tests the functions of the DataSet Manager
 */
public final class DataSetManagerTest extends AbstractManagerTest{

	private DataSetManager dman;
	// !<@brief DataSet Manager instance tested.
	private DataSetManagerDB db;
	// !<@brief DbHandler to insert the responsible account when required in the tests
	private AccountID respAccountID;
	/** Name of responsible account. */
	private String respAccountName;
	// !<@brief An Account for the responsible of the new dataset.
	private static String DBFILESDIRNAME = System.getProperty("user.dir") + "/dbfiles/DataSetManagerTest";

	/**
	 * @brief This method is executed before all tests. It is used to instantiate the DataSetManager.
	 * @author jmarti
	 */
	@BeforeClass
	public static void beforeAll() {
		TestUtils.createOrCleanDirectory(DBFILESDIRNAME);
	}

	/**
	 * @brief This method is executed before each test. It is used to create an account for the dataset responsible and store it in
	 *        the DB.
	 * @author jmarti
	 * @throws RemoteException
	 */
	@Override
	@Before
	public void before() {
		super.before();

		final DataSetManagerDB cdb = new DataSetManagerDB(dataSource);
		cdb.dropTables();

		dman = new DataSetManager(dataSource);
		db = dman.getDbHandler();
		respAccountID = new AccountID();
		respAccountName = "RespAccount";
	}

	/**
	 * @brief This method is executed after each test. It is used to delete the test database
	 * @author jmarti
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
	 *        DataSetManager::newDataSet(String name, AccountID responsible)
	 * @author jmarti
	 * @pre The functions dbhandler.DbHandler#existsObjectByID(java.lang.Object)} and dbhandler.DbHandler#store(java.lang.Object)}
	 *      must be tested and correct.
	 * @post Test the creation and storage of a new DataSet, associated to an existing responsible. \n
	 */
	@Test
	public void testNewDataSet() {

		// If an exception is thrown, the test fails
		final DataSet newDataSet = new DataSet("newDataSet", respAccountName, true);
		newDataSet.setProviderAccountID(respAccountID);
		newDataSet.setDataClayID(new DataSetID());
		final DataSetID newID = dman.newDataSet(newDataSet);

		// Since we use the same database we need to open it again
		// to verify that the DataSet exists in the database by
		// calling the DbHandler directly.
		final DataSet curDataSet = db.getDataSetByID(newID);
		assertTrue(curDataSet != null);

		// Check ID
		assertTrue(curDataSet.getDataClayID().equals(newID));

		// Check resp
		assertTrue(curDataSet.getProviderAccountID().equals(respAccountID));

	}

	/**
	 * @brief Test method for
	 * 
	 *        DataSetManager::newDataSet(String name, AccountID responsible)
	 * @author jmarti
	 * @pre The functions dbhandler.DbHandler#existsObjectByID(java.lang.Object)} and dbhandler.DbHandler#store(java.lang.Object)}
	 *      must be tested and correct.
	 * @post Test the creation and storage of a new DataSet, associated to an existing responsible. \n
	 */
	@Test(expected = DataSetExistsException.class)
	public void testNewDataSetAlreadyExists() {
		// We create a dataset and store it in the DB
		final DataSet newDataSet = new DataSet("newDataSet", respAccountName, true);
		newDataSet.setProviderAccountID(respAccountID);
		newDataSet.setDataClayID(new DataSetID());
		db.store(newDataSet);

		// If an exception is thrown, the test succeeds
		final DataSet newDataSet2 = new DataSet("newDataSet", respAccountName, true);
		newDataSet2.setProviderAccountID(respAccountID);
		newDataSet2.setDataClayID(new DataSetID());
		dman.newDataSet(newDataSet2);
	}

	/**
	 * @brief Test method for DataSetManager::getDataSetID(String)
	 * @author jmarti
	 * @post Test that the function "getDataSetID" returns the ID of the dataset created.
	 */
	@Test
	public void testGetDataSet() {
		final String datasetName = "newDataSet";
		// We create a dataset and store it in the DB
		final DataSet newDataSet = new DataSet(datasetName, respAccountName, true);
		newDataSet.setProviderAccountID(respAccountID);
		newDataSet.setDataClayID(new DataSetID());
		db.store(newDataSet);

		// Check it exists
		assertTrue(db.getDataSetByID(newDataSet.getDataClayID()) != null);

		// Use manager
		assertTrue(dman.getDataSetID(datasetName).equals(newDataSet.getDataClayID()));
	}

	/**
	 * @brief Test method for DataSetManager::getDataSetsInfo(HashSet<String>)
	 * @author jmarti
	 * @post Test that the function "getDataSetsInfo" returns the info the datasets created.
	 */
	@Test
	public void testGetDataSetsInfo() {
		final String datasetName = "newDataSet";
		// We create a dataset and store it in the DB
		final DataSet newDataSet = new DataSet(datasetName, respAccountName, true);
		newDataSet.setProviderAccountID(respAccountID);
		newDataSet.setDataClayID(new DataSetID());
		db.store(newDataSet);

		// Check it exists
		assertTrue(db.getDataSetByID(newDataSet.getDataClayID()) != null);

		// Use manager
		final DataSet dsetInfo = dman.getDataSetInfo(datasetName);
		assertTrue(dsetInfo.getDataClayID().equals(newDataSet.getDataClayID()));
	}

	/**
	 * @brief Test method for
	 * 
	 *        DataSetManager::checkDataSetResponsible(AccountID responsible, DataSetID datasetID)
	 * @author jmarti
	 * @post Test that "checkDataSetResponsible" returns false when the dataset does not exist, and the responsible exists or not
	 */
	@Test(expected = DataSetDoesNotExistException.class)
	public void testCheckDataSetResponsibleWrongDataSet() {
		// we check first with a non-existing dataset and a non-existing responsible
		final AccountID resp = new AccountID();
		final DataSetID dom = new DataSetID();
		assertFalse(dman.checkDataSetResponsible(dom, resp));
	}

	/**
	 * @brief Test method for
	 * 
	 *        DataSetManager::checkDataSetResponsible(AccountID responsible, DataSetID datasetID)
	 * @author jmarti
	 * @pre The function datasetmgr.DataSetManager::newDataSet} and must be tested and correct.
	 * @post Test that "checkDataSetResponsible" returns true when the given AccountID is responsible for an existing datasetID, and
	 *       false when it is not
	 */
	@Test
	public void testCheckDataSetResponsibleCorrectDataSet() {
		final AccountID resp = new AccountID();
		// an existing dataset with a non-existing responsible
		final DataSet newDataSet = new DataSet("DataSet 1", respAccountName, true);
		newDataSet.setProviderAccountID(respAccountID);

		newDataSet.setDataClayID(new DataSetID());
		final DataSetID newDom = dman.newDataSet(newDataSet);
		assertFalse(dman.checkDataSetResponsible(newDom, resp));
		// a dataset with its corresponding responsible
		assertTrue(dman.checkDataSetResponsible(newDom, respAccountID));
	}

	/**
	 * @brief Test method for
	 * 
	 *        DataSetManager::removeDataSet(DataSetID datasetID)
	 * @author jmarti
	 * @pre The function datasetmgr.DataSetManager::newDataSet} datasetmgr.DataSetManager::existsDataSet} must be tested and
	 *      correct.
	 * @post Test that "removeDataSet" succeeds when an existing dataset is provided.
	 */
	@Test
	public void testRemoveDataSetCorrectDataSet() {
		// We create an initial dataset
		final DataSet newDataSet = new DataSet("DataSet 1", respAccountName, true);
		newDataSet.setProviderAccountID(respAccountID);
		newDataSet.setDataClayID(new DataSetID());
		db.store(newDataSet);

		// Check it exists
		assertTrue(db.getDataSetByID(newDataSet.getDataClayID()) != null);

		// We delete it
		dman.removeDataSet(newDataSet.getDataClayID());

		// we check that it does not exist
		assertFalse(db.getDataSetByID(newDataSet.getDataClayID()) != null);
	}

	/**
	 * @brief Test method for DataSetManager::removeDataSet(DataSetID datasetID)
	 * @author jmarti
	 * @pre The function datasetmgr.DataSetManager::newDataSet, datasetmgr.DataSetManager::existsDataSet must be tested and correct.
	 * @post Test that "removeDataSet" succeeds when an existing dataset is provided, deleting the dataset but not its responsible
	 *       account (thus another dataset with same responsible is ok)
	 */
	@Test
	public void testRemoveDataSet2DataSetsWithSameResponsible() {
		// We create an initial dataset
		final DataSet newDataSet1 = new DataSet("DataSet 1", respAccountName, true);
		newDataSet1.setProviderAccountID(respAccountID);
		newDataSet1.setDataClayID(new DataSetID());
		final String datasetName2 = "DataSet 2";
		final DataSet newDataSet2 = new DataSet(datasetName2, respAccountName, true);
		newDataSet2.setProviderAccountID(respAccountID);
		newDataSet2.setDataClayID(new DataSetID());
		db.store(newDataSet1);
		db.store(newDataSet2);

		// Check both exist
		assertTrue(db.getDataSetByID(newDataSet1.getDataClayID()) != null);
		assertTrue(db.getDataSetByID(newDataSet2.getDataClayID()) != null);

		// We delete dataset1
		dman.removeDataSet(newDataSet1.getDataClayID());

		// we check that dataset1 does not exist
		assertFalse(db.getDataSetByID(newDataSet1.getDataClayID()) != null);

		// but the other dataset must be ok
		final DataSet curDataSet = db.getDataSetByID(newDataSet2.getDataClayID());
		assertTrue(curDataSet != null);
		assertTrue(curDataSet.getProviderAccountID().equals(respAccountID));
		assertTrue(curDataSet.getName().equals(datasetName2));
		assertTrue(curDataSet.getDataClayID().equals(newDataSet2.getDataClayID()));
	}

	/**
	 * @brief Test method for DataSetManager::removeDataSet(DataSetID datasetID)
	 * @author jmarti
	 * @post Test that "removeDataSet" returns an exception when a non existing dataset is provided.
	 */
	@Test(expected = DataSetDoesNotExistException.class)
	public void testRemoveDataSetWrongDataSet() {
		// We try to delete a non-existing dataset
		dman.removeDataSet(new DataSetID());
	}

	/**
	 * @brief Test method for DataSetManager::getPublicDataSets()
	 * @author jmarti
	 * @pre The function datasetmgr.DataSetManager::newDataSet, datasetmgr.DataSetManager::existsDataSet must be tested and correct.
	 * @post Test that "getPublicDataSets" succeeds when an existing dataset is stored
	 */
	@Test
	public void testGetPublicDataSets() {
		// We create an initial public dataset
		final String datasetName1 = "DataSet 1";
		final DataSet newDataSet1 = new DataSet(datasetName1, respAccountName, true);
		newDataSet1.setProviderAccountID(respAccountID);
		newDataSet1.setDataClayID(new DataSetID());
		db.store(newDataSet1);
		// We create an initial private dataset
		final String datasetName2 = "DataSet 2";
		final DataSet newDataSet2 = new DataSet(datasetName2, respAccountName, false);
		newDataSet2.setProviderAccountID(respAccountID);
		newDataSet2.setDataClayID(new DataSetID());
		db.store(newDataSet2);

		// Check both exist
		assertTrue(db.getDataSetByID(newDataSet1.getDataClayID()) != null);
		assertTrue(db.getDataSetByID(newDataSet2.getDataClayID()) != null);

		// Check public data sets only contains dataset1
		assertTrue(dman.getPublicDataSets().size() == 1);
		assertTrue(dman.getPublicDataSets().iterator().next().getDataClayID().equals(newDataSet1.getDataClayID()));

		// Check both still exist
		assertTrue(db.getDataSetByID(newDataSet1.getDataClayID()) != null);
		assertTrue(db.getDataSetByID(newDataSet2.getDataClayID()) != null);
	}

	/**
	 * @brief Test method for DataSetManager::checkDataSetIsPublic()
	 * @author jmarti
	 * @pre The function datasetmgr.DataSetManager::newDataSet, datasetmgr.DataSetManager::existsDataSet must be tested and correct.
	 * @post Test that "checkDataSetIsPublic" succeeds on public and private datasets (returning true or false)
	 */
	@Test
	public void testCheckDataSetIsPublic() {
		// We create an initial public dataset
		final String datasetName1 = "DataSet 1";
		final DataSet newDataSet1 = new DataSet(datasetName1, respAccountName, true);
		newDataSet1.setProviderAccountID(respAccountID);
		newDataSet1.setDataClayID(new DataSetID());
		db.store(newDataSet1);
		// We create an initial private dataset
		final String datasetName2 = "DataSet 2";
		final DataSet newDataSet2 = new DataSet(datasetName2, respAccountName, false);
		newDataSet2.setProviderAccountID(respAccountID);
		newDataSet2.setDataClayID(new DataSetID());
		db.store(newDataSet2);

		// Check both exist
		assertTrue(db.getDataSetByID(newDataSet1.getDataClayID()) != null);
		assertTrue(db.getDataSetByID(newDataSet2.getDataClayID()) != null);

		// Check public data sets only contains dataset1
		assertTrue(dman.checkDataSetIsPublic(newDataSet1.getDataClayID()));
		assertFalse(dman.checkDataSetIsPublic(newDataSet2.getDataClayID()));

		// Check both still exist
		assertTrue(db.getDataSetByID(newDataSet1.getDataClayID()) != null);
		assertTrue(db.getDataSetByID(newDataSet2.getDataClayID()) != null);

	}

}
