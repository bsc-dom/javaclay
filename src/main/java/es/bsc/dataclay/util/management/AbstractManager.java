
package es.bsc.dataclay.util.management;

import org.apache.commons.dbcp2.BasicDataSource;

import es.bsc.dataclay.util.CommonManager;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteDataSource;

public abstract class AbstractManager implements CommonManager{
	protected final SQLiteDataSource dataSource;
	
	public AbstractManager(SQLiteDataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public abstract void cleanCaches();

}
