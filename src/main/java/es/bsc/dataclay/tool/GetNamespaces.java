
package es.bsc.dataclay.tool;

import java.util.Set;

import es.bsc.dataclay.commonruntime.ClientManagementLib;
import es.bsc.dataclay.tool.Util.ERRCODE;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;

public class GetNamespaces {

	public static void main(final String[] args) throws Exception {
		if (args.length != 2) {
			Util.finishErr(
					"Bad arguments. Usage: \n\n " + GetNamespaces.class.getSimpleName() + " <user_name> <user_pass> \n",
					ERRCODE.ERROR);
			return;
		}
		try {
			Util.init();

			final String applicantName = args[0];
			final String applicantPass = args[1];

			// Check account
			final AccountID accountID = ClientManagementLib.getAccountID(applicantName);
			if (accountID == null) {
				Util.finishErr("Invalid account", ERRCODE.ERROR);
				return;
			}
			final PasswordCredential credential = new PasswordCredential(applicantPass);

			// Get available public namespaces
			final Set<String> namespaces = ClientManagementLib.getNamespaces(accountID, credential);
			String msg = "Available namespaces:\n";
			for (final String namespace : namespaces) {
				msg += namespace + "\n";
			}

			Util.finishOut(msg);
		} catch (final Exception ex) {
			Util.finishErr("Exception caught. Check your account and credentials.", ERRCODE.ERROR);
		}
	}
}
