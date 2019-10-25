
package es.bsc.dataclay.serialization;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import es.bsc.dataclay.serialization.buffer.DirectNettyBuffer;
import es.bsc.dataclay.test.TestingTools;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;

/** Class to test the PasswordCredential class. */
public final class PasswordCredentialTest {
	/** Serialization buffer. */
	private DirectNettyBuffer nettyBuf;
	/** Max array length. */
	private static final int MAX_ARRAY_LEN = 10;

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

	/** Test a simple PasswordCredential serialization. password is NULL */
	@Test(expected = NullPointerException.class)
	public void testSimplePasswordCredentialSerialization() {
		final PasswordCredential orig = new PasswordCredential();
		orig.serialize(nettyBuf, false, null, null, null, null);
	}

	/**
	 * Test a PasswordCredential serialization.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPasswordCredentialSerialization() throws Exception {
		final PasswordCredential orig = new PasswordCredential(TestingTools.generateRandomString(MAX_ARRAY_LEN));
		orig.serialize(nettyBuf, false, null, null, null, null);
		final PasswordCredential nou = new PasswordCredential();
		nou.deserialize(nettyBuf, null, null, null);
		assertTrue("Deserialize a serialized object should return the same passwords ", nou.equals(orig));
	}

	/**
	 * Test a PasswordCredential double serialization.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPasswordCredentialDoubleSerialization() throws Exception {
		final PasswordCredential orig = new PasswordCredential(TestingTools.generateRandomString(MAX_ARRAY_LEN));
		orig.serialize(nettyBuf, false, null, null, null, null);
		final PasswordCredential nou = new PasswordCredential();
		nou.deserialize(nettyBuf, null, null, null);
		nou.serialize(nettyBuf, false, null, null, null, null);
		final PasswordCredential nou2 = new PasswordCredential();
		nou2.deserialize(nettyBuf, null, null, null);
		assertTrue("Deserialize a serialized object should return the same passwords ", nou2.equals(orig));
	}
}
