
package es.bsc.dataclay.test;

import org.apache.commons.dbcp2.BasicDataSource;

import es.bsc.dataclay.dbhandler.DBHandlerFactory.DBHandlerType;
import es.bsc.dataclay.dbhandler.sql.SQLHandler;
import es.bsc.dataclay.dbhandler.sql.postgres.PostgresHandlerConf;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteHandlerConfig;
import es.bsc.dataclay.util.Configuration.Flags;

public class AbstractManagerTest {
	protected SQLHandler<?> dbHandler;
	protected BasicDataSource dataSource;

	private final static String POSTGRES_HOST = "127.0.0.1";
	private final static int POSTGRES_PORT = 5432;
	private final static String POSTGRES_USER = "postgres";
	private final static String POSTGRES_PASSWORD = "postgres";
	private final static String POSTGRES_DBNAME = "postgres";

	final private static DBHandlerType dbHandlerType = (DBHandlerType) Flags.DB_HANDLER_TYPE_FOR_LOGICMODULE.getValue();

	public void before() {
		dbHandler = initDBHandler();
		dbHandler.open();
		dataSource = dbHandler.getDataSource();
	}

	private SQLHandler<?> initDBHandler() {
		switch (dbHandlerType) {
		case POSTGRES:
			return (SQLHandler<?>) new PostgresHandlerConf(POSTGRES_HOST, POSTGRES_PORT, POSTGRES_USER, POSTGRES_PASSWORD, POSTGRES_DBNAME).getDBHandler();
		case SQLITE:
			return (SQLHandler<?>) new SQLiteHandlerConfig("test", true).getDBHandler();
		default:
			throw new IllegalArgumentException(dbHandlerType + " not yet supported");
		}
	}

	public void after() throws Exception {
		dataSource.close();
		dbHandler.close();
	}
}
