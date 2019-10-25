
package es.bsc.dataclay.heap;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.commonruntime.DataClayRuntime;

/**
 * This class is responsible to manage memory Heap at client side.
 */
public class ClientHeapManager extends HeapManager {
	/**
	 * Contructor.
	 * 
	 * @param theruntime
	 *            The runtime.
	 */
	public ClientHeapManager(final DataClayRuntime theruntime) {
		super(theruntime);
	}

	@Override
	public final void run() {
		super.cleanReferencesAndLockers();
	}

	@Override
	public void flushAll() {

	}

	@Override
	public void addToHeap(final DataClayObject dcObject) {
		super.addToHeapMap(dcObject);
	}

}
