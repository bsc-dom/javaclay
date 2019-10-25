package es.bsc.dataclay.dbhandler.sql.postgres;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import es.bsc.dataclay.dbhandler.sql.common.SQLConnectionWrapper;

public class ExtendedPostgresDriver extends org.postgresql.Driver{
	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		return new SQLConnectionWrapper(super.connect(url, info));
	}
}