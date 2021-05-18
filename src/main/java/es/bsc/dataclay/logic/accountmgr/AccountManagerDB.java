
package es.bsc.dataclay.logic.accountmgr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.exceptions.dbhandler.DbObjectAlreadyExistException;
import es.bsc.dataclay.exceptions.dbhandler.DbObjectNotExistException;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.CredentialID;
import es.bsc.dataclay.util.management.accountmgr.Account;
import es.bsc.dataclay.util.management.accountmgr.AccountRole;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteDataSource;

/**
 * Data base connection.
 */
public final class AccountManagerDB {
	private static final Logger logger = LogManager.getLogger("managers.AccountManager.DB");
	
	/** DataSource. */
	private final SQLiteDataSource dataSource;

	/**
	 * Constructor.
	 * 
	 * @param dataSource
	 *            Data base source.
	 */
	public AccountManagerDB(final SQLiteDataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Create tables of MDS.
	 */
	public void createTables() {
		synchronized (dataSource) {
			Connection conn = null;

			try {
				conn = dataSource.getConnection();
				for (final AccountMgrSQLStatements.SqlStatements stmt : AccountMgrSQLStatements.SqlStatements.values()) {
					if (stmt.name().startsWith("CREATE_TABLE")) {
						final PreparedStatement updateStatement = conn.prepareStatement(stmt.getSqlStatement());
						updateStatement.execute();
						updateStatement.close();
					}
				}
			} catch (final SQLException e) {
				logger.debug("SQL error in createTables operation", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
		}
	}

	/**
	 * Delete the tables of MDS. Just the other way around of createTables --much simpler.
	 */
	public void dropTables() {
		synchronized (dataSource) {

			Connection conn = null;

			try {
				conn = dataSource.getConnection();
				for (final AccountMgrSQLStatements.SqlStatements stmt : AccountMgrSQLStatements.SqlStatements.values()) {
					if (stmt.name().startsWith("DROP_TABLE")) {
						final PreparedStatement updateStatement = conn.prepareStatement(stmt.getSqlStatement());
						updateStatement.execute();
						updateStatement.close();
					}
				}
			} catch (final SQLException e) {
				logger.debug("SQL error in dropTables operation", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
		}
	}

	/**
	 * Store credential into database
	 * @param passwordCredential
	 *            Password credential
	 */
	public void store(final PasswordCredential passwordCredential) {
		synchronized (dataSource) {

			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(AccountMgrSQLStatements.SqlStatements.INSERT_CREDENTIAL.getSqlStatement());
				insertStatement.setObject(1, passwordCredential.getDataClayID().getId());
				// CHECKSTYLE:OFF
				insertStatement.setString(2, passwordCredential.getPassword());
				// CHECKSTYLE:ON
				insertStatement.executeUpdate();
				insertStatement.close();

			} catch (final Exception e) {
				logger.debug("SQL error in store passwordCredential", e);
				throw new DbObjectAlreadyExistException(passwordCredential.getDataClayID());
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
		}

	}

	/**
	 * Store Account into database
	 * @param account
	 *            The account
	 */
	public void store(final Account account) {
		synchronized (dataSource) {

			if (account.getCredential().getDataClayID() == null) {
				account.getCredential().setDataClayID(new CredentialID());
			}
			this.store((PasswordCredential) account.getCredential());

			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(AccountMgrSQLStatements.SqlStatements.INSERT_ACCOUNT.getSqlStatement());
				insertStatement.setObject(1, account.getDataClayID().getId());
				// CHECKSTYLE:OFF
				insertStatement.setString(2, account.getUsername());
				insertStatement.setObject(3, account.getCredential().getDataClayID().getId());
				insertStatement.setString(4, account.getRole().name());

				// CHECKSTYLE:ON
				insertStatement.executeUpdate();
				insertStatement.close();
			} catch (final Exception e) {
				logger.debug("SQL error in store account", e);
				throw new DbObjectAlreadyExistException(account.getDataClayID());
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
		}
	}

	/**
	 * Deserialize account
	 * @param rs
	 *            Result set
	 * @return Account
	 */
	private Account deserializeAccount(
			final ResultSet rs) {
		Account account = null;
		try {
			// CHECKSTYLE:OFF
			final AccountID accountID = new AccountID((UUID)rs.getObject("id"));
			final String accountName = rs.getString("username");
			final String accountRole = rs.getString("role");
			final AccountRole role = AccountRole.valueOf(accountRole);

			// Query credential
			final PasswordCredential passCred = getCredentialByID((UUID)rs.getObject("credential"));

			account = new Account(accountName, role, passCred);
			account.setDataClayID(accountID);

		} catch (final SQLException e) {
			logger.debug("SQL error in deserializeAccount", e);
		}
		return account;
	}

	/**
	 * Deserialize PasswordCredential
	 * @param rs
	 *            Result set
	 * @return PasswordCredential
	 */
	private PasswordCredential deserializePasswordCredential(final ResultSet rs) {
		PasswordCredential passCred = null;
		try {
			final CredentialID credentialID = new CredentialID((UUID)rs.getObject("id"));
			final String password = rs.getString("password");
			passCred = new PasswordCredential(password);
			passCred.setDataClayID(credentialID);
		} catch (final SQLException e) {
			logger.debug("SQL error in deserializePasswordCredential", e);
		}
		return passCred;
	}

	/**
	 * Get password credential by ID
	 * @param credentialID
	 *            ID of the password credential
	 * @return The password credential
	 * @throws SQLException
	 */
	private PasswordCredential getCredentialByID(final UUID credentialID) throws SQLException {
		ResultSet rs = null;
		PasswordCredential passCred = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(AccountMgrSQLStatements.SqlStatements.SELECT_CREDENTIAL.getSqlStatement());

			selectStatement.setObject(1, credentialID);
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				passCred = deserializePasswordCredential(rs);
			}
			selectStatement.close();
		} catch (final SQLException e) {
			throw e;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}

		return passCred;
	}

	/**
	 * Get Account by ID
	 * @param accountID
	 *            ID of the Account
	 * @return The Account
	 */
	public Account getByID(final AccountID accountID) {
		synchronized (dataSource) {
			ResultSet rs = null;
			Account account = null;
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(AccountMgrSQLStatements.SqlStatements.SELECT_ACCOUNT.getSqlStatement());

				selectStatement.setObject(1, accountID.getId());
				rs = selectStatement.executeQuery();
				if (rs.next()) {
					account = deserializeAccount(rs);
				}
				selectStatement.close();
			} catch (final SQLException e) {
				logger.debug("SQL error in getByID", e);
				throw new DbObjectNotExistException(accountID);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}

			return account;
		}
	}

	/**
	 * Get Account by name
	 * @param accountName
	 *            Name of the Account
	 * @return The Account
	 */
	public Account getByName(final String accountName) {
		synchronized (dataSource) {

			ResultSet rs = null;
			Account account = null;
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(AccountMgrSQLStatements.SqlStatements.SELECT_ACCOUNT_BY_NAME.getSqlStatement());

				selectStatement.setString(1, accountName);
				rs = selectStatement.executeQuery();
				if (rs.next()) {
					account = deserializeAccount(rs);
				}
				selectStatement.close();
			} catch (final SQLException e) {
				logger.debug("SQL error in getByName", e);
				throw new DbObjectNotExistException();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}

			return account;
		}
	}

	/**
	 * Check if there is an account with name provided
	 * @param accountName
	 *            Name of the account
	 * @return TRUE if exists. FALSE otherwise
	 */
	public boolean existsAccountByName(final String accountName) {

		synchronized (dataSource) {
			ResultSet rs = null;
			boolean exists = false;
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement existsStatement = conn.prepareStatement(AccountMgrSQLStatements.SqlStatements.EXISTS_ACCOUNT_BY_NAME.getSqlStatement());

				existsStatement.setString(1, accountName);
				rs = existsStatement.executeQuery();
				rs.next();
				exists = rs.getBoolean(1);
				existsStatement.close();
			} catch (final Exception e) {
				logger.debug("existsAccountByName error", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}

			return exists;
		}
	}

	/**
	 * Check if there is an account with ID provided
	 * @param accountID
	 *            ID of the account
	 * @return TRUE if exists. FALSE otherwise
	 */
	public boolean existsAccountByID(final AccountID accountID) {
		synchronized (dataSource) {
			ResultSet rs = null;
			boolean exists = false;
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement existsStatement = conn.prepareStatement(AccountMgrSQLStatements.SqlStatements.EXISTS_ACCOUNT_BY_ID.getSqlStatement());

				existsStatement.setObject(1, accountID.getId());
				rs = existsStatement.executeQuery();
				rs.next();
				exists = rs.getBoolean(1);
				existsStatement.close();

			} catch (final Exception e) {
				logger.debug("existsAccountByID error", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}

			return exists;
		}
	}

	/**
	 * Get all normal accounts
	 * @return The accounts with normal role.
	 */
	public List<Account> getAllNormalAccounts() {
		synchronized (dataSource) {
			ResultSet rs = null;
			final ArrayList<Account> resultList = new ArrayList<>();
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(AccountMgrSQLStatements.SqlStatements.SELECT_ALL_NORMAL_ACCOUNTS.getSqlStatement());

				rs = selectStatement.executeQuery();
				while (rs.next()) {
					final Account account = deserializeAccount(rs);
					resultList.add(account);
				}
				selectStatement.close();

			} catch (final SQLException e) {
				logger.debug("SQL error in getAllNormalAccounts", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}

			return resultList;
		}
	}

	/**
	 * Close DB.
	 */
	public void close() {
		try {
			dataSource.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
