
package es.bsc.dataclay.util.prefetchingspec.accesschains;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AccessChainSet implements Set<AccessChain> {
	
	private HashSet<AccessChain> accessChains;
	
	public AccessChainSet() {
		super();
		accessChains = new HashSet<AccessChain>();
	}

	@Override
	public boolean add(AccessChain e) {
		// Check if newAccessChain should be added or not to accessChains
		boolean chainFound = false;
		for (AccessChain chain : accessChains) {
			// If newAccessChain is a sub-chain of or equals to chain, do not add c1
			if (Collections.indexOfSubList(chain.getElements(), e.getElements()) > -1) {
				chainFound = true;
				break;
			// If chain is a sub-chain of newAccessChain, remove chain of the list and replace it with newAccessChain
			} else if (Collections.indexOfSubList(e.getElements(), chain.getElements()) > -1) {
				accessChains.remove(chain);
				break;
			}
		}
		if (!chainFound) {
			accessChains.add(e);
		}
		
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends AccessChain> c) {
		if (c != null) {
			for (AccessChain chain : c) {
				add(chain);
			}
		}
		return true;
	}

	@Override
	public boolean contains(Object o) {
		return accessChains.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return accessChains.containsAll(c);
	}

	@Override
	public boolean remove(Object o) {
		return accessChains.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return accessChains.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return accessChains.retainAll(c);
	}

	@Override
	public Object[] toArray() {
		return accessChains.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return accessChains.toArray(a);
	}

	@Override
	public int size() {
		return accessChains.size();
	}

	@Override
	public boolean isEmpty() {
		return accessChains.isEmpty();
	}

	@Override
	public Iterator<AccessChain> iterator() {
		return accessChains.iterator();
	}

	@Override
	public void clear() {
		accessChains.clear();
	}
	
	@Override
	public String toString() {
		return accessChains.toString();
	}
}
