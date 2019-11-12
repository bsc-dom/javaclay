
package es.bsc.dataclay.tool;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.bsc.dataclay.commonruntime.ClientManagementLib;
import es.bsc.dataclay.tool.Util.ERRCODE;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.classmgr.Operation;
import es.bsc.dataclay.util.management.classmgr.Property;
import es.bsc.dataclay.util.management.contractmgr.Contract;
import es.bsc.dataclay.util.management.contractmgr.InterfaceInContract;
import es.bsc.dataclay.util.management.contractmgr.OpImplementations;
import es.bsc.dataclay.util.management.interfacemgr.Interface;

public class AccessNamespace {

	public static void main(final String[] args) throws Exception {
		if (args.length != 3) {
			Util.finishErr("Bad arguments. Usage: \n\n " + AccessNamespace.class.getSimpleName()
					+ " <applicant_username> <applicant_pass> <namespace_name> \n", ERRCODE.ERROR);
			return;
		}
		try {
			Util.init();

			final String applicantName = args[0];
			final String applicantPass = args[1];
			final String namespace = args[2];

			// Check account
			final AccountID applicantID = ClientManagementLib.getAccountID(applicantName);
			if (applicantID == null) {
				Util.finishErr("Invalid account", ERRCODE.ERROR);
				return;
			}
			final PasswordCredential applicantCredential = new PasswordCredential(applicantPass);

			// Check namespace
			final NamespaceID nspaceID = ClientManagementLib.getNamespaceID(applicantID, applicantCredential, namespace);
			if (nspaceID == null) {
				Util.finishErr("Namespace " + namespace + " does not exist.", ERRCODE.ERROR);
				return;
			}

			ContractID contractID = null;
			final Map<ContractID, Contract> curContracts = ClientManagementLib.getContractsOfApplicant(applicantID,
					applicantCredential, nspaceID);
			if (curContracts != null && curContracts.size() != 0) {
				contractID = curContracts.keySet().iterator().next();
			} else {
				// Register to public contract of the namespace
				contractID = ClientManagementLib.registerToPublicContractOfNamespace(applicantID, applicantCredential,
						nspaceID);
			}

			if (contractID == null) {
				// ======== Create public contract with namespace provided ======

				final Map<MetaClassID, MetaClass> registeredClasses = ClientManagementLib.getClassesInfoInNamespace(applicantID, applicantCredential, nspaceID);

				// Parse info of metaclasses
				final List<InterfaceInContract> ifacesInContract = new ArrayList<>();
				for (final MetaClass currentMclassInfo : registeredClasses.values()) {
					final Set<String> operationsSignatures = new HashSet<>();
					final Set<OpImplementations> opImpls = new HashSet<>();
					final Map<OperationID, OpImplementations> actualOpImpls = new HashMap<>();
					for (final Operation op : currentMclassInfo.getOperations()) {
						operationsSignatures.add(op.getNameAndDescriptor());
						final OpImplementations opImplem = new OpImplementations(op.getNameAndDescriptor(), 0, 0);
						opImpls.add(opImplem);
						final ImplementationID implID = op.getImplementations().get(0).getDataClayID();
						opImplem.setLocalImplementationID(implID);
						opImplem.setRemoteImplementationID(implID);
						actualOpImpls.put(op.getDataClayID(), opImplem);
					}

					final Set<String> propNames = new HashSet<>();
					for (final Property prop : currentMclassInfo.getProperties()) {
						propNames.add(prop.getName());
					}

					// Create a new Interface for the class
					final Interface newInterface = new Interface(applicantName, namespace, namespace, currentMclassInfo.getName(),
							propNames, operationsSignatures);

					final InterfaceID newIfaceID = ClientManagementLib.newInterface(applicantID, applicantCredential, newInterface);
					if (newIfaceID != null) {
						// Update the interfaces in contract
						final InterfaceInContract interfaceInContract = new InterfaceInContract(newInterface, opImpls);
						interfaceInContract.setAccessibleImplementations(actualOpImpls);
						ifacesInContract.add(interfaceInContract);
					}
				}

				// Create public contract
				final Calendar beginDate = Calendar.getInstance();
				final Calendar endDate = Calendar.getInstance();
				beginDate.add(Calendar.YEAR, -1);
				endDate.add(Calendar.YEAR, 10);
				final Contract newContract = new Contract(namespace, applicantName, ifacesInContract, beginDate, endDate);
				ClientManagementLib.newPublicContract(applicantID, applicantCredential, newContract);

				// Register to public contract of the namespace
				contractID = ClientManagementLib.registerToPublicContractOfNamespace(applicantID, applicantCredential,
						nspaceID);
			}

			// TODO Notice that break line is needed for the global tool to "tail" the contract ID
			System.out.println("User " + applicantName + " registered in data model of namespace: " + namespace
					+ " with contract ID: \n" + contractID.toString());
		} catch (final Exception ex) {
			Util.finishErr("Exception caught. Check your account and credentials.", ERRCODE.ERROR);
		}
		System.exit(0); // Call this to finish logging threads

	}
}
