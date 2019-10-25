
package es.bsc.dataclay.serialization;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import es.bsc.dataclay.serialization.buffer.DirectNettyBuffer;
import es.bsc.dataclay.util.ids.ID;
import es.bsc.dataclay.util.ids.ObjectID;

/** Class to test the ID class. */
public final class IDTest {

	/** Serialization buffer. */
	private DirectNettyBuffer nettyBuf;

	/**
	 * Initializes the serialization buffer.
	 */
	@Before
	public void initBuffer() {
		nettyBuf = new DirectNettyBuffer();
	}

	/**
	 * Release serialization buffer.
	 */
	@After
	public void finiBuffer() {
		nettyBuf = null;
	}

	/**
	 * Test a simple Object ID serialization.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSimpleIDSerialization() throws Exception {
		final ID id = new ObjectID();
		id.serialize(nettyBuf, false, null, null, null, null);
		final ID n = new ObjectID();
		n.deserialize(nettyBuf, null, null, null);
		assertTrue("Deserialize a serialized object should return the same object ids", id.equals(n));
	}

}
