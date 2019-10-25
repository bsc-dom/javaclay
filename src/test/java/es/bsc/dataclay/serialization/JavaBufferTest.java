
package es.bsc.dataclay.serialization;

import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import es.bsc.dataclay.serialization.buffer.JavaByteBuffer;
import es.bsc.dataclay.test.TestingTools;


/**
 * JavaByteBufferTest.
 */
public final class JavaBufferTest {
	
	/** Max array length.*/
	private static final int MAX_ARRAY_LEN = 10;
	
	/**
	 * @brief Serialize int test.
	 */
	@Test
	public void testSerializeInt() {
		final Random random = new Random();
		final JavaByteBuffer wsbBuffer = new JavaByteBuffer();
		final int serializedVal = random.nextInt();
		wsbBuffer.writeInt(serializedVal);
		wsbBuffer.rewind();
		final int result = wsbBuffer.readInt();
		assertTrue(result == serializedVal);
	}
	
	/**
	 * @brief Serialize float test.
	 */
	@Test
	public void testSerializeFloat() {
		final Random random = new Random();
		final JavaByteBuffer wsbBuffer = new JavaByteBuffer();
		final float serializedVal = random.nextFloat();
		wsbBuffer.writeFloat(serializedVal);
		wsbBuffer.rewind();
		final float result = wsbBuffer.readFloat();
		assertTrue(result == serializedVal);
	}
	
	/**
	 * @brief Serialize long test.
	 */
	@Test
	public void testSerializeLong() {
		final Random random = new Random();
		final JavaByteBuffer wsbBuffer = new JavaByteBuffer();
		final long serializedVal = random.nextLong();
		wsbBuffer.writeLong(serializedVal);
		wsbBuffer.rewind();
		final long result = wsbBuffer.readLong();
		assertTrue(result == serializedVal);
	}
	
	/**
	 * @brief Serialize short test.
	 */
	@Test
	public void testSerializeShort() {
		final Random random = new Random();
		final JavaByteBuffer wsbBuffer = new JavaByteBuffer();
		final short serializedVal = (short) random.nextInt(Short.MAX_VALUE + 1);
		wsbBuffer.writeShort(serializedVal);
		wsbBuffer.rewind();
		final long result = wsbBuffer.readShort();
		assertTrue(result == serializedVal);
	}
	
	/**
	 * @brief Serialize bool test.
	 */
	@Test
	public void testSerializeBool() {
		final Random random = new Random();
		final JavaByteBuffer wsbBuffer = new JavaByteBuffer();
		final boolean serializedVal = random.nextBoolean();
		wsbBuffer.writeBoolean(serializedVal);
		wsbBuffer.rewind();
		final boolean result = wsbBuffer.readBoolean();
		assertTrue(result == serializedVal);
	}
	
	/**
	 * @brief Serialize byte test.
	 */
	@Test
	public void testSerializeByte() {
		final Random random = new Random();
		final JavaByteBuffer wsbBuffer = new JavaByteBuffer();
		final byte[] randomBytes = new byte[MAX_ARRAY_LEN];
		random.nextBytes(randomBytes);
		final byte serializedVal = randomBytes[random.nextInt(MAX_ARRAY_LEN)];
		wsbBuffer.writeByte(serializedVal);
		wsbBuffer.rewind();
		final byte result = wsbBuffer.readByte();
		assertTrue(result == serializedVal);
	}
	
	/**
	 * @brief Serialize char test.
	 */
	@Test
	public void testSerializeChar() {
		final Random random = new Random();
		final JavaByteBuffer wsbBuffer = new JavaByteBuffer();
		final char serializedVal = TestingTools.ALPHABET.charAt(random.nextInt(TestingTools.ALPHABET.length()));
		wsbBuffer.writeChar(serializedVal);
		wsbBuffer.rewind();
		final char result = wsbBuffer.readChar();
		assertTrue(result == serializedVal);
	}
	
	/**
	 * @brief Serialize String test.
	 */
	@Test
	public void testSerializeString() {
		final String serializedVal = TestingTools.generateRandomString(MAX_ARRAY_LEN);
		final JavaByteBuffer wsbBuffer = new JavaByteBuffer();
		wsbBuffer.writeString(serializedVal);
		wsbBuffer.rewind();
		final String result = wsbBuffer.readString();
		assertTrue(result.equals(serializedVal));
	}
	
	/**
	 * @brief Serialize int array test.
	 */
	@Test
	public void testSerializeIntArray() {
		final Random random = new Random();
		final int[] serializedVal = new int[MAX_ARRAY_LEN];
		for (int i = 0; i < serializedVal.length; ++i) {
			serializedVal[i] = random.nextInt();
		}
		
		final JavaByteBuffer wsbBuffer = new JavaByteBuffer();
		wsbBuffer.writeInts(serializedVal);
		wsbBuffer.rewind();
		final int[] result = wsbBuffer.readInts(MAX_ARRAY_LEN);
		
		for (int i = 0; i < serializedVal.length; ++i) {
			assertTrue(result[i] == serializedVal[i]);
		}
	}
	
	/**
	 * @brief Serialize array of floats test.
	 */
	@Test
	public void testSerializeFloatArray() {
		final Random random = new Random();
		final float[] serializedVal = new float[MAX_ARRAY_LEN];
		for (int i = 0; i < serializedVal.length; ++i) {
			serializedVal[i] = random.nextFloat();
		}
		
		final JavaByteBuffer wsbBuffer = new JavaByteBuffer();
		wsbBuffer.writeFloats(serializedVal);
		wsbBuffer.rewind();
		final float[] result = wsbBuffer.readFloats(MAX_ARRAY_LEN);
		
		for (int i = 0; i < serializedVal.length; ++i) {
			assertTrue(result[i] == serializedVal[i]);
		}
	}
	
	/**
	 * @brief Serialize long array test.
	 */
	@Test
	public void testSerializeLongArray() {
		final Random random = new Random();
		final long[] serializedVal = new long[MAX_ARRAY_LEN];
		for (int i = 0; i < serializedVal.length; ++i) {
			serializedVal[i] = random.nextLong();
		}
		
		final JavaByteBuffer wsbBuffer = new JavaByteBuffer();
		wsbBuffer.writeLongs(serializedVal);
		wsbBuffer.rewind();
		final long[] result = wsbBuffer.readLongs(MAX_ARRAY_LEN);
		
		for (int i = 0; i < serializedVal.length; ++i) {
			assertTrue(result[i] == serializedVal[i]);
		}
	}
	
	/**
	 * @brief Serialize short array test.
	 */
	@Test
	public void testSerializeShortArray() {
		final Random random = new Random();
		final short[] serializedVal = new short[MAX_ARRAY_LEN];
		for (int i = 0; i < serializedVal.length; ++i) {
			serializedVal[i] = (short) random.nextInt(Short.MAX_VALUE + 1);
		}
		
		final JavaByteBuffer wsbBuffer = new JavaByteBuffer();
		wsbBuffer.writeShorts(serializedVal);
		wsbBuffer.rewind();
		final short[] result = wsbBuffer.readShorts(MAX_ARRAY_LEN);
		
		for (int i = 0; i < serializedVal.length; ++i) {
			assertTrue(result[i] == serializedVal[i]);
		}
		
	}
	
	/**
	 * @brief Serialize bool array test.
	 */
	@Test
	public void testSerializeBoolArray() {
		final Random random = new Random();
		final boolean[] serializedVal = new boolean[MAX_ARRAY_LEN];
		for (int i = 0; i < serializedVal.length; ++i) {
			serializedVal[i] = random.nextBoolean();
		}
		
		final JavaByteBuffer wsbBuffer = new JavaByteBuffer();
		wsbBuffer.writeBooleans(serializedVal);
		wsbBuffer.rewind();
		final boolean[] result = wsbBuffer.readBooleans(MAX_ARRAY_LEN);
		
		for (int i = 0; i < serializedVal.length; ++i) {
			assertTrue(result[i] == serializedVal[i]);
		}
	}
	
	/**
	 * @brief Serialize byte array test.
	 */
	@Test
	public void testSerializeByteArray() {
		final Random random = new Random();
		final byte[] serializedVal = new byte[MAX_ARRAY_LEN];
		random.nextBytes(serializedVal);
		
		final JavaByteBuffer wsbBuffer = new JavaByteBuffer();
		wsbBuffer.writeBytes(serializedVal);
		wsbBuffer.rewind();
		final byte[] result = new byte[MAX_ARRAY_LEN];
		wsbBuffer.readBytes(result);
		
		for (int i = 0; i < serializedVal.length; ++i) {
			assertTrue(result[i] == serializedVal[i]);
		}
	}
	
	/**
	 * @brief Serialize char test.
	 */
	@Test
	public void testSerializeCharArray() {
		final Random random = new Random();
		final char[] serializedVal = new char[MAX_ARRAY_LEN];
		for (int i = 0; i < serializedVal.length; ++i) {
			serializedVal[i] = (char) (random.nextInt(TestingTools.ALPHABET.length()) + 'a');
		}
		
		final JavaByteBuffer wsbBuffer = new JavaByteBuffer();
		wsbBuffer.writeChars(serializedVal);
		wsbBuffer.rewind();
		final char[] result = wsbBuffer.readChars(MAX_ARRAY_LEN);
		for (int i = 0; i < serializedVal.length; ++i) {
			assertTrue(result[i] == serializedVal[i]);
		}
	}
	
	/**
	 * @brief Serialize String test.
	 */
	@Test
	public void testSerializeStringArray() {
		
		final String[] serializedVal = new String[MAX_ARRAY_LEN];
		for (int i = 0; i < serializedVal.length; ++i) {
			serializedVal[i] = TestingTools.generateRandomString(MAX_ARRAY_LEN);
		}
		
		final JavaByteBuffer wsbBuffer = new JavaByteBuffer();
		wsbBuffer.writeStrings(serializedVal);
		wsbBuffer.rewind();
		final String[] result = wsbBuffer.readStrings(MAX_ARRAY_LEN);
		for (int i = 0; i < serializedVal.length; ++i) {
			assertTrue(result[i].equals(serializedVal[i]));
		}
		
	}
	
	/**
	 * @brief Serialize Double test.
	 */
	@Test
	public void testSerializeDouble() {
		final Random random = new Random();
		final JavaByteBuffer wsbBuffer = new JavaByteBuffer();
		final double serializedVal = random.nextDouble();
		wsbBuffer.writeDouble(serializedVal);
		wsbBuffer.rewind();
		final double result = wsbBuffer.readDouble();
		assertTrue(result == serializedVal);
	}
	
	@Test
	public void testIncreaseBufferSize() {
		final Random random = new Random();
		final byte[] serializedVal = new byte[4050];
		random.nextBytes(serializedVal);

		final JavaByteBuffer wsbBuffer = new JavaByteBuffer();
		wsbBuffer.writeBytes(serializedVal);
		wsbBuffer.rewind();
		final byte[] result = new byte[4050];
		wsbBuffer.readBytes(result);

		for (int i = 0; i < serializedVal.length; ++i) {
			assertTrue(result[i] == serializedVal[i]);
		}

	}
	
}
