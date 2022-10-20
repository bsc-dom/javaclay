
package es.bsc.dataclay.metadata;

/**
 * This interface define the methods of the Logic Module that can be executed
 * remotely.
 * 
 */
public interface MetadataServiceAPI {

	/**
	 * Register storage location
	 * 
	 * @param name     Name
	 * @param hostname Hostname
	 * @param port     Port
	 */
	public void autoregisterSL(final String name, final String hostname, final int port);

}
