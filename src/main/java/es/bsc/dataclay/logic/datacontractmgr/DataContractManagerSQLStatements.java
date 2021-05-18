
package es.bsc.dataclay.logic.datacontractmgr;

import es.bsc.dataclay.logic.accountmgr.AccountMgrSQLStatements;
import es.bsc.dataclay.logic.contractmgr.ContractManagerSQLStatements;

import java.util.ResourceBundle;

/**
 * This class represents SQL statements for Data Set Manager.
 */
public final class DataContractManagerSQLStatements {


	/**
	 * Utility classes should have private constructor.
	 */
	private DataContractManagerSQLStatements() {

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
			ResourceBundle props = ResourceBundle.getBundle("es.bsc.dataclay.properties.datacontract_mgr_sql");

			for (DataContractManagerSQLStatements.SqlStatements statement : DataContractManagerSQLStatements.SqlStatements.values()) {
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
