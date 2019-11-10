
package es.bsc.dataclay.tool;

import es.bsc.dataclay.commonruntime.ClientManagementLib;
import es.bsc.dataclay.tool.Util.ERRCODE;
import es.bsc.dataclay.util.ids.DataClayInstanceID;

public class GetCurrentDataClayID {
	public static void main(String[] args) throws Exception {
		try {
			Util.init();

			final DataClayInstanceID dcID = ClientManagementLib.getDataClayID();
			Util.finishOut(dcID.toString());

		} catch (Exception ex) {
			ex.printStackTrace();
			Util.finishErr("Exception caught: " + ex.getLocalizedMessage() + ".", ERRCODE.ERROR);
		}
		System.exit(0); // Call this to finish logging threads

	}
}
