
/**
 * @file LruCache.java
 * @date May 23, 2013
 */

package es.bsc.dataclay.util.structs;

import java.util.Calendar;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import es.bsc.dataclay.util.Configuration;

/**
 * Class that implements a simple LRU cache with specified max entries.
 * 
 * @param <A>
 *            Key element class
 * @param <B>
 *            Value element class
 */
public final class LruCacheByDate<A, B> {
	
	/** Map used for this cache. */
	private final Map<A, B> map;
	
	/** Cache threads. */
	private final ScheduledExecutorService execService;

	/**
	 * LruCache constructor
	 * @param theMaxEntries
	 *            Maximum entries in Cache.
	 */
	public LruCacheByDate(final int theMaxEntries) {
		map = Collections.synchronizedMap(
				new CacheLinkedHashMap<A, B>(theMaxEntries, Configuration.Flags.LRU_LOAD_FACTOR.getFloatValue(), 
						true));
		final Runnable task = new Runnable() {
			@Override
			public void run() {
				cleanCache();
			}
		};
		execService = Executors.newSingleThreadScheduledExecutor();
		execService.scheduleAtFixedRate(task, 0, Configuration.Flags.LRU_BYDATE_TIMEUNIT_VALUE.getIntValue(),
				(TimeUnit) Configuration.Flags.LRU_BYDATE_TIMEUNIT.getValue());
	}



	/**
	 * Cleans expired entries
	 */
	@SuppressWarnings("unchecked")
	public void cleanCache() {
		final boolean firstCheckDone = false;
		// We assume that type B of value is a Tuple<X, Date>
		for (A key : this.keySet()) {
			final Tuple<Object, Calendar> curValue;
			if (firstCheckDone) {
				curValue = (Tuple<Object, Calendar>) this.get(key);
			} else {
				final Object aux = this.get(key);
				if (!(aux instanceof Tuple<?, ?>)) {
					return;
				}
				try {
					curValue = (Tuple<Object, Calendar>) aux;
				} catch (ClassCastException ex) {
					return;
				}
			}
			final Calendar today = Calendar.getInstance();
			if (curValue.getSecond().before(today)) {
				this.remove(key);
			}
		}
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
	
	public Set<A> keySet() { 
		return map.keySet();
	}
	
	public int size() { 
		return map.size();
	}
	
	public void putAll(final Map<? extends A, ? extends B> m) { 
		map.putAll(m);
	}
	
	public void clear() { 
		map.clear();
	}
	//CHECKSTYLE:ON
	
	/**
	 * Finish cache threads
	 * @throws InterruptedException if shutdown was interrupted.
	 */
	public void finishCacheThreads() throws InterruptedException { 
		//CHECKSTYLE:OFF
		execService.shutdown();
		execService.awaitTermination(10, TimeUnit.SECONDS);
		//CHECKSTYLE:ON
	}
}
