
package es.bsc.dataclay.logic.notificationmgr;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.logic.LogicModule;
import es.bsc.dataclay.serialization.lib.SerializedParametersOrReturn;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.events.EventMessageStateOuter.EventState;
import es.bsc.dataclay.util.events.listeners.ECA;
import es.bsc.dataclay.util.events.message.EventMessage;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.SessionID;
import es.bsc.dataclay.util.management.metadataservice.MetaDataInfo;
import es.bsc.dataclay.util.structs.LruCache;

/**
 * Class responsible to handle events and notifications.
 */
public final class NotificationManager {

	/** Logger. */
	private static final Logger logger = LogManager.getLogger("managers.NotificationManager");

	/** Waiting time during shutdown for checking notifications. */
	private static final int WAITING_TIME_CLOSE_NOTIFICATION = 500;

	/** LogicModule used to execute. */
	private final LogicModule logicModule;

	/** Database. */
	private NotificationManagerDB notificationDB;

	/** Messages to process. */
	private final NotificationManagerPriorityQueue<EventMessage> events;

	/** Executor service. Responsible to execute notifications. */
	private final ScheduledExecutorService execService;

	/** Indicates if manager is being closed. */
	private boolean isShuttingDown;

	/** Indicates if manager is processing some event. */
	private boolean isProcessingEvent;

	/** Timer. */
	private final Timer timer;

	/** Cache of sessions. */
	private final LruCache<AccountID, SessionID> cacheOfSessions = new LruCache<>(
			Configuration.Flags.MAX_ENTRIES_NOTIFICATION_MANAGER_SESSION_CACHE.getIntValue());

	/**
	 * Constructor
	 * @param managerName
	 *            Manager/service name.
	 * @param thelogicModule
	 *            Reference to logic module for executing actions
	 */
	public NotificationManager(final LogicModule thelogicModule, final BasicDataSource dataSource) {
		this.logicModule = thelogicModule;
		this.notificationDB = new NotificationManagerDB(dataSource);
		notificationDB.createTables();
		this.events = new NotificationManagerPriorityQueue<>(
				Configuration.Flags.MAX_ENTRIES_NOTIFICATION_MANAGER_MSG_QUEUE.getIntValue(),
				notificationDB);
		final Notifier notifier = new Notifier();
		timer = new Timer(); // Instantiate Timer Object
		execService = Executors.newScheduledThreadPool(1);

		// Create Repetitively task for every 1 secs
		timer.schedule(notifier, 0, Configuration.Flags.NOTIFICATION_MANAGER_INTERVAL.getLongValue());

		this.isShuttingDown = false;

	}

	/**
	 * Register a new event listener.
	 * @param newEventListenerImpl
	 *            Event listener.
	 */
	public void registerEventListenerImpl(final ECA newEventListenerImpl) {
		// Add to database
		this.notificationDB.store(newEventListenerImpl);

		logger.debug("Registered event listener {}", newEventListenerImpl);
	}

	/**
	 * New event arrived. Add to queue.
	 * @param newEvent
	 *            New event to add to queue.
	 */
	public void adviseEvent(final EventMessage newEvent) {

		// == FAULT-TOLERANCE behaviour == //
		// Add it to disk
		newEvent.setEventState(EventState.QUEUED);
		this.notificationDB.store(newEvent);

		// =============================== //
		logger.info("Event arrived: {}", newEvent);

		// Add to queue
		this.events.add(newEvent);

		logger.debug("Event added to queue (#{} elements in queue)",
				this.events.size());
	}

	/**
	 * Get the NotificationManager::notificationDB
	 * @return the notificationDB
	 */
	public NotificationManagerDB getNotificationDB() {
		return notificationDB;
	}

	/**
	 * Set the NotificationManager::notificationDB
	 * @param newnotificationDB
	 *            the notificationDB to set
	 */
	public void setNotificationDB(final NotificationManagerDB newnotificationDB) {
		if (newnotificationDB == null) {
			throw new IllegalArgumentException("notificationDB cannot be null");
		}
		this.notificationDB = newnotificationDB;
	}

	/**
	 * Get the NotificationManager::events
	 * @return the events
	 */
	public Queue<EventMessage> getEvents() {
		return events;
	}

	/**
	 * Remove all entries that belong to session id
	 * @param accountID
	 *            Id of the account
	 */
	public void removeSessionInCache(final AccountID accountID) {
		this.cacheOfSessions.remove(accountID);
	}

	/**
	 * Shutdown execution service.
	 */
	public void closeManager() {
		this.isShuttingDown = true;
		while (isProcessingEvent) {
			try {
				Thread.sleep(WAITING_TIME_CLOSE_NOTIFICATION);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Process all in queue

		this.execService.shutdown();
		try {
			this.execService.awaitTermination(1, TimeUnit.SECONDS);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		this.timer.cancel();
	}

	/**
	 * Class that read from queue and run proper actions.
	 */
	private class Notifier extends TimerTask {

		@Override
		public void run() {
			if (isShuttingDown || isProcessingEvent) {
				return;
			}

			isProcessingEvent = true;

			try {
				// Get event
				final EventMessage message = events.poll();
				if (message != null) {

					logger.debug("Processing event: {}", message);
					// Check which conditions are accomplished

					// WARNING: modify getAllListeners to receive a 'subset' of listeners in case there is not enough
					// memory heap. For now, since we do not have this problem, leave it like that.
					// Having a cache does not solve anything since we must check 'all' event listeners and therefore
					// a certain number of misses in cache is guaranteed.
					final List<ECA> listeners = notificationDB.getAllEventListeners();

					// Update event listeners target
					// Get ALL target conditions registered and check if message accomplishes EACH one.
					for (final ECA eventListener : listeners) {
						logger.debug("Checking listener: {}", eventListener);

						// Check if message accomplishes event type to execute event
						final boolean eventTypeOK = eventListener.checkIsEventType(message);
						logger.debug("Event type is: {}", eventTypeOK);

						if (eventTypeOK) {
							final SerializedParametersOrReturn params = message.getParams();
							final Map<ObjectID, MetaDataInfo> objectsInfo = logicModule.getObjectsMetaDataInfoOfClassForNM(eventListener.getTargetClass());
							boolean firstExecutionOfFilterMethod = true;
							boolean filterMethodResult = true;
							for (final ObjectID targetOID : objectsInfo.keySet()) {

								// === START SESSION AS OWNER OF THE OBJECT === //
								// Get owner's
								final AccountID ownerID = logicModule.getOwner(targetOID);
								SessionID sessionOfOwnerID = cacheOfSessions.get(ownerID);
								if (sessionOfOwnerID == null) {
									// TODO: IMPORTANT: data set of store is randomly selected
									// As defined, we should set the dataset of store as the same of
									// the target object BUT if we do that we would have to implement
									// a new session PER OBJECT which different dataset each.
									// Remember that when a volatile is assigned to a persistent object, the volatile will be
									// persisted using the same dataset as the container object. Store DataSet is only used
									// when a method is calling "makePersistent", so it is not a critical issue. One solution
									// could
									// be to specify which dataset to use in makePersistent call.

									sessionOfOwnerID = logicModule.initializeSessionAsOwnerOfObject(ownerID);
									cacheOfSessions.put(ownerID, sessionOfOwnerID);
								}

								// TODO: WARNING: filter method is being executed as the owner of the object, and only once,
								// should it be like that?
								if (firstExecutionOfFilterMethod && eventListener.getFilterMethod() != null) {
									logger.debug("Executing filterMethod {}", message);
									final OperationID opID = eventListener.getFilterMethod();

									try {
										filterMethodResult = logicModule.executeFilterMethod(targetOID,
												sessionOfOwnerID, params, opID);
										firstExecutionOfFilterMethod = false;
										if (!filterMethodResult) { // WARNING: Workaround while it cannot be executed as static
																	// method.
											break;
										}
									} catch (final Exception e) {
										logger.error("Error executing filterMethod", e);
									}
								}
								if (filterMethodResult) {
									logger.debug("Executing action: {}", message);
									final OperationID opID = eventListener.getAction();
									// Update database message (FAULT-TOLERANCE BEHAVIOUR)
									message.setEventState(EventState.EXECUTING);
									notificationDB.updateByIDEventMessage(message);

									execService.execute(new WorkerThread(targetOID, message, opID, sessionOfOwnerID));
								}
							}
						}
					}

					// Removes event from Database if success
					notificationDB.deleteByID(message.getId());

					// Fill queue
					events.fillQueueFromDB();
				} else {
					logger.debug("Empty queue");
				}
			} catch (final Throwable e) {
				logger.error("Exception during timer", e);
			} finally {
				isProcessingEvent = false;
			}
		}

		/**
		 * Class that implements a WorkerThread to execute notifications / actions.
		 */
		private class WorkerThread implements Runnable {

			/** Target object to execute the operation. */
			private final ObjectID targetOID;
			/** Event message with information about the produced event and session id. */
			private final EventMessage message;
			/** Operation to be exected on target object. */
			private final OperationID opID;
			/** ID of the session of the owner to use. */
			private final SessionID sessionID;

			/**
			 * Constructor.
			 * @param newtargetOID
			 *            target object that will execute the operation.
			 * @param neweventMessage
			 *            information about the event produced.
			 * @param newopID
			 *            operation to be executed on target object.
			 * @param sessionOfOwnerID
			 *            ID of the session of the owner of the target object
			 */
			WorkerThread(final ObjectID newtargetOID, final EventMessage neweventMessage,
					final OperationID newopID, final SessionID sessionOfOwnerID) {
				targetOID = newtargetOID;
				message = neweventMessage;
				opID = newopID;
				sessionID = sessionOfOwnerID;
			}

			@Override
			public void run() {

				logicModule.executeNotificationAction(
						targetOID,
						sessionID,
						message.getParams(), opID);
			}

			@Override
			public String toString() {
				return "OID:" + targetOID + " reacts to event:" + message.getEventType() + " executing:" + opID;
			}
		}

	}

}
