
package es.bsc.dataclay.logic.logicmetadata;

import java.util.ResourceBundle;

/**
 * This class represents SQL statements.
 */
public final class LogicMetadataSQLStatements {

	/** Properties. */
	private static ResourceBundle props = null;

	/**
	 * Utility classes should have private constructor.
	 */
	private LogicMetadataSQLStatements() {

	}

	/**
	 * Init properties of the properties file
	 */
	static {
		if (props == null) {
			props = ResourceBundle.getBundle("es.bsc.dataclay.properties.logicmodule_metadata_sql");
		}
	}

	/**
	 * This enumeration represent all possible SQL statements.
	 */
	public enum SqlStatements {

		/** Create table. */
		CREATE_TABLE_LOGICMODULE,

		/** Drop table. */
		DROP_TABLE_LOGICMODULE,

		/** Insert. */
		INSERT_LOGICMODULE,

		/** Exists. */
		EXISTS_LOGICMODULE_BY_ID,

		/** Select. */
		SELECT_LOGICMODULE;

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
