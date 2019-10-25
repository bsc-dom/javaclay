
package es.bsc.dataclay.dbhandler;

import java.util.Arrays;
import java.util.Random;

import es.bsc.dataclay.dbhandler.DBHandler;
import es.bsc.dataclay.exceptions.dbhandler.DbObjectNotExistException;
import es.bsc.dataclay.util.ids.ObjectID;

/**
 * @brief Common tests for all DbHandlers.
 */
public class DbHandlerCommonTester {

	/** DbHandler tested. */
	private final DBHandler testDb; 

	/**
	 * @brief DbHandlerCommonTests constructor 
	 * @param thetestDb Database to be tested
	 */
	public DbHandlerCommonTester(final DBHandler thetestDb) { 
		this.testDb = thetestDb;

	}

	// ========================= STORE ========================= //


	public boolean testStoreAndGet() { 
		Random r = new Random();
		byte[] arr = new byte[10];
		r.nextBytes(arr);

		ObjectID objectID = new ObjectID();
		testDb.store(objectID, arr);

		byte[] result = testDb.get(objectID);
		return Arrays.equals(arr, result);
	}

	public boolean testUpdate() throws Exception { 
		Random r = new Random();
		byte[] arr = new byte[10];
		r.nextBytes(arr);

		ObjectID objectID = new ObjectID();
		testDb.store(objectID, arr);

		byte[] newarr = new byte[10];
		r.nextBytes(newarr);
		testDb.update(objectID, newarr);

		byte[] result = testDb.get(objectID);
		return Arrays.equals(newarr, result);
	}

	public boolean testDelete() { 
		Random r = new Random();
		byte[] arr = new byte[10];
		r.nextBytes(arr);

		ObjectID objectID = new ObjectID();
		testDb.store(objectID, arr);

		testDb.delete(objectID);

		try {
			testDb.get(objectID);
		} catch (DbObjectNotExistException ex) { 
			return true;
		}

		return false;
	}
	/**
	public boolean testGlobal(final DbHandlerType dbHandlerType) throws Exception { 
		return true;
		
		//TODO: Modify this implementation after new YAMLs 

		DbHandlerType curType = (DbHandlerType) Configuration.Flags.DB_HANDLER_TYPE_FOR_DATASERVICES.getValue();
		Configuration.Flags.DB_HANDLER_TYPE_FOR_DATASERVICES.setValue(dbHandlerType);

		TestUtils.deleteDatabases();
		TestDataService.DATASERVICE2.setRegistered(false);
		TestDataService.DATASERVICE3.setRegistered(false);


		// Clean classPath
		FileAndAspectsUtils.deleteFolderContent(new File(TestUser.DBTEST_CONSUMER.getClassPath()), ".class", true);


		ServiceTestUtils.initManagersAndLogicModule();

		// Register admin account
		AccountID adminID = ServiceTestUtils.newAdminUser(ServiceTestUtils.ADMINNAME, ServiceTestUtils.ADMINCRED);
		Credential adminCred = ServiceTestUtils.ADMINCRED;

		// Register registrator user
		AccountID registratorID = ClientManagementLib.newAccount(adminID, adminCred, new Account(TestUser.DBTEST_REG.getUserName(), 
				TestUser.DBTEST_REG.getRole(), 
				TestUser.DBTEST_REG.getCredential()));
		TestUser.DBTEST_REG.setAccountID(registratorID);

		// Register CONSUMER user
		AccountID consumerID = ClientManagementLib.newAccount(adminID, adminCred, new Account(TestUser.DBTEST_CONSUMER.getUserName(), 
				TestUser.DBTEST_CONSUMER.getRole(), 
				TestUser.DBTEST_CONSUMER.getCredential()));
		TestUser.DBTEST_CONSUMER.setAccountID(consumerID);

		NamespaceID regNamespaceID = ClientManagementLib.newNamespace(registratorID, TestUser.DBTEST_REG.getCredential(), 
				new Namespace("regNamespace", TestUser.DBTEST_REG.getUserName(), Langs.LANG_JAVA));

		String dataSetName = "DataSet";
		DataSetID regDataSetID = ClientManagementLib.newDataSet(
				registratorID, TestUser.DBTEST_REG.getCredential(), new DataSet(dataSetName, TestUser.DBTEST_REG.getUserName()));

		// Register the new class through the Logic Module
		Map<String, MetaClass> registeredClasses = ClientManagementLib.newClass(registratorID,
				TestUser.DBTEST_REG.getCredential(), "regNamespace", 
				"TestGlobalClass", TestUser.DBTEST_REG.getClassPath());

		// Get stubs
		// Contract (and stubs) between registrator and consumer
		ServiceTestUtils.registerContractAndStoreStubsUsingLib(registratorID, TestUser.DBTEST_REG.getCredential(),
				consumerID, TestUser.DBTEST_CONSUMER.getCredential(), regNamespaceID, registeredClasses, 
				TestUser.DBTEST_CONSUMER.getClassPath());

		// DataContract
		ServiceTestUtils.registerPrivateDataContractUsingLib(TestUser.DBTEST_REG.getAccountID(), 
				TestUser.DBTEST_REG.getCredential(),
				TestUser.DBTEST_CONSUMER.getAccountID(), regDataSetID);



		// Get the stubs and use them 
		File libPath = new File(TestUser.DBTEST_CONSUMER.getClassPath());
		URL[] creatorClassUrls = { libPath.toURI().toURL() };
		URLClassLoader userClassLoader = new URLClassLoader(creatorClassUrls);
		// Sets the classloader of the current thread
		Thread.currentThread().setContextClassLoader(userClassLoader);

		// New session
		HashSet<String> dataSets = new HashSet<String>();
		dataSets.add(dataSetName);
		ClientManagementLib.newSession(TestUser.DBTEST_CONSUMER.getAccountID(), 
				TestUser.DBTEST_CONSUMER.getCredential(), TestUser.DBTEST_CONSUMER.getClassPath(),
				dataSets, dataSetName);


		Class<?> globalClass = userClassLoader.loadClass("TestGlobalClass");

		DataClayObject test = (DataClayObject) globalClass.getConstructor(int.class).newInstance(1);

		boolean result = false;
		try {


			// ==== STORE OBJECT ==== //
			test.makePersistent(true, null);

			// ==== GET OBJECT ==== //
			//DataClayObject localTest = (DataClayObject) test.copyToLocal(true);

			//result = localTest.equals(test);
			result = true;

		} catch (Exception e) { 
			e.printStackTrace();
			throw e;
		}

		// Clean classPath
		ServiceTestUtils.finishServices();
		FileAndAspectsUtils.deleteFolderContent(new File(TestUser.DBTEST_CONSUMER.getClassPath()), ".class", true);
		TestUtils.deleteDatabases();
		Configuration.Flags.DB_HANDLER_TYPE_FOR_DATASERVICES.setValue(curType);
		return result;
	}**/

}
