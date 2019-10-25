
package es.bsc.dataclay.util.structs;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class represents a LRU cache. 
 * @param <A> Key type
 * @param <B> Value type
 */
public class CacheLinkedHashMap<A, B> extends LinkedHashMap<A, B> {
	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 4662972672772141851L;

	/** Max entries of cache. */
	private final int maxEntries;
	
	/**
	 * Constructor
	 * @param theMaxEntries Max entries
	 * @param loadFactor Load factor
	 * @param order Indicates order
	 */
	public CacheLinkedHashMap(final int theMaxEntries, final float loadFactor, final boolean order) { 
		super(theMaxEntries, loadFactor, order);
		this.maxEntries = theMaxEntries;
	}
	
	/**
	 * Returns <tt>true</tt> if this <code>LruCache</code> has more entries than the maximum specified when it was created.This
	 * method <em>does not</em> modify the underlying <code>Map</code>; it relies on the implementation of
	 * <code>LinkedHashMap</code> to do that, but that behavior is documented in the JavaDoc for <code>LinkedHashMap</code>.
	 * 
	 * @param eldest
	 *            the <code>Entry</code> in question; this implementation doesn't care what it is, since the implementation is
	 *            only dependent on the size of the cache
	 * @return <tt>true</tt> if the oldest
	 * @see java.util.LinkedHashMap#removeEldestEntry(Map.Entry)
	 */
	@Override
	protected final boolean removeEldestEntry(final Map.Entry<A, B> eldest) {
		return this.size() > maxEntries;
	}
}
