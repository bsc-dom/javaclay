
package es.bsc.dataclay.util.ids;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import es.bsc.dataclay.serialization.DataClaySerializable;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;

/** This class represents an identification. */
@SuppressWarnings("serial")
public abstract class ID implements DataClaySerializable, Serializable {

	// === YAML CAN MODIFY FIELDS === //
	// CHECKSTYLE:OFF
	/** Java universal ID. */
	public UUID id;
	// CHECKSTYLE:ON

	/**
	 * ID deserialize base
	 * @param dcBuffer
	 *            Buffer with message bytes to read
	 */
	protected final void deserializeIDBase(final DataClayByteBuffer dcBuffer) {
		final long most = dcBuffer.readLong();
		final long least = dcBuffer.readLong();
		this.setId(new UUID(most, least));
	}

	/**
	 * Get ID::id
	 * @return the id
	 */
	public final UUID getId() {
		return id;
	}

	/**
	 * Set ID::id
	 * @param newid
	 *            New id to set
	 */
	public final void setId(final UUID newid) {
		this.id = newid;
	}

	@Override
	public final boolean equals(final Object t) {
		if (t == null) {
			return false;
		}
		if (t instanceof ID) {
			final ID other = (ID) t;
			return other.getId().equals(this.id);
		}
		return false;
	}

	@Override
	public final int hashCode() {
		return Objects.hashCode(this.id);
	}

	@Override
	public final String toString() {
		return id.toString();
	}

	/**
	 * Serializes the current object. It uses the following pattern:
	 *
	 * |LONG|LONG|
	 *
	 * where: LONG is a serialized long
	 * 
	 * @param dcBuffer
	 *            Destination buffer to store the current object
	 */
	public final void serializeBase(final DataClayByteBuffer dcBuffer) {
		// === ID === //
		dcBuffer.writeLong(this.getId().getMostSignificantBits());
		dcBuffer.writeLong(this.getId().getLeastSignificantBits());
	}

}
