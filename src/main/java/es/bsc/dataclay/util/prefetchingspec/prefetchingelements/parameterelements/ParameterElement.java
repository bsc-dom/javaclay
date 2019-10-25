
/**
 * @file ParameterAccessElement.java
 * 
 * @date Dec 11, 2015
 */
package es.bsc.dataclay.util.prefetchingspec.prefetchingelements.parameterelements;

import com.ibm.wala.types.TypeReference;

import es.bsc.dataclay.util.prefetchingspec.prefetchingelements.PrefetchingElement;

/**
 * This class represents a parameter access prefetching element.
 * 
 *
 */
public abstract class ParameterElement implements PrefetchingElement {

	/** The value number corresponding to the accessed parameter. **/
	private int paramValueNumber;

	/** The name of the parameter. **/
	private String paramName;

	private TypeReference paramType;

	/**
	 * Create a new parameter access element with the corresponding parameters.
	 * 
	 * @param newParamValueNumber
	 *            the value number of the parameter
	 * @param newParamName
	 *            the name of the parameter
	 */
	protected ParameterElement(final int newParamValueNumber, final String newParamName, final TypeReference newParamType) {
		super();
		this.paramValueNumber = newParamValueNumber;
		this.paramName = newParamName;
		this.paramType = newParamType;
	}

	/**
	 * Get ParameterAccessElement::paramValueNumber.
	 * 
	 * @return paramValueNumber
	 */
	public final int getParamValueNumber() {
		return paramValueNumber;
	}

	/**
	 * Set ParameterAccessElement::paramValueNumber.
	 * 
	 * @param newParamValueNumber
	 *            the paramValueNumber to set
	 */
	public final void setParamValueNumber(final int newParamValueNumber) {
		this.paramValueNumber = newParamValueNumber;
	}

	/**
	 * Get ParameterAccessElement::paramName.
	 * 
	 * @return paramName
	 */
	@Override
	public final String getName() {
		return paramName;
	}

	/**
	 * Set ParameterAccessElement::paramName.
	 * 
	 * @param newParamName
	 *            the paramName to set
	 */
	public final void setParamName(final String newParamName) {
		this.paramName = newParamName;
	}

	public TypeReference getType() {
		return paramType;
	}

	public void setParamType(TypeReference paramType) {
		this.paramType = paramType;
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		// CHECKSTYLE:OFF
		result = prime * result + ((paramName == null) ? 0 : paramName.hashCode());
		result = prime * result + paramValueNumber;
		// CHECKSTYLE:ON
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
		final ParameterElement other = (ParameterElement) obj;
		if (paramName == null) {
			if (other.paramName != null) {
				return false;
			}
		} else if (!paramName.equals(other.paramName)) {
			return false;
		}
		if (paramValueNumber != other.paramValueNumber) {
			return false;
		}
		return true;
	}

}
