
package es.bsc.dataclay.tool;

import java.util.Arrays;

import es.bsc.dataclay.commonruntime.ClientManagementLib;
import es.bsc.dataclay.tool.Util.ERRCODE;

public class WaitForDataClayToBeAlive {

	public static void main(final String[] args) throws Exception {
		if (args.length != 2) {
			Util.finishErr("Bad arguments. Usage: \n\n " + WaitForDataClayToBeAlive.class.getSimpleName()
					+ " <maxretries> <waitseconds>\n", ERRCODE.ERROR);
			return;
		}
		try {
			final int maxretries = Integer.parseInt(args[0]);
			final int waitingseconds = Integer.parseInt(args[1]) * 1000;
			int retries = 0; 
			boolean connected = false;
			while (retries < maxretries) {
				if (ClientManagementLib.initializeCMLib(null)) {
					connected = true;
					break;
				}
				retries++;
				Thread.sleep(waitingseconds);
			}
			if (connected) { 
				Util.finishOut("Connection success!");
			} else { 
				Util.finishErr("Could not connect to dataClay", ERRCODE.ERROR);
			}
			
		} catch (final Exception ex) {
			Util.finishErr("Exception caught.", ERRCODE.ERROR);
		}
		System.exit(0); // Call this to finish logging threads
	}
}
