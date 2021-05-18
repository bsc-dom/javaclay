
package es.bsc.dataclay.logic.accountmgr;

import java.util.ResourceBundle;

/**
 * This class represents SQL statements for Account Manager.
 */
public final class AccountMgrSQLStatements {


	/**
	 * Utility classes should have private constructor.
	 */
	private AccountMgrSQLStatements() {

	}

	/**
	 * This enumeration represent all possible SQL statements.
	 */
	public enum SqlStatements {

		/** Create credentials table. */
		CREATE_TABLE_CREDENTIAL,

		/** Create account table. */
		CREATE_TABLE_ACCOUNT,

		/** Drop credential table. */
		DROP_TABLE_CREDENTIAL,

		/** Drop Account table. */
		DROP_TABLE_ACCOUNT,

		/** Insert credential. */
		INSERT_CREDENTIAL,

		/** Insert account. */
		INSERT_ACCOUNT,

		/** Exists account by name. */
		EXISTS_ACCOUNT_BY_NAME,

		/** Select account. */
		SELECT_ACCOUNT,

		/** Get all normal accounts. */
		SELECT_ALL_NORMAL_ACCOUNTS,

		/** Select credential. */
		SELECT_CREDENTIAL,

		/** Select account by name. */
		SELECT_ACCOUNT_BY_NAME,

		/** Select account by id. */
		EXISTS_ACCOUNT_BY_ID;

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
			ResourceBundle props = ResourceBundle.getBundle("es.bsc.dataclay.properties.account_mgr_sql");
			for (AccountMgrSQLStatements.SqlStatements statement : AccountMgrSQLStatements.SqlStatements.values()) {
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
