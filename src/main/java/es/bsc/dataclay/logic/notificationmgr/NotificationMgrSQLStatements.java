
package es.bsc.dataclay.logic.notificationmgr;

import java.util.ResourceBundle;

/**
 * This class represents SQL statements.
 */
public final class NotificationMgrSQLStatements {

	/** Properties. */
	private static ResourceBundle props = null;

	/**
	 * Utility classes should have private constructor.
	 */
	private NotificationMgrSQLStatements() {

	}

	/**
	 * Init properties of the properties file
	 */
	static {
		if (props == null) {
			props = ResourceBundle.getBundle("es.bsc.dataclay.properties.notification_mgr_sql");
		}
	}

	/**
	 * This enumeration represent all possible SQL statements.
	 */
	public enum SqlStatements {

		/** Create Listeners table. */
		CREATE_TABLE_EVENT_LISTENERS,

		/** Create messages table. */
		CREATE_TABLE_EVENT_MESSAGES,

		/** Drop listeners table. */
		DROP_TABLE_EVENT_LISTENERS,

		/** Drop messages table. */
		DROP_TABLE_EVENT_MESSAGES,

		/** Insert ECA sql query. */
		INSERT_ECA,

		/** Insert Message sql query. */
		INSERT_MESSAGE,

		/** Delete message. */
		DELETE_MESSAGE,

		/** Update ECA. */
		UPDATE_ECA,

		/** Update message. */
		UPDATE_MESSAGE,

		/** Select all messages. */
		SELECT_ALL_MESSAGES,

		/** Select all Ecas. */
		SELECT_ALL_ECAS,

		/** Select all ecas limited. */
		SELECT_ALL_ECAS_LIMITED,

		/** Select all messages limited. */
		SELECT_ALL_MESSAGES_LIMITED;

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
