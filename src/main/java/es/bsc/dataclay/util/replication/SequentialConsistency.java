
package es.bsc.dataclay.util.replication;

import java.util.Set;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.api.BackendID;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.ImplementationID;

public abstract class SequentialConsistency {
	public static void replicateToSlaves(final DataClayObject dataClayObject, final String setterID, final Object[] args) {

		// Current dataClay replicas
		final BackendID masterLocation = dataClayObject.getMasterLocation();
		for (final BackendID replicaLocation : dataClayObject.getAllLocations()) {
			if (!replicaLocation.equals(masterLocation)) {
				try {
					dataClayObject.runRemote(replicaLocation, setterID, args);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}
		return;
	}
	
	public static void replicateToDataClaysObjectIsFederatedWith(final DataClayObject dataClayObject, 
			final String setterID, final String setUpdateID, final Object[] args) {
		final Set<DataClayInstanceID> externalDcs = dataClayObject.getDataClaysObjectIsFederatedWith();
		final boolean isFederated = externalDcs.size() != 0;
		System.out.println("Replicated to external dataclays object is federated with? :" + isFederated);
		if (isFederated) {
			for (final DataClayInstanceID dcID : externalDcs) {
					try {
						System.out.println("Replicating to " + dcID);
						dataClayObject.synchronizeFederated(dcID, new ImplementationID(setterID), args);
					} catch (final Exception e) {
						e.printStackTrace();
					}
			}
		}
		// send to original one
		final DataClayInstanceID originalDc = dataClayObject.getExternalSourceDataClayOfObject();
		if (originalDc != null) {
			dataClayObject.synchronizeFederated(originalDc, new ImplementationID(setUpdateID), 
					new Object[] {args[0], true});
		}
		return;
	}

	public static void replicateToDataClaysObjectIsFederatedWith(final DataClayObject o, final String setterID,  final String setUpdateID, final Object arg) {
		replicateToDataClaysObjectIsFederatedWith(o, setterID, setUpdateID, new Object[] { arg });
	}
	
	public static void replicateToSlaves(final DataClayObject o, final String setterID, final String setUpdateID, final Object arg) {
		replicateToSlaves(o, setterID, new Object[] { arg });
	}
}
