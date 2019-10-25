
/**
 * @file EmptyPrefetchingElement.java
 * 
 * @date Dec 11, 2015
 */
package es.bsc.dataclay.util.prefetchingspec.prefetchingelements;

import com.ibm.wala.types.TypeReference;

/**
 * This class represents an empty prefetching element. It is only used to distinguish between a "null" and an empty prefetching
 * element.
 * 
 *
 */
public class EmptyPrefetchingElement implements PrefetchingElement {

	/**
	 * Create an empty EmptyPrefetchingElement.
	 */
	public EmptyPrefetchingElement() {
		super();
	}
	
	@Override
	public String getName() {
		return "EMPTY_PREFETCHIN_ELEMENT";
	}
	
	@Override
	public TypeReference getType() {
		return null;
	}
	
	@Override
	public final String toString() {
		return "EMPTY PREFETCHING ELEMENT";
	}

}
