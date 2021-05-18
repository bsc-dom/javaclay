
package es.bsc.dataclay.logic.namespacemgr;

import es.bsc.dataclay.logic.accountmgr.AccountMgrSQLStatements;
import es.bsc.dataclay.logic.logicmetadata.LogicMetadataSQLStatements;

import java.util.ResourceBundle;

/**
 * This class represents SQL statements for Namespace Manager.
 */
public final class NamespaceManagerSQLStatements {



	/**
	 * Utility classes should have private constructor.
	 */
	private NamespaceManagerSQLStatements() {

	}

	/**
	 * This enumeration represent all possible SQL statements.
	 */
	public enum SqlStatements {

		/** Create. */
		CREATE_TABLE_IMPORTEDIFACE,
		
		/** Create. */
		CREATE_TABLE_NAMESPACE,
		
		/** Drop. */
		DROP_TABLE_IMPORTEDIFACE,

		/** Drop. */
		DROP_TABLE_NAMESPACE,
		
		/** Insert. */
		INSERT_IMPORTEDIFACE,

		/** Insert. */
		INSERT_NAMESPACE,

		/** Select. */
		SELECT_IMPORTEDIFACE,

		/** Select. */
		SELECT_NAMESPACE,
		
		/** Select. */
		SELECT_NAMESPACES_NAMES,
		
		/** Delete. */
		DELETE_IMPORTEDIFACE,

		/** Delete. */
		DELETE_NAMESPACE,
		
		/** Select. */
		SELECT_NAMESPACE_BY_NAME,

		/** Select. */
		UPDATE_NAMESPACE_ADD_IMPORTEDIFACE, 
		
		/** Select. */
		UPDATE_NAMESPACE_REMOVE_IMPORTEDIFACE, 
		
		/** Select. */
		SELECT_ALL_NAMESPACES_OF_ACCOUNT,
		
		/** Select. */
		SELECT_ALL_NAMESPACES_IMPORT_IFACE,
		
		/** Select. */
		SELECT_ALL_NAMESPACES_OF_ACCOUNT_AND_ID,
		
		/** Update. */
		UPDATE_IMPORTEDIFACE_ADD_PROPERTY, 
		
		/** Update. */
		UPDATE_IMPORTEDIFACE_ADD_OPERATION, 
		
		/** Update. */
		UPDATE_IMPORTEDIFACE_ADD_IMPLEMENTATION, 
		
		/** Update. */
		UPDATE_IMPORTEDIFACE_ADD_SUBCLASS, 
		
		/** Update. */
		UPDATE_IMPORTEDIFACE_REMOVE_PROPERTY, 
		
		/** Update. */
		UPDATE_IMPORTEDIFACE_REMOVE_OPERATION, 
		
		/** Update. */
		UPDATE_IMPORTEDIFACE_REMOVE_IMPLEMENTATION, 
		
		/** Update. */
		UPDATE_IMPORTEDIFACE_REMOVE_SUBCLASS, 
		;

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
			ResourceBundle props = ResourceBundle.getBundle("es.bsc.dataclay.properties.namespace_mgr_sql");

			for (NamespaceManagerSQLStatements.SqlStatements statement : NamespaceManagerSQLStatements.SqlStatements.values()) {
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
