
/**
 * @file Db4oHandlerTest.java
 * 
 * @date Sep 6, 2012
 * @author dgasull
 */
package es.bsc.dataclay.dbhandler;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import es.bsc.dataclay.dbhandler.DBHandlerFactory.DBHandlerType;
import es.bsc.dataclay.dbhandler.sql.SQLHandler;
import es.bsc.dataclay.dbhandler.sql.postgres.PostgresHandler;
import es.bsc.dataclay.dbhandler.sql.postgres.PostgresHandlerConf;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteHandlerConfig;
import es.bsc.dataclay.util.Configuration.Flags;

/**
 * @brief This class tests the openings, closes and basic operations of the DbHandler class.
 */
public final class SQLHandlerTest {

	/** Tested database. */
	private SQLHandler testDb;

	/** DbHandler common tester. */
	private DbHandlerCommonTester commonTester;

	/** Host IP. */
	private static String HOSTIP = "127.0.0.1";

	final private static DBHandlerType dbHandlerType = (DBHandlerType) Flags.DB_HANDLER_TYPE_FOR_LOGICMODULE.getValue();

	private SQLHandler<?> initDBHandler() {
		switch (dbHandlerType) {
		case POSTGRES:
			return new PostgresHandler(new PostgresHandlerConf(HOSTIP, 5432, "postgres", "postgres", "postgres"));
		case SQLITE:
			return (SQLHandler<?>) new SQLiteHandlerConfig("test", true).getDBHandler();
		default:
			throw new IllegalArgumentException(dbHandlerType + " not yet supported");
		}
	}

	@Before
	public void before() {
		testDb = initDBHandler();
		testDb.open();
		testDb.dropAllDatabases();
		testDb.createTables();
		commonTester = new DbHandlerCommonTester(testDb);
	}

	@After
	public void after() throws Exception {
		testDb.close();
		// testDb.dropDatabase();
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
