
package es.bsc.dataclay.util.management.classmgr;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.yaml.snakeyaml.Yaml;

import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.yaml.CommonYAML;

/**
 * This class presents the information obtained through static source code analysis.
 * 
 *
 */
public final class PrefetchingInformation {

	/** ID. */
	private UUID id;
	/**
	 * Flag indicating whether the implementation should be monitored at runtime or not (i.e. whether the static prefetching
	 * results are final or not).
	 **/
	private boolean disableDynamicPrefetching;

	/** List of "access chains" containing properties to prefetch. **/
	private List<List<Property>> propertiesToPrefetch;

	/** Flag to determine whether this implementation needs an inject to a prefetching method. **/
	private boolean injectPrefetchingCall;

	/** Namespace of the prefetching class. **/
	private String prefetchingNameSpace;

	/** Name of the prefetching class. **/
	private String prefetchingClassName;

	/** Signature of the prefetching method. **/
	private String prefetchingMethodSignature;

	/** ID of the implementation to call in a prefetching method. NULL until set inside dataClay. */
	public ImplementationID prefetchingImplementationID;

	/** ID of the class to call in a prefetching method. NULL until set inside dataClay. */
	public MetaClassID prefetchingClassID;

	/**
	 * Creates an empty PrefetchingInformation.
	 */
	public PrefetchingInformation() {
		this.propertiesToPrefetch = new ArrayList<>();
		this.disableDynamicPrefetching = false;
		this.injectPrefetchingCall = false;
		this.prefetchingNameSpace = "";
		this.prefetchingClassName = "";
		this.prefetchingMethodSignature = "";
	}

	/**
	 * Get PrefetchingInformation::propertiesToPrefetch.
	 * 
	 * @return propertiesToPrefetch
	 */
	public List<List<Property>> getPropertiesToPrefetch() {
		return propertiesToPrefetch;
	}

	/**
	 * Set PrefetchingInformation::propertiesToPrefetch.
	 * 
	 * @param newPropertiesToPrefetch
	 *            the propertiesToPrefetch to set
	 */
	public void setPropertiesToPrefetch(final List<List<Property>> newPropertiesToPrefetch) {
		if (newPropertiesToPrefetch == null) {
			throw new IllegalArgumentException("propertiesToPrefetch cannot be null");
		}
		this.propertiesToPrefetch = newPropertiesToPrefetch;
	}

	/**
	 * Get PrefetchingInformation::disableDynamicPrefetching.
	 * 
	 * @return disableDynamicPrefetching
	 */
	public boolean getDisableDynamicPrefetching() {
		return disableDynamicPrefetching;
	}

	/**
	 * Set PrefetchingInformation::disableDynamicPrefetching.
	 * 
	 * @param newDisableDynamicPrefetching
	 *            the disableDynamicPrefetching to set
	 */
	public void setDisableDynamicPrefetching(final boolean newDisableDynamicPrefetching) {
		this.disableDynamicPrefetching = newDisableDynamicPrefetching;
	}

	/**
	 * Get PrefetchingInformation::injectPrefetchingCall.
	 * 
	 * @return injectPrefetchingCall
	 */
	public boolean getInjectPrefetchingCall() {
		return injectPrefetchingCall;
	}

	/**
	 * Set PrefetchingInformation::injectPrefetchingCall.
	 * 
	 * @param newInjectPrefetchingCall
	 *            the injectPrefetchingCall to set
	 */
	public void setInjectPrefetchingCall(final boolean newInjectPrefetchingCall) {
		this.injectPrefetchingCall = newInjectPrefetchingCall;
	}

	/**
	 * Get PrefetchingInformation::prefetchingNameSpace.
	 * 
	 * @return prefetchingNameSpace
	 */
	public String getPrefetchingNameSpace() {
		return prefetchingNameSpace;
	}

	/**
	 * Set PrefetchingInformation::prefetchingNameSpace.
	 * 
	 * @param newPrefetchingNameSpace
	 *            the prefetchingNameSpace to set
	 */
	public void setPrefetchingNameSpace(final String newPrefetchingNameSpace) {
		this.prefetchingNameSpace = newPrefetchingNameSpace;
	}

	/**
	 * Get PrefetchingInformation::prefetchingClassName.
	 * 
	 * @return prefetchingClassName
	 */
	public String getPrefetchingClassName() {
		return prefetchingClassName;
	}

	/**
	 * Set PrefetchingInformation::prefetchingClassName.
	 * 
	 * @param newPrefetchingClassName
	 *            the prefetchingClassName to set
	 */
	public void setPrefetchingClassName(final String newPrefetchingClassName) {
		this.prefetchingClassName = newPrefetchingClassName;
	}

	/**
	 * Get PrefetchingInformation::prefetchingMethodName.
	 * 
	 * @return prefetchingMethodName
	 */
	public String getPrefetchingMethodSignature() {
		return prefetchingMethodSignature;
	}

	/**
	 * Set PrefetchingInformation::prefetchingMethodSignature.
	 * 
	 * @param newPrefetchingMethodSignature
	 *            the prefetchingMethodSignature to set
	 */

	public void setPrefetchingMethodSignature(final String newPrefetchingMethodSignature) {
		this.prefetchingMethodSignature = newPrefetchingMethodSignature;
	}

	@Override
	public String toString() {
		final Yaml yaml = CommonYAML.getYamlObject();
		return yaml.dump(this);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.propertiesToPrefetch);

	}

	/**
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * @param newid
	 *            the id to set
	 */
	public void setId(final UUID newid) {
		this.id = newid;
	}

	/**
	 * @return the prefetchingImplementationID
	 */
	public ImplementationID getPrefetchingImplementationID() {
		return prefetchingImplementationID;
	}

	/**
	 * @param theprefetchingImplementationID
	 *            the prefetchingImplementationID to set
	 */
	public void setPrefetchingImplementationID(final ImplementationID theprefetchingImplementationID) {
		this.prefetchingImplementationID = theprefetchingImplementationID;
	}

	/**
	 * @return the prefetchingClassID
	 */
	public MetaClassID getPrefetchingClassID() {
		return prefetchingClassID;
	}

	/**
	 * @param theprefetchingClassID
	 *            the prefetchingClassID to set
	 */
	public void setPrefetchingClassID(final MetaClassID theprefetchingClassID) {
		this.prefetchingClassID = theprefetchingClassID;
	}

}
