
/**
 * @file PrefetchingElement.java
 * 
 * @date Dec 11, 2015
 */
package es.bsc.dataclay.util.prefetchingspec.prefetchingelements;

import com.ibm.wala.types.TypeReference;

/**
 * An interface representing any prefetching element within a method.
 * 
 *
 */
public interface PrefetchingElement {

	String getName();
	
	TypeReference getType();
	
	String toString();

}
