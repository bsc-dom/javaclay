
/**
 * @file SessionManager.java
 * @date May 31, 2013
 */
package es.bsc.dataclay.logic.sessionmgr;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.exceptions.logicmodule.sessionmgr.SessionExternalIsNotActiveException;
import es.bsc.dataclay.exceptions.logicmodule.sessionmgr.SessionExternalNotExistException;
import es.bsc.dataclay.exceptions.logicmodule.sessionmgr.SessionIsNotActiveException;
import es.bsc.dataclay.exceptions.logicmodule.sessionmgr.SessionNotExistException;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.DataContractID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.SessionID;
import es.bsc.dataclay.util.management.AbstractManager;
import es.bsc.dataclay.util.management.sessionmgr.SessionContract;
import es.bsc.dataclay.util.management.sessionmgr.SessionDataContract;
import es.bsc.dataclay.util.management.sessionmgr.SessionInfo;
import es.bsc.dataclay.util.structs.MemoryCache;
import es.bsc.dataclay.util.structs.Tuple;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteDataSource;

/**
 * This class is responsible to manage information related to sessions.
 */
public final class SessionManager extends AbstractManager {

	/** Logger. */
	private Logger logger;

	/** Indicates if debug is enabled. */
	protected static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** DbHandler for the management of Sessions. */
	private final SessionManagerDB sessionDB;

	/** Cache for sessions. */
	private MemoryCache<SessionID, Tuple<Session, Calendar>> sessionCache;

	/** Cache for ext sessions. */
	private MemoryCache<DataClayInstanceID, Tuple<Session, Calendar>> extSessionCache;

	/**
	 * Instantiates an SessionManager that uses the Backend configuration provided.
	 * 
	 * @param managerName
	 *            Manager/service name.
	 * @post Creates an Class manager and hash initializes the backend.
	 */
	public SessionManager(final SQLiteDataSource dataSource) {
		super(dataSource);
		if (DEBUG_ENABLED) {
			logger = LogManager.getLogger("LogicModule");
		}
		this.sessionDB = new SessionManagerDB(dataSource);
		this.sessionDB.createTables();

		// Init caches
		this.sessionCache = new MemoryCache<>();
		this.extSessionCache = new MemoryCache<>();
	}

	/**
	 * Registers a new Session for the Account with ID provided and the information about the contracts (and interfaces,...) in
	 * the session.
	 * 
	 * @param newaccountID
	 *            ID of the account of the session
	 * @param newSessionContracts
	 *            Contracts (and interfaces) of the session
	 * @param newSessionDataContracts
	 *            Data contracts of the session
	 * @param dataContractIDofStore
	 *            ID of the data contract for store
	 * @param endDate
	 *            the end date of the session
	 * @param languageForSession
	 *            language for the session
	 * @param ifaceBitmaps
	 *            interface bitmaps for the session.
	 * @return Information new session created
	 * @throws Exception
	 *             if an exception occurs.
	 */
	public SessionInfo newSession(final AccountID newaccountID,
			final Map<ContractID, SessionContract> newSessionContracts,
			final Map<DataContractID, SessionDataContract> newSessionDataContracts,
			final DataContractID dataContractIDofStore, final Calendar endDate, final Langs languageForSession,
			final Map<MetaClassID, byte[]> ifaceBitmaps) {

		// TODO: Can we have more than one session with same AccountID and same contracts?

		final Session session = new Session(newaccountID, newSessionContracts, newSessionDataContracts, dataContractIDofStore,
				endDate, languageForSession, ifaceBitmaps);
		session.setDataClayID(new SessionID());
		this.sessionDB.store(session);

		// Update cache
		sessionCache.put(session.getDataClayID(),
				new Tuple<>(session, session.getEndDate()));
		return new SessionInfo(session.getDataClayID(),
				session.getAccountID(), session.getPropertiesOfClasses(), session.getSessionContracts(),
				session.getSessionDataContracts(), session.getDataContractIDofStore(), session.getLanguage(),
				session.getIfaceBitmaps(), session.getEndDate());
	}

	/**
	 * Registers a new Session for the external dataClay instance only valid for shared objects
	 * 
	 * @param extDataClayID
	 *            id of the external dataClay instance
	 * @return Information new session created
	 * @throws Exception
	 *             if an exception occurs.
	 */
	public SessionInfo newExtSession(final DataClayInstanceID extDataClayID, final AccountID accountID, final Langs language, final Calendar endDate) {
		final Session session = new Session(extDataClayID, accountID, language, endDate);
		session.setDataClayID(new SessionID());

		// Update db
		try {
			this.sessionDB.storeExt(session);
		} catch (final Exception ex) {
			// retry in case it already exists an expired session
			this.sessionDB.deleteExtSession(extDataClayID);
			this.sessionDB.storeExt(session);
		}

		// Update cache
		extSessionCache.put(extDataClayID,
				new Tuple<>(session, session.getEndDate()));

		return new SessionInfo(session.getDataClayID(), session.getExtDataClayID(), session.getAccountID(), session.getLanguage(), session.getEndDate());
	}

	/**
	 * Close session
	 * 
	 * @param sessionID
	 *            ID of the session to close
	 */
	public void closeSession(final SessionID sessionID) {
		// === design note == //
		// sessions are not actually removed from DB. Using the timestamp a "LM collector" should
		// be able to clean them from DB. Also, any ongoing event (execution, makepers...) that
		// entered using a session that actually expired should finish before so we must implement
		// some mechanism for that.

		// TODO: set expiration date of session to "now" and update expiration date in cache

		// sessionDB.deleteSession(sessionID);
		// sessionCache.remove(sessionID);
	}

	/**
	 * Close external session
	 * 
	 * @param extDataClayID
	 *            ID of the external dataClay instance
	 */
	public void closeExtSession(final DataClayInstanceID extDataClayID) {
		sessionDB.deleteExtSession(extDataClayID);
		extSessionCache.remove(extDataClayID);
	}

	/**
	 * Retrieves the info of a session
	 * 
	 * @param sessionID
	 *            ID of the session
	 * @return the info of the session
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             SessionIsNotActive if the session is not active. SessionNotExistException if the session does not exist.
	 */
	public SessionInfo getSessionInfo(final SessionID sessionID) {

		// Try in cache
		final Session session;
		final Tuple<Session, Calendar> cacheEntry = sessionCache.get(sessionID);
		if (cacheEntry == null) { // Not in cache
			if (DEBUG_ENABLED) {
				logger.debug("Querying session in DB with ID " + sessionID);
			}
			session = sessionDB.getSessionByID(sessionID);
			if (session == null) {
				throw new SessionNotExistException(sessionID);
			}
		} else {
			if (DEBUG_ENABLED) {
				logger.debug("Session found in cache: " + sessionID);
			}
			session = cacheEntry.getFirst();
		}

		if (!session.isActive()) {
			throw new SessionIsNotActiveException(sessionID);
		}
		if (DEBUG_ENABLED) {
			logger.debug("Creating new session info: " + sessionID);
		}
		return new SessionInfo(sessionID,
				session.getAccountID(), session.getPropertiesOfClasses(), session.getSessionContracts(),
				session.getSessionDataContracts(), session.getDataContractIDofStore(), session.getLanguage(),
				session.getIfaceBitmaps(), session.getEndDate());
	}

	public SessionInfo getExtSessionInfo(final DataClayInstanceID extDataClayID) {
		final Session session;
		final Tuple<Session, Calendar> cacheEntry = extSessionCache.get(extDataClayID);
		if (cacheEntry == null) {
			if (DEBUG_ENABLED) {
				logger.debug("Querying external session in DB for dataClay: " + extDataClayID);
			}
			session = sessionDB.getExtSession(extDataClayID);
			if (session == null) {
				throw new SessionExternalNotExistException(extDataClayID);
			}
		} else {
			if (DEBUG_ENABLED) {
				logger.debug("External session found in cache for dataClay: " + extDataClayID);
			}
			session = cacheEntry.getFirst();
		}
		if (!session.isActive()) {
			throw new SessionExternalIsNotActiveException(extDataClayID);
		}
		if (DEBUG_ENABLED) {
			logger.debug("Creating new session info for external dataClay: " + extDataClayID);
		}

		return new SessionInfo(session.getDataClayID(), extDataClayID, session.getAccountID(), session.getLanguage(), session.getEndDate());
	}

	/**
	 * Get all active sessions of the account provided
	 * 
	 * @param accountID
	 *            ID of the account
	 * @return all existing active sessions.
	 */
	public List<Session> getAllActiveSessionsOfAccount(final AccountID accountID) {
		return sessionDB.getSessionsOfAccount(accountID);
	}

	// ======== OTHER ======= //

	/**
	 * Method used for unit testing.
	 * 
	 * @return The db handler reference of this manager.
	 */
	public SessionManagerDB getDbHandler() {
		return sessionDB;
	}

	@Override
	public void cleanCaches() {
		sessionCache.clear();
	}
	/**
	 * Finish cache threads.
	 * 
	 * @if some exception occurs
	 */
	public void finishCacheThreads() {
		//try {
		//	this.sessionCache.finishCacheThreads();
		//} catch (final InterruptedException e) {
		//	e.printStackTrace();
		//}
	}

}
