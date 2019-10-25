
package es.bsc.dataclay.tool;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import es.bsc.dataclay.commonruntime.ClientManagementLib;
import es.bsc.dataclay.tool.Util.ERRCODE;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.DataContractID;
import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;
import es.bsc.dataclay.util.management.datacontractmgr.DataContract;
import es.bsc.dataclay.util.management.datasetmgr.DataSet;

public class NewDataContract {

	public static void main(final String[] args) throws Exception {
		if (args.length != 4) {
			Util.finishErr("Bad arguments. Usage: \n\n " + NewDataContract.class.getSimpleName()
					+ " <owner_name> <owner_pass> <dataset_name> <benef_name> \n", ERRCODE.ERROR);
			return;
		}
		try {
			Util.init();

			final String ownerName = args[0];
			final String ownerPass = args[1];
			final String datasetName = args[2];
			final String benefName = args[3];

			final AccountID ownerID = ClientManagementLib.getAccountID(ownerName);
			if (ownerID == null) {
				Util.finishErr("Invalid account", ERRCODE.ERROR);
				return;
			}

			final PasswordCredential ownerCredential = new PasswordCredential(ownerPass);
			DataSetID datasetID = ClientManagementLib.getDatasetID(ownerID, ownerCredential, datasetName);
			if (datasetID == null) {
				final DataSet newDataSet = new DataSet(datasetName, ownerName, false);
				datasetID = ClientManagementLib.newDataSet(ownerID, ownerCredential, newDataSet);
				if (datasetID == null) {
					Util.finishErr("Dataset " + datasetName + " is required but cannot be created.", ERRCODE.ERROR);
					return;
				}
			}

			final AccountID benefID = ClientManagementLib.getAccountID(benefName);
			if (benefID == null) {
				Util.finishErr("Invalid beneficiary account", ERRCODE.ERROR);
				return;
			}

			final Set<AccountID> applicantIDs = new HashSet<>();
			applicantIDs.add(benefID);
			final Calendar beginDate = Calendar.getInstance();
			beginDate.add(Calendar.YEAR, -1);
			final Calendar endDate = Calendar.getInstance();
			endDate.add(Calendar.YEAR, 100);
			final DataContract dContract = new DataContract(datasetID, ownerID, applicantIDs, beginDate, endDate);

			// Create a new contract for the benef to access it
			final DataContractID dContractID = ClientManagementLib.newPrivateDataContract(ownerID, ownerCredential, dContract);

			if (dContractID != null) {
				Util.finishOut("User " + benefName + " is granted access to dataset " + datasetName);
			} else {
				Util.finishErr("User " + benefName + " probably has already granted access to dataset " + datasetName,
						ERRCODE.WARNING);
			}
		} catch (final Exception ex) {
			Util.finishErr("Exception caught. Check your account and credentials.", ERRCODE.ERROR);
		}
	}
}
