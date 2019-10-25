
package es.bsc.dataclay.logic.notificationmgr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;

import es.bsc.dataclay.exceptions.logicmodule.notificationmgr.EventListenerAlreadyRegisteredException;
import es.bsc.dataclay.exceptions.logicmodule.notificationmgr.EventListenerNotRegisteredException;
import es.bsc.dataclay.exceptions.logicmodule.notificationmgr.EventMessageAlreadyStoredException;
import es.bsc.dataclay.exceptions.logicmodule.notificationmgr.EventMessageNotRegisteredException;
import es.bsc.dataclay.util.events.listeners.ECA;
import es.bsc.dataclay.util.events.message.EventMessage;
import es.bsc.dataclay.util.ids.ECAID;
import es.bsc.dataclay.util.ids.EventMessageID;
import es.bsc.dataclay.util.yaml.CommonYAML;

/**
 * LogicModule data base.
 */
public final class NotificationManagerDB {

	/** Data source. */
	private final BasicDataSource dataSource;

	/**
	 * NotificationManagerDB constructor.
	 * 
	 * @param managerName
	 *            Name of the LM service managing.
	 */
	public NotificationManagerDB(final BasicDataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Create tables of MDS.
	 */
	public void createTables() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			for (final NotificationMgrSQLStatements.SqlStatements stmt : NotificationMgrSQLStatements.SqlStatements.values()) {
				if (stmt.name().startsWith("CREATE_TABLE")) {
					final PreparedStatement updateStatement = conn.prepareStatement(stmt.getSqlStatement());
					updateStatement.execute();
				}
			}
		} catch (final SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				ex1.printStackTrace();
			}
		}
	}

	/**
	 * Delete the tables of MDS. Just the other way around of createTables --much simpler.
	 */
	public void dropTables() {

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			for (final NotificationMgrSQLStatements.SqlStatements stmt : NotificationMgrSQLStatements.SqlStatements.values()) {
				if (stmt.name().startsWith("DROP_TABLE")) {
					final PreparedStatement updateStatement = conn.prepareStatement(stmt.getSqlStatement());
					updateStatement.execute();
				}
			}
		} catch (final SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				ex1.printStackTrace();
			}
		}
	}

	/**
	 * Store a EventListenerImpl into database
	 * @param eventListenerImpl
	 *            EventListenerImpl
	 */
	public void store(final ECA eventListenerImpl) {
		// Serialize
		final String strYaml = CommonYAML.getYamlObject().dump(eventListenerImpl);
		final byte[] bytes = strYaml.getBytes();
		final ECAID id = eventListenerImpl.getId();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement insertStatement = conn.prepareStatement(NotificationMgrSQLStatements.SqlStatements.INSERT_ECA.getSqlStatement());

			insertStatement.setObject(1, id.getId());
			// CHECKSTYLE:OFF
			insertStatement.setBytes(2, bytes);
			// CHECKSTYLE:ON
			insertStatement.executeUpdate();

		} catch (final Exception e) {
			e.printStackTrace();
			throw new EventListenerAlreadyRegisteredException(id);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Store a EventMessage into database
	 * @param eventMessage
	 *            EventMessage
	 */
	public void store(final EventMessage eventMessage) {
		// CHECKSTYLE:OFF

		// Serialize
		final String strYaml = CommonYAML.getYamlObject().dump(eventMessage);
		final byte[] bytes = strYaml.getBytes();

		final EventMessageID id = eventMessage.getId();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement insertStatement = conn.prepareStatement(NotificationMgrSQLStatements.SqlStatements.INSERT_MESSAGE.getSqlStatement());

			insertStatement.setObject(1, id.getId());
			// CHECKSTYLE:OFF
			insertStatement.setBytes(2, bytes);
			// CHECKSTYLE:ON
			insertStatement.executeUpdate();

		} catch (final Exception e) {
			throw new EventMessageAlreadyStoredException(id);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}
		// CHECKSTYLE:ON
	}

	/**
	 * Delete event message identified by ID provided
	 * @param eventMessageID
	 *            ID of the event message
	 */
	public void deleteByID(final EventMessageID eventMessageID) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement deleteStatement = conn.prepareStatement(NotificationMgrSQLStatements.SqlStatements.DELETE_MESSAGE.getSqlStatement());

			deleteStatement.setObject(1, eventMessageID.getId());
			deleteStatement.executeUpdate();
		} catch (final SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Update eventListener
	 * @param newEventListener
	 *            Event Listener with new values.
	 */
	public void updateByIDEventListener(final ECA newEventListener) {
		// CHECKSTYLE:OFF
		// Serialize
		final String strYaml = CommonYAML.getYamlObject().dump(newEventListener);
		final byte[] bytes = strYaml.getBytes();
		final ECAID id = newEventListener.getId();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement updateStatement = conn.prepareStatement(NotificationMgrSQLStatements.SqlStatements.UPDATE_ECA.getSqlStatement());

			updateStatement.setBytes(1, bytes);
			updateStatement.setObject(2, id.getId());
			updateStatement.executeUpdate();
		} catch (final SQLException e) {
			throw new EventListenerNotRegisteredException(id);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}
		// CHECKSTYLE:ON
	}

	/**
	 * Update event message
	 * @param newEventMessage
	 *            event message with new values.
	 */
	public void updateByIDEventMessage(final EventMessage newEventMessage) {
		// CHECKSTYLE:OFF

		// Serialize
		final String strYaml = CommonYAML.getYamlObject().dump(newEventMessage);
		final byte[] bytes = strYaml.getBytes();
		final EventMessageID id = newEventMessage.getId();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement updateStatement = conn.prepareStatement(NotificationMgrSQLStatements.SqlStatements.UPDATE_MESSAGE.getSqlStatement());

			updateStatement.setBytes(1, bytes);
			updateStatement.setObject(2, id.getId());
			updateStatement.executeUpdate();
		} catch (final SQLException e) {
			throw new EventMessageNotRegisteredException(id);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}
		// CHECKSTYLE:ON
	}

	/**
	 * Get all Event listeners
	 * @return The Event listeners
	 */
	public List<ECA> getAllEventListeners() {

		ResultSet rs = null;
		Connection conn = null;
		final List<ECA> resultList = new ArrayList<>();
		try {
			conn = dataSource.getConnection();
			final PreparedStatement statement = conn.prepareStatement(NotificationMgrSQLStatements.SqlStatements.SELECT_ALL_ECAS.getSqlStatement());
			rs = statement.executeQuery();

			while (rs.next()) {
				final byte[] bytes = rs.getBytes(2);
				final String strYaml = new String(bytes);
				final ECA eventListener = (ECA) CommonYAML.getYamlObject().load(strYaml);
				resultList.add(eventListener);
			}

		} catch (final SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}

		return resultList;
	}

	/**
	 * Get all Event Messages
	 * @return The Event Messages
	 */
	public List<EventMessage> getAllEventMessages() {
		ResultSet rs = null;
		final List<EventMessage> resultList = new ArrayList<>();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement statement = conn.prepareStatement(NotificationMgrSQLStatements.SqlStatements.SELECT_ALL_MESSAGES.getSqlStatement());

			rs = statement.executeQuery();

			while (rs.next()) {
				final byte[] bytes = rs.getBytes(2);

				final String strYaml = new String(bytes);
				final EventMessage eventMsg = (EventMessage) CommonYAML.getYamlObject().load(strYaml);
				resultList.add(eventMsg);
			}

		} catch (final SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}
		return resultList;
	}

	/**
	 * Get some Event Messages
	 * @param limit
	 *            Maximum events to get
	 * @return The Event Message
	 */
	public List<EventMessage> getSomeEventMessages(final int limit) {
		ResultSet rs = null;
		final List<EventMessage> resultList = new ArrayList<>();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement statement = conn.prepareStatement(NotificationMgrSQLStatements.SqlStatements.SELECT_ALL_MESSAGES_LIMITED.getSqlStatement());

			statement.setInt(1, limit);
			rs = statement.executeQuery();

			while (rs.next()) {
				final byte[] bytes = rs.getBytes(2);
				final String strYaml = new String(bytes);
				final EventMessage eventMsg = (EventMessage) CommonYAML.getYamlObject().load(strYaml);
				resultList.add(eventMsg);
			}

		} catch (final SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}
		return resultList;
	}

	/**
	 * Get some Event listeners
	 * @param limit
	 *            Maximum events to get
	 * @return The Event listeners
	 */
	public List<ECA> getSomeEventListeners(final int limit) {
		ResultSet rs = null;
		final List<ECA> resultList = new ArrayList<>();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement statement = conn.prepareStatement(NotificationMgrSQLStatements.SqlStatements.SELECT_ALL_ECAS.getSqlStatement());

			statement.setInt(1, limit);
			rs = statement.executeQuery();

			while (rs.next()) {
				final byte[] bytes = rs.getBytes(2);
				final String strYaml = new String(bytes);
				final ECA eventListener = (ECA) CommonYAML.getYamlObject().load(strYaml);
				resultList.add(eventListener);
			}

		} catch (final SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}

		return resultList;
	}

	/**
	 * Close DB.
	 */
	public void close() {
		try {
			dataSource.close();
		}catch(Exception e) {
			// TODO
			e.printStackTrace();
		}
	}
}
