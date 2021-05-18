
package es.bsc.dataclay.dbhandler;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;

// CHECKSTYLE:OFF
public class Utils {

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] result = Hex.encodeHex(bytes);
		return new String(result);
	}

	public static String unhex(String hex) throws DecoderException {
		return new String(Hex.decodeHex(hex));
	}

	/**
	 * Convert Hexadecimal string to byte array
	 * @param hex
	 *            String
	 * @return Bytes
	 */
	public static byte[] hexStringToByteArray(final String hex) throws DecoderException {
		String unhex = unhex(hex);
		return unhex.getBytes(StandardCharsets.UTF_8);
	}

}
// CHECKSTYLE:ON
