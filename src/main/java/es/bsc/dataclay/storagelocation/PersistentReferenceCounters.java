
package es.bsc.dataclay.storagelocation;

import java.io.Serializable;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import es.bsc.dataclay.api.BackendID;
import es.bsc.dataclay.util.ids.ObjectID;

/** Class containing DiskGC caches. */
public class PersistentReferenceCounters implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4244299627338569938L;

	/**
	 * Reference counting to add/subtract for each object.
	 */
	private final Map<BackendID, Map<ObjectID, AtomicInteger>> countersPerNode;

	/**
	 * Objects in quarantine.
	 */
	private final Map<ObjectID, Long> quarantine;

	/**
	 * Candidates list of objects to be removed.
	 */
	private final Set<ObjectID> candidates;

	/**
	 * Constructor
	 * @param thecountersPerNode
	 *            counters per node
	 * @param thequarantine
	 *            quarantine objs
	 * @param thecandidates
	 *            candidate objs
	 */
	public PersistentReferenceCounters(final Map<BackendID, Map<ObjectID, AtomicInteger>> thecountersPerNode,
			final Map<ObjectID, Long> thequarantine, final Set<ObjectID> thecandidates) {
		this.countersPerNode = thecountersPerNode;
		this.quarantine = thequarantine;
		this.candidates = thecandidates;

	}

	/**
	 * @return the countersPerNode
	 */
	public Map<BackendID, Map<ObjectID, AtomicInteger>> getCountersPerNode() {
		return countersPerNode;
	}

	/**
	 * @return the quarantine
	 */
	public Map<ObjectID, Long> getQuarantine() {
		return quarantine;
	}

	/**
	 * @return the candidates
	 */
	public Set<ObjectID> getCandidates() {
		return candidates;
	}

}
