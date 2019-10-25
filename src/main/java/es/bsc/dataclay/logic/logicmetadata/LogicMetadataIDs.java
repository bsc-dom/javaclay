
package es.bsc.dataclay.logic.logicmetadata;

import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.NamespaceID;

/** LM metadata IDs (admin, datasets, contracts, ...). */
public class LogicMetadataIDs {
	/** Admin account ID. */
	public AccountID dcAdminID;
	/** ID of DataClay registrator (for DataClay classes). */
	public AccountID dcRegistratorID;
	/** ID of public namespace DataClay (for DataClay classes). */
	public NamespaceID dcPublicNamespaceID;
	/** ID of public contract of DataClay (for DataClay classes). */
	public ContractID dcPublicContractID;
	/** ID of current dataClay. */
	public DataClayInstanceID dcID;
}
