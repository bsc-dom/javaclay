
/**
 * @file Account.java
 * @date July 26, 2012
 */
package es.bsc.dataclay.util.management.accountmgr;

import java.util.Objects;

import javax.annotation.Nonnull;

import es.bsc.dataclay.util.MgrObject;
import es.bsc.dataclay.util.ids.AccountID;

/**
 * This class represents a system account.
 */
public final class Account extends MgrObject<AccountID> {
	
	/** User name of the account. */
	@Nonnull
	private String username;
	/** Credential of the account. */
	@Nonnull
	private PasswordCredential credential;
	/** Role of the account. The account can be normal account, admin account, .... */
	@Nonnull
	private AccountRole role;
	
	/**
	 * Creates an empty account
	 */
	public Account() {

	}

	/**
	 * Account constructor with provided username, role and credentials.
	 * @param newusername
	 *            Username to be set
	 * @param newrole
	 *            Role to be set
	 * @param newcredential
	 *            Credential to be associated
	 * @post Creates a new account with provided username, role and credentials and generates a new AccountID.
	 */
	public Account(final String newusername, final AccountRole newrole, final PasswordCredential newcredential) {
		this.setUsername(newusername);
		this.setRole(newrole);
		this.setCredential(newcredential);
	}

	/**
	 * Get the username of this Account
	 * @return Account::username of this Account.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Set the Account::username of this Account
	 * @param newusername
	 *            New username to be set
	 */
	public void setUsername(final String newusername) {
		this.username = newusername;
	}

	/**
	 * Get the role of this Account
	 * @return Account::role of container Account.
	 */
	public AccountRole getRole() {
		return this.role;
	}

	/**
	 * Set the Account::role of this Account.
	 * @param newrole
	 *            New role to be set
	 */
	public void setRole(final AccountRole newrole) {
		this.role = newrole;
	}

	/**
	 * Get the Account::credentialID of this Account.
	 * @return the credential
	 */

	public PasswordCredential getCredential() {
		return this.credential;
	}

	/**
	 * Set the Account::credential of this Account.
	 * @param newcredential
	 *            the credential
	 */
	public void setCredential(final PasswordCredential newcredential) {
		this.credential = newcredential;
	}

	@Override
	public boolean equals(final Object t) {
		if (t instanceof Account) {
			final Account other = (Account) t;
			return this.getUsername().equals(other.getUsername()) && this.getRole().equals(other.getRole());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.username);
	}

}
