
package es.bsc.dataclay.logic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages;
import es.bsc.dataclay.logic.LogicModule;
import es.bsc.dataclay.util.ObjectGraph;
import es.bsc.dataclay.util.management.metadataservice.ExecutionEnvironment;
import es.bsc.dataclay.util.management.metadataservice.StorageLocation;
import es.bsc.dataclay.util.structs.Tuple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.api.BackendID;
import es.bsc.dataclay.commonruntime.DataServiceRuntime;
import es.bsc.dataclay.dataservice.api.DataServiceAPI;
import es.bsc.dataclay.serialization.lib.DataClayDeserializationLib;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.management.metadataservice.MetaDataInfo;

/**
 * This class is responsible to collect objects in disk.
 */
public final class GlobalGarbageCollector {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger("GlobalGC");

	/** Indicates if debug is enabled. */
	private static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** Reference to logicmodule from which to get nodes to use. */
	private LogicModule logicModule;


	/** Pool for tasks. */
	protected final ScheduledExecutorService threadPool = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
		@Override
		public Thread newThread(final Runnable r) {
			final Thread t = Executors.defaultThreadFactory().newThread(r);
			t.setName("GlobalGC");
			t.setDaemon(true);
			return t;
		}
	});

	/**
	 * Constructor.
	 * @param logicModule Reference to logicmodule from which to get nodes to use.
	 *
	 */
	public GlobalGarbageCollector(final LogicModule logicModule) {
		this.logicModule = logicModule;
		long initalDelay = 0;
		if (Configuration.Flags.GLOBALGC_COLLECTOR_INITIAL_DELAY_HOURS.getLongValue() != 0) {
			// === prepare delay === //
			final LocalDateTime now = LocalDateTime.now();
			final LocalDateTime startTime = now.plusHours(Configuration.Flags.GLOBALGC_COLLECTOR_INITIAL_DELAY_HOURS.getLongValue());
			final Duration duration = Duration.between(LocalDateTime.now(), startTime);
			initalDelay = duration.toMillis();
		}

		final TimerTask collectorTask = new CollectorTask();
		threadPool.scheduleAtFixedRate(collectorTask, initalDelay,
				Configuration.Flags.GLOBALGC_COLLECT_TIME_INTERVAL.getLongValue(), TimeUnit.MILLISECONDS);

	}

	/**
	 * Collect all objects that are garbage.
	 */
	private void collect() {

		try {
			// Get all graphs
			List<ObjectGraph> graphs = new ArrayList<>();
			for (final Tuple<DataServiceAPI, StorageLocation> elem : this.logicModule.getStorageLocations().values()) {
				LOGGER.debug("Getting graph of references from {}", elem.getSecond());
				ObjectGraph curGraph = elem.getFirst().getObjectGraph();
				graphs.add(curGraph);
			}
			// Merge graphs
			ObjectGraph snapshot = new ObjectGraph();
			for (ObjectGraph curGraph : graphs) {
				snapshot.merge(curGraph);
			}

			this.logicModule.currentNumberOfObjects = snapshot.getNumberOfVertices() - 1; //minus root

			// Print graph
			if (DEBUG_ENABLED) {
				LOGGER.debug("CURRENT GRAPH SNAPSHOT:");
				LOGGER.debug(snapshot);
			}

			// Find unaccessible objects
			/**
			 * Replica design: having replicas just change the weight of the edges but
			 * objects that are unaccessible means that any of the replicas is accessible
			 */
			Set<ObjectID> unaccessibleObjects = snapshot.getUnaccesibleObjects();
			if (DEBUG_ENABLED) {
				LOGGER.debug("Found unaccesible objects: " + unaccessibleObjects);
			}
			// Delete unaccessible objects in all SLs
			//TODO: can we improve and just call delete where we know there are replicas? race conditions...
			if (!unaccessibleObjects.isEmpty()) {
				for (final Tuple<DataServiceAPI, StorageLocation> elem : this.logicModule.getStorageLocations().values()) {
					elem.getFirst().deleteSet(unaccessibleObjects);
				}
			}

			} catch (final Exception ex) {
			LOGGER.debug("collect error", ex);
		}
	}

	/**
	 * Task that collects objects in disk and send update of references.
	 */
	private class CollectorTask extends TimerTask {
		@Override
		public final void run() {
			collect();
		}
	}


	
}
