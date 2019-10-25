
package es.bsc.dataclay.dataservice;

import java.util.Map;
import java.util.Stack;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.ids.SessionID;

/**
 * Run lazy tasks in DataService.
 */
public final class LazyTasksRunner extends TimerTask {

	/** Pending tasks to run. */
	private final Map<ObjectID, Stack<LazyTask>> pendingTasks = new ConcurrentHashMap<>();

	/** DataService in which to run tasks. */
	private final DataService dataService;

	/**
	 * Constructor
	 * @param theDataService
	 *            DataService.
	 */
	public LazyTasksRunner(final DataService theDataService) {
		this.dataService = theDataService;
		
	}

	@Override
	public void run() {
		try {
			for (final Stack<LazyTask> lazyTasksPerObject : pendingTasks.values()) {
				while (!lazyTasksPerObject.isEmpty()) {
					final LazyTask lazyTask = lazyTasksPerObject.pop();
					final ObjectID objectID = lazyTask.getObjectID();
					final ImplementationID implID = lazyTask.getImplementationID();
					final SessionID sessionID = lazyTask.getSessionID();
					final MetaClassID classID = lazyTask.getClassID();
					dataService.executeLazyTask(implID, objectID, sessionID, classID);
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
			// do nothing, the receiver will debug accordingly if needed
		}
	}

	/**
	 * Add lazy task
	 * @param objectID
	 *            ID of the object
	 * @param lazyTask
	 *            Lazy task
	 */
	public void addLazyTask(final ObjectID objectID, final LazyTask lazyTask) {
		Stack<LazyTask> lazyTasks = null;
		synchronized (pendingTasks) {
			lazyTasks = this.pendingTasks.get(objectID);
			if (lazyTasks == null) {
				lazyTasks = new Stack<>();
				this.pendingTasks.put(objectID, lazyTasks);
			}
		}
		lazyTasks.add(lazyTask);
	}

}
