
package es.bsc.dataclay.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import es.bsc.dataclay.commonruntime.ClientManagementLib;
import es.bsc.dataclay.tool.Util.ERRCODE;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.classmgr.Operation;
import es.bsc.dataclay.util.management.classmgr.Property;
import es.bsc.dataclay.util.management.contractmgr.Contract;
import es.bsc.dataclay.util.management.contractmgr.InterfaceInContract;
import es.bsc.dataclay.util.management.contractmgr.OpImplementations;
import es.bsc.dataclay.util.management.interfacemgr.Interface;

public class NewModel {

	public static void main(final String[] args) throws Exception {
		if (args.length < 4 || (args.length > 4 && !args[4].equals("--prefetch"))) {
			Util.finishErr("Bad arguments. Usage: \n\n " + NewModel.class.getSimpleName()
					+ " <owner_name> <owner_pass> <namespace_name> <class_path> \n"
					+ " [--prefetch on <src_path> <lib_path> | rop <fetch_depth> <src_path> <lib_path>] \n", ERRCODE.ERROR);
			return;
		}
		try {
			Util.init();
			
			System.out.println(Arrays.toString(args));

			final String ownerName = args[0];
			final String ownerPass = args[1];
			final String namespaceName = args[2];
			final String classPath = args[3];
			String srcPath = "";
			String libPath = "";

			if (args.length > 4) {
				if (!ClientManagementLib.getDataClayClientLib().getLogicModuleAPI().isPrefetchingEnabled()) {
					System.out.println("[WARNING] "
							+ "Registering classes with prefetching in a dataClay instance where prefetching is disabled. "
							+ "Restart dataClay with the property PREFETCHING_ENABLED set to true in order to activate prefetching.");
				} else {
					System.out.println("Registering classes with prefetching.");
				}
				Configuration.Flags.PREFETCHING_ENABLED.setValue(true);
				final String prefetchType = args[5];
				if (prefetchType.equals("rop")) {
					Configuration.Flags.PREFETCHING_ROP_ENABLED.setValue(true);
					Configuration.Flags.PREFETCHING_ROP_DEPTH.setValue(Integer.valueOf(args[6]));
					// Parallel prefetching CANNOT be used with ROP prefetching
					Configuration.Flags.PREFETCHING_PARALLEL_ENABLED.setValue(false);
					srcPath = args[7];
					libPath = args[8];
				} else {
					Configuration.Flags.PREFETCHING_PARALLEL_ENABLED.setValue(true);
					srcPath = args[6];
					libPath = args[7];
				}
			}

			// Get accounts info
			final AccountID ownerID = ClientManagementLib.getAccountID(ownerName);
			if (ownerID == null) {
				Util.finishErr("Invalid account", ERRCODE.ERROR);
				return;
			}
			final PasswordCredential ownerCredential = new PasswordCredential(ownerPass);

			// Check namespace
			if (ClientManagementLib.getNamespaceID(ownerID, ownerCredential, namespaceName) == null) {
				Util.finishErr("Invalid namespace or permissions", ERRCODE.ERROR);
				return;
			}

			// Find classes
			final Set<String> classes = getAllClassPaths(classPath);
			if (classes == null || classes.isEmpty()) {
				Util.finishErr("No class found in " + classPath, ERRCODE.ERROR);
				return;
			}

			// Register the included classes
			System.out.println("Registering classes in " + classPath);
			final Map<String, MetaClass> registeredClasses = ClientManagementLib.newClasses(ownerID, ownerCredential,
					namespaceName, classes, classPath, srcPath, libPath);
			// Create interfaces for the public contract
			if (registeredClasses == null || registeredClasses.isEmpty()) {
				Util.finishErr("No classes to be registered", ERRCODE.WARNING);
				return;
			}
			System.out.println("Registered classes: \n " + registeredClasses.keySet());

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
				final Interface newInterface = new Interface(ownerName, namespaceName, namespaceName, currentMclassInfo.getName(),
						propNames, operationsSignatures);

				final InterfaceID newIfaceID = ClientManagementLib.newInterface(ownerID, ownerCredential, newInterface);
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
			final Contract newContract = new Contract(namespaceName, ownerName, ifacesInContract, beginDate, endDate);
			ClientManagementLib.newPublicContract(ownerID, ownerCredential, newContract);

			Util.finishOut("Registered data model in namespace: " + namespaceName);
		} catch (final Exception ex) {
			Util.finishErr("Exception caught. Check your account and credentials.", ERRCODE.ERROR);
		}
	}

	private static Set<String> getAllClassPaths(final String classPath) {
		final Set<String> result = new HashSet<>();
		final Stack<File> stack = new Stack<>();
		File curFile = new File(classPath);
		final String classPathAbsolute = curFile.getAbsolutePath();
		updateStackResult(curFile, stack, result, classPathAbsolute);
		while (!stack.isEmpty()) {
			curFile = stack.pop();
			updateStackResult(curFile, stack, result, classPathAbsolute);
		}
		return result;
	}

	private static void updateStackResult(final File curFile, final Stack<File> stack, final Set<String> result,
			final String classPathAbsolute) {
		final File[] contents;
		if (curFile.isDirectory()) {
			contents = curFile.listFiles();
			stack.addAll(Arrays.asList(contents));
		} else {
			final String filename = curFile.getName();
			if (filename.endsWith(".class")) {
				final String curFileAbsPath = curFile.getAbsolutePath();
				final String classPath = curFileAbsPath.substring(classPathAbsolute.length() + 1);
				final String fixedClassPath = classPath.substring(0, classPath.length() - ".class".length());
				final String className = fixedClassPath.replace('/', '.').replace('\\', '.');
				result.add(className);
			}
		}
	}
}
