
/**
 * @file SessionInfo.java
 * @date Mar 4, 2014
 */
package es.bsc.dataclay.util.management.sessionmgr;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.DataContractID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.ids.SessionID;

/**
 * Class that represents the info of a session.
 * 
 */
public final class SessionInfo {

	/** ID of the session. */
	private SessionID sessionID;
	/** Account of the session. */
	private AccountID accountID;
	/** Visible properties per class. */
	private Map<MetaClassID, Set<PropertyID>> propertiesOfClasses;
	/** Session contracts. */
	private Map<ContractID, SessionContract> sessionContracts;
	/** Session contracts. */
	private Map<DataContractID, SessionDataContract> sessionDataContracts;
	/** Data Contract of store. */
	private DataContractID dataContractIDforStore;
	/** Programming language used during the session. */
	private Langs language;
	/** Struct to store stub interface bitmaps. */
	private Map<MetaClassID, byte[]> ifaceBitmaps;
	/** Expiration date of the session considering its contracts. */
	private Calendar endDate;

	/** ID of external dataClay instance if needed. */
	private DataClayInstanceID extDataClayID;

	/**
	 * For yaml deserialization.
	 */
	public SessionInfo() {

	}

	/**
	 * Constructor for the info of the session
	 * @param newsessionID
	 *            ID of the session
	 * @param newaccountID
	 *            account of the session
	 * @param thesessionContracts
	 *            info of the contracts in the session
	 * @param thesessionDataContracts
	 *            info of the data contracts in the session
	 * @param thedataContractIDforStore
	 *            id of the data contract for store
	 * @param newlanguage
	 *            programming language for the session
	 * @param newpropertiesOfClasses
	 *            visible properties per class for this session
	 * @param newifaceBitmaps
	 *            interface bitmaps for the session.
	 * @param newendDate
	 *            end date of the session considering its contracts
	 */
	public SessionInfo(
			final SessionID newsessionID,
			final AccountID newaccountID, final Map<MetaClassID, Set<PropertyID>> newpropertiesOfClasses,
			final Map<ContractID, SessionContract> thesessionContracts,
			final Map<DataContractID, SessionDataContract> thesessionDataContracts,
			final DataContractID thedataContractIDforStore, final Langs newlanguage,
			final Map<MetaClassID, byte[]> newifaceBitmaps, final Calendar newendDate) {
		setSessionID(newsessionID);
		setAccountID(newaccountID);
		setSessionContracts(thesessionContracts);
		setSessionDataContracts(thesessionDataContracts);
		setDataContractIDforStore(thedataContractIDforStore);
		setLanguage(newlanguage);
		setPropertiesOfClasses(newpropertiesOfClasses);
		setIfaceBitmaps(newifaceBitmaps);
		setEndDate(newendDate);
	}

	/**
	 * Constructor for external sessions
	 * @param sessionID
	 *            id of the session
	 * @param extDataClayID
	 *            id of external dataClay instance
	 * @param accountID
	 *            id of the account on behalf of external dataClay instance
	 * @param language
	 *            language of the session
	 * @param endDate
	 *            expiration date
	 */
	public SessionInfo(final SessionID sessionID, final DataClayInstanceID extDataClayID, final AccountID accountID, final Langs language, final Calendar endDate) {
		setSessionID(sessionID);
		setExtDataClayID(extDataClayID);
		setAccountID(accountID);
		setLanguage(language);
		setEndDate(endDate);
	}

	/**
	 * Get the SessionInfo::sessionContracts
	 * @return the sessionContracts
	 */
	public Map<ContractID, SessionContract> getSessionContracts() {
		return sessionContracts;
	}

	/**
	 * Set the SessionInfo::sessionContracts
	 * @param newsessionContracts
	 *            the sessionContracts to set
	 */
	public void setSessionContracts(final Map<ContractID, SessionContract> newsessionContracts) {
		this.sessionContracts = newsessionContracts;
	}

	/**
	 * Get the SessionInfo::sessionDataContracts
	 * @return the sessionDataContracts
	 */
	public Map<DataContractID, SessionDataContract> getSessionDataContracts() {
		return sessionDataContracts;
	}

	/**
	 * Set the SessionInfo::sessionDataContracts
	 * @param newsessionDataContracts
	 *            the sessionDataContracts to set
	 */
	public void setSessionDataContracts(final Map<DataContractID, SessionDataContract> newsessionDataContracts) {
		this.sessionDataContracts = newsessionDataContracts;
	}

	/**
	 * Get the SessionInfo::dataContractIDforStore
	 * @return the dataContractIDforStore
	 */
	public DataContractID getDataContractIDforStore() {
		return dataContractIDforStore;
	}

	/**
	 * Set the SessionInfo::dataContractIDforStore
	 * @param newdataContractIDforStore
	 *            the dataContractIDforStore to set
	 */
	public void setDataContractIDforStore(final DataContractID newdataContractIDforStore) {
		this.dataContractIDforStore = newdataContractIDforStore;
	}

	/**
	 * Get the SessionInfo::accountID
	 * @return the accountID
	 */
	public AccountID getAccountID() {
		return accountID;
	}

	/**
	 * Set the SessionInfo::accountID
	 * @param newaccountID
	 *            the accountID to set
	 */
	public void setAccountID(final AccountID newaccountID) {
		this.accountID = newaccountID;
	}

	/**
	 * Get the SessionInfo::language
	 * @return the language
	 */
	public Langs getLanguage() {
		return language;
	}

	/**
	 * Set the SessionInfo::language
	 * @param newlanguage
	 *            the language to set
	 */
	public void setLanguage(final Langs newlanguage) {
		this.language = newlanguage;
	}

	/**
	 * Get the SessionInfo::propertiesOfClasses
	 * @return the propertiesOfClasses
	 */
	public Map<MetaClassID, Set<PropertyID>> getPropertiesOfClasses() {
		return propertiesOfClasses;
	}

	/**
	 * Set the SessionInfo::propertiesOfClasses
	 * @param newpropertiesOfClasses
	 *            the propertiesOfClasses to set
	 */
	public void setPropertiesOfClasses(final Map<MetaClassID, Set<PropertyID>> newpropertiesOfClasses) {
		this.propertiesOfClasses = newpropertiesOfClasses;
	}

	/**
	 * Get the SessionInfo::ifaceBitmaps
	 * @return the ifaceBitmaps
	 */
	public Map<MetaClassID, byte[]> getIfaceBitmaps() {
		return ifaceBitmaps;
	}

	/**
	 * Set the SessionInfo::ifaceBitmaps
	 * @param newifaceBitmaps
	 *            the ifaceBitmaps to set
	 */
	public void setIfaceBitmaps(final Map<MetaClassID, byte[]> newifaceBitmaps) {
		this.ifaceBitmaps = newifaceBitmaps;
	}

	/**
	 * Get the SessionInfo::endDate
	 * @return the endDate
	 */
	public Calendar getEndDate() {
		return endDate;
	}

	/**
	 * Set the SessionInfo::endDate
	 * @param newendDate
	 *            the newendDate to set
	 */
	public void setEndDate(final Calendar newendDate) {
		this.endDate = newendDate;
	}

	/**
	 * @return the sessionID
	 */
	public SessionID getSessionID() {
		return sessionID;
	}

	/**
	 * @param thesessionID
	 *            the sessionID to set
	 */
	public void setSessionID(final SessionID thesessionID) {
		this.sessionID = thesessionID;
	}

	/**
	 * @return id of external dataClay of this session
	 */
	public DataClayInstanceID getExtDataClayID() {
		return extDataClayID;
	}

	/**
	 * Set the SessionInfo::extDataClayID
	 * @param extDataClayID
	 *            id external dataClayID to set
	 */
	public void setExtDataClayID(DataClayInstanceID extDataClayID) {
		this.extDataClayID = extDataClayID;
	}
}
