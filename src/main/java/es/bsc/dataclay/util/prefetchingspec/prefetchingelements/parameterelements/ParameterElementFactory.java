
package es.bsc.dataclay.util.prefetchingspec.prefetchingelements.parameterelements;

import com.ibm.wala.types.TypeReference;

import es.bsc.dataclay.util.prefetchingspec.visitors.MethodAnalyzer;

public class ParameterElementFactory {

	public static ParameterElement createParameterAccessElement(final String paramName, final int paramValueNumber, final TypeReference paramTypeRef) {
		if (paramTypeRef.isArrayType() || MethodAnalyzer.isCollectionType(paramTypeRef)) {
			return new ParameterCollectionElement(paramValueNumber, paramName, paramTypeRef);
		} else {
			return new ParameterSingleElement(paramValueNumber, paramName, paramTypeRef);
		}
	}

}
