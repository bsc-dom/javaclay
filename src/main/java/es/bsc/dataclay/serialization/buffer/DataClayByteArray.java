
package es.bsc.dataclay.serialization.buffer;

import com.google.protobuf.ByteString;

/**
 * This class is a wrapper to a byte array. Used for serialization of messages that need to deal with DataClayByteBuffer.
 */
public final class DataClayByteArray {

	/** In case of using GRPC, the byteString itself so GRPC will be the one serializing. */
	private ByteString byteString;

	/**
	 * Constructor
	 * @param thebyteString
	 *            The byte array
	 */
	public DataClayByteArray(final ByteString thebyteString) {
		this.setByteString(thebyteString);
	}

	/**
	 * Constructor
	 * @param theByteArray
	 *            The byte array
	 */
	public DataClayByteArray(final byte[] theByteArray) {
		this.setByteString(ByteString.copyFrom(theByteArray));
	}

	/**
	 * @return the byteArray
	 */
	public byte[] getByteArray() {
		return byteString.toByteArray();
	}

	/**
	 * @return the byteString
	 */
	public ByteString getByteString() {
		return byteString;
	}

	/**
	 * @param thebyteString
	 *            the byteString to set
	 */
	public void setByteString(final ByteString thebyteString) {
		this.byteString = thebyteString;
	}
	
	public String toString() { 
		StringBuilder strb = new StringBuilder();
		strb.append("[ lenght = " + this.byteString.size() + ", values = " + this.byteString.toStringUtf8() + "]");
		return strb.toString();
	}

}
