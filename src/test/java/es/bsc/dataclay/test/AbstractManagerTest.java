
package es.bsc.dataclay.test;

import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteDataSource;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.FileAndAspectsUtils;
import org.apache.commons.dbcp2.BasicDataSource;

import es.bsc.dataclay.dbhandler.DBHandlerFactory.DBHandlerType;
import es.bsc.dataclay.dbhandler.sql.SQLHandler;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteHandlerConfig;
import es.bsc.dataclay.util.Configuration.Flags;

import java.io.File;

public class AbstractManagerTest {
	protected SQLHandler<?> dbHandler;
	protected SQLiteDataSource dataSource;
	final private static DBHandlerType dbHandlerType = (DBHandlerType) Flags.DB_HANDLER_TYPE_FOR_LOGICMODULE.getValue();

	public void before() {
		Configuration.Flags.STORAGE_PATH.setValue(System.getProperty("user.dir") + "/dbfiles");
		dbHandler = initDBHandler();
		dbHandler.open();
		dataSource = dbHandler.getDataSource();
	}

	private SQLHandler<?> initDBHandler() {
		switch (dbHandlerType) {
		case SQLITE:
			return (SQLHandler<?>) new SQLiteHandlerConfig("test", false).getDBHandler();
		default:
			throw new IllegalArgumentException(dbHandlerType + " not yet supported");
		}
	}

	public void after() throws Exception {
		dataSource.close();
		dbHandler.close();
		FileAndAspectsUtils.deleteFolderContent(new File(Configuration.Flags.STORAGE_PATH.getStringValue()));
	}
}
