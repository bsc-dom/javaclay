
/**
 * @file ExecutionEnvironment.java
 * @date Nov 26, 2015
 */
package es.bsc.dataclay.util.management.metadataservice;

import es.bsc.dataclay.api.Backend;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.util.MgrObject;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;

/**
 * This class represents the information of an external ExecutionEnvironment.
 */
public final class ExternalExecutionEnvironment extends MgrObject<ExecutionEnvironmentID> implements Backend {

	/** ID of external dataClay this EE belongs to. */
	private DataClayInstanceID dataClayInstanceID;
	/** Name of the DataService (shared with the StorageLocation). */
	private String name;
	/** Host name of the Execution environment. */
	private String hostname;
	/** Port of the Execution Environment. */
	private int port;
	/** Language for the Execution Environment. */
	private Langs lang;

	/**
	 * Empty constructor of the ExecutionEnvironment
	 */
	public ExternalExecutionEnvironment() {

	}

	/**
	 * ExecutionEnvironment specification constructor
	 *
	 * @param newhostname
	 *            Host name of the Execution Environment
	 * @param newname
	 *            Name of the Execution Environment
	 * @param newtcpport
	 *            TCP Port of the Execution Environment
	 * @param newlang
	 *            Language for this Execution Environment
	 */
	public ExternalExecutionEnvironment(final DataClayInstanceID thedataClayInstanceID,
										final String newhostname, final String newname,
                                        final int newtcpport, final Langs newlang) {
		this.setDataClayID(new ExecutionEnvironmentID());
		this.setDataClayInstanceID(thedataClayInstanceID);
		this.setHostname(newhostname);
		this.setName(newname);
		this.setPort(newtcpport);
		this.setLang(newlang);
	}

	/**
	 * Get the ExternalExecutionEnvironment::dataClayInstanceID
	 *
	 * @return the dataClay instance ID this EE belongs to
	 */
	public DataClayInstanceID getDataClayInstanceID() {
		return this.dataClayInstanceID;
	}

	/**
	 * Set the ExternalExecutionEnvironment::dataClayInstanceID
	 *
	 * @param thedataClayInstanceID
	 *            the dataClayInstanceID to set
	 */
	public void setDataClayInstanceID(final DataClayInstanceID thedataClayInstanceID) {
		this.dataClayInstanceID = thedataClayInstanceID;
	}


	/**
	 * Get the ExecutionEnvironment::hostname
	 * 
	 * @return the hostname
	 */
	@Override
	public String getHostname() {
		return hostname;
	}

	/**
	 * Set the ExecutionEnvironment::hostname
	 * 
	 * @param newhostname
	 *            the hostname to set
	 */
	@Override
	public void setHostname(final String newhostname) {
		this.hostname = newhostname;
	}

	/**
	 * Get the ExecutionEnvironment::name
	 * 
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Set the ExecutionEnvironment::name
	 * 
	 * @param newname
	 *            the name to set
	 */
	@Override
	public void setName(final String newname) {
		this.name = newname;
	}

	/**
	 * Get the ExecutionEnvironment::storageTCPPort
	 * 
	 * @return the port
	 */

	@Override
	public int getPort() {
		return port;
	}

	/**
	 * Set the ExecutionEnvironment::port
	 * 
	 * @param newport
	 *            the port to set
	 */
	@Override
	public void setPort(final int newport) {
		this.port = newport;
	}

	/**
	 * Set the ExecutionEnvironment::storageTCPPort
	 * 
	 * @return the language of this Execution Environment
	 */
	@Override
	public Langs getLang() {
		return lang;
	}

	/**
	 * Set the ExecutionEnvironment::lang
	 * 
	 * @param newlang
	 *            the Language for this Execution Environment
	 */
	@Override
	public void setLang(final Langs newlang) {
		this.lang = newlang;
	}

	/**
	 * Get addrees of this EE.
	 * 
	 * @return Address.
	 */
	public String getAddress() {
		return this.hostname + ":" + this.port;
	}

	@Override
	public String toString() {
		return "[EE/" + name + "/" + this.getAddress() + "]";
	}

	/**
	 * This operation allows to compare this object with other object.
	 * 
	 * @param t
	 *            Object to compare
	 * @return If the object is the same, returns TRUE. FALSE, otherwise.
	 */
	@Override
	public boolean equals(final Object t) {
		if (t instanceof ExternalExecutionEnvironment) {
			final ExternalExecutionEnvironment other = (ExternalExecutionEnvironment) t;
			return other.getHostname().equals(this.getHostname())
					&& other.getDataClayInstanceID().equals(this.getDataClayInstanceID())
					&& other.getLang().equals(this.getLang())
					&& other.getName().equals(this.getName())
					&& other.getPort() == this.getPort();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
}
