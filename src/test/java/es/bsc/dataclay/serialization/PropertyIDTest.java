
package es.bsc.dataclay.serialization;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import es.bsc.dataclay.serialization.buffer.DirectNettyBuffer;
import es.bsc.dataclay.util.ids.PropertyID;

/**
 * Class to test serialization of PropertyID class.
 * 
 * @author jcosta
 * 
 */
public final class PropertyIDTest {
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
	 * Serialize a simple PropertyID. Only mandatory fields.
	 */
	@Test
	public void testSimplePropertyIDSerialization() {
		final PropertyID id = new PropertyID();
		// Serialize...
		id.serialize(nettyBuf, false, null, null, null, null);
		// ...deserialize
		final PropertyID n = new PropertyID();
		n.deserialize(nettyBuf, null, null, null);
		assertEquals("Deserialize a serialized object should return the same object ids", id.getId().equals(n.getId()), true);
	}

	/**
	 * Serialize a simple PropertyID with position.
	 * 
	 */
	@Test
	public void testPropertyIDSerialization() {
		final PropertyID id = new PropertyID();
		// Serialize...
		id.serialize(nettyBuf, false, null, null, null, null);
		// ...deserialize
		final PropertyID n = new PropertyID();
		n.deserialize(nettyBuf, null, null, null);
		assertEquals("Deserialize a serialized object should return the same object ids", id.getId().equals(n.getId()), true);
	}

	/**
	 * Serialize a simple PropertyID a couple times. Only mandatory fields.
	 */
	@Test
	public void testPropertyIDDoubleSerialization() {
		final PropertyID id = new PropertyID();
		// Serialize...
		id.serialize(nettyBuf, false, null, null, null, null);
		// ...deserialize
		final PropertyID n = new PropertyID();
		n.deserialize(nettyBuf, null, null, null);
		assertEquals("Deserialize a serialized object should return the same object ids", id.getId().equals(n.getId()), true);
		// Serialize...
		n.serialize(nettyBuf, false, null, null, null, null);
		final PropertyID n2 = new PropertyID();
		n2.deserialize(nettyBuf, null, null, null);
		assertEquals("Deserialize a serialized object should return the same object ids", id.getId().equals(n2.getId()), true);
	}
}
