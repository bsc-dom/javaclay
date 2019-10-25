
package es.bsc.dataclay.logic.interfacemgr;

import java.util.ResourceBundle;

/**
 * This class represents SQL statements for Interface Manager.
 */
public final class InterfaceManagerSQLStatements {

	/** Properties. */
	private static ResourceBundle props = null;

	/**
	 * Utility classes should have private constructor.
	 */
	private InterfaceManagerSQLStatements() {

	}

	/**
	 * Init properties of the properties file
	 */
	static {
		if (props == null) {
			props = ResourceBundle.getBundle("es.bsc.dataclay.properties.interface_mgr_sql");
		}
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

		/**
		 * Init properties of the properties file
		 */
		private void init() {
			final String sqlSt = props.getString(this.name());
			setSqlStatement(sqlSt);
		}

		/**
		 * Get the sqlStatement
		 * @return the sqlStatement
		 */
		public String getSqlStatement() {
			if (sqlStatement == null) {
				init();
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
