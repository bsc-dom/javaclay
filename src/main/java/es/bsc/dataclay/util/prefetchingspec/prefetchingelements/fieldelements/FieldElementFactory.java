
package es.bsc.dataclay.util.prefetchingspec.prefetchingelements.fieldelements;

import com.ibm.wala.types.TypeReference;

import es.bsc.dataclay.util.prefetchingspec.visitors.MethodAnalyzer;

public class FieldElementFactory {
	
	public static FieldElement createFieldAccessElement(final String fieldName, final String fieldClassName, final TypeReference fieldTypeRef) {
		if (fieldTypeRef.isArrayType() || MethodAnalyzer.isCollectionType(fieldTypeRef)) {
			return new FieldCollectionElement(fieldName, fieldClassName, fieldTypeRef);
		} else {
			return new FieldSingleElement(fieldName, fieldClassName, fieldTypeRef);
		}
	}
}
