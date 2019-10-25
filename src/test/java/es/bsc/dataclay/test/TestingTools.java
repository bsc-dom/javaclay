
package es.bsc.dataclay.test;

import java.util.Random;

/** Container class for generic methods used for testing. */
public final class TestingTools {
	/** Random generator. */
	private static final Random RANDOM = new Random();
	
	/** Alphabet. */
	public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

	/** Default private constructor for utility class.*/
	private TestingTools() {
	
	}
	/**
	 * @brief Generate a random string
	 * @param stringLen String length
	 * @return A random string
	 */
	public static String generateRandomString(final int stringLen) {
		StringBuffer buffer = new StringBuffer();
		int charactersLength = ALPHABET.length();
		for (int i = 0; i < stringLen; i++) {
			buffer.append(ALPHABET.charAt(RANDOM.nextInt(charactersLength)));
		}
		return buffer.toString();
	}
	
	/**
	 * @brief Generate a positive integer
	 * @return A positive integer
	 */
	public static int generateRandomPositiveInteger() {
		int res = RANDOM.nextInt();
		if (res < 0) { res = -res; }
		return res;
	}
	
}
