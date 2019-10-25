
package es.bsc.dataclay.logic.sessionmgr;

import java.util.ResourceBundle;

/**
 * This class represents SQL statements for Session Manager.
 */
public final class SessionManagerSQLStatements {

	/** Properties. */
	private static ResourceBundle props = null;

	/**
	 * Utility classes should have private constructor.
	 */
	private SessionManagerSQLStatements() {

	}

	/**
	 * Init properties of the properties file
	 */
	static {
		if (props == null) {
			props = ResourceBundle.getBundle("es.bsc.dataclay.properties.session_mgr_sql");
		}
	}

	/**
	 * This enumeration represent all possible SQL statements.
	 */
	public enum SqlStatements {

		/** Create. */
		CREATE_TABLE_SESSIONCONTRACT,
		
		/** Create. */
		CREATE_TABLE_SESSIONINTERFACE,
		
		/** Create. */
		CREATE_TABLE_SESSIONPROPERTY,
		
		/** Create. */
		CREATE_TABLE_SESSIONOPERATION,
		
		/** Create. */
		CREATE_TABLE_SESSIONIMPLEMENTATION,
		
		/** Create. */
		CREATE_TABLE_SESSIONDATACONTRACT,
		
		/** Create. */
		CREATE_TABLE_SESSIONINFO,

		/** Drop table. */
		DROP_TABLE_SESSIONCONTRACT,
		
		/** Drop table. */
		DROP_TABLE_SESSIONINTERFACE,

		/** Drop table. */
		DROP_TABLE_SESSIONPROPERTY,
		
		/** Drop table. */
		DROP_TABLE_SESSIONOPERATION,
		
		/** Drop table. */
		DROP_TABLE_SESSIONIMPLEMENTATION,
		
		/** Drop table. */
		DROP_TABLE_SESSIONDATACONTRACT,
		
		/** Drop table. */
		DROP_TABLE_SESSIONINFO,
		
		/** Insert. */
		INSERT_SESSIONCONTRACT,

		/** Insert. */
		INSERT_SESSIONINTERFACE,
		
		/** Insert. */
		INSERT_SESSIONPROPERTY,
		
		/** Insert. */
		INSERT_SESSIONOPERATION,
		
		/** Insert. */
		INSERT_SESSIONIMPLEMENTATION,
		
		/** Insert. */
		INSERT_SESSIONDATACONTRACT,
		
		/** Insert. */
		INSERT_SESSIONINFO,
		
		/** Select. */
		SELECT_SESSIONCONTRACT,

		/** Select. */
		SELECT_SESSIONINTERFACE, 
		
		/** Select. */
		SELECT_SESSIONPROPERTY,
		
		/** Select. */
		SELECT_SESSIONOPERATION, 
		
		/** Select. */
		SELECT_SESSIONIMPLEMENTATION, 
		
		/** Select. */
		SELECT_SESSIONDATACONTRACT, 
		
		/** Select. */
		SELECT_SESSIONINFO, 
		
		/** Delete. */
		DELETE_SESSIONCONTRACT,
		
		/** Delete. */
		DELETE_SESSIONINTERFACE,
		
		/** Delete. */
		DELETE_SESSIONPROPERTY,
		
		/** Delete. */
		DELETE_SESSIONOPERATION,
		
		/** Delete. */
		DELETE_SESSIONIMPLEMENTATION,
		
		/** Delete. */
		DELETE_SESSIONDATACONTRACT,
		
		/** Delete. */
		DELETE_SESSIONINFO,
		
		/** Select. */
		SELECT_SESSIONS_OF_ACCOUNT,
		
		/** Create external dataClay sessions. */
		CREATE_TABLE_EXT_SESSIONINFO,
		
		/** Insert external dataClay session. */
		INSERT_EXT_SESSION,
		
		/** Select external dataClay session. */
		SELECT_EXT_SESSION,
		
		/** Delete external dataClay session. */
		DELETE_EXT_SESSION;

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
