
/**
 * @file PasswordCredential.java
 * @date Sep 4, 2012
 */
package es.bsc.dataclay.util.management.accountmgr;

import java.util.IdentityHashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.serialization.DataClaySerializable;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.MgrObject;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.ids.CredentialID;
import es.bsc.dataclay.util.ids.MetaClassID;

/**
 * This class represents a system PasswordCredential for the system Accounts.
 * 
 * @version 0.1
 */

public final class PasswordCredential extends MgrObject<CredentialID> implements DataClaySerializable{//extends Credential {

	/** password credentials. */
	private String password;

	/**
	 * PasswordCredential constructor
	 * @post Creates an empty PasswordCredential
	 */
	public PasswordCredential() {

	}

	/**
	 * PasswordCredential constructor
	 * @param newpassword
	 *            Password of the credential
	 * @post Creates a new PasswordCredential and ID with the provided password
	 */
	public PasswordCredential(final String newpassword) {
		this.password = newpassword;
	}

	/**
	 * Sets a new password for the PasswordCredential
	 * @param newpassword
	 *            New password to be set
	 * @post Sets a new password for this PasswordCredential
	 */
	public void setPassword(final String newpassword) {
		this.password = newpassword;
	}

	/**
	 * Get password of the PasswordCredential
	 * @return Password of the credential
	 */
	public String getPassword() {
		return this.password;

	}

	@Override
	public String toString() {
		return this.password + ":" + this.getDataClayID();
	}

	@Override
	public boolean equals(final Object t) {
		if (t == null) {
			return false;
		}
		if (t instanceof PasswordCredential) {
			final PasswordCredential other = (PasswordCredential) t;
			return other.getPassword().equals(this.password);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.password);
	}

	@Override
	public void serialize(final DataClayByteBuffer dcBuffer,
			final boolean ignoreUserTypes, final Map<MetaClassID, byte[]> ifaceBitMaps,
			final IdentityHashMap<Object, Integer> curSerializedObjs,
			final ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting) {
		// === PASSWORD === //
		dcBuffer.writeString(this.password);
	}

	@Override
	public void deserialize(final DataClayByteBuffer dcBuffer, final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata,
			final Map<Integer, Object> curDeserializedJavaObjs) {
		// === PASSWORD === //
		this.password = dcBuffer.readString();
	}

}
