
package es.bsc.dataclay.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import es.bsc.dataclay.exceptions.DataClayRuntimeException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;

/** Jar utilites. */
public final class JarUtils {

	/** Max bytes in buffer. */
	private static final int MAX_BYTES = 1024;

	/**
	 * Utility classes should have a private constructor.
	 */
	private JarUtils() {

	}

	/**
	 * Read Jar file into byte array
	 * @param path
	 *            Path of the Jar file
	 * @return Byte array representation of the Jar file
	 */
	public static byte[] readJar(final String path) {
		byte[] result = null;
		try {
			final FileInputStream fis = new FileInputStream(path);
			// System.out.println(file.exists() + "!!");
			// InputStream in = resource.openStream();
			final ByteArrayOutputStream bos = new ByteArrayOutputStream();
			final byte[] buf = new byte[MAX_BYTES];
			int totalbytes = 0;
			int readNum = fis.read(buf);
			while (readNum != -1) {
				bos.write(buf, 0, readNum); // no doubt here is 0
				// Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
				totalbytes += readNum;
				readNum = fis.read(buf);
			}
			System.out.println("[DATACLAY] Library serialized: " + totalbytes + " bytes");
			result = bos.toByteArray();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataClayRuntimeException(ERRORCODE.ERROR_DEPLOYING_LIB, "Error while reading Jar", true);
		}
		return result;
	}

	/**
	 * Write jar into disk from bytes
	 * @param jarPath
	 *            Path of jar
	 * @param jarBytes
	 *            Bytes of jar
	 */
	public static void writeJar(final String jarPath, final byte[] jarBytes) {
		try {
			final File newFile = new File(jarPath);
			newFile.getParentFile().mkdirs();
			newFile.createNewFile();
			final FileOutputStream fos = new FileOutputStream(newFile);
			fos.write(jarBytes);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataClayRuntimeException(ERRORCODE.ERROR_DEPLOYING_LIB, "Error while writing Jar", true);
		}
	}

	/**
	 * Get all class names in JAR
	 * @param jarPath
	 *            Path to JAR
	 * @return List of class names in JAR
	 * @throws Exception
	 *             if some exception occurs.
	 */
	public static List<String> getAllClassesInJar(final String jarPath) throws Exception {
		final List<String> classNames = new ArrayList<String>();
		ZipInputStream zip = null;
		try {
			zip = new ZipInputStream(new FileInputStream(jarPath));
			for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
				if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
					// This ZipEntry represents a class. Now, what class does it represent?
					final String className = entry.getName().replace('/', '.'); // including ".class"
					classNames.add(className.substring(0, className.length() - ".class".length()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error while listing classes in Jar " + jarPath);
		} finally {
			zip.close();
		}
		return classNames;
	}

}
