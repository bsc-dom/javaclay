
/**
 * @file AccessChain.java
 * 
 * @date Dec 11, 2015
 */
package es.bsc.dataclay.util.prefetchingspec.accesschains;

import java.util.ArrayList;
import java.util.ListIterator;

import es.bsc.dataclay.util.prefetchingspec.prefetchingelements.PrefetchingElement;

/**
 * This class represents an access chain consisting of a list of prefetching instructions.
 * 
 *
 */
public class AccessChain {

	/** The list of prefetching instructions in the current access chain. **/
	private ArrayList<PrefetchingElement> elements;

	/** A flag indicating whether the current access chain is the return of its enclosing method. **/
	private boolean isReturnAccessChain;

	/**
	 * Creates a new empty access chain.
	 */
	public AccessChain() {
		this(new ArrayList<PrefetchingElement>());
	}

	/**
	 * Creates a new access chain with the list of indicated elements.
	 * 
	 * @param newElements
	 *            the elements to add to the new access chain
	 */
	public AccessChain(final ArrayList<PrefetchingElement> newElements) {
		this.elements = newElements;
		this.isReturnAccessChain = false;
	}

	/**
	 * A Copy constructor that creates a new access chain with the same properties as the indicated access chain.
	 * 
	 * @param chain
	 *            the access chain to copy
	 */
	public AccessChain(final AccessChain chain) {
		this.elements = new ArrayList<PrefetchingElement>(chain.getElements());
		this.isReturnAccessChain = chain.isReturnAccessChain();
	}

	/***************************/
	/**** GETTERS / SETTERS ****/
	/***************************/

	/**
	 * Get AccessChain::elements.
	 * 
	 * @return elements
	 */
	public final ArrayList<PrefetchingElement> getElements() {
		return elements;
	}

	/**
	 * Set AccessChain::elements.
	 * 
	 * @param newElements
	 *            the elements to set
	 */
	public final void setElements(final ArrayList<PrefetchingElement> newElements) {
		this.elements = newElements;
	}

	/**
	 * Get AccessChain::isReturnAccessChain.
	 * 
	 * @return isReturnAccessChain
	 */
	public final boolean isReturnAccessChain() {
		return isReturnAccessChain;
	}

	/**
	 * Set AccessChain::isReturnAccessChain.
	 * 
	 * @param newIsReturnAccessChain
	 *            the isReturnAccessChain to set
	 */
	public final void setReturnAccessChain(final boolean newIsReturnAccessChain) {
		this.isReturnAccessChain = newIsReturnAccessChain;
	}

	@Override
	public final String toString() {
		String output = "[";

		final ListIterator<PrefetchingElement> iterator = elements.listIterator();
		while (iterator.hasNext()) {
			output += iterator.next().toString();
			if (iterator.hasNext()) {
				output += " -> ";
			}
		}
		output += "]";
		if (isReturnAccessChain) {
			output += "(isReturn)";
		}

		return output;
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		if (elements == null) {
			result = prime * result + 0;
		} else {
			for (PrefetchingElement pi : elements) {
				// CHECKSTYLE:OFF
				result = prime * result + ((pi == null) ? 0 : pi.hashCode());
				// CHECKSTYLE:ON
			}
		}
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
		if (!(obj instanceof AccessChain)) {
			return false;
		}
		final AccessChain other = (AccessChain) obj;
		if (elements == null) {
			if (other.elements != null) {
				return false;
			}
		} else if (elements.size() != other.elements.size()) {
			return false;
		} else {
			for (int i = 0; i < elements.size(); i++) {
				if (!elements.get(i).equals(other.elements.get(i))) {
					return false;
				}
			}
		}
		return true;
	}

}
