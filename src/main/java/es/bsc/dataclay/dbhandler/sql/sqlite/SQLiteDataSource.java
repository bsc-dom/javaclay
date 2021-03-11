package es.bsc.dataclay.dbhandler.sql.sqlite;

import org.apache.commons.dbcp2.DelegatingConnection;
import org.apache.commons.dbcp2.PoolableConnection;
import org.sqlite.Function;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

// Solves locking due to managers are opening more than one connection from the datasource
public class SQLiteDataSource {

    private ExtendedSQLiteConnection connection = null;
    private String url = null;
    public SQLiteDataSource(final String theurl) {
        url = theurl;
    }


    public Connection getConnection() throws SQLException {

        //CONNECTION = new ExtendedSQLiteConnection(url, extractAddress(url), prop);

            if (!org.sqlite.JDBC.isValidURL(url))
                return null;
            Properties props = new Properties();
            props.setProperty("journal_mode", "MEMORY");
            props.setProperty("synchronous", "OFF");
            url = url.trim();

            String addr = extractAddress(url);
        ExtendedSQLiteConnection conn = new ExtendedSQLiteConnection(url,
                extractAddress(url), props);
            // Getting the original SQLiteConnection to add the CHR function
            //final PoolableConnection poolableConn = (PoolableConnection) ((DelegatingConnection<?>) connection).getDelegate();
            Function.create(conn, "CHR", CHR);
            //connection = new UncloseableConnection(conn);

            return conn;
    }


    public static String extractAddress(String url) {
        // if no file name is given use a memory database
        return org.sqlite.JDBC.PREFIX.equalsIgnoreCase(url) ? ":memory:" : url.substring(org.sqlite.JDBC.PREFIX.length());
    }

    //@Override
    public boolean isClosed() {
        return false;
    }

    // Setting the delegating connection accessible
    /**@Override
    protected DataSource createDataSourceInstance() throws SQLException {
    final PoolingDataSource<?> pds = (PoolingDataSource<?>)super.createDataSourceInstance();
    pds.setAccessToUnderlyingConnectionAllowed(true);
    return pds;
    }**/

    //@Override
    public void close() throws SQLException {
        // Ignore, needed because managers are closing the only possible sqlite connection
        /**if (numConnections.get() == 0) {
         System.err.println("CLOSING UNCLOSEABLE CONNECTION !!!!!!!!!!!");
         super.close();
         }
         numConnections.decrementAndGet();**/
    }

    private static final Function CHR = new Function(){
        // The CHR function is equivalent to sqlite's char function
        // and it's created for compatibility with Postgres CHR
        @Override
        protected void xFunc() throws SQLException {
            result((char)value_int(0) + "");
        }
    };
}