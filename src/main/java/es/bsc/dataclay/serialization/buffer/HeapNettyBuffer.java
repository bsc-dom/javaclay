
package es.bsc.dataclay.serialization.buffer;

import java.nio.charset.StandardCharsets;

import io.grpc.netty.shaded.io.netty.buffer.ByteBuf;
import io.grpc.netty.shaded.io.netty.buffer.Unpooled;

/**
 * Netty buffer used in case Java UNSAFE is not supported.
 */
public final class HeapNettyBuffer implements DataClayByteBuffer {

	/** Netty byte buffer. */
	private ByteBuf nettyByteBuf;

	/**
	 * Constructor used for decodification
	 * @param newBuffer
	 *            The byte array to deserialize
	 */
	public HeapNettyBuffer(final byte[] newBuffer) {
		wrapBytes(newBuffer);
	}

	/**
	 * Constructor used for codification
	 */
	public HeapNettyBuffer() {
		nettyByteBuf = Unpooled.buffer();
	}

	/**
	 * Constructor
	 * @param data
	 *            ByteBuf containing data
	 */
	public HeapNettyBuffer(final ByteBuf data) {
		this.nettyByteBuf = data;
	}

	@Override
	public void wrapBytes(final byte[] newBuffer) {
		nettyByteBuf = Unpooled.wrappedBuffer(newBuffer);
	}

	@Override
	public boolean isReadable() {
		return nettyByteBuf.isReadable();
	}

	@Override
	public int readableBytes() {
		return nettyByteBuf.readableBytes();
	}

	@Override
	public int readerIndex() {
		return nettyByteBuf.readerIndex();
	}

	@Override
	public int writerIndex() {
		return nettyByteBuf.writerIndex();
	}

	@Override
	public void setReaderIndex(final int index) {
		nettyByteBuf.setIndex(index, nettyByteBuf.writerIndex());
	}

	@Override
	public void setWriterIndex(final int index) {
		nettyByteBuf.setIndex(nettyByteBuf.readerIndex(), index);
	}

	@Override
	public int maxCapacity() {
		return nettyByteBuf.maxCapacity();
	}

	@Override
	public void clear() {
		nettyByteBuf.clear();
	}

	@Override
	public void release() {
		nettyByteBuf.release();
	}

	@Override
	public void increaseBufferSize(final int newBufferSize) {
		final ByteBuf otherByteBuf = Unpooled.buffer(newBufferSize);
		otherByteBuf.writeBytes(nettyByteBuf);
		nettyByteBuf = otherByteBuf;
	}

	@Override
	public void discardSomeReadBytes() {
		nettyByteBuf.discardReadBytes();
	}

	@Override
	public ByteBuf getBuffer() {
		return nettyByteBuf;
	}

	@Override
	public void setBuffer(final ByteBuf byteBuf) {
		nettyByteBuf = byteBuf;
	}

	@Override
	public byte[] getArray() {
		final byte[] curArray = new byte[nettyByteBuf.readableBytes()];
		nettyByteBuf.getBytes(this.readerIndex(), curArray);
		return curArray;
	}

	// ===== INTEGER ===== //

	@Override
	public void writeInt(final int value) {
		nettyByteBuf.writeInt(value);
	}

	@Override
	public int readInt() {
		return nettyByteBuf.readInt();
	}

	// CHECKSTYLE:OFF
	@Override
	public void writeVLQInt(final int value) {
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
			nettyByteBuf.writeByte(b);
		}
	}

	@Override
	public int readVLQInt() {
		int value = 0;
		int i = 0;
		int b = 0;
		while (((b = nettyByteBuf.readByte()) & 0x80) != 0) {
			value |= (b & 0x7F) << i;
			i += 7;
			if (i > 35) {
				throw new IllegalArgumentException("Variable length quantity is too long");
			}
		}
		final int finalValue = value | (b << i);
		return finalValue;
	}
	// CHECKSTYLE:ON

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

		nettyByteBuf.writeByte(value);

	}

	@Override
	public byte readByte() {
		return nettyByteBuf.readByte();
	}

	@Override
	public void writeBytes(final byte[] values) {
		nettyByteBuf.writeBytes(values);
	}

	@Override
	public void writeBytes(final DataClayByteBuffer binBuffer) {
		nettyByteBuf.writeBytes(binBuffer.getBuffer());
	}

	/**
	 * Write bytes in byte buffer.
	 * @param byteBuf
	 *            Netty byte buffer
	 */
	public void writeBytes(final ByteBuf byteBuf) {
		nettyByteBuf.writeBytes(byteBuf);
	}

	@Override
	public void readBytes(final byte[] values) {
		nettyByteBuf.readBytes(values);
	}

	// ===== LONG ===== //

	@Override
	public void writeLong(final long value) {

		nettyByteBuf.writeLong(value);

	}

	@Override
	public long readLong() {
		return nettyByteBuf.readLong();
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
		nettyByteBuf.readBytes(result);
		return result;
	}

	@Override
	public void writeFloat(final float value) {

		nettyByteBuf.writeFloat(value);

	}

	@Override
	public float readFloat() {
		return nettyByteBuf.readFloat();
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
		nettyByteBuf.writeDouble(value);

	}

	@Override
	public double readDouble() {
		return nettyByteBuf.readDouble();
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
		nettyByteBuf.writeChar(value);

	}

	@Override
	public char readChar() {
		return nettyByteBuf.readChar();
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

		nettyByteBuf.writeShort(value);

	}

	@Override
	public short readShort() {
		return nettyByteBuf.readShort();
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

	@Override
	public void rewind() {

	}

}
