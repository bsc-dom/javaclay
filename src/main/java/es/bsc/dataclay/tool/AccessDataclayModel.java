
package es.bsc.dataclay.tool;

import es.bsc.dataclay.commonruntime.ClientManagementLib;
import es.bsc.dataclay.tool.Util.ERRCODE;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;

public class AccessDataclayModel {

	public static void main(final String[] args) throws Exception {
		if (args.length != 2) {
			Util.finishErr("Bad arguments. Usage: \n\n " + AccessDataclayModel.class.getSimpleName()
					+ " <applicant_username> <applicant_pass> \n", ERRCODE.ERROR);
			return;
		}
		try {
			Util.init();

			final String applicantName = args[0];
			final String applicantPass = args[1];

			final AccountID applicantID = ClientManagementLib.getAccountID(applicantName);
			if (applicantID == null) {
				Util.finishErr("Invalid account", ERRCODE.ERROR);
				return;
			}
			final PasswordCredential applicantCredential = new PasswordCredential(applicantPass);
			final ContractID publicContract = ClientManagementLib.getContractOfDataClayProvider(applicantID, applicantCredential);
			if (publicContract == null) {
				Util.finishErr("No public contract of dataClay can be found.", ERRCODE.ERROR);
				return;
			} else {
				final boolean result = ClientManagementLib.registerToPublicContract(applicantID, applicantCredential,
						publicContract);
				if (!result) {
					Util.finishErr("Account " + applicantName + "[" + applicantID
							+ "] could not be registered to public dataClay contract", ERRCODE.ERROR);
					return;
				}
			}
			Util.finishOut("User " + applicantName + " registered in data model of dataClay");
		} catch (final Exception ex) {
			Util.finishErr("Exception caught. Check your account and credentials.", ERRCODE.ERROR);
		}
		System.exit(0); // Call this to finish logging threads
	}
}
