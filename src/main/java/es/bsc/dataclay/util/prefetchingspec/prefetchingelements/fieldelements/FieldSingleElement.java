
package es.bsc.dataclay.util.prefetchingspec.prefetchingelements.fieldelements;

import com.ibm.wala.types.TypeReference;

public class FieldSingleElement extends FieldElement {

	protected FieldSingleElement(final String newFieldName, final String newFieldClassName, final TypeReference newFieldType) {
		super(newFieldName, newFieldClassName, newFieldType);
	}

	@Override
	public final String toString() {
		return getFieldClassName() + "." + getName();
	}
}
