
package es.bsc.dataclay.logic.contractmgr;

import es.bsc.dataclay.logic.accountmgr.AccountMgrSQLStatements;
import es.bsc.dataclay.logic.classmgr.ClassManagerSQLStatements;

import java.util.ResourceBundle;

/**
 * This class represents SQL statements for Account Manager.
 */
public final class ContractManagerSQLStatements {


	/**
	 * Utility classes should have private constructor.
	 */
	private ContractManagerSQLStatements() {

	}

	/**
	 * This enumeration represent all possible SQL statements.
	 */
	public enum SqlStatements {

		/** Create. */
		CREATE_TABLE_CONTRACT,

		/** Create. */
		CREATE_TABLE_IFACEINCONTRACT,

		/** Create. */
		CREATE_TABLE_OPIMPLEMENTATIONS,

		/** Drop. */
		DROP_TABLE_CONTRACT,

		/** Drop. */
		DROP_TABLE_IFACEINCONTRACT,

		/** Drop. */
		DROP_TABLE_OPIMPLEMENTATIONS,

		/** Insert. */
		INSERT_CONTRACT,

		/** Insert. */
		INSERT_IFACEINCONTRACT,

		/** Insert. */
		INSERT_OPIMPLEMENTATIONS,

		/** Select account. */
		SELECT_CONTRACT,

		/** Select. */
		SELECT_IFACEINCONTRACT,

		/** Select. */
		SELECT_OPIMPLEMENTATIONS,

		/** Delete. */
		DELETE_CONTRACT,

		/** Delete. */
		DELETE_IFACEINCONTRACT,

		/** Delete. */
		DELETE_OPIMPLEMENTATIONS,

		/** Update. */
		UPDATE_CONTRACT_ADD_APPLICANT,

		/** Select. */
		SELECT_ALL_CONTRACTS_OF_NAMESPACE,

		/** Select. */
		SELECT_ALL_CONTRACTS_WITH_INTERFACE,

		/** Select. */
		SELECT_ALL_OPIMPLS_WITH_IMPL,

		/** Select. */
		SELECT_ALL_CONTRACTS_WITH_APPLICANT,

		/** Select. */
		SELECT_ALL_CONTRACTS_WITH_PROVIDER,

		/** Select. */
		SELECT_ALL_CONTRACTS_WITH_APPLICANT_AND_NAMESPACE;

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
			ResourceBundle props = ResourceBundle.getBundle("es.bsc.dataclay.properties.contract_mgr_sql");

			for (ContractManagerSQLStatements.SqlStatements statement : ContractManagerSQLStatements.SqlStatements.values()) {
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
