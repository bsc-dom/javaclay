package es.bsc.dataclay.logic;

import es.bsc.dataclay.dbhandler.sql.SQLHandler;
import es.bsc.dataclay.dbhandler.sql.postgres.PostgresHandler;
import es.bsc.dataclay.dbhandler.sql.postgres.PostgresHandlerConf;
import es.bsc.dataclay.util.configs.CfgPostgresConnEnvLoader;

public class PostgresLogicModule extends LogicModule<PostgresHandlerConf>{

	public PostgresLogicModule(final String lmName, final String thehostname, final int theport, 
			final String theexposedIPForClient) throws InterruptedException {
		super(lmName, thehostname, theport, theexposedIPForClient);
	}

	@Override
	protected PostgresHandlerConf initDBConf() {
		return CfgPostgresConnEnvLoader.parsePostgresConn();
	}

	@Override
	protected SQLHandler<PostgresHandlerConf> initDBHandler() {
		final SQLHandler<PostgresHandlerConf> dbHandler = (PostgresHandler)dbConf.getDBHandler();
		dbHandler.open();
		return dbHandler;
	}
}
