
package es.bsc.dataclay.util;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import es.bsc.dataclay.util.structs.LruCache;

public final class LruCacheTest {
	
	private enum Items {
		ITEM1(1, "item1"),
		ITEM2(2, "item2"),
		ITEM3(3, "item3"),
		ITEM4(4, "item4"),
		ITEM5(5, "item4");
		
		private int id;
		private String tag;
		Items(final int newid, final String newtag) {
			this.setId(newid);
			this.setTag(newtag);
		}
		/**
		* @brief Get the Items::id
		* @return the id
		*/
		public int getId() {
			return id;
		}
		/**
		* @brief Set the Items::id
		* @param newid the id to set
		*/
		public void setId(final int newid) {
			this.id = newid;
		}
		/**
		* @brief Get the Items::tag
		* @return the tag
		*/
		public String getTag() {
			return tag;
		}
		/**
		* @brief Set the Items::tag
		* @param newtag the tag to set
		*/
		public void setTag(final String newtag) {
			if (newtag == null) { 
				throw new IllegalArgumentException("tag cannot be null");
			}
			this.tag = newtag;
		}
	}
	
	@Test
	public void lruCacheTest() {
		//Create a new LRU cache of 3 elements MAX
		final int maxCacheSize = 3;
		LruCache<Integer, String> myCache = new LruCache<Integer, String>(maxCacheSize);
		myCache.put(Items.ITEM1.getId(), Items.ITEM1.getTag());
		myCache.put(Items.ITEM2.getId(), Items.ITEM2.getTag());
		myCache.put(Items.ITEM3.getId(), Items.ITEM3.getTag());
		assertTrue(myCache.containsKey(Items.ITEM1.getId()));
		assertTrue(myCache.containsKey(Items.ITEM2.getId()));
		assertTrue(myCache.containsKey(Items.ITEM3.getId()));
		
		//As value 1 is the eldest, it is removed
		myCache.put(Items.ITEM4.getId(), Items.ITEM4.getTag());
		assertFalse(myCache.containsKey(Items.ITEM1.getId()));
		assertTrue(myCache.containsKey(Items.ITEM2.getId()));
		assertTrue(myCache.containsKey(Items.ITEM3.getId()));
		assertTrue(myCache.containsKey(Items.ITEM4.getId()));
		
		
		//Access value 2
		myCache.get(2);
		myCache.put(Items.ITEM5.getId(), Items.ITEM5.getTag());

		//Although value 2 was insted before value 3, since we perform a get operation, value 3
		//becomes elder than value 2
		assertFalse(myCache.containsKey(Items.ITEM1.getId()));
		assertTrue(myCache.containsKey(Items.ITEM2.getId()));
		assertFalse(myCache.containsKey(Items.ITEM3.getId()));
		assertTrue(myCache.containsKey(Items.ITEM4.getId()));
		assertTrue(myCache.containsKey(Items.ITEM5.getId()));
	}
	
}
