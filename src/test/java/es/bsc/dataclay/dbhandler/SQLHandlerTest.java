
/**
 * @file Db4oHandlerTest.java
 * 
 * @date Sep 6, 2012
 * @author dgasull
 */
package es.bsc.dataclay.dbhandler;

import static org.junit.Assert.assertTrue;

import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteHandler;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.FileAndAspectsUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import es.bsc.dataclay.dbhandler.DBHandlerFactory.DBHandlerType;
import es.bsc.dataclay.dbhandler.sql.SQLHandler;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteHandlerConfig;
import es.bsc.dataclay.util.Configuration.Flags;

import java.io.File;

/**
 * @brief This class tests the openings, closes and basic operations of the DbHandler class.
 */
public final class SQLHandlerTest {

	/** Tested database. */
	private SQLiteHandler testDb;

	/** DbHandler common tester. */
	private DbHandlerCommonTester commonTester;

	/** Host IP. */
	private static String HOSTIP = "127.0.0.1";

	final private static DBHandlerType dbHandlerType = (DBHandlerType) Flags.DB_HANDLER_TYPE_FOR_LOGICMODULE.getValue();

	private SQLHandler<?> initDBHandler() {
		switch (dbHandlerType) {
		case SQLITE:
			return (SQLHandler<?>) new SQLiteHandlerConfig(Flags.STORAGE_METADATA_PATH.getStringValue(), "test", false).getDBHandler();
		default:
			throw new IllegalArgumentException(dbHandlerType + " not yet supported");
		}
	}

	@Before
	public void before() {
		Configuration.Flags.STORAGE_PATH.setValue(System.getProperty("user.dir") + "/dbfiles");
		testDb = (SQLiteHandler) initDBHandler();
		testDb.open();
		//testDb.dropAllDatabases();
		commonTester = new DbHandlerCommonTester(testDb);
	}

	@After
	public void after() throws Exception {
		testDb.dropAllDatabases();
		testDb.close();
		FileAndAspectsUtils.deleteFolderContent(new File(Configuration.Flags.STORAGE_PATH.getStringValue()));
	}

	@Test
	public void testStoreAndGet() throws Exception {
		assertTrue(commonTester.testStoreAndGet());
	}

	@Test
	public void testUpdate() throws Exception {
		assertTrue(commonTester.testUpdate());
	}

	@Test
	public void testDelete() throws Exception {
		assertTrue(commonTester.testDelete());
	}

	@Test
	public void testGlobal() throws Exception {
		// TODO refactor testGlobal
		// assertTrue(commonTester.testGlobal(DbHandlerType.POSTGRES));
	}

}
