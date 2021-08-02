
package es.bsc.dataclay.storagelocation;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import es.bsc.dataclay.util.ObjectGraph;
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

/**
 * This class is responsible to collect objects in disk.
 */
public final class StorageLocationGC {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger("StorageLocationGC");

	/** Indicates if debug is enabled. */
	private static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** Storage Location. */
	private final StorageLocationService storageLocation;

	/** Runtime used to connect to other EE, SL or LM. */
	private final DataServiceRuntime runtime;

	/** Path in which to store graph of references in case of restart or shutdown (in StorageLocationGC, where graph is stored). */
	private final String cachePath;

	/**
	 * Graph representing objects in current SL
	 */
	private ObjectGraph objectGraph = new ObjectGraph();

	/**
	 * Constructor.
	 * 
	 * @param thestorageLocationService
	 *            Storage location service.
	 * @param theruntime
	 *            The runtime to use for communications and others.
	 */
	public StorageLocationGC(final StorageLocationService thestorageLocationService, final DataServiceRuntime theruntime) {
		this.storageLocation = thestorageLocationService;
		this.runtime = theruntime;
		this.cachePath = Configuration.Flags.STORAGE_PATH.getStringValue()
				+ "sl_gc_" + runtime.getDataService().dsName + ".cache";
		if (DEBUG_ENABLED) {
			LOGGER.debug("Starting StorageLocationGC");
		}
		initGraphFromFile();
	}

	/**
	 * Serialize graph into files. Used for restart or shutdown.
	 */
	private void storeGraphIntoFile() {
		// Store cache in file
		try {
			LOGGER.debug("Storing in " + this.cachePath);
			final FileOutputStream fos = new FileOutputStream(this.cachePath);
			final ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this.objectGraph);
			oos.close();
			fos.close();
		} catch (final IOException ioe) {
			LOGGER.debug("serializeCachesIntoFiles got an I/O exception", ioe);
		}
	}

	/**
	 * Initialize graph from persistent files
	 */
	private void initGraphFromFile() {
		// Deserialize metadata cache if present
		try {
			LOGGER.debug("Recovering graph from " + this.cachePath);
			final FileInputStream fis = new FileInputStream(this.cachePath);
			final ObjectInputStream ois = new ObjectInputStream(fis);
			this.objectGraph = (ObjectGraph) ois.readObject();
			ois.close();
			fis.close();
		} catch (final IOException ioe) {
			// do nothing, object not exist
		} catch (final ClassNotFoundException c) {
			LOGGER.debug("initGraphFromFile error", c);
			// do nothing
		}
	}

	/**
	 * Finish all threads.
	 */
	public void shutDown() {
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==GGC Shutdown==] Shutting down StorageLocationGC");
		}
		// store graph into file
		storeGraphIntoFile();
	}

	/**
	 * At each store or update, update graph of references
	 */
	public synchronized void updateGraphOfReferences(final ObjectID objectID, final byte[] serializedObject) {
		try {
			if (DEBUG_ENABLED) {
				LOGGER.debug("[==GGC counters processor==] Updating graph of references for "
						+ objectID);
			}
			final ReferenceCounting refCounting = new ReferenceCounting();
			refCounting.deserializeReferenceCounting(objectID,
					DataClayDeserializationLib.extractReferenceCounting(serializedObject));

			// add object to graph if it does not exist
			this.objectGraph.addVertex(objectID);
			// remove all edges of the object
			this.objectGraph.removeAllEdges(objectID);
			// remove all edges from root to the object
			this.objectGraph.removeEdgeFromRoot(objectID);
			// add alias and federation references
			int externalRefs = refCounting.getExternalReferences();
			if (externalRefs > 0) {
				// add reference from root to object
				this.objectGraph.addOrSetEdgeFromRoot(objectID, externalRefs);
			}
			for (final Entry<BackendID, Map<ObjectID, Integer>> countingsPerHint : refCounting.getReferenceCounting().entrySet()) {
				final BackendID ownerID = countingsPerHint.getKey();
				for (final Entry<ObjectID, Integer> pointedRefEntry : countingsPerHint.getValue().entrySet()) {
					final Integer counting = pointedRefEntry.getValue();
					final ObjectID pointedRef = pointedRefEntry.getKey();
					this.objectGraph.addVertex(pointedRef); //objects that are in memory and have never been flushed
					this.objectGraph.addOrSetEdge(objectID, pointedRef, counting);
				}
			}
		} catch (final Exception e) {
			if (DEBUG_ENABLED) {
				LOGGER.debug(e);
			}
		}
	}

	/**
	 * Get objects retained by heap or session in all associated EEs of current SL
	 * @return set of ids of objects retained by heap or session in all associated EEs of current SL
	 */
	private synchronized Set<ObjectID> getReferencesInEE() {
		// ==== PULL from EE: update reference counting === //
		// get information from EE to check if candidates are in memory, used by some session
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==GGC collector==] Getting references in EE");
		}
		final Set<ObjectID> referencedObjectsByEE = new HashSet<>();
		for (final ExecutionEnvironmentID associatedExecutionEnvironmentID : this.storageLocation.getAssociateExecutionEnvironments()) {
			final DataServiceAPI dsAPI = runtime.getRemoteDSAPI(associatedExecutionEnvironmentID);
			referencedObjectsByEE.addAll(dsAPI.getRetainedReferences());
		}
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==GGC collector==] Retained refs in EE: " + referencedObjectsByEE.size());
		}
		return referencedObjectsByEE;
	}

	/**
	 * Return a graph representing objects and their references in current SL
	 * @return a graph representing objects and their references in current SL
	 */
	public synchronized ObjectGraph getObjectGraph() {


		/**
		 * RACE CONDITION DESIGN:
		 *
		 * When we are obtaining current references in EE is it possible that we get that EE is not retaining object A
		 * but it is actually retained by B but this 'notification' is still 'in progress'?
		 *
		 * NO. Let's see why.
		 *
		 * At each EE, we have two map, one to 'retain' objects in memory and one of objects actually in memory
		 * - retention map: ids of the objects retained -> objects
		 * - inmemory map: ids of objects -> weak references to objects
		 *
		 * This means that until object is not removed from retention map, it will not be removed from inmemory map,
		 * with our query to EE we always check inmemorymap, taking into account that *if an object is not in retention map
		 * it means it was send to SL so Graph is properly updated*.
		 *
		 * In our example, B -> A and therefore A is 'retained' by B, so it's even better.
		 *
		 */


		// Get objects being used by sessions or EE
		Set<ObjectID> curRefsInEE = this.getReferencesInEE();
		ObjectGraph currentGraph = this.objectGraph.clone();
		// add references from root to objects in session/EE
		/**
		 * One reference is enough, in the merged graph we will have 1 reference per SL representing sessions and EEs
		 * and N references representing aliases and other external refs.
		 */
		for (ObjectID objectInUse : curRefsInEE) {
			currentGraph.addVertex(objectInUse); //objects that are in memory and have never been flushed
			currentGraph.addOrSetEdgeFromRoot(objectInUse, 1);
		}

		if (DEBUG_ENABLED) {
			LOGGER.debug("Storage Location Object graph: ");
			LOGGER.debug(currentGraph);
		}

		return currentGraph;
	}

	/**
	 * Remove vertex from graph (and all its edges)
	 * @param objectID ID of vertex to remove
	 */
	public synchronized void deleteObject(final ObjectID objectID) {
		this.objectGraph.removeVertex(objectID);
	}
}
