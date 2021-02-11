package es.bsc.dataclay.dbhandler.sql.sqlite;

import es.bsc.dataclay.dbhandler.sql.common.SQLArray;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class ExtendedSQLiteConnection extends org.sqlite.SQLiteConnection{

    public ExtendedSQLiteConnection(String url, String fileName, Properties info) throws SQLException {
        super(url, fileName, info);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int rst, int rsc, int rsh) throws SQLException {
        checkOpen();
        checkCursor(rst, rsc, rsh);
        ExtendedPreparedStatement preparedStatement = new ExtendedPreparedStatement(this, sql);
        return preparedStatement;
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return new SQLArray(typeName, elements);
    }
}
