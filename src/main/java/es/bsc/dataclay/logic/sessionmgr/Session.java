
/**
 * @file Session.java
 * @date May 31, 2013
 */
package es.bsc.dataclay.logic.sessionmgr;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.util.MgrObject;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.DataContractID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.ids.SessionID;
import es.bsc.dataclay.util.management.sessionmgr.SessionContract;
import es.bsc.dataclay.util.management.sessionmgr.SessionDataContract;
import es.bsc.dataclay.util.management.sessionmgr.SessionInterface;
import es.bsc.dataclay.util.management.sessionmgr.SessionProperty;

/**
 * This class represents a Session.
 * 
 */
public final class Session extends MgrObject<SessionID> {
	
	// === YAML SPECIFICATION === // 
	// Properties must be public for YAML parsing.
	//CHECKSTYLE:OFF

	
	//CHECKSTYLE:ON
	// ==== DYNAMIC FIELDS ==== //
	/** Account ID of the Session. */
	private AccountID accountID;
	/** Visible properties per class. */
	private Map<MetaClassID, Set<PropertyID>> propertiesOfClasses;
	/** Session contracts. */
	private Map<ContractID, SessionContract> sessionContracts;
	/** Session contracts. */
	private Map<DataContractID, SessionDataContract> sessionDataContracts;
	/** Data Contract of store. */
	private DataContractID dataContractIDofStore;
	/** End date of the session. */
	private Calendar endDate;
	/** Programming language for the session. */
	private Langs language;
	
	/** id of the external dataClayID */
	private DataClayInstanceID extDataClayID;

	/** Struct to store stub interface bitmaps. */
	private Map<MetaClassID, byte[]> ifaceBitmaps;

	/**
	 * Empty constructor
	 */
	public Session() {

	}
	
	/**
	 * minimal session for external dataClay acting on shared objects
	 * @param newExtDataClayID id of the external dataClay instance
	 * @param newendDate 
	 * @param newlanguage 
	 * @param newaccountID 
	 */
	public Session(final DataClayInstanceID newExtDataClayID, AccountID newaccountID, Langs newlanguage, Calendar newEndDate) {
		setExtDataClayID(newExtDataClayID);
		setAccountID(newaccountID);
		setLanguage(newlanguage);
		setEndDate(newEndDate);
	}

	/**
	 * Session constructor
	 * @param newaccountID
	 *            ID of the Account of the session
	 * @param newsessionContracts
	 *            Session contracts
	 * @param newsessionDataContracts
	 *            Session data contracts
	 * @param newdataContractIDofStore
	 *            DataContract ID where objects will be stored using this session
	 * @param newEndDate
	 *            Ende date of the session.
	 * @param newlanguage
	 *            Programming language for the session (datasets and namespaces of contracts).
	 * @param newifaceBitmaps
	 *            interface bitmaps for the session.
	 */
	public Session(final AccountID newaccountID, final Map<ContractID, SessionContract> newsessionContracts,
			final Map<DataContractID, SessionDataContract> newsessionDataContracts,
			final DataContractID newdataContractIDofStore, final Calendar newEndDate, final Langs newlanguage,
			final Map<MetaClassID, byte[]> newifaceBitmaps) {		
		this.setAccountID(newaccountID);
		this.setSessionContracts(newsessionContracts);
		this.setSessionDataContracts(newsessionDataContracts);
		this.setDataContractIDofStore(newdataContractIDofStore);
		this.setEndDate(newEndDate);
		this.setLanguage(newlanguage);
		this.setIfaceBitmaps(newifaceBitmaps);
		
		// Set the dataClayID for the MgrObject
		this.setDataClayID(new SessionID());

		final Map<MetaClassID, Set<PropertyID>> classProperties = new HashMap<MetaClassID, Set<PropertyID>>();
		for (SessionContract sessionContract : newsessionContracts.values()) {
			for (SessionInterface sessionInterface : sessionContract.getSessionInterfaces().values()) {
				final MetaClassID classID = sessionInterface.getClassOfInterface();
				if (!classProperties.containsKey(classID)) {
					classProperties.put(classID, new HashSet<PropertyID>());
				}
				final Set<PropertyID> propsOfClass = new HashSet<PropertyID>();
				if (sessionInterface.getSessionProperties() != null) {
					for (SessionProperty sessionProperty : sessionInterface.getSessionProperties().values()) {
						final PropertyID propID = sessionProperty.getPropertyID();
						propsOfClass.add(propID);
					}
					classProperties.get(classID).addAll(propsOfClass);
				}
			}
		}
		this.setPropertiesOfClasses(classProperties);
	}

	/**
	 * Get the Session::accountID
	 * @return the accountID
	 */

	public AccountID getAccountID() {
		return accountID;
	}

	/**
	 * Set the Session::accountID
	 * @param newaccountID
	 *            the accountID to set
	 */
	public void setAccountID(final AccountID newaccountID) {
		if (newaccountID == null) {
			throw new IllegalArgumentException("AccountID cannot be null");
		}
		this.accountID = newaccountID;
	}

	/**
	 * Get the Session::sessionContracts
	 * @return the sessionContracts
	 */

	public Map<ContractID, SessionContract> getSessionContracts() {
		return sessionContracts;
	}

	/**
	 * Set the Session::sessionContracts
	 * @param newsessionContracts
	 *            the sessionContracts to set
	 */
	public void setSessionContracts(final Map<ContractID, SessionContract> newsessionContracts) {
		if (newsessionContracts == null) {
			throw new IllegalArgumentException("Session contracts cannot be null");
		}
		this.sessionContracts = newsessionContracts;
	}

	/**
	 * Get the Session::sessionDataContracts
	 * @return the sessionDataContracts
	 */
	public Map<DataContractID, SessionDataContract> getSessionDataContracts() {
		return sessionDataContracts;
	}

	/**
	 * Set the Session::sessionDataContracts
	 * @param newsessionDataContracts
	 *            the sessionDataContracts to set
	 */
	public void setSessionDataContracts(final Map<DataContractID, SessionDataContract> newsessionDataContracts) {
		if (newsessionDataContracts == null) {
			throw new IllegalArgumentException("sessionDataContracts cannot be null");
		}
		this.sessionDataContracts = newsessionDataContracts;
	}

	/**
	 * Get the Session::isActive
	 * @return the isActive
	 */

	public boolean isActive() {
		return this.endDate.after(Calendar.getInstance());
	}

	/**
	 * Get the Session::maxEndDate
	 * @return the maxEndDate
	 */
	public Calendar getEndDate() {
		return this.endDate;
	}

	/**
	 * Set the Session::endDate
	 * @param newEndDate
	 *            the endDate to set
	 */
	public void setEndDate(final Calendar newEndDate) {
		if (newEndDate == null) {
			throw new IllegalArgumentException("newEndDate cannot be null");
		}
		this.endDate = newEndDate;

		// When calling getTimeInMillis, the current calendar sets the field time needed
		// to be stored and compared afterwards
		this.endDate.getTimeInMillis();
	}

	/**
	 * Get the Session::dataContractIDofStore
	 * @return the dataContractIDofStore
	 */
	public DataContractID getDataContractIDofStore() {
		return dataContractIDofStore;
	}

	/**
	 * Set the Session::dataContractIDofStore
	 * @param newdataContractIDofStore
	 *            the dataContractIDofStore to set
	 */
	public void setDataContractIDofStore(final DataContractID newdataContractIDofStore) {
		if (newdataContractIDofStore == null) {
			throw new IllegalArgumentException("dataContractIDofStore cannot be null");
		}
		this.dataContractIDofStore = newdataContractIDofStore;
	}

	/**
	 * Get the Session::language
	 * @return the language
	 */
	public Langs getLanguage() {
		return language;
	}

	/**
	 * Set the Session::language
	 * @param newlanguage
	 *            the language to set
	 */
	public void setLanguage(final Langs newlanguage) {
		if (newlanguage == null) {
			throw new IllegalArgumentException("language cannot be null");
		}
		this.language = newlanguage;
	}

	/**
	 * Get the Session::propertiesOfClasses
	 * @return the propertiesOfClasses
	 */
	public Map<MetaClassID, Set<PropertyID>> getPropertiesOfClasses() {
		return propertiesOfClasses;
	}

	/**
	 * Set the Session::propertiesOfClasses
	 * @param newpropertiesOfClasses
	 *            the propertiesOfClasses to set
	 */
	public void setPropertiesOfClasses(final Map<MetaClassID, Set<PropertyID>> newpropertiesOfClasses) {
		if (newpropertiesOfClasses == null) {
			throw new IllegalArgumentException("propertiesOfClasses cannot be null");
		}
		this.propertiesOfClasses = newpropertiesOfClasses;
	}

	/**
	 * Get the Session::ifaceBitmaps
	 * @return the ifaceBitmaps
	 */
	public Map<MetaClassID, byte[]> getIfaceBitmaps() {
		return ifaceBitmaps;
	}

	/**
	 * Set the Session::ifaceBitmaps
	 * @param newifaceBitmaps
	 *            the ifaceBitmaps to set
	 */
	public void setIfaceBitmaps(final Map<MetaClassID, byte[]> newifaceBitmaps) {
		if (newifaceBitmaps == null) {
			throw new IllegalArgumentException("ifaceBitmaps cannot be null");
		}
		this.ifaceBitmaps = newifaceBitmaps;
	}
	
	/**
	 * Set the Session::extDataClayID
	 * @param newextDataClayID id to set
	 */
	public void setExtDataClayID(final DataClayInstanceID newextDataClayID) {
		this.extDataClayID = newextDataClayID;
	}
	
	/**
	 * Get the Session::extDataClayID
	 * @return the extDataClayID
	 */
	public DataClayInstanceID getExtDataClayID() {
		return this.extDataClayID;
	}

}
