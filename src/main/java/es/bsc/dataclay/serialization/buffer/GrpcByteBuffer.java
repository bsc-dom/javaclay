
package es.bsc.dataclay.serialization.buffer;

import com.google.protobuf.ByteString;

import io.grpc.netty.shaded.io.netty.buffer.ByteBuf;

/**
 * Grpc buffer.
 */
public final class GrpcByteBuffer implements DataClayByteBuffer {

	/** GRPC byte buffer. */
	private ByteString.Output byteBuf;

	@Override
	public void wrapBytes(final byte[] newBuffer) {

	}

	@Override
	public boolean isReadable() {
		return false;
	}

	@Override
	public int readableBytes() {
		return byteBuf.size();
	}

	@Override
	public int readerIndex() {
		return 0;
	}

	@Override
	public int writerIndex() {
		return 0;
	}

	@Override
	public void setReaderIndex(final int index) {

	}

	@Override
	public void setWriterIndex(final int index) {

	}

	@Override
	public int maxCapacity() {

		return 0;
	}

	@Override
	public void clear() {

	}

	@Override
	public void release() {

	}

	@Override
	public void increaseBufferSize(final int newBufferSize) {

	}

	@Override
	public void discardSomeReadBytes() {

	}

	@Override
	public ByteBuf getBuffer() {

		return null;
	}

	@Override
	public void setBuffer(final ByteBuf thebyteBuf) {

	}

	@Override
	public byte[] getArray() {

		return null;
	}

	@Override
	public void writeInt(final int value) {

	}

	@Override
	public int readInt() {

		return 0;
	}

	@Override
	public void writeVLQInt(final int value) {

	}

	@Override
	public int readVLQInt() {

		return 0;
	}

	@Override
	public void writeInts(final int[] values) {

	}

	@Override
	public int[] readInts(final int size) {

		return null;
	}

	@Override
	public void writeByte(final byte value) {

	}

	@Override
	public void writeBytes(final byte[] values) {

	}

	@Override
	public void writeBytes(final DataClayByteBuffer binBuffer) {

	}

	@Override
	public byte readByte() {

		return 0;
	}

	@Override
	public void readBytes(final byte[] values) {

	}

	@Override
	public byte[] readBytes(final int size) {

		return null;
	}

	@Override
	public void writeLong(final long value) {

	}

	@Override
	public long readLong() {

		return 0;
	}

	@Override
	public void writeLongs(final long[] values) {

	}

	@Override
	public long[] readLongs(final int size) {

		return null;
	}

	@Override
	public void writeFloat(final float value) {

	}

	@Override
	public float readFloat() {

		return 0;
	}

	@Override
	public void writeFloats(final float[] values) {

	}

	@Override
	public float[] readFloats(final int size) {

		return null;
	}

	@Override
	public void writeDouble(final double value) {

	}

	@Override
	public double readDouble() {

		return 0;
	}

	@Override
	public void writeDoubles(final double[] values) {

	}

	@Override
	public double[] readDoubles(final int size) {

		return null;
	}

	@Override
	public void writeBoolean(final boolean value) {

	}

	@Override
	public boolean readBoolean() {

		return false;
	}

	@Override
	public void writeBooleans(final boolean[] values) {

	}

	@Override
	public boolean[] readBooleans(final int size) {

		return null;
	}

	@Override
	public void writeChar(final char value) {

	}

	@Override
	public char readChar() {

		return 0;
	}

	@Override
	public void writeChars(final char[] values) {

	}

	@Override
	public char[] readChars(final int size) {

		return null;
	}

	@Override
	public void writeShort(final short value) {

	}

	@Override
	public short readShort() {

		return 0;
	}

	@Override
	public void writeShorts(final short[] values) {

	}

	@Override
	public short[] readShorts(final int size) {

		return null;
	}

	@Override
	public void writeString(final String value) {

	}

	@Override
	public String readString() {

		return null;
	}

	@Override
	public void writeStrings(final String[] values) {

	}

	@Override
	public String[] readStrings(final int size) {

		return null;
	}

	@Override
	public void writeByteArray(final byte[] value) {

	}

	@Override
	public byte[] readByteArray() {

		return null;
	}

	@Override
	public void writeByteArrays(final byte[][] values) {

	}

	@Override
	public byte[][] readByteArrays(final int size) {

		return null;
	}

	@Override
	public void rewind() {

	}

}
