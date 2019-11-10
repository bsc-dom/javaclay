
package es.bsc.dataclay.tool;

import es.bsc.dataclay.commonruntime.ClientManagementLib;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.tool.Util.ERRCODE;
import es.bsc.dataclay.tool.Util.LANGS;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;
import es.bsc.dataclay.util.management.namespacemgr.Namespace;

public class NewNamespace {
	public static void main(final String[] args) throws Exception {
		if (args.length != 4) {
			Util.finishErr("Bad arguments. Usage: \n\n " + NewNamespace.class.getSimpleName()
					+ " <owner_name> <owner_pass> <namespace_name> <language> ( language : " + LANGS.getSupported() + " )\n",
					ERRCODE.ERROR);
			return;
		}
		try {
			Util.init();

			final String ownerName = args[0];
			final String ownerPass = args[1];
			final String namespaceName = args[2];
			final String language = args[3];
			final Langs lang = LANGS.getLang(language);
			if (lang == null) {
				Util.finishErr("Unsupported language " + language + ". Supported ones are " + LANGS.getSupported(), ERRCODE.ERROR);
				return;
			}

			// Get accounts info
			final AccountID ownerID = ClientManagementLib.getAccountID(ownerName);
			if (ownerID == null) {
				Util.finishErr("Invalid account", ERRCODE.ERROR);
				return;
			}
			final PasswordCredential ownerCredential = new PasswordCredential(ownerPass);

			NamespaceID namespaceID = ClientManagementLib.getNamespaceID(ownerID, ownerCredential, namespaceName);
			if (namespaceID != null) {
				Util.finishErr("Namespace " + namespaceName + " already exists", ERRCODE.WARNING);
				return;
			}
			final Namespace newNamespace = new Namespace(namespaceName, ownerName, lang);
			namespaceID = ClientManagementLib.newNamespace(ownerID, ownerCredential, newNamespace);
			if (namespaceID == null) {
				Util.finishErr("Cannot create namespace " + namespaceName + ". Check your account and credentials.", ERRCODE.ERROR);
				return;
			}
			Util.finishOut("Namespace " + namespaceName + " for " + language + " data model has been created.");
		} catch (final Exception ex) {
			Util.finishErr("Exception caught. Check your account and credentials.", ERRCODE.ERROR);
		}
		System.exit(0); // Call this to finish logging threads
	}
}
