
package es.bsc.dataclay.tool;

import es.bsc.dataclay.commonruntime.ClientManagementLib;
import es.bsc.dataclay.tool.Util.ERRCODE;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.management.accountmgr.Account;
import es.bsc.dataclay.util.management.accountmgr.AccountRole;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;

public class NewAccount {
	public static void main(final String[] args) throws Exception {
		if (args.length != 2) {
			Util.finishErr("Bad arguments. Usage: \n\n " + NewAccount.class.getSimpleName()
					+ " <newaccount_name> <newaccount_pass> \n", ERRCODE.ERROR);
			return;
		}
		try {
			Util.init();

			final String newAccName = args[0];
			final String newAccPass = args[1];

			// Get accounts info
			final PasswordCredential newAccPassCred = new PasswordCredential(newAccPass);
			final Account newAccount = new Account(newAccName, AccountRole.NORMAL_ROLE, newAccPassCred);
			final AccountID newAccID = ClientManagementLib.newAccount(newAccount);

			if (newAccID != null) {
				Util.finishOut("Account created for user " + newAccName);
			} else {
				Util.finishErr("Account " + newAccName + " already exists.", ERRCODE.WARNING);
			}
		} catch (final Exception ex) {
			Util.finishErr("Exception caught. Check your account and credentials.", ERRCODE.ERROR);
		}
		System.exit(0); // Call this to finish logging threads
	}
}
