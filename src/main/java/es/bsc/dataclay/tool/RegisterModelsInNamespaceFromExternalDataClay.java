
package es.bsc.dataclay.tool;

import es.bsc.dataclay.commonruntime.ClientManagementLib;
import es.bsc.dataclay.tool.Util.ERRCODE;
import es.bsc.dataclay.util.ids.DataClayInstanceID;

public class RegisterModelsInNamespaceFromExternalDataClay {
	public static void main(final String[] args) throws Exception {
		if (args.length != 3) {
			Util.finishErr("Bad arguments. Usage: \n\n " + RegisterModelsInNamespaceFromExternalDataClay.class.getSimpleName()
					+ " <host> <port> <namespace> \n", ERRCODE.ERROR);
			return;
		}
		try {
			Util.init();

			final String dcHost = args[0];
			final int dcPort = Integer.valueOf(args[1]);
			String namespace = args[2];

			final DataClayInstanceID dcID = ClientManagementLib.registerExternalDataClay(dcHost, dcPort);
			ClientManagementLib.registerClassesInNamespaceFromExternalDataClay(namespace, dcID);
			Util.finishOut("Registered models in namespace " + namespace
					+ " from external dataClay with id " + dcID);

		} catch (final Exception ex) {
			Util.finishErr("Exception caught. Check dataClay info is valid.", ERRCODE.ERROR);
		}
		System.exit(0); // Call this to finish logging threads
	}
}
