
package es.bsc.dataclay.tool;

import java.util.LinkedList;
import java.util.Map;

import es.bsc.dataclay.commonruntime.ClientManagementLib;
import es.bsc.dataclay.tool.Util.ERRCODE;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;
import es.bsc.dataclay.util.management.contractmgr.Contract;

public class GetStubs {

	public static void main(final String[] args) throws Exception {
		if (args.length != 4) {
			Util.finishErr("Bad arguments. Usage: \n\n " + GetStubs.class.getSimpleName()
					+ " <user_name> <user_pass> <namespace_name> <stubs_path> \n", ERRCODE.ERROR);
			return;
		}
		try {
			Util.init();

			final String userName = args[0];
			final String userPass = args[1];
			final String namespace = args[2];
			final String userStubsPath = args[3];

			// Get account info
			final AccountID accountID = ClientManagementLib.getAccountID(userName);
			if (accountID == null) {
				Util.finishErr("Invalid account", ERRCODE.ERROR);
				return;
			}
			final PasswordCredential userCredential = new PasswordCredential(userPass);

			// Check namespace
			final NamespaceID nspaceID = ClientManagementLib.getNamespaceID(accountID, userCredential, namespace);
			if (nspaceID == null) {
				Util.finishErr("Namespace " + namespace + " does not exist.", ERRCODE.ERROR);
				return;
			}

			// Check if already registered
			ContractID contractID = null;
			final Map<ContractID, Contract> curContracts = ClientManagementLib.getContractsOfApplicant(accountID, userCredential,
					nspaceID);
			if (curContracts != null && curContracts.size() != 0) {
				contractID = curContracts.keySet().iterator().next();
			}
			if (contractID == null) {
				Util.finishErr("User " + userName + " is not registered to data model of namespace " + namespace,
						ERRCODE.ERROR);
				return;
			}
			final LinkedList<ContractID> contractsIDs = new LinkedList<>();
			contractsIDs.add(contractID);

			// Get and store stubs
			ClientManagementLib.getAndStoreStubs(accountID, userCredential, contractsIDs, userStubsPath);

			// finish
			Util.finishOut("Stubs stored in: " + userStubsPath);
		} catch (final Exception ex) {
			Util.finishErr("Exception caught. Check your account and credentials.", ERRCODE.ERROR);
		}
	}
}
