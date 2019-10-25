
package es.bsc.dataclay.dbhandler.sql;

import java.util.ResourceBundle;

/**
 * This class represents SQL statements for Account Manager.
 */
public final class DataServiceDBSQLStatements {

	/** Properties. */
	private static ResourceBundle props = null;

	/**
	 * Utility classes should have private constructor.
	 */
	private DataServiceDBSQLStatements() {

	}

	/**
	 * Init properties of the properties file
	 */
	static {
		if (props == null) {
			props = ResourceBundle.getBundle("es.bsc.dataclay.properties.dataservice_db_sql");
		}
	}

	/**
	 * This enumeration represent all possible SQL statements.
	 */
	public enum SqlStatements {

		/** Create DS table. */
		CREATE_TABLE_DATASERVICE,

		/** Drop DS table. */
		DROP_TABLE_DATASERVICE,

		/** Insert object sql query. */
		INSERT_OBJECT,

		/** Select object sql query. */
		SELECT_OBJECT,

		/** Exists object query. */
		EXISTS_OBJECT,

		/** Delete object. */
		DELETE_OBJECT,

		/** Update object. */
		UPDATE_OBJECT;

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
