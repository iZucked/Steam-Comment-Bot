/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

final public class Followers<Q> implements Iterable<Q> {
	/**
	 * @param followers
	 */
	public Followers(final Collection<Q> followers) {
		backingList.addAll(followers);
		containsSet.addAll(followers);
	}

	private final List<Q> backingList = new ArrayList<Q>();
	private final Set<Q> containsSet = new HashSet<Q>();

	/**
	 * @return
	 */
	public int size() {
		return backingList.size();
	}

	/**
	 * @param nextInt
	 * @return
	 */
	public Q get(final int nextInt) {
		return backingList.get(nextInt);
	}

	/**
	 * @param firstElementInSegment
	 * @return
	 */
	public boolean contains(final Q firstElementInSegment) {
		return containsSet.contains(firstElementInSegment);
	}

	@Override
	public Iterator<Q> iterator() {
		return backingList.iterator();
	}
}
