
package es.bsc.dataclay.tool;

import es.bsc.dataclay.commonruntime.ClientManagementLib;
import es.bsc.dataclay.tool.Util.ERRCODE;
import es.bsc.dataclay.util.ids.DataClayInstanceID;

public class NewDataClayInstance {
	public static void main(final String[] args) throws Exception {
		if (args.length != 2) {
			Util.finishErr("Bad arguments. Usage: \n\n " + NewDataClayInstance.class.getSimpleName()
					+ " <host> <port> \n", ERRCODE.ERROR);
			return;
		}
		try {
			Util.init();

			final String dcHost = args[0];
			final int dcPort = Integer.valueOf(args[1]);

			final DataClayInstanceID dcID = ClientManagementLib.registerExternalDataClay(dcHost, dcPort);
			Util.finishOut("dataClay instance found with id: " + dcID);

		} catch (final Exception ex) {
			Util.finishErr("Exception caught. Check dataClay info is valid.", ERRCODE.ERROR);
		}
	}
}
