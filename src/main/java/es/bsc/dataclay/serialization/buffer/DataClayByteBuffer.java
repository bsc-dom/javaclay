
package es.bsc.dataclay.serialization.buffer;

import io.grpc.netty.shaded.io.netty.buffer.ByteBuf;

/**
 * DataClay Byte buffer.
 */
public interface DataClayByteBuffer {

	
	/**
	 * Wrap bytes to avoid copying them.
	 * @param newBuffer The byte array to deserialize
	 */
	void wrapBytes(final byte[] newBuffer);
	
	/**
	 * Check if the buffer has some bytes to read
	 * @return TRUE if the buffer has some bytes to read. FALSE otherwise.
	 */
	boolean isReadable();

	/**
	 * Get the number of bytes available to be read. 
	 * @return The number of bytes available to be read. 
	 */
	int readableBytes();

	/**
	 * Get the index in buffer of the reader pointer
	 * @return The index in buffer of the reader pointer
	 */
	int readerIndex();

	/**
	 * Get the index in buffer of the writer pointer
	 * @return The index in buffer of the writer pointer
	 */
	int writerIndex();

	/**
	 * Sets the reader index to the position with index provided
	 * @param index the new reader position
	 */
	void setReaderIndex(int index);
	
	/**
	 * Sets the writer index to the position with index provided
	 * @param index the new writer position
	 */
	void setWriterIndex(int index);

	/**
	 * Gets the maximum capacity of the buffer
	 * @return the maximum capacity of the buffer
	 */
	int maxCapacity();

	/**
	 * Clear the buffer
	 */
	void clear();
	
	/**
	 * Release the buffer
	 */
	void release();

	/**
	 * Increase the buffer size to a new size provided
	 * @param newBufferSize The new buffer size
	 */
	void increaseBufferSize(final int newBufferSize);

	/**
	 * Discard the bytes available to be read.
	 */
	void discardSomeReadBytes();

	/**
	 * Gets the netty buffer
	 * @return The netty buffer
	 */
	ByteBuf getBuffer();

	/**
	 * Sets the netty buffer
	 * @param byteBuf the netty buffer to set
	 */
	void setBuffer(final ByteBuf byteBuf);

	/**
	 * Gets the byte array used by this buffer
	 * @return the byte array used by this buffer
	 */
	byte[] getArray();

	// ===== INTEGER ===== //

	/**
	 * Put an integer into the byte buffer.
	 * @param value Integer to add.
	 */
	void writeInt(final int value);

	/**
	 * Get an integer from the byte buffer
	 * @return integer from the array.
	 */
	int readInt();

	/**
	 * Put a variable length integer into the byte buffer.
	 * @param value Integer to represent.
	 */
	void writeVLQInt(final int value);
	
	/**
	 * Get a variable length integer from the byte buffer.
	 * @return integer represented in byte array.
	 */
	int readVLQInt();
	
	/**
	 * Put an array of integers into the byte buffer
	 * @param values Array of integers to add.
	 */
	void writeInts(final int[] values);

	/**
	 * Get an array of integers from the byte buffer
	 * @param size Number of ints to read
	 * @return Array of integers read
	 */
	int[] readInts(final int size);

	// ==== BYTE ==== //

	/**
	 * Put a byte into the byte buffer.
	 * @param value
	 *            Integer to add.
	 */
	void writeByte(final byte value);


	/**
	 * Put an array of bytes into the byte buffer.
	 * @param values
	 *            Array of bytes to add.
	 */
	void writeBytes(final byte[] values);

	/**
	 * Copy all bytes from the buffer provided
	 * @param binBuffer Binary buffer from which to copy bytes
	 */
	void writeBytes(final DataClayByteBuffer binBuffer);

	/**
	 * Get a byte from the byte buffer
	 * @return byte from the arrray.
	 */
	byte readByte();
	
	
	/**
	 * Get an array of bytes into the variable provided.
	 * @param values destination byte array.
	 */
	void readBytes(final byte[] values);
	

	/**
	 * Get an array of bytes.
	 * @param size number of bytes to read
	 * @return An array of bytes.
	 */
	byte[] readBytes(final int size);

	// ===== LONG ===== //

	/**
	 * Put a long into the byte buffer.
	 * @param value Long to add.
	 */
	void writeLong(final long value);

	/**
	 * Get a long from the byte buffer
	 * @return long from the array.
	 */
	long readLong();

	/**
	 * Put an array of longs into the byte buffer.
	 * @param values
	 *            Array of longs to add.
	 */
	void writeLongs(final long[] values);

	/**
	 * Get an array of longs.
	 * @param size number of longs to read
	 * @return An array of longs.
	 */
	long[] readLongs(final int size);

	// ===== FLOAT ===== //

	/**
	 * Put a float into the byte buffer.
	 * @param value float to add.
	 */
	void writeFloat(final float value);

	/**
	 * Get a float from the byte buffer
	 * @return float from the array.
	 */
	float readFloat();

	/**
	 * Put an array of floats into the byte buffer.
	 * @param values
	 *            Array of floats to add.
	 */
	void writeFloats(final float[] values);

	/**
	 * Get an array of floats.
	 * @param size number of floats to read
	 * @return An array of floats.
	 */
	float[] readFloats(final int size);

	// ===== DOUBLE ===== //

	/**
	 * Put a double into the byte buffer.
	 * @param value double to add.
	 */
	void writeDouble(final double value);

	/**
	 * Get a double from the byte buffer
	 * @return double from the array.
	 */
	double readDouble();

	/**
	 * Put an array of doubles into the byte buffer.
	 * @param values
	 *            Array of doubles to add.
	 */
	void writeDoubles(final double[] values);

	/**
	 * Get an array of doubles.
	 * @param size number of doubles to read
	 * @return An array of doubles.
	 */
	double[] readDoubles(final int size);

	// ===== BOOLEAN ===== //

	/**
	 * Put a boolean into the byte buffer.
	 * @param value boolean to add.
	 */
	void writeBoolean(final boolean value);

	/**
	 * Get a boolean from the byte buffer
	 * @return boolean from the array.
	 */
	boolean readBoolean();

	/**
	 * Put an array of booleans into the byte buffer.
	 * @param values
	 *            Array of booleans to add.
	 */
	void writeBooleans(final boolean[] values);

	/**
	 * Get an array of booleans.
	 * @param size number of booleans to read
	 * @return An array of booleans.
	 */
	boolean[] readBooleans(final int size);
 
	// ===== CHAR ===== //

	/**
	 * Put a char into the byte buffer.
	 * @param value char to add.
	 */
	void writeChar(final char value);

	/**
	 * Get a char from the byte buffer
	 * @return char from the array.
	 */
	char readChar();

	/**
	 * Put an array of chars into the byte buffer.
	 * @param values
	 *            Array of chars to add.
	 */
	void writeChars(final char[] values);

	/**
	 * Get an array of chars.
	 * @param size number of chars to read
	 * @return An array of chars.
	 */
	char[] readChars(final int size);

	// ===== SHORT ===== //

	/**
	 * Put a short into the byte buffer.
	 * @param value short to add.
	 */
	void writeShort(final short value);

	/**
	 * Get a short from the byte buffer
	 * @return short from the array.
	 */
	short readShort();


	/**
	 * Put an array of shorts into the byte buffer.
	 * @param values
	 *            Array of shorts to add.
	 */
	void writeShorts(final short[] values);

	/**
	 * Get an array of shorts.
	 * @param size number of shorts to read
	 * @return An array of shorts.
	 */
	short[] readShorts(final int size);
	
	// ===== STRING ===== //

	/**
	 * Put a string into the byte buffer.
	 * @param value string to add.
	 */
	void writeString(final String value);

	/**
	 * Get a string from the byte buffer
	 * @return string from the array.
	 */
	String readString();

	/**
	 * Put an array of strings into the byte buffer.
	 * @param values
	 *            Array of strings to add.
	 */
	void writeStrings(final String[] values);

	/**
	 * Get an array of strings.
	 * @param size number of strings to read
	 * @return An array of strings.
	 */
	String[] readStrings(final int size);
	
	// ===== BYTE ARRAY (byte[]) ===== //
	
	/**
	 * Put a byte array into the byte buffer.
	 * @param value byte array to add.
	 */
	void writeByteArray(final byte[] value);

	/**
	 * Get a byte array from the byte buffer
	 * @return byte array from the array.
	 */
	byte[] readByteArray();
	
	/**
	 * Put an array of byte arrays into the byte buffer.
	 * @param values
	 *            Array of byte arrays to add.
	 */
	void writeByteArrays(final byte[][] values);

	/**
	 * Get an array of byte arrays.
	 * @param size number of byte arrays to read
	 * @return An array of byte arrays.
	 */
	byte[][] readByteArrays(final int size);
	
	/**
	 * Rewind the buffer.
	 */
	void rewind();
	

}
