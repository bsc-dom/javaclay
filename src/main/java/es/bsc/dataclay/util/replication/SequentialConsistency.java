
package es.bsc.dataclay.util.replication;

import java.lang.reflect.Executable;
import java.util.Set;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.api.BackendID;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.ImplementationID;

public abstract class SequentialConsistency {
	public static void synchronize(final DataClayObject dataClayObject, final String setterID, final Object[] args) {
		dataClayObject.synchronize(new ImplementationID(setterID), args);
	}

	public static void replicateToDataClaysObjectIsFederatedWith(final DataClayObject o, final String setterID,  final String setUpdateID, final Object arg) {
		synchronize(o, setterID, new Object[] { arg });
	}
	
	public static void replicateToSlaves(final DataClayObject o, final String setterID, final String setUpdateID, final Object arg) {
		synchronize(o, setterID, new Object[] { arg });
	}
}
