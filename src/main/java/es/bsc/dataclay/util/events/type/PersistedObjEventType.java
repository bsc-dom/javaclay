
package es.bsc.dataclay.util.events.type;

import java.util.Objects;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.logic.api.LogicModuleAPI;
import es.bsc.dataclay.util.events.EventTypeOuter.EventTypeEnum;
import es.bsc.dataclay.util.events.message.EventMessage;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;
import es.bsc.dataclay.util.management.stubs.StubInfo;

/** This class represents an event type for an object was persisted. */
public final class PersistedObjEventType extends EventType {

	/** Class ID. */
	private MetaClassID classID;

	/** Namespace. */
	private String namespace;

	/** Class name. */
	private String className;

	/**
	 * Constructor used for deserialization.
	 */
	public PersistedObjEventType() {

	}

	/**
	 * Constructor
	 * @param newnamespace
	 *            Namespace of class
	 * @param newclassName
	 *            Class name of class
	 */
	public PersistedObjEventType(final String newnamespace, final String newclassName) {
		super(EventTypeEnum.PERSISTED_OBJ);
		this.setNamespace(newnamespace);
		this.setClassName(newclassName);
	}

	/**
	 * Constructor
	 * @param newclassID
	 *            Class ID of condition.
	 */
	public PersistedObjEventType(final MetaClassID newclassID) {
		super(EventTypeEnum.PERSISTED_OBJ);
		this.setClassID(newclassID);
	}

	/**
	 * Constructor
	 * @param stubClazz
	 *            Stub of class being used. If it is a class being registered, only the name will be set.
	 */
	public PersistedObjEventType(final Class<?> stubClazz) {
		super(EventTypeEnum.PERSISTED_OBJ);
		if (DataClayObject.isStub(stubClazz)) {
			final StubInfo stubInfo = DataClayObject.getStubInfoFromClass(stubClazz.getName());
			this.setNamespace(stubInfo.getNamespace());
		}
		this.setClassName(stubClazz.getName());
	}

	@Override
	public void init(final AccountID accountID, final PasswordCredential credential, final String newnamespace, final LogicModuleAPI lm) {
		if (this.getNamespace() == null) {
			// uses the one provided.
			this.setNamespace(newnamespace);
		}
		final NamespaceID namespaceID = lm.getNamespaceID(accountID, credential, this.getNamespace());
		final MetaClassID mclassID = lm.getClassID(accountID, credential, namespaceID, this.getClassName());
		this.setClassID(mclassID);
	}

	/**
	 * Get the PersistedObjExecCondition::classID
	 * @return the classID
	 */
	public MetaClassID getClassID() {
		return classID;
	}

	/**
	 * Set the PersistedObjExecCondition::classID
	 * @param newclassID
	 *            the classID to set
	 */
	public void setClassID(final MetaClassID newclassID) {
		this.classID = newclassID;
	}

	@Override
	public String toString() {
		return "Class " + classID;
	}

	@Override
	public int hashCode() {
		// Used in map of Notification Manager
		return Objects.hash(this.classID);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof PersistedObjEventType) {
			final PersistedObjEventType pEv = (PersistedObjEventType) obj;
			return this.classID.equals(pEv.getClassID());
		}
		return false;
	}

	@Override
	public boolean checkIsEventType(final EventMessage message) {
		// Remember: this function is called to check if a certain message accomplishes a certain condition.

		// Any 'persistence' of the class provided accomplishes this condition.
		final EventType evCondition = message.getEventType();
		if (evCondition instanceof PersistedObjEventType) {
			final PersistedObjEventType persistedCondition = (PersistedObjEventType) evCondition;
			return persistedCondition.getClassID().equals(this.getClassID());
		}
		return false;
	}

	/**
	 * @return the namespace
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * @param newnamespace
	 *            the namespace to set
	 */
	public void setNamespace(final String newnamespace) {
		this.namespace = newnamespace;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param newclassName
	 *            the className to set
	 */
	public void setClassName(final String newclassName) {
		this.className = newclassName;
	}
}
