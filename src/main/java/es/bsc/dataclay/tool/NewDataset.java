
package es.bsc.dataclay.tool;

import java.util.Calendar;

import es.bsc.dataclay.commonruntime.ClientManagementLib;
import es.bsc.dataclay.tool.Util.DATASETTYPES;
import es.bsc.dataclay.tool.Util.ERRCODE;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;
import es.bsc.dataclay.util.management.datacontractmgr.DataContract;
import es.bsc.dataclay.util.management.datasetmgr.DataSet;

public class NewDataset {
	public static void main(final String[] args) throws Exception {
		if (args.length != 4) {
			Util.finishErr("Bad arguments. Usage: \n\n " + NewDataset.class.getSimpleName()
					+ " <owner_name> <owner_pass> <dataset_name> <dataset_type> ( dataset_type : " + DATASETTYPES.getDatasetTypes() + ")\n",
					ERRCODE.ERROR);
			return;
		}
		try {
			Util.init();

			final String ownerName = args[0];
			final String ownerPass = args[1];
			final String datasetName = args[2];
			final String datasetType = args[3];
			final DATASETTYPES dsType = DATASETTYPES.getType(datasetType);
			if (dsType == null) {
				Util.finishErr("Unsupported dataset type " + datasetType + ". Supported ones are " + DATASETTYPES.getDatasetTypes(), ERRCODE.ERROR);
				return;
			}
			boolean isPublic = false;
			if (dsType.equals(DATASETTYPES.PUBLIC)) {
				isPublic = true;
			}

			// Get accounts info
			final AccountID ownerID = ClientManagementLib.getAccountID(ownerName);
			if (ownerID == null) {
				Util.finishErr("Invalid account", ERRCODE.ERROR);
				return;
			}
			final PasswordCredential ownerCredential = new PasswordCredential(ownerPass);

			final DataSet newDataset = new DataSet(datasetName, ownerName, isPublic);
			final DataSetID datasetID = ClientManagementLib.newDataSet(ownerID, ownerCredential, newDataset);
			if (datasetID == null) {
				Util.finishErr("Dataset " + datasetName + " cannot be created", ERRCODE.ERROR);
				return;
			}

			if (isPublic) {
				final Calendar beginDate = Calendar.getInstance();
				beginDate.add(Calendar.YEAR, -1);
				final Calendar endDate = Calendar.getInstance();
				endDate.add(Calendar.YEAR, 100);
				final DataContract newContract = new DataContract(datasetID, ownerID, beginDate, endDate);
				ClientManagementLib.newPublicDataContract(ownerID, ownerCredential, newContract);
				Util.finishOut("Created new public dataset: " + datasetName + " with public contract.");
			} else {
				Util.finishOut("Created new private dataset: " + datasetName + ".");
			}
		} catch (final Exception ex) {
			Util.finishErr("Exception caught. Check your account and credentials.", ERRCODE.ERROR);
		}
		System.exit(0); // Call this to finish logging threads
	}
}
