
package es.bsc.dataclay.logic.interfacemgr;

import es.bsc.dataclay.logic.accountmgr.AccountMgrSQLStatements;
import es.bsc.dataclay.logic.datasetmgr.DataSetManagerSQLStatements;

import java.util.ResourceBundle;

/**
 * This class represents SQL statements for Interface Manager.
 */
public final class InterfaceManagerSQLStatements {


	/**
	 * Utility classes should have private constructor.
	 */
	private InterfaceManagerSQLStatements() {

	}

	/**
	 * This enumeration represent all possible SQL statements.
	 */
	public enum SqlStatements {

		/** Create. */
		CREATE_TABLE_INTERFACE,

		/** Drop table. */
		DROP_TABLE_INTERFACE,

		/** Insert. */
		INSERT_INTERFACE,

		/** Select. */
		SELECT_INTERFACE, 
		
		/** Delete. */
		DELETE_INTERFACE, 
		
		/** Select. */
		SELECT_IFACES_OF_CLASS,
		
		/** Select. */
		SELECT_IFACE_FROM_NAMES;

		/** SQL statement. */
		private String sqlStatement;

		/** Indicates statements are loaded in memory. */
		private static boolean LOADED = false;
		/**
		 * Unload statements.
		 */
		public static void unloadStatements() {
			LOADED = false;
			for (AccountMgrSQLStatements.SqlStatements statement : AccountMgrSQLStatements.SqlStatements.values()) {
				statement.setSqlStatement(null);
			}
		}


		/**
		 * Init properties of the properties file
		 */
		public static void loadStatements() {
			ResourceBundle props = ResourceBundle.getBundle("es.bsc.dataclay.properties.interface_mgr_sql");

			for (InterfaceManagerSQLStatements.SqlStatements statement : InterfaceManagerSQLStatements.SqlStatements.values()) {
				statement.init(props);
			}
			LOADED = true;
		}

		/**
		 * Init properties of the properties file
		 */
		private void init(ResourceBundle props) {
			final String sqlSt = props.getString(this.name());
			setSqlStatement(sqlSt);
		}

		/**
		 * Get the sqlStatement
		 * @return the sqlStatement
		 */
		public String getSqlStatement() {
			if (!LOADED) {
				loadStatements();
			}
			return sqlStatement;
		}

		/**
		 * Set the sqlStatement
		 * @param newsqlStatement
		 *            the sqlStatement
		 */
		public void setSqlStatement(final String newsqlStatement) {
			sqlStatement = newsqlStatement;
		}

	}
}
