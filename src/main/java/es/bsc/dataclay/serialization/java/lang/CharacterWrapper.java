
package es.bsc.dataclay.serialization.java.lang;

import java.util.IdentityHashMap;
import java.util.ListIterator;
import java.util.Map;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.serialization.java.DataClayJavaWrapper;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.ids.MetaClassID;

/**
 * Wrapper for Character class. *
 */
public final class CharacterWrapper extends DataClayJavaWrapper {

	/** Character. */
	private Character charObject;

	/**
	 * Constructor used for deserialization
	 */
	public CharacterWrapper() {

	}

	/**
	 * Constructor
	 * @param newchar
	 *            The char.
	 */
	public CharacterWrapper(final Character newchar) {
		this.setCharacter(newchar);
	}

	@Override
	public Object getJavaObject() {
		return charObject;
	}

	/**
	 * Set the CharacterWrapper::char
	 * @param newCharacter
	 *            the char to set
	 */
	public void setCharacter(final Character newCharacter) {
		this.charObject = newCharacter;
	}

	@Override
	public void serialize(final DataClayByteBuffer dcBuffer, final boolean ignoreUserTypes,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final IdentityHashMap<Object, Integer> curSerializedObjs,
			final ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting) {
		dcBuffer.writeChar(this.charObject);
	}

	@Override
	public void deserialize(final DataClayByteBuffer dcBuffer,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata,
			final Map<Integer, Object> curDeserializedObjs) {
		this.charObject = dcBuffer.readChar();
	}

	@Override
	public int hashCode() {
		return charObject.hashCode();
	}

	@Override
	public boolean equals(final Object object) {
		if (!(object instanceof CharacterWrapper)) {
			return false;
		}
		final CharacterWrapper value = (CharacterWrapper) object;
		return value.getJavaObject().equals(this.getJavaObject());
	}

	@Override
	public boolean isImmutable() {
		return true;
	}

	@Override
	public boolean isNull() {
		return charObject == null;
	}
}
