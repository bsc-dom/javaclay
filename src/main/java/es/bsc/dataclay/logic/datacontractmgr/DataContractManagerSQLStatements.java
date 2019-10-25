
package es.bsc.dataclay.logic.datacontractmgr;

import java.util.ResourceBundle;

/**
 * This class represents SQL statements for Data Set Manager.
 */
public final class DataContractManagerSQLStatements {

	/** Properties. */
	private static ResourceBundle props = null;

	/**
	 * Utility classes should have private constructor.
	 */
	private DataContractManagerSQLStatements() {

	}

	/**
	 * Init properties of the properties file
	 */
	static {
		if (props == null) {
			props = ResourceBundle.getBundle("es.bsc.dataclay.properties.datacontract_mgr_sql");
		}
	}

	/**
	 * This enumeration represent all possible SQL statements.
	 */
	public enum SqlStatements {

		/** Create. */
		CREATE_TABLE_DATACONTRACT,

		/** Drop. */
		DROP_TABLE_DATACONTRACT,

		/** Insert. */
		INSERT_DATACONTRACT,

		/** Select account. */
		SELECT_DATACONTRACT,

		/** Delete. */
		DELETE_DATACONTRACT,

		/** Update. */
		UPDATE_DATACONTRACT_ADD_APPLICANT,

		/** Select. */
		SELECT_ALL_DATACONTRACTS_OF_DATASET,

		/** Select. */
		SELECT_ALL_DATACONTRACTS_WITH_APPLICANT,

		/** Select. */
		SELECT_ALL_DATACONTRACTS_WITH_PROVIDER,

		/** Select. */
		SELECT_ALL_DATACONTRACTS_WITH_APPLICANT_AND_DATASET;

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
