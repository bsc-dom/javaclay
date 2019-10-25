
package es.bsc.dataclay.tool;

import es.bsc.dataclay.commonruntime.ClientManagementLib;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.tool.Util.ERRCODE;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;

public class GetNamespaceLang {

	public static void main(final String[] args) throws Exception {
		if (args.length != 3) {
			Util.finishErr("Bad arguments. Usage: \n\n " + GetNamespaceLang.class.getSimpleName()
					+ " <applicant_username> <applicant_pass> <namespace_name> \n", ERRCODE.ERROR);
			return;
		}
		try {
			Util.init();

			final String applicantName = args[0];
			final String applicantPass = args[1];
			final String namespace = args[2];

			// Check account
			final AccountID accountID = ClientManagementLib.getAccountID(applicantName);
			if (accountID == null) {
				Util.finishErr("Invalid account", ERRCODE.ERROR);
				return;
			}
			final PasswordCredential accountCred = new PasswordCredential(applicantPass);

			// Check namespace
			final Langs language = ClientManagementLib.getNamespaceLanguage(accountID, accountCred, namespace);
			if (language == null) {
				Util.finishErr("Namespace " + namespace + " not found.", ERRCODE.ERROR);
				return;
			}

			// TODO Notice that break line is needed for the global tool to "tail" the language
			Util.finishOut("Language of namespace " + namespace + " is \n" + language.name());
		} catch (final Exception ex) {
			Util.finishErr("Exception caught. Check your account and credentials.", ERRCODE.ERROR);
		}
	}
}
