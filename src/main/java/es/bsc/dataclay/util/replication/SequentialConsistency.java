
package es.bsc.dataclay.util.replication;

import java.util.Set;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.api.BackendID;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.ImplementationID;

public abstract class SequentialConsistency {
	public static void synchronize(final DataClayObject dataClayObject, final String setterID, final Object[] args) {

		// Current dataClay replicas
		final BackendID masterLocation = dataClayObject.getMasterLocation();
		for (final BackendID replicaLocation : dataClayObject.getAllLocations()) {
			if (!replicaLocation.equals(masterLocation)) {
				try {
					dataClayObject.setInBackend(replicaLocation, setterID, args);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}
		return;
	}
	
	public static void synchronizeFederated(final DataClayObject dataClayObject, 
			final String setterID, final String setUpdateID, final Object[] args) {
		final Set<DataClayInstanceID> externalDcs = dataClayObject.getFederationTargets();
		final boolean isFederated = externalDcs.size() != 0;
		if (isFederated) {
			for (final DataClayInstanceID dcID : externalDcs) {
					try {
						dataClayObject.setInDataClayInstance(dcID, new ImplementationID(setterID), args);
					} catch (final Exception e) {
						e.printStackTrace();
					}
			}
		}
		// send to original one
		final DataClayInstanceID originalDc = dataClayObject.getFederationSource();
		if (originalDc != null) {
			dataClayObject.setInDataClayInstance(originalDc, new ImplementationID(setUpdateID), 
					new Object[] {args[0], true});
		}
		return;
	}

	public static void replicateToDataClaysObjectIsFederatedWith(final DataClayObject o, final String setterID,  final String setUpdateID, final Object arg) {
		synchronizeFederated(o, setterID, setUpdateID, new Object[] { arg });
	}
	
	public static void replicateToSlaves(final DataClayObject o, final String setterID, final String setUpdateID, final Object arg) {
		synchronize(o, setterID, new Object[] { arg });
	}
}
