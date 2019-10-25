
package es.bsc.dataclay.util.configs;

import es.bsc.dataclay.util.ProcessEnvironment;
import es.bsc.dataclay.util.management.accountmgr.Account;
import es.bsc.dataclay.util.management.accountmgr.AccountRole;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;

/**
 * This class parses the information of the admin superuser from environment variables.
 */
public final class CfgAdminEnvLoader {

	/**
	 * Constructor
	 * 
	 * @throws IllegalAccessException
	 *             Prevent instantiation
	 */
	private CfgAdminEnvLoader() throws IllegalAccessException {
		throw new IllegalAccessException("This class cannot be instantiated");
	}

	/**
	 * Parse account admin user
	 * 
	 * @return Account admin
	 */
	public static Account parseAdminUser() {
		final String userName = ProcessEnvironment.getInstance().get("DATACLAY_ADMIN_USER");
		final String userPassword = ProcessEnvironment.getInstance().get("DATACLAY_ADMIN_PASSWORD");

		if (userName != null && !userName.isEmpty() && userPassword != null && !userPassword.isEmpty()) {
			final Account adminAccount = new Account(userName,
					AccountRole.ADMIN_ROLE, new PasswordCredential(userPassword));
			return adminAccount;
		} else {
			return null;
		}
	}
}
