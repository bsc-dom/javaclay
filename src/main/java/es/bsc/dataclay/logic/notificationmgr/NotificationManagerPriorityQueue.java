
package es.bsc.dataclay.logic.notificationmgr;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import es.bsc.dataclay.util.events.message.EventMessage;

/**
 * Priority queue with a fixed length that stores in disk pending elements in queue and recovers them every time an object is
 * retrieved.
 * 
 * @param <E>
 *            type of element
 */
public final class NotificationManagerPriorityQueue<E> extends PriorityBlockingQueue<E> {

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -8998138602541332450L;

	/** Limit size. */
	private final int limit;
	/** Database used to get/store objects. */
	private final NotificationManagerDB db;

	/**
	 * Constructor
	 * @param newlimit
	 *            Maximum number of elements
	 * @param newdb
	 *            Database used to get/store objects.
	 */
	public NotificationManagerPriorityQueue(final int newlimit, final NotificationManagerDB newdb) {
		this.limit = newlimit;
		this.db = newdb;
	}

	@Override
	public boolean add(final E msg) {
		// Add it so it can be ordered by priority
		super.add(msg);
		if (size() > limit) {
			// Remove last element
			/**
			 * Java's PriorityQueue, like most priority queues, is implemented with a heap. A heap is a data structure that
			 * maintains only the property that all parents are less than their children, or all parents are greater than their
			 * children. There is no inherent ordering among children.
			 *
			 * Thus, the only way to find the tail would be to do a linear search among the bottom layer, which costs O(n) time
			 * for a size n priority queue.
			 */
			E lastMessage = null;
			final Iterator<E> it = this.iterator();
			while (it.hasNext()) {
				// do nothing.
				lastMessage = it.next();
			}
			if (lastMessage != null) {
				// Remove last.
				it.remove();

				// Store into databse
				db.store((EventMessage) lastMessage);
			}
		}
		return true;
	}

	@Override
	public String toString() {
		final NotificationManagerPriorityQueue<E> copy = new NotificationManagerPriorityQueue<>(limit, db);
		copy.addAll(this);
		final StringBuilder strBuild = new StringBuilder();
		strBuild.append("[");
		while (!copy.isEmpty()) {
			strBuild.append(copy.poll() + ", ");
		}
		strBuild.append("]");
		return strBuild.toString();
	}

	@Override
	public E poll() {
		final E removedElement = super.poll();
		return removedElement;
	}

	/**
	 * Fill queue with messages from Database. This function must be executed after events are processed to avoid
	 *        REPEATED events.
	 */
	@SuppressWarnings("unchecked")
	public void fillQueueFromDB() {
		// IMPORTANT: only fill queue if it is empty to avoid repeated elements.
		if (size() == 0) {
			// Recover from DB some messages
			// Remember that messages are removed once processed so any of them is good to add.
			final List<EventMessage> events = db.getSomeEventMessages(1);
			int k = 0;
			while (size() < limit && k < events.size()) {
				final E recoveredMessage = (E) events.get(k);
				super.add(recoveredMessage);
				k++;
			}
		}
	}

}
