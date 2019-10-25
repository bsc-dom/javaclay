
/**
 * @file LruCache.java
 * @date May 23, 2013
 */

package es.bsc.dataclay.util.structs;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import es.bsc.dataclay.util.Configuration;

import java.util.Set;

/**
 * Class that implements a simple LRU cache with specified max entries.
 * 
 * @param <A>
 *            Key element class
 * @param <B>
 *            Value element class
 */
public class LruCache<A, B> {

	
	/** Map used for this cache. */
	private final Map<A, B> map;
	
	/**
	 * LruCache constructor
	 * @param theMaxEntries
	 *            Maximum entries in Cache.
	 */
	public LruCache(final int theMaxEntries) {
		final int capacity = theMaxEntries;
		map = Collections.synchronizedMap(
				new CacheLinkedHashMap<A, B>(capacity, Configuration.Flags.LRU_LOAD_FACTOR.getFloatValue(), 
						true));
	}



	//CHECKSTYLE:OFF
	public B put(final A key, final B value) {
		return map.put(key, value);
	}
	
	public B get(final A key) { 
		return map.get(key);
	}
	
	public B remove(final A key) { 
		return map.remove(key);
	}
	
	public boolean containsKey(final A key) { 
		return map.containsKey(key);
	}
	
	public boolean containsValue(final B value) { 
		return map.containsValue(value);
	}
	
	public Set<Entry<A, B>> entrySet() { 
		return map.entrySet();
	}
	
	public boolean isEmpty() { 
		return map.isEmpty();
	}
	
	public void putAll(final Map<? extends A, ? extends B> m) { 
		map.putAll(m);
	}
	
	public void clear() { 
		map.clear();
	}
	//CHECKSTYLE:ON
	
}
