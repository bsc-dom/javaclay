
package es.bsc.dataclay.util.prefetchingspec.prefetchingelements.fieldelements;

import com.ibm.wala.types.TypeReference;

import es.bsc.dataclay.util.prefetchingspec.accesschains.AccessChainSet;
import es.bsc.dataclay.util.prefetchingspec.prefetchingelements.collectionelements.CollectionPrefetchingElement;

public class FieldCollectionElement extends FieldElement implements CollectionPrefetchingElement {

	private AccessChainSet memberAccessChains;

	protected FieldCollectionElement(final String newFieldName, final String newFieldClassName, final TypeReference newFieldType) {
		super(newFieldName, newFieldClassName, newFieldType);
		this.memberAccessChains = new AccessChainSet();
	}

	@Override
	public final String toString() {
		return getFieldClassName() + "." + getName()
				+ memberAccessChains.toString();
	}

	@Override
	public AccessChainSet getMemberAccessChains() {
		return memberAccessChains;
	}

}
