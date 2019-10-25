
package es.bsc.dataclay.serialization;

import static org.junit.Assert.assertTrue;

import java.lang.ref.Reference;
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
import es.bsc.dataclay.serialization.java.util.MapWrapper;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.ids.ObjectID;

/** Class to test the serialization of the DataClayJavaArray class. */
public final class DataClayJavaMapTest {

	@SuppressWarnings("unused")
	private Object serializeAndDeserialize(final Map<?, ?> arr) throws Exception {
		final MapWrapper dcColl = new MapWrapper(arr);
		final DirectNettyBuffer dcBuffer = new DirectNettyBuffer();

		final IdentityHashMap<Object, Integer> curSerializedObjs = new IdentityHashMap<>();
		final List<DataClayObject> pendingObjs = new LinkedList<>();
		final ReferenceCounting referenceCounting = new ReferenceCounting();
		dcColl.serialize(dcBuffer, false, null, curSerializedObjs, pendingObjs.listIterator(), referenceCounting);

		final MapWrapper resMap = new MapWrapper();

		final Map<ObjectID, Reference<DataClayObject>> objCache = new HashMap<>();
		final DataClayObjectMetaData metadata = new DataClayObjectMetaData();

		resMap.deserialize(dcBuffer, null, metadata, null);

		return resMap.getJavaObject();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testIntMap() throws Exception {

		final Random r = new Random();
		final Map<Integer, Integer> coll = new HashMap<>();
		for (int i = 0; i < 10; ++i) {
			coll.put(r.nextInt(), r.nextInt());
		}
		final Map<Integer, Integer> result = (HashMap<Integer, Integer>) serializeAndDeserialize(coll);
		assertTrue(result.equals(coll));

	}

}
