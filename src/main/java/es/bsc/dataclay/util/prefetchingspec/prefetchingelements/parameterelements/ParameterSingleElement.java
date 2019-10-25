
package es.bsc.dataclay.util.prefetchingspec.prefetchingelements.parameterelements;

import com.ibm.wala.types.TypeReference;

public class ParameterSingleElement extends ParameterElement {

	protected ParameterSingleElement(final int newParamValueNumber, final String newParamName, final TypeReference newParamType) {
		super(newParamValueNumber, newParamName, newParamType);
	}

	@Override
	public final String toString() {
		return getName();
	}

}
