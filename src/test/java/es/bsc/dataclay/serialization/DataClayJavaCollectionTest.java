
package es.bsc.dataclay.serialization;

import static org.junit.Assert.assertTrue;

import java.lang.ref.Reference;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.serialization.buffer.DirectNettyBuffer;
import es.bsc.dataclay.serialization.java.util.CollectionWrapper;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.ids.ObjectID;

/** Class to test the serialization of the DataClayJavaArray class. */
public final class DataClayJavaCollectionTest {

	@SuppressWarnings("unused")
	private Object serializeAndDeserialize(final Collection<?> arr) throws Exception {
		final CollectionWrapper dcColl = new CollectionWrapper(arr);
		final DirectNettyBuffer dcBuffer = new DirectNettyBuffer();

		final IdentityHashMap<Object, Integer> curSerializedObjs = new IdentityHashMap<>();
		final List<DataClayObject> pendingObjs = new LinkedList<>();
		final ReferenceCounting referenceCounting = new ReferenceCounting();
		dcColl.serialize(dcBuffer, false, null, curSerializedObjs, pendingObjs.listIterator(), referenceCounting);

		final CollectionWrapper resColl = new CollectionWrapper();

		final Map<ObjectID, Reference<DataClayObject>> objCache = new HashMap<>();
		final DataClayObjectMetaData metadata = new DataClayObjectMetaData();
		final HashMap<Integer, Object> curDeSerializedJavaObjs = new HashMap<>();

		resColl.deserialize(dcBuffer, null, metadata, curDeSerializedJavaObjs);

		return resColl.getJavaObject();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testIntColl() throws Exception {

		final Random r = new Random();
		final Collection<Integer> coll = new LinkedList<>();
		for (int i = 0; i < 10; ++i) {
			coll.add(r.nextInt());
		}
		final LinkedList<Integer> result = (LinkedList<Integer>) serializeAndDeserialize(coll);
		final Iterator<Integer> it = result.iterator();
		for (final Integer orig : coll) {
			final Integer res = it.next();
			assertTrue(res.equals(orig));
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFloatColl() throws Exception {

		final Random r = new Random();
		final Collection<Float> coll = new LinkedList<>();
		for (int i = 0; i < 10; ++i) {
			coll.add(r.nextFloat());
		}
		final LinkedList<Float> result = (LinkedList<Float>) serializeAndDeserialize(coll);
		final Iterator<Float> it = result.iterator();
		for (final Float orig : coll) {
			final Float res = it.next();
			assertTrue(res.equals(orig));
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDoubleColl() throws Exception {

		final Random r = new Random();
		final Collection<Double> coll = new LinkedList<>();
		for (int i = 0; i < 10; ++i) {
			coll.add(r.nextDouble());
		}
		final LinkedList<Double> result = (LinkedList<Double>) serializeAndDeserialize(coll);
		final Iterator<Double> it = result.iterator();
		for (final Double orig : coll) {
			final Double res = it.next();
			assertTrue(res.equals(orig));
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testBooleanColl() throws Exception {
		final Random r = new Random();
		final Collection<Boolean> coll = new LinkedList<>();
		for (int i = 0; i < 10; ++i) {
			coll.add(r.nextBoolean());
		}
		final LinkedList<Boolean> result = (LinkedList<Boolean>) serializeAndDeserialize(coll);
		final Iterator<Boolean> it = result.iterator();
		for (final Boolean orig : coll) {
			final Boolean res = it.next();
			assertTrue(res.equals(orig));
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testShortColl() throws Exception {

		final Random r = new Random();
		final Collection<Short> coll = new LinkedList<>();
		for (int i = 0; i < 10; ++i) {
			coll.add((short) r.nextInt(Short.MAX_VALUE));
		}
		final LinkedList<Short> result = (LinkedList<Short>) serializeAndDeserialize(coll);
		final Iterator<Short> it = result.iterator();
		for (final Short orig : coll) {
			final Short res = it.next();
			assertTrue(res.equals(orig));
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testLongColl() throws Exception {

		final Random r = new Random();
		final Collection<Long> coll = new LinkedList<>();
		for (int i = 0; i < 10; ++i) {
			coll.add(r.nextLong());
		}
		final LinkedList<Long> result = (LinkedList<Long>) serializeAndDeserialize(coll);
		final Iterator<Long> it = result.iterator();
		for (final Long orig : coll) {
			final Long res = it.next();
			assertTrue(res.equals(orig));
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCharColl() throws Exception {

		final String alphabet = "abcdefghijklmnopqrstuvwxyz1234567890.=$&";
		final Random r = new Random();
		final Collection<Character> coll = new LinkedList<>();
		for (int i = 0; i < 10; ++i) {
			coll.add(alphabet.charAt(r.nextInt(alphabet.length())));
		}
		final LinkedList<Character> result = (LinkedList<Character>) serializeAndDeserialize(coll);
		final Iterator<Character> it = result.iterator();
		for (final Character orig : coll) {
			final Character res = it.next();
			assertTrue(res.equals(orig));
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testByteColl() throws Exception {

		final Random r = new Random();
		final Collection<Byte> coll = new LinkedList<>();
		for (int i = 0; i < 10; ++i) {
			final byte[] bs = new byte[1];
			r.nextBytes(bs);
			coll.add(bs[0]);
		}
		final LinkedList<Byte> result = (LinkedList<Byte>) serializeAndDeserialize(coll);
		final Iterator<Byte> it = result.iterator();
		for (final Byte orig : coll) {
			final Byte res = it.next();
			assertTrue(res.equals(orig));
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testBigColl() throws Exception {

		final Random r = new Random();
		final Collection<Integer> coll = new LinkedList<>();
		for (int i = 0; i < 300; ++i) {
			coll.add(r.nextInt());
		}
		final LinkedList<Integer> result = (LinkedList<Integer>) serializeAndDeserialize(coll);
		final Iterator<Integer> it = result.iterator();
		for (final Integer orig : coll) {
			final Integer res = it.next();
			assertTrue(res.equals(orig));
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCollOfColls() throws Exception {

		final Random r = new Random();
		final Collection<Collection<Integer>> coll = new LinkedList<>();
		for (int i = 0; i < 10; ++i) {
			final Collection<Integer> subcoll = new LinkedList<>();
			for (int j = 0; j < 10; ++j) {
				subcoll.add(r.nextInt());
			}
			coll.add(subcoll);
		}
		final LinkedList<Collection<Integer>> result = (LinkedList<Collection<Integer>>) serializeAndDeserialize(coll);
		final Iterator<Collection<Integer>> itColl = result.iterator();
		for (final Collection<Integer> origColl : coll) {
			final Collection<Integer> resColl = itColl.next();
			final Iterator<Integer> it = resColl.iterator();
			for (final Integer orig : origColl) {
				final Integer res = it.next();
				assertTrue(res.equals(orig));
			}
		}
	}

}
