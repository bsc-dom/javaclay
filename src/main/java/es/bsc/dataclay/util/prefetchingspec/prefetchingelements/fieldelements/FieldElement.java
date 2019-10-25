
/**
 * @file FieldAccessElement.java
 * 
 * @date Dec 11, 2015
 */
package es.bsc.dataclay.util.prefetchingspec.prefetchingelements.fieldelements;

import com.ibm.wala.types.TypeReference;

import es.bsc.dataclay.util.prefetchingspec.prefetchingelements.PrefetchingElement;

/**
 * This class represents a field access prefetching element.
 * 
 *
 */
public abstract class FieldElement implements PrefetchingElement {

	/** The name of the field being accessed. **/
	private String fieldName;

	/**
	 * The name of the class that contains the field being accessed. This name
	 * should contain the FULL PATH to the class.
	 **/
	private String fieldClassName;
	
	/**
	 * Wala TypeReference representing the type of this field
	 */
	private TypeReference fieldType;

	/**
	 * Creates a new field access element with the specified parameters.
	 * 
	 * @param newFieldName
	 *            the name of the field
	 * @param newFieldClassName
	 *            the name of the class containing the field
	 */
	protected FieldElement(final String newFieldName, final String newFieldClassName, final TypeReference newFieldType) {
		super();
		this.fieldName = newFieldName;
		this.fieldClassName = newFieldClassName;
		this.fieldType = newFieldType;
	}

	/**
	 * Get FieldAccessElement::fieldName.
	 * 
	 * @return fieldName
	 */
	@Override
	public final String getName() {
		return fieldName;
	}

	/**
	 * Set FieldAccessElement::fieldName.
	 * 
	 * @param newFieldName
	 *            the field name to set
	 */
	public final void setFieldName(final String newFieldName) {
		this.fieldName = newFieldName;
	}

	/**
	 * Get FieldAccessElement::fieldClassName.
	 * 
	 * @return fieldClassName
	 */
	public final String getFieldClassName() {
		return fieldClassName;
	}

	/**
	 * Set FieldAccessElement::fieldClassName.
	 * 
	 * @param newFieldClassName
	 *            the field class name to set
	 */
	public final void setFieldClassName(final String newFieldClassName) {
		this.fieldClassName = newFieldClassName;
	}
	
	public TypeReference getType() {
		return fieldType;
	}
	
	public void setFieldType(TypeReference fieldType) {
		this.fieldType = fieldType;
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		//CHECKSTYLE:OFF
		result = prime * result + ((fieldClassName == null) ? 0 : fieldClassName.hashCode());
		result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
		//CHECKSTYLE:ON
		return result;
	}

	@Override
	public final boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final FieldElement other = (FieldElement) obj;
		if (fieldClassName == null) {
			if (other.fieldClassName != null) {
				return false;
			}
		} else if (!fieldClassName.equals(other.fieldClassName)) {
			return false;
		}
		if (fieldName == null) {
			if (other.fieldName != null) {
				return false;
			}
		} else if (!fieldName.equals(other.fieldName)) {
			return false;
		}
		return true;
	}

}
