
package es.bsc.dataclay.tool;

import java.util.Map;
import java.util.Set;

import es.bsc.dataclay.commonruntime.ClientManagementLib;
import es.bsc.dataclay.tool.Util.ERRCODE;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.DataContractID;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;
import es.bsc.dataclay.util.management.datacontractmgr.DataContract;

public class GetDatasets {

	public static void main(final String[] args) throws Exception {
		if (args.length != 2) {
			Util.finishErr("Bad arguments. Usage: \n\n " + GetDatasets.class.getSimpleName()
					+ " <user_name> <user_pass> \n", ERRCODE.ERROR);
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

			// Get available public datasets
			final Set<String> publicDatasets = ClientManagementLib.getPublicDatasets(accountID, credential);

			String msg = "Accessible public datasets:" + "\n";
			for (final String publicDS : publicDatasets) {
				msg += publicDS + "\n";
			}

			// Get accessible datasets from data contracts
			final Map<DataContractID, DataContract> dataContracts = ClientManagementLib.getDataContractsOfApplicant(accountID,
					credential);
			msg += "\n";
			msg += "Accessible non-public datasets ids:" + "\n";
			for (final DataContract dc : dataContracts.values()) {
				// TODO Create getDatasetName from id in order to show the names
				final String dataset = dc.getProviderDataSetID().toString();
				msg += dataset + "\n";
			}
			Util.finishOut(msg);
		} catch (final Exception ex) {
			Util.finishErr("Exception caught. Check your account and credentials.", ERRCODE.ERROR);
		}
		System.exit(0); // Call this to finish logging threads

	}
}
