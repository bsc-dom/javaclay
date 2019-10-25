
/**
 * @file ImplementationStubInfo.java
 * @date Jun 10, 2013
 */
package es.bsc.dataclay.util.management.stubs;

import java.util.List;
import java.util.Map;

import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.management.classmgr.Type;

/**
 * This class represents the information about an operation in a stub.
 */
public final class ImplementationStubInfo {

	/** Namespace this implementation belongs to. Different of stub in case of enrichment. */
	private String namespace;
	/** Name of class this implementation belongs to. Different in super classes. */
	private String className;
	/** Signature. */
	private String signature;
	/** Parameters. */
	private Map<String, Type> params;
	/** Order of parameters. */
	private List<String> paramsOrder;
	/** Type for the return (either String or Class' UUID). */
	private Type returnType;
	/** ID of the namespace of this implementation. */
	private NamespaceID namespaceID;
	/** ID of the Operation. */
	private OperationID operationID;
	/** ID of the local implementation. */
	private ImplementationID localImplID;
	/** IDs for the remote implementation. */
	private ImplementationID remoteImplID;
	/** ID of the contract of the implementation. */
	private ContractID contractID; 
	/** ID of the interface of the implementation. */
	private InterfaceID interfaceID;
	/** ID of the account responsible of the remote implementation. */
	private AccountID responsibleRemoteAccountID;
	/** Implementation position. */
	private int implPosition;

	/**
	 * Empty constructor
	 */
	public ImplementationStubInfo() {

	}

	/**
	 * Constructor
	 * @param implNamespace Namespace of the implementation (in case of enrichments it's different)
	 * @param implCassName Name of class of the implementatin (different for superclasses)
	 * @param newsignature
	 *            Signature of implementation
	 * @param newparameters
	 *            Parameters of implementation
	 * @param newparamsOrder Order of params by name.
	 * @param newreturnType
	 *            Return type for the operation
	 * @param newoperationID
	 *            ID of operation
	 * @param newlocalImplID
	 *            ID of the local implementation
	 * @param newremoteImplID
	 *            ID of the remote implementation
	 * @param newcontractID
	 *            Contract of remote implementation
	 * @param newinterfaceID
	 *            Interface of remote implementation
	 * @param newresponsibleRemoteAccountID
	 *            ID of the account responsible of the remote implementation
	 * @param newnamespaceID ID of namespace     
	 * @param newimplPosition New implementation position.
	 */
	public ImplementationStubInfo(final String implNamespace, final String implCassName,
			final String newsignature, final Map<String, Type> newparameters, final List<String> newparamsOrder,
			final Type newreturnType, final OperationID newoperationID, final ImplementationID newlocalImplID, 
			final ImplementationID newremoteImplID, final ContractID newcontractID, final InterfaceID newinterfaceID, 
			final AccountID newresponsibleRemoteAccountID, final NamespaceID newnamespaceID, 
			final int newimplPosition) {
		this.setNamespace(implNamespace);
		this.setClassName(implCassName);
		this.setNamespaceID(newnamespaceID);
		this.setSignature(newsignature);
		this.setParamsOrder(newparamsOrder);
		this.setParams(newparameters);
		this.setReturnType(newreturnType);
		this.setOperationID(newoperationID);
		this.setLocalImplID(newlocalImplID);
		this.setRemoteImplID(newremoteImplID);
		this.setContractID(newcontractID);
		this.setInterfaceID(newinterfaceID);
		this.setResponsibleRemoteAccountID(newresponsibleRemoteAccountID);
		this.setImplPosition(newimplPosition);
	}

	/**
	 * Get the ImplementationStubInfo::operationID
	 * @return the operationID
	 */

	public OperationID getOperationID() {
		return operationID;
	}

	/**
	 * Set the ImplementationStubInfo::operationID
	 * @param newoperationID
	 *            the operationID to set
	 */
	public void setOperationID(final OperationID newoperationID) {
		this.operationID = newoperationID;
	}

	/**
	 * Get the ImplementationStubInfo::localImplID
	 * @return the localImplID
	 */

	public ImplementationID getLocalImplID() {
		return localImplID;
	}

	/**
	 * Set the ImplementationStubInfo::localImplID
	 * @param newlocalImplID
	 *            the localImplID to set
	 */
	public void setLocalImplID(final ImplementationID newlocalImplID) {
		this.localImplID = newlocalImplID;
	}

	@Override
	public boolean equals(final Object t) {
		if (t instanceof ImplementationStubInfo) {
			final ImplementationStubInfo other = (ImplementationStubInfo) t;
			return other.getOperationID().equals(this.getOperationID());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.getOperationID().hashCode();
	}

	/**
	 * Get the ImplementationStubInfo::returnType
	 * @return the returnType.
	 */
	public Type getReturnType() {
		return returnType;
	}

	/**
	 * Set the ImplementationStubInfo::returnType
	 * @param newreturnType
	 *            the returnType to set
	 */
	public void setReturnType(final Type newreturnType) {
		this.returnType = newreturnType;
	}

	/**
	 * Get the ImplementationStubInfo::responsibleRemoteAccountID
	 * @return the responsibleRemoteAccountID
	 */
	public AccountID getResponsibleRemoteAccountID() {
		return responsibleRemoteAccountID;
	}

	/**
	 * Set the ImplementationStubInfo::responsibleRemoteAccountID
	 * @param newresponsibleRemoteAccountID
	 *            the responsibleRemoteAccountID to set
	 */
	public void setResponsibleRemoteAccountID(final AccountID newresponsibleRemoteAccountID) {
		// it can be null for virtual stubs
		this.responsibleRemoteAccountID = newresponsibleRemoteAccountID;
	}

	/**
	 * Get parameters
	 * @return the parameters
	 */
	public Map<String, Type> getParams() {
		return params;
	}

	/**
	 * Set parameters
	 * @param newparameters the parameters to set
	 */
	public void setParams(final Map<String, Type> newparameters) {
		this.params = newparameters;
	}

	/**
	 * Get contractID
	 * @return the contractID
	 */
	public ContractID getContractID() {
		return contractID;
	}

	/**
	 * Set contractID
	 * @param newcontractID the contractID to set
	 */
	public void setContractID(final ContractID newcontractID) {
		this.contractID = newcontractID;
	}

	/**
	 * Get interfaceID
	 * @return the interfaceID
	 */
	public InterfaceID getInterfaceID() {
		return interfaceID;
	}

	/**
	 * Set interfaceID
	 * @param newinterfaceID the interfaceID to set
	 */
	public void setInterfaceID(final InterfaceID newinterfaceID) {
		this.interfaceID = newinterfaceID;
	}

	/**
	 * Get remoteImplID
	 * @return the remoteImplID
	 */
	public ImplementationID getRemoteImplID() {
		return this.remoteImplID;
	}
	
	/**
	 * Set remoteImplID
	 * @param newremoteImplID the remoteImplID to set
	 */
	public void setRemoteImplID(final ImplementationID newremoteImplID) {
		this.remoteImplID = newremoteImplID;
	}

	/**
	 * Get signature
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * Set signature
	 * @param newsignature the signature to set
	 */
	public void setSignature(final String newsignature) {
		this.signature = newsignature;
	}

	/**
	 * Get paramsOrder
	 * @return the paramsOrder
	 */
	public List<String> getParamsOrder() {
		return paramsOrder;
	}

	/**
	 * Set paramsOrder
	 * @param newparamsOrder the paramsOrder to set
	 */
	public void setParamsOrder(final List<String> newparamsOrder) {
		this.paramsOrder = newparamsOrder;
	}

	/**
	 * Get namespace
	 * @return the namespace
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * Set namespace
	 * @param newnamespace the namespace to set
	 */
	public void setNamespace(final String newnamespace) {
		this.namespace = newnamespace;
	}

	/**
	 * Get namespaceID
	 * @return the namespaceID
	 */
	public NamespaceID getNamespaceID() {
		return namespaceID;
	}

	/**
	 * Set namespaceID
	 * @param newnamespaceID the namespaceID to set
	 */
	public void setNamespaceID(final NamespaceID newnamespaceID) {
		this.namespaceID = newnamespaceID;
	}

	/**
	 * Get className
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Set className
	 * @param newclassName the className to set
	 */
	public void setClassName(final String newclassName) {
		this.className = newclassName;
	}

	/**
	 * @return the implPosition
	 */
	public int getImplPosition() {
		return implPosition;
	}

	/**
	 * @param newimplPosition the implPosition to set
	 */
	public void setImplPosition(final int newimplPosition) {
		this.implPosition = newimplPosition;
	}	
}
