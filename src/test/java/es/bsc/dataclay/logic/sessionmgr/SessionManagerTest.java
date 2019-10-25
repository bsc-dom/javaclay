
package es.bsc.dataclay.logic.sessionmgr;

import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Hashtable;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.exceptions.logicmodule.sessionmgr.SessionNotExistException;
import es.bsc.dataclay.logic.sessionmgr.Session;
import es.bsc.dataclay.logic.sessionmgr.SessionManager;
import es.bsc.dataclay.logic.sessionmgr.SessionManagerDB;
import es.bsc.dataclay.test.AbstractManagerTest;
import es.bsc.dataclay.test.TestUtils;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.DataContractID;
import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.SessionID;
import es.bsc.dataclay.util.management.sessionmgr.SessionContract;
import es.bsc.dataclay.util.management.sessionmgr.SessionDataContract;
import es.bsc.dataclay.util.management.sessionmgr.SessionInfo;
import es.bsc.dataclay.util.management.sessionmgr.SessionInterface;
import es.bsc.dataclay.util.management.sessionmgr.SessionOperation;

public class SessionManagerTest extends AbstractManagerTest{

	private SessionManager sessionManager; // !<@brief Session Manager instance tested.
	private static String DBFILESDIRNAME = System.getProperty("user.dir") + "/dbfiles/SessionManagerTest";
	// !<@brief Path to the test databases.
	private SessionManagerDB dbHandler;

	private static Calendar endDate;

	@BeforeClass
	public static void beforeAll() {
		TestUtils.createOrCleanDirectory(DBFILESDIRNAME);
		endDate = Calendar.getInstance();
		endDate.roll(Calendar.YEAR, 1);
	}

	/**
	 * @brief This method is executed before each test. It is used create the test database.
	 * @author dgasull
	 * @throws RemoteException
	 */
	@Override
	@Before
	public final void before() {
		super.before();

		final SessionManagerDB cdb = new SessionManagerDB(dataSource);
		cdb.dropTables();

		sessionManager = new SessionManager(dataSource);
		dbHandler = sessionManager.getDbHandler();
	}

	/**
	 * @brief This method is executed after each test.
	 * @author dgasull
	 * @throws Exception
	 */
	@Override
	@After
	public void after() throws Exception {
		dbHandler.close();
		super.after();
		TestUtils.cleanDirectory(DBFILESDIRNAME);
	}

	@AfterClass
	public static void afterAll() throws Exception {
		TestUtils.deleteDirectory(DBFILESDIRNAME);
	}

	@Test
	public final void testNewSession() {
		final AccountID newaccountID = new AccountID();
		final Hashtable<ContractID, SessionContract> sessionContracts = new Hashtable<>();
		final Hashtable<DataContractID, SessionDataContract> sessionDataContracts = new Hashtable<>();
		final SessionID sessionID = sessionManager.newSession(newaccountID, sessionContracts, sessionDataContracts,
				new DataContractID(), endDate, Langs.LANG_JAVA,
				new Hashtable<MetaClassID, byte[]>()).getSessionID();

		// Verify it exists
		assertTrue(dbHandler.existsObjectByID(sessionID));
	}

	@Test
	public final void testGetSessionInfo() {

		// Store a session with some information
		final AccountID newaccountID = new AccountID();
		final Hashtable<ContractID, SessionContract> sessionContracts = new Hashtable<>();
		final SessionContract sc = new SessionContract();

		final ContractID contractID = new ContractID();
		final MetaClassID classOfInterface = new MetaClassID();
		final InterfaceID interfaceID = new InterfaceID();
		final OperationID operationID = new OperationID();

		final Hashtable<OperationID, SessionOperation> sessionOps = new Hashtable<>();
		final SessionOperation sessionop = new SessionOperation();
		sessionop.setOperationID(operationID);
		sessionOps.put(operationID, sessionop);

		final Hashtable<InterfaceID, SessionInterface> sessionIfaces = new Hashtable<>();
		final SessionInterface si = new SessionInterface();
		si.setClassOfInterface(classOfInterface);
		si.setInterfaceID(interfaceID);
		si.setSessionOperations(sessionOps);
		sessionIfaces.put(interfaceID, si);

		sc.setContractID(contractID);
		sc.setSessionInterfaces(sessionIfaces);
		sessionContracts.put(contractID, sc);

		final Hashtable<DataContractID, SessionDataContract> sessionDataContracts = new Hashtable<>();
		final DataContractID dataContractID = new DataContractID();
		final DataSetID dataSetID = new DataSetID();
		final SessionDataContract sdc = new SessionDataContract(dataContractID, dataSetID);
		sessionDataContracts.put(dataContractID, sdc);

		final Session session = new Session(newaccountID, sessionContracts, sessionDataContracts, dataContractID, endDate,
				Langs.LANG_JAVA, new Hashtable<MetaClassID, byte[]>());
		dbHandler.store(session);

		// Verify it exists
		assertTrue(dbHandler.existsObjectByID(session.getDataClayID()));

		// Check session with all information provided before
		final SessionInfo resultSet = sessionManager.getSessionInfo(session.getDataClayID());

		// Verify
		assertTrue(resultSet != null);

		resultSet.getEndDate().equals(endDate);

		// This also verifies that fields can be retrieved although they are transient
		assertTrue(endDate.get(Calendar.YEAR) == resultSet.getEndDate().get(Calendar.YEAR));

		final SessionDataContract result1 = resultSet.getSessionDataContracts().values().iterator().next();
		assertTrue(result1 != null);
		assertTrue(result1.getDataContractID().equals(dataContractID));

		final SessionContract result2 = resultSet.getSessionContracts().values().iterator().next();
		assertTrue(result2 != null);
		assertTrue(result2.getContractID().equals(contractID));
		assertTrue(result2.getSessionInterfaces() != null);
		assertTrue(result2.getSessionInterfaces().size() == 1);

		final SessionInterface siresult = result2.getSessionInterfaces().get(interfaceID);
		assertTrue(siresult != null);
		assertTrue(siresult.getClassOfInterface().equals(classOfInterface));
		assertTrue(siresult.getInterfaceID().equals(interfaceID));
		assertTrue(siresult.getSessionOperations().containsKey(sessionop.getOperationID()));
	}

	@Test(expected = SessionNotExistException.class)
	public void badSessionTest() {
		// Check session with all information provided before modifying something to make it fail
		sessionManager.getSessionInfo(new SessionID());
	}
}
