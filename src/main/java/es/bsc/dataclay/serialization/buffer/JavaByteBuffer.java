
package es.bsc.dataclay.serialization.buffer;


import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import io.grpc.netty.shaded.io.netty.buffer.ByteBuf;
import io.grpc.netty.shaded.io.netty.buffer.Unpooled;


/**
 * Java byte buffer to be used while no communication is nedded.
 */
public final class JavaByteBuffer implements DataClayByteBuffer {

	/** Java byte buffer. */
	private ByteBuffer javaByteBuffer;

	/** Initial capacity. */
	private static final int INITIAL_CAPACITY = 4096;

	/** Increase capacity. */
	private static final int INCREASE_CAPACITY = 4096;

	/**
	 * Constructor used for decodification
	 * @param newBuffer The byte array to deserialize
	 */
	public JavaByteBuffer(final byte[] newBuffer) {
		wrapBytes(newBuffer);
	}

	/**
	 * Constructor used for codification
	 */
	public JavaByteBuffer() {
		javaByteBuffer = ByteBuffer.allocate(INITIAL_CAPACITY);
	}

	/**
	 * Constructor
	 * @param data
	 *            ByteBuffer containing data
	 */
	public JavaByteBuffer(final ByteBuffer data) {
		this.javaByteBuffer = data;
	}

	@Override
	public void rewind() { 
		this.javaByteBuffer.rewind();
	}

	@Override
	public void wrapBytes(final byte[] newBuffer) {
		javaByteBuffer = ByteBuffer.wrap(newBuffer);
	}
	
	@Override
	public boolean isReadable() {
		return javaByteBuffer.remaining() > 0;
	}

	@Override
	public int readableBytes() {
		return javaByteBuffer.remaining();
	}

	@Override
	public int readerIndex() {
		return javaByteBuffer.position();
	}

	@Override
	public int writerIndex() {
		return javaByteBuffer.position();
	}

	@Override
	public void clear() {
		javaByteBuffer.clear();
	}
	
	@Override
	public void setReaderIndex(final int index) {
		javaByteBuffer.position(index);
	}

	@Override
	public void setWriterIndex(final int index) {
		javaByteBuffer.position(index);
	}

	@Override
	public int maxCapacity() {
		return javaByteBuffer.capacity();
	}

	@Override
	public void release() {
		javaByteBuffer = null;
	}

	@Override
	public void increaseBufferSize(final int newBufferSize) {
		final byte[] oldBytes = new byte[javaByteBuffer.position()];
		javaByteBuffer.rewind();
		javaByteBuffer.get(oldBytes);
		javaByteBuffer = ByteBuffer.allocate(javaByteBuffer.capacity() + newBufferSize);
		javaByteBuffer.put(oldBytes);
	}

	@Override
	public void discardSomeReadBytes() {

	}

	@Override
	public ByteBuf getBuffer() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setBuffer(final ByteBuf byteBuf) {
		throw new UnsupportedOperationException();
	}


	// ===== INTEGER ===== //

	@Override
	public byte[] getArray() {
		final byte[] curBytes = new byte[javaByteBuffer.position()];
		javaByteBuffer.rewind();
		javaByteBuffer.get(curBytes);
		return curBytes;
	}

	@Override
	public void writeInt(final int value) {
		try {
			javaByteBuffer.putInt(value);
		} catch (final BufferOverflowException e) { 
			this.increaseBufferSize(INCREASE_CAPACITY);
			javaByteBuffer.putInt(value);
		}
	}

	@Override
	public int readInt() {
		return javaByteBuffer.getInt();
	}
	
	@Override
	public void writeVLQInt(final int value) {
		try {
			int tmp = value;
			final byte[] byteArrayList = new byte[10];
			int i = 0;
			while ((tmp & 0xFFFFFF80) != 0L) {
				byteArrayList[i++] = ((byte) ((tmp & 0x7F) | 0x80));
				tmp >>>= 7;
			}
			byteArrayList[i] = ((byte) (tmp & 0x7F));
			final byte[] out = new byte[i + 1];
			System.arraycopy(byteArrayList, 0, out, 0, i + 1);
			/*
			 * for (; i >= 0; i--) { out[i] = byteArrayList[i]; }
			 */
			for (final byte b : out) {
				javaByteBuffer.put(b);
			}

		} catch (final BufferOverflowException e) { 
			this.increaseBufferSize(INCREASE_CAPACITY);
			javaByteBuffer.putInt(value);
		}
	}
	
	@Override
	public int readVLQInt() {
		int value = 0;
		int i = 0;
		int b = 0;
		while (((b = javaByteBuffer.get()) & 0x80) != 0) {
			value |= (b & 0x7F) << i;
			i += 7;
			if (i > 35) {
				throw new IllegalArgumentException("Variable length quantity is too long");
			}
		}
		final int finalValue = value | (b << i);
		return finalValue;
	}


	// ==== BYTE ==== //

	@Override
	public void writeInts(final int[] values) {
		for (final int value : values) {
			writeInt(value);
		}
	}

	@Override
	public int[] readInts(final int size) {
		final int[] result = new int[size];
		for (int i = 0; i < size; ++i) {
			result[i] = readInt();
		}
		return result;
	}

	@Override
	public void writeByte(final byte value) {
		try {
			javaByteBuffer.put(value);
		} catch (final BufferOverflowException e) { 
			this.increaseBufferSize(INCREASE_CAPACITY);
			javaByteBuffer.put(value);
		}

	}

	@Override
	public byte readByte() {
		return javaByteBuffer.get();
	}

	@Override
	public void writeBytes(final byte[] values) {
		
		try {
			javaByteBuffer.put(values);
		} catch (final BufferOverflowException e) { 
			this.increaseBufferSize(INCREASE_CAPACITY);
			javaByteBuffer.put(values);
		}
	}

	@Override
	public void writeBytes(final DataClayByteBuffer binBuffer) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void readBytes(final byte[] values) {
		javaByteBuffer.get(values);
	}

	// ===== LONG ===== //

	@Override
	public void writeLong(final long value) {
		try {
			javaByteBuffer.putLong(value);
		} catch (final BufferOverflowException e) { 
			this.increaseBufferSize(INCREASE_CAPACITY);
			javaByteBuffer.putLong(value);
		}
	}

	@Override
	public long readLong() {
		return javaByteBuffer.getLong();
	}

	@Override
	public void writeLongs(final long[] values) {
		for (final long value : values) {
			writeLong(value);
		}

	}

	@Override
	public long[] readLongs(final int size) {
		final long[] result = new long[size];
		for (int i = 0; i < size; ++i) {
			result[i] = readLong();
		}
		return result;
	}

	@Override
	public byte[] readBytes(final int size) {
		final byte[] result = new byte[size];
		javaByteBuffer.get(result);
		return result;
	}

	@Override
	public void writeFloat(final float value) {
		try {
			javaByteBuffer.putFloat(value);
		} catch (final BufferOverflowException e) { 
			this.increaseBufferSize(INCREASE_CAPACITY);
			javaByteBuffer.putFloat(value);
		}
	}

	@Override
	public float readFloat() {
		return javaByteBuffer.getFloat();
	}

	@Override
	public void writeFloats(final float[] values) {

		for (final float value : values) {
			writeFloat(value);
		}
	}

	@Override
	public float[] readFloats(final int size) {
		final float[] result = new float[size];
		for (int i = 0; i < size; ++i) {
			result[i] = readFloat();
		}
		return result;
	}

	@Override
	public void writeDouble(final double value) {
		try {
			javaByteBuffer.putDouble(value);
		} catch (final BufferOverflowException e) { 
			this.increaseBufferSize(INCREASE_CAPACITY);
			javaByteBuffer.putDouble(value);
		}

	}

	@Override
	public double readDouble() {
		return javaByteBuffer.getDouble();
	}

	@Override
	public void writeDoubles(final double[] values) {

		for (final double value : values) {
			writeDouble(value);
		}
	}

	@Override
	public double[] readDoubles(final int size) {
		final double[] result = new double[size];
		for (int i = 0; i < size; ++i) {
			result[i] = readDouble();
		}
		return result;
	}

	@Override
	public void writeBoolean(final boolean value) {		
		byte boolToByte = 0;
		if (value) {
			boolToByte = 1;
		}
		writeByte(boolToByte);

	}

	@Override
	public boolean readBoolean() {
		final byte result = readByte();
		return result == 1;
	}

	@Override
	public void writeBooleans(final boolean[] values) {

		final byte[] boolsToByte = new byte[values.length];
		for (int i = 0; i < values.length; ++i) {
			if (values[i]) {
				boolsToByte[i] = 1;
			} else {
				boolsToByte[i] = 0;
			}
		}
		writeBytes(boolsToByte);
	}

	@Override
	public boolean[] readBooleans(final int size) {
		final byte[] readBytes = readBytes(size);
		final boolean[] result = new boolean[size];
		for (int i = 0; i < size; ++i) {
			if (readBytes[i] == 0) {
				result[i] = false;
			} else {
				result[i] = true;
			}
		}
		return result;
	}

	@Override
	public void writeChar(final char value) {
		try {
			javaByteBuffer.putChar(value);
		} catch (final BufferOverflowException e) { 
			this.increaseBufferSize(INCREASE_CAPACITY);
			javaByteBuffer.putChar(value);
		}

	}

	@Override
	public char readChar() {
		return javaByteBuffer.getChar();
	}

	@Override
	public void writeChars(final char[] values) {

		for (final char value : values) {
			writeChar(value);
		}
	}

	@Override
	public char[] readChars(final int size) {
		final char[] result = new char[size];
		for (int i = 0; i < size; ++i) {
			result[i] = readChar();
		}
		return result;
	}

	@Override
	public void writeShort(final short value) {
		try {
			javaByteBuffer.putShort(value);
		} catch (final BufferOverflowException e) { 
			this.increaseBufferSize(INCREASE_CAPACITY);
			javaByteBuffer.putShort(value);
		}

	}

	@Override
	public short readShort() {
		return javaByteBuffer.getShort();
	}

	@Override
	public void writeShorts(final short[] values) {

		for (final short value : values) {
			writeShort(value);
		}

	}

	@Override
	public short[] readShorts(final int size) {
		final short[] result = new short[size];
		for (int i = 0; i < size; ++i) {
			result[i] = readShort();
		}
		return result;
	}

	@Override
	public void writeString(final String value) {
		writeInt(value.getBytes(StandardCharsets.UTF_16BE).length);
		writeBytes(value.getBytes(StandardCharsets.UTF_16BE));
	}

	@Override
	public String readString() {
		final int length = readInt();
		final byte[] bytes = readBytes(length);
		return new String(bytes, StandardCharsets.UTF_16BE);
	}

	@Override
	public void writeStrings(final String[] values) {

		for (final String value : values) {
			writeString(value);
		}
	}

	@Override
	public String[] readStrings(final int size) {
		final String[] result = new String[size];
		for (int i = 0; i < size; ++i) {
			result[i] = readString();
		}
		return result;
	}

	@Override
	public void writeByteArray(final byte[] value) {
		writeInt(value.length);
		writeBytes(value);
	}

	@Override
	public byte[] readByteArray() {
		final int length = readInt();
		return readBytes(length);
	}

	@Override
	public void writeByteArrays(final byte[][] values) {
		for (final byte[] value : values) {
			writeByteArray(value);
		}
	}

	@Override
	public byte[][] readByteArrays(final int size) {
		final byte[][] result = new byte[size][];
		for (int i = 0; i < size; ++i) {
			result[i] = readByteArray();
		}
		return result;
	}
}
