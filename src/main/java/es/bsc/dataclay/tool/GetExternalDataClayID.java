
package es.bsc.dataclay.tool;

import es.bsc.dataclay.commonruntime.ClientManagementLib;
import es.bsc.dataclay.tool.Util.ERRCODE;
import es.bsc.dataclay.util.ids.DataClayInstanceID;

public class GetExternalDataClayID {
	public static void main(final String[] args) throws Exception {
		if (args.length != 2) {
			Util.finishErr("Bad arguments. Usage: \n\n " + GetExternalDataClayID.class.getSimpleName()
					+ " <host> <port> \n", ERRCODE.ERROR);
			return;
		}
		try {
			Util.init();

			final String dcHost = args[0];
			final int dcPort = Integer.valueOf(args[1]);

			final DataClayInstanceID dcID = ClientManagementLib.getExternalDataClayID(dcHost, dcPort);
			if (dcID == null) {
				Util.finishErr("Cannot retrieve external dataClay ID from " + dcHost + ":" + dcPort, ERRCODE.ERROR);
			} else {
				Util.finishOut(dcID.toString());
			}

		} catch (final Exception ex) {
			ex.printStackTrace();
			Util.finishErr("Exception caught: " + ex.getLocalizedMessage() + ".", ERRCODE.ERROR);
		}
	}
}
