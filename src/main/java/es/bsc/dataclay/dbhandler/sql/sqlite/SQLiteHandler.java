
package es.bsc.dataclay.dbhandler.sql.sqlite;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.DelegatingConnection;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sqlite.Function;

import es.bsc.dataclay.dbhandler.sql.SQLHandler;
import es.bsc.dataclay.dbhandler.sql.common.ConnectionWrapper;

public class SQLiteHandler extends SQLHandler<SQLiteHandlerConfig> {
	
	private static final Logger LOGGER = LogManager.getLogger(SQLiteHandler.class);

	public SQLiteHandler(final SQLiteHandlerConfig connConfig) {
		super(connConfig);
	}

	@Override
	protected void initDataSource() {
		if (dataSource == null) {
			String url = "jdbc:sqlite:";
			if(configuration.isInMemory()) {
				url += ":memory:";
			}else {
				url  += configuration.getDbPath();
			}
			LOGGER.debug("SQLITE DB URL = " + url);
			dataSource = new SQLiteDataSource(url);
		}
	}

	@Override
	public boolean databaseExists() {
		return new File(configuration.getDbPath()).exists();
	}

	@Override
	public void createDatabase() {

	}

	@Override
	public void dropAllDatabases() {
		try {
			FileUtils.deleteDirectory(new File(configuration.getFolder()));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
