
package es.bsc.dataclay.serialization;

import static org.junit.Assert.assertTrue;

import java.lang.ref.Reference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.serialization.buffer.DirectNettyBuffer;
import es.bsc.dataclay.serialization.java.util.ArrayWrapper;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.ids.ObjectID;

/** Class to test the serialization of the DataClayJavaArray class. */
public final class DataClayJavaArrayTest {

	@SuppressWarnings("unused")
	private Object serializeAndDeserialize(final Object arr) throws Exception {
		final ArrayWrapper dcArray = new ArrayWrapper(arr);
		final DirectNettyBuffer dcBuffer = new DirectNettyBuffer();

		final IdentityHashMap<Object, Integer> curSerializedObjs = new IdentityHashMap<>();
		final List<DataClayObject> pendingObjs = new LinkedList<>();
		final ReferenceCounting referenceCounting = new ReferenceCounting();
		dcArray.serialize(dcBuffer, false, null, curSerializedObjs, pendingObjs.listIterator(), referenceCounting);

		final ArrayWrapper resArray = new ArrayWrapper();

		final Map<ObjectID, Reference<DataClayObject>> objCache = new HashMap<>();
		final DataClayObjectMetaData metadata = new DataClayObjectMetaData();
		final Map<Integer, Object> curDeserJavaObjs = new HashMap<>();
		resArray.deserialize(dcBuffer, null, metadata, curDeserJavaObjs);

		return resArray.getJavaObject();
	}

	@Test
	public void testIntArray() throws Exception {

		final Random r = new Random();
		final int[] arr = new int[10];
		for (int i = 0; i < arr.length; ++i) {
			arr[i] = r.nextInt();
		}
		final int[] result = (int[]) serializeAndDeserialize(arr);
		assertTrue(Arrays.equals(arr, result));
	}

	@Test
	public void testFloatArray() throws Exception {

		final Random r = new Random();
		final float[] arr = new float[10];
		for (int i = 0; i < arr.length; ++i) {
			arr[i] = r.nextFloat();
		}
		final float[] result = (float[]) serializeAndDeserialize(arr);
		assertTrue(Arrays.equals(arr, result));
	}

	@Test
	public void testDoubleArray() throws Exception {

		final Random r = new Random();
		final double[] arr = new double[10];
		for (int i = 0; i < arr.length; ++i) {
			arr[i] = r.nextDouble();
		}

		final double[] result = (double[]) serializeAndDeserialize(arr);
		assertTrue(Arrays.equals(arr, result));
	}

	@Test
	public void testBooleanArray() throws Exception {

		final Random r = new Random();
		final boolean[] arr = new boolean[10];
		for (int i = 0; i < arr.length; ++i) {
			arr[i] = r.nextBoolean();
		}

		final boolean[] result = (boolean[]) serializeAndDeserialize(arr);
		assertTrue(Arrays.equals(arr, result));
	}

	@Test
	public void testShortArray() throws Exception {

		final Random r = new Random();
		final short[] arr = new short[10];
		for (int i = 0; i < arr.length; ++i) {
			arr[i] = (short) r.nextInt(Short.MAX_VALUE);
		}

		final short[] result = (short[]) serializeAndDeserialize(arr);
		assertTrue(Arrays.equals(arr, result));
	}

	@Test
	public void testLongArray() throws Exception {

		final Random r = new Random();
		final long[] arr = new long[10];
		for (int i = 0; i < arr.length; ++i) {
			arr[i] = r.nextLong();
		}

		final long[] result = (long[]) serializeAndDeserialize(arr);
		assertTrue(Arrays.equals(arr, result));
	}

	@Test
	public void testCharArray() throws Exception {

		final String alphabet = "abcdefghijklmnopqrstuvwxyz1234567890.=$&";
		final Random r = new Random();
		final char[] arr = new char[10];
		for (int i = 0; i < arr.length; ++i) {
			arr[i] = alphabet.charAt(r.nextInt(alphabet.length()));
		}

		final char[] result = (char[]) serializeAndDeserialize(arr);
		assertTrue(Arrays.equals(arr, result));
	}

	@Test
	public void testByteArray() throws Exception {

		final Random r = new Random();
		final byte[] arr = new byte[10];
		r.nextBytes(arr);

		final byte[] result = (byte[]) serializeAndDeserialize(arr);
		assertTrue(Arrays.equals(arr, result));
	}

	@Test
	public void testJavaIntArray() throws Exception {

		final Random r = new Random();
		final Integer[] arr = new Integer[10];
		for (int i = 0; i < arr.length; ++i) {
			arr[i] = r.nextInt();
		}

		final Integer[] result = (Integer[]) serializeAndDeserialize(arr);
		assertTrue(Arrays.equals(arr, result));
	}

	@Test
	public void testJavaFloatArray() throws Exception {

		final Random r = new Random();
		final Float[] arr = new Float[10];
		for (int i = 0; i < arr.length; ++i) {
			arr[i] = r.nextFloat();
		}

		final Float[] result = (Float[]) serializeAndDeserialize(arr);
		assertTrue(Arrays.equals(arr, result));
	}

	@Test
	public void testJavaDoubleArray() throws Exception {

		final Random r = new Random();
		final Double[] arr = new Double[10];
		for (int i = 0; i < arr.length; ++i) {
			arr[i] = r.nextDouble();
		}

		final Double[] result = (Double[]) serializeAndDeserialize(arr);
		assertTrue(Arrays.equals(arr, result));
	}

	@Test
	public void testJavaBooleanArray() throws Exception {

		final Random r = new Random();
		final Boolean[] arr = new Boolean[10];
		for (int i = 0; i < arr.length; ++i) {
			arr[i] = r.nextBoolean();
		}

		final Boolean[] result = (Boolean[]) serializeAndDeserialize(arr);
		assertTrue(Arrays.equals(arr, result));
	}

	@Test
	public void testJavaShortArray() throws Exception {

		final Random r = new Random();
		final Short[] arr = new Short[10];
		for (int i = 0; i < arr.length; ++i) {
			arr[i] = (short) r.nextInt(Short.MAX_VALUE);
		}

		final Short[] result = (Short[]) serializeAndDeserialize(arr);
		assertTrue(Arrays.equals(arr, result));
	}

	@Test
	public void testJavaLongArray() throws Exception {

		final Random r = new Random();
		final Long[] arr = new Long[10];
		for (int i = 0; i < arr.length; ++i) {
			arr[i] = r.nextLong();
		}

		final Long[] result = (Long[]) serializeAndDeserialize(arr);
		assertTrue(Arrays.equals(arr, result));
	}

	@Test
	public void testJavaCharArray() throws Exception {

		final String alphabet = "abcdefghijklmnopqrstuvwxyz1234567890.=$&";
		final Random r = new Random();
		final Character[] arr = new Character[10];
		for (int i = 0; i < arr.length; ++i) {
			arr[i] = alphabet.charAt(r.nextInt(alphabet.length()));
		}

		final Character[] result = (Character[]) serializeAndDeserialize(arr);
		assertTrue(Arrays.equals(arr, result));
	}

	@Test
	public void testJavaByteArray() throws Exception {

		final Random r = new Random();
		final Byte[] arr = new Byte[10];
		final byte[] tmp = new byte[10];
		r.nextBytes(tmp);
		for (int i = 0; i < tmp.length; ++i) {
			arr[i] = tmp[i];
		}

		final Byte[] result = (Byte[]) serializeAndDeserialize(arr);
		assertTrue(Arrays.equals(arr, result));
	}

	@Test
	public void testVLQ() throws Exception {
		final DirectNettyBuffer dcBuffer = new DirectNettyBuffer();
		dcBuffer.writeVLQInt(300);
		final int result = dcBuffer.readVLQInt();
		assertTrue(result == 300);
	}

	@Test
	public void testBigArray() throws Exception {

		final Random r = new Random();
		final Integer[] arr = new Integer[300]; // VLQ must be 2
		for (int i = 0; i < arr.length; ++i) {
			arr[i] = r.nextInt();
		}

		final Integer[] result = (Integer[]) serializeAndDeserialize(arr);
		assertTrue(Arrays.equals(arr, result));
	}

}
