
/**
 * @file AccountNotResponsibleOfNamespace.java
 * 
 * @date Mar 11, 2014
 */

package es.bsc.dataclay.exceptions.logicmodule.namespacemgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.NamespaceID;

/**
 * This class represents the exceptions produced in NamespaceManager module when a Namespace does not belong to account provided.
 */
public class AccountNotResponsibleOfNamespace extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = 6813619666353612407L;

	/**
	 * Exception produced when the account provided is not responsible of the given namespace
	 * @param namespaceID
	 *            ID of the namespace
	 * @param badresponsible
	 *            ID of the bad account
	 */
	public AccountNotResponsibleOfNamespace(final NamespaceID namespaceID, final AccountID badresponsible) {
		super(ERRORCODE.BAD_NAMESPACE_RESPONSIBLE, "The account " + badresponsible + " is not the responsible of the namespace : "
				+ namespaceID, false);
	}
}
