
package es.bsc.dataclay.tool;

import java.util.Set;

import es.bsc.dataclay.commonruntime.ClientManagementLib;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.tool.Util.ERRCODE;
import es.bsc.dataclay.tool.Util.LANGS;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;

public class GetBackends {

	public static void main(final String[] args) throws Exception {
		if (args.length != 3) {
			Util.finishErr("Bad arguments. Usage: \n\n " + GetBackends.class.getSimpleName()
					+ " <user_name> <user_pass> <java | python>\n", ERRCODE.ERROR);
			return;
		}
		try {
			Util.init();

			final String applicantName = args[0];
			final String applicantPass = args[1];
			final String language = args[2];
			final Langs lang = LANGS.getLang(language);
			if (lang == null) {
				Util.finishErr("Unsupported language " + language + ". Supported ones are " + LANGS.getSupported(),
						ERRCODE.ERROR);
				return;
			}
			// Get backend info
			final Set<String> backendNames = getBackends(applicantName, applicantPass, lang);
			if (backendNames == null) { 
				return; //error produced
			}
			String msg = "Available " + language.toLowerCase() + " backends:" + "\n";
			for (final String bk : backendNames) {
				msg += bk + "\n";
			}
			Util.finishOut(msg);
		} catch (final Exception ex) {
			Util.finishErr("Exception caught. Check your account and credentials.", ERRCODE.ERROR);
		}
		System.exit(0); // Call this to finish logging threads

	}

	public static Set<String> getBackends(final String applicantName, final String applicantPass, final Langs lang) throws Exception { 
		// Check account
		final AccountID accountID = ClientManagementLib.getAccountID(applicantName);
		if (accountID == null) {
			Util.finishErr("Invalid account", ERRCODE.ERROR);
			return null;
		}
		final PasswordCredential credential = new PasswordCredential(applicantPass);
		
		// Get backend info
		return ClientManagementLib.getBackendNames(accountID, credential, lang);
	}
}
