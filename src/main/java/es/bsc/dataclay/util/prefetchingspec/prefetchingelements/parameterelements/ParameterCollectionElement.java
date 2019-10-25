
package es.bsc.dataclay.util.prefetchingspec.prefetchingelements.parameterelements;

import com.ibm.wala.types.TypeReference;

import es.bsc.dataclay.util.prefetchingspec.accesschains.AccessChainSet;
import es.bsc.dataclay.util.prefetchingspec.prefetchingelements.collectionelements.CollectionPrefetchingElement;

public class ParameterCollectionElement extends ParameterElement implements CollectionPrefetchingElement {

	private AccessChainSet memberAccessChains;

	protected ParameterCollectionElement(final int newParamValueNumber, final String newParamName, final TypeReference newParamType) {
		super(newParamValueNumber, newParamName, newParamType);
		this.memberAccessChains = new AccessChainSet();
	}

	@Override
	public final String toString() {
		return getName()
				+ memberAccessChains.toString();
	}

	@Override
	public AccessChainSet getMemberAccessChains() {
		return memberAccessChains;
	}
}
