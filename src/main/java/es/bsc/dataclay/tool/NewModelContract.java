
package es.bsc.dataclay.tool;

import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import es.bsc.dataclay.commonruntime.ClientManagementLib;
import es.bsc.dataclay.tool.Util.ERRCODE;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.classmgr.Operation;
import es.bsc.dataclay.util.management.classmgr.Property;
import es.bsc.dataclay.util.management.contractmgr.Contract;
import es.bsc.dataclay.util.management.contractmgr.InterfaceInContract;
import es.bsc.dataclay.util.management.contractmgr.OpImplementations;
import es.bsc.dataclay.util.management.interfacemgr.Interface;

import java.util.Set;

public class NewModelContract {

	public static void main(final String[] args) throws Exception {
		if (args.length != 4) {
			Util.finishErr("Bad arguments. Usage: \n\n " + NewModelContract.class.getSimpleName()
					+ "<owner_name> <owner_pass> <namespace_name> <benef_name> \n", ERRCODE.ERROR);
			return;
		}
		try {
			Util.init();

			final String ownerName = args[0];
			final String ownerPass = args[1];
			final String namespaceName = args[2];
			final String benefName = args[3];

			final AccountID ownerID = ClientManagementLib.getAccountID(ownerName);
			if (ownerID == null) {
				Util.finishErr("Invalid account", ERRCODE.ERROR);
				return;
			}
			final PasswordCredential ownerCredential = new PasswordCredential(ownerPass);
			final AccountID benefID = ClientManagementLib.getAccountID(benefName);
			if (benefID == null) {
				Util.finishErr("Invalid beneficiary account", ERRCODE.ERROR);
				return;
			}
			final NamespaceID ownerNamespaceID = ClientManagementLib.getNamespaceID(ownerID, ownerCredential,
					namespaceName);
			if (ownerNamespaceID == null) {
				Util.finishErr("Invalid namespace", ERRCODE.ERROR);
				return;
			}
			final Map<MetaClassID, MetaClass> classesInNamespace = ClientManagementLib.getClassesInfoInNamespace(ownerID,
					ownerCredential, ownerNamespaceID);

			if (classesInNamespace != null && classesInNamespace.size() > 0) {
				final List<InterfaceInContract> newInterfacesInContract = new LinkedList<>();

				for (final Entry<MetaClassID, MetaClass> curClass : classesInNamespace.entrySet()) {
					final MetaClassID mclassID = curClass.getKey();
					final MetaClass mclass = curClass.getValue();
					System.out.println(" == Current class " + mclass.getName() + " ID " + mclassID);
					final Set<String> propertiesNames = new HashSet<>();
					for (final Property prop : mclass.getProperties()) {
						propertiesNames.add(prop.getName());
					}

					final Set<String> opsSignature = new HashSet<>();
					final Set<OpImplementations> opImpls = new HashSet<>();
					for (final Operation op : mclass.getOperations()) {
						opsSignature.add(op.getNameAndDescriptor());

						final OpImplementations newOpImpls = new OpImplementations(op.getNameAndDescriptor(), 0, 0);
						opImpls.add(newOpImpls);
					}

					final Interface newIface = new Interface(ownerName, namespaceName, namespaceName, mclass.getName(),
							propertiesNames, opsSignature);

					final InterfaceID newIfaceID = ClientManagementLib.newInterface(ownerID, ownerCredential, newIface);
					if (newIfaceID != null) {
						System.out.println("[LOG] Created interface for class " + newIface.getClassName());
						final InterfaceInContract ifaceInContract = new InterfaceInContract(newIface, opImpls);
						newInterfacesInContract.add(ifaceInContract);
					}
				}

				if (newInterfacesInContract.size() <= 0) {
					Util.finishErr("No new interfaces found, contract is not created.", ERRCODE.ERROR);
					return;
				}

				final Set<AccountID> applicantIDs = new HashSet<>();
				applicantIDs.add(benefID);
				final Calendar beginDate = Calendar.getInstance();
				final Calendar endDate = Calendar.getInstance();
				beginDate.add(Calendar.YEAR, -1);
				endDate.add(Calendar.YEAR, 1);
				final Contract newContract = new Contract(namespaceName, ownerID, applicantIDs, newInterfacesInContract,
						beginDate, endDate);
				final ContractID newContractID = ClientManagementLib.newPrivateContract(ownerID, ownerCredential,
						newContract);

				if (newContractID != null) {
					Util.finishOut("Created contract " + newContractID);
				} else {
					Util.finishErr("Model contract cannot be created", ERRCODE.WARNING);
				}
			}
		} catch (final Exception ex) {
			Util.finishErr("Exception caught. Check your account and credentials.", ERRCODE.ERROR);
		}
		System.exit(0); // Call this to finish logging threads
	}
}
