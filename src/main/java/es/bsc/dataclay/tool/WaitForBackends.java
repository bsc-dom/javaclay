
package es.bsc.dataclay.tool;

import es.bsc.dataclay.api.DataClay;
import es.bsc.dataclay.commonruntime.ClientManagementLib;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages;
import es.bsc.dataclay.tool.Util.ERRCODE;

public class WaitForBackends {

	public static void main(final String[] args) throws Exception {
		if (args.length != 2) {
			Util.finishErr("Bad arguments. Usage: \n\n " + WaitForBackends.class.getSimpleName()
					+ " <language> <numBackends>\n", ERRCODE.ERROR);
			return;
		}
		try {
			final String language = args[0];
			CommonMessages.Langs lang = null;
			if (language.equals("java")) {
				lang = CommonMessages.Langs.LANG_JAVA;
			} else if (language.equals("python")) {
				lang = CommonMessages.Langs.LANG_PYTHON;
			}
			final int numBackends = Integer.parseInt(args[1]);
			Util.init();
			// Wait for one DS to be ready
			try {
				while ( GetBackends.getBackends("admin","admin", lang).size() < numBackends) {
					System.out.println("[dataClay] Waiting for " + numBackends + " " + language + " backends to be ready...");
					Thread.sleep(2000L); //sleep 2 seconds
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
			Util.finishOut("Found " + numBackends + " " + language + " backends");

			
		} catch (final Exception ex) {
			Util.finishErr("Exception caught.", ERRCODE.ERROR);
		}
		System.exit(0); // Call this to finish logging threads
	}
}
