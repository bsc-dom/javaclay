
package es.bsc.dataclay.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/** Class used for system.err and out of commands. */
public final class StreamGobbler extends Thread {

	/** Input stream. */
	private final InputStream is;
	/** Type. */
	private final String type;
	/** Print output. */
	private final boolean printOutput;
	/** Output builder. */
	private final StringBuilder builder;

	/**
	 * Constructor
	 * @param theis
	 *            Input stream
	 * @param thetype
	 *            Type
	 * @param theprintOutput
	 *            print output
	 * @param thebuilder Output builder
	 */
	StreamGobbler(final InputStream theis, final String thetype, 
			final boolean theprintOutput, final StringBuilder thebuilder) {
		this.is = theis;
		this.type = thetype;
		this.printOutput = theprintOutput;
		this.setDaemon(true);
		this.builder = thebuilder;
	}

	@Override
	public void run() {
		try {
			final InputStreamReader isr = new InputStreamReader(is);
			final BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				if (printOutput) {
					if (type.equals("ERROR")) {
						System.err.println(line);
					} else if (type.equals("INFO")) {
						System.out.println(line);
					}
				}
				builder.append(line);
				builder.append(System.getProperty("line.separator"));
			}
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
