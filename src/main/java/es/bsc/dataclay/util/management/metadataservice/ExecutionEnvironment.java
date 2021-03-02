
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

import javax.xml.crypto.Data;

/**
 * This class represents the information of a ExecutionEnvironment.
 */
public final class ExecutionEnvironment extends MgrObject<ExecutionEnvironmentID> implements Backend {

	/** Name of the DataService (shared with the StorageLocation). */
	private String name;
	/** Host name of the Execution environment. */
	private String hostname;
	/** Port of the Execution Environment. */
	private int port;
	/** Language for the Execution Environment. */
	private Langs lang;
	/** ID of dataClay this EE belongs to. */
	private DataClayInstanceID dataClayInstanceID;

	/**
	 * Empty constructor of the ExecutionEnvironment
	 */
	public ExecutionEnvironment() {

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
	 * @param dataClayInstanceID ID of dataClay instance of this exec env
	 */
	public ExecutionEnvironment(final String newhostname, final String newname,
			final int newtcpport, final Langs newlang, final DataClayInstanceID dataClayInstanceID) {
		this.setDataClayID(new ExecutionEnvironmentID());
		this.setHostname(newhostname);
		this.setName(newname);
		this.setPort(newtcpport);
		this.setLang(newlang);
		this.setDataClayInstanceID(dataClayInstanceID);

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
	 * Get the ExecutionEnvironment::dataClayInstanceID
	 *
	 * @return the dataClayInstanceID
	 */
	public DataClayInstanceID getDataClayInstanceID() {
		return this.dataClayInstanceID;
	}

	/**
	 * Set the ExecutionEnvironment::dataClayInstanceID
	 *
	 * @param newdataClayInstanceID
	 *            the dataClayInstanceID to set
	 */
	public void setDataClayInstanceID(final DataClayInstanceID newdataClayInstanceID) {
		this.dataClayInstanceID = newdataClayInstanceID;
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
		return "[EE/" + name + "/" + this.getAddress() + "/dC_ID="+ this.getDataClayInstanceID() + "]";
	}

	@Override
	public boolean equals(final Object t) {
		if (t instanceof ExecutionEnvironment) {
			final ExecutionEnvironment other = (ExecutionEnvironment) t;
			return other.getHostname().equals(this.getHostname())
					&& other.getLang().equals(this.getLang())
					&& other.getName().equals(this.getName())
					&& other.getPort() == this.getPort()
					&& other.getDataClayInstanceID().equals(this.getDataClayInstanceID());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
}
