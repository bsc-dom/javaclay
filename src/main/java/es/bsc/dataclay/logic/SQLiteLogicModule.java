package es.bsc.dataclay.logic;

import es.bsc.dataclay.dbhandler.sql.SQLHandler;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteHandlerConfig;

public class SQLiteLogicModule extends LogicModule<SQLiteHandlerConfig>{
	private final boolean inMemory;

	public SQLiteLogicModule(final String lmName, final String thehostname, final int theport, 
			final boolean inMemory, final String theexposedIPForClient) throws InterruptedException {
		super(lmName, thehostname, theport, theexposedIPForClient);
		this.inMemory = inMemory;
	}

	@Override
	protected SQLiteHandlerConfig initDBConf() {
		return new SQLiteHandlerConfig(name, false);
	}

	@Override
	protected SQLHandler<SQLiteHandlerConfig> initDBHandler() {
		final SQLHandler<SQLiteHandlerConfig> dbHandler = (SQLHandler)dbConf.getDBHandler();
		dbHandler.open();
		return dbHandler;
	}

}
