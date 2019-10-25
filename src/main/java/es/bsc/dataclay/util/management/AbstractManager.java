
package es.bsc.dataclay.util.management;

import org.apache.commons.dbcp2.BasicDataSource;

import es.bsc.dataclay.util.CommonManager;

public abstract class AbstractManager implements CommonManager{
	protected final BasicDataSource dataSource;
	
	public AbstractManager(BasicDataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public abstract void cleanCaches();

}
