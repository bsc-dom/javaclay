package es.bsc.dataclay.api;

import es.bsc.dataclay.util.ids.ID;

import java.io.Serializable;
import java.util.UUID;

/** ID of a backend. */
public interface BackendID extends Serializable {

	/**
	 * Get UUID representation of Backend ID.
	 * 
	 * @return UUID representation of current Backend ID.
	 **/
	UUID getId();
}
