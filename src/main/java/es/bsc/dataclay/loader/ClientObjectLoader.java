package es.bsc.dataclay.loader;

import java.util.Map;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.commonruntime.ClientRuntime;
import es.bsc.dataclay.commonruntime.DataClayRuntime;
import es.bsc.dataclay.serialization.lib.DataClayDeserializationLib;
import es.bsc.dataclay.serialization.lib.ObjectWithDataParamOrReturn;
import es.bsc.dataclay.util.classloaders.DataClayClassLoader;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;

/**
 * This class is responsable to create DataClayObjects and load them with data
 * coming from different resources. All possible constructions of DataClayObject
 * should be included here. All possible "filling instance" use-cases should be
 * managed here.
 * 
 *
 */
public class ClientObjectLoader extends DataClayObjectLoader {

	/** Runtime being managed. **/
	private final ClientRuntime runtime;

	/**
	 * Constructor
	 * 
	 * @param theruntime
	 *            Runtime being managed
	 */
	public ClientObjectLoader(final ClientRuntime theruntime) {
		this.runtime = theruntime;
	}

	@Override
	protected DataClayRuntime getRuntime() {
		return runtime;
	}

	@Override
	public DataClayObject newInstance(final MetaClassID classID, final ObjectID objectID) {
		return DataClayClassLoader.newInstance(classID, objectID);
	}

	@Override
	public void deserializeDataIntoInstance(DataClayObject instance, ObjectWithDataParamOrReturn data,
			Map<MetaClassID, byte[]> ifaceBitMaps) {
		DataClayDeserializationLib.deserializeObjectWithDataInClient(data, instance, ifaceBitMaps, getRuntime(),
				getRuntime().getSessionID());
	}
}
