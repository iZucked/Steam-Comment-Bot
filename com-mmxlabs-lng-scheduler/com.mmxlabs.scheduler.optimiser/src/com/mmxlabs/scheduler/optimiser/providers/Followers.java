/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public final class Followers<Q> implements Iterable<Q> {
	/**
	 * @param followers
	 */
	public Followers(final Collection<Q> followers) {
		backingList.addAll(followers);
		containsSet.addAll(followers);
	}

	private final List<Q> backingList = new ArrayList<>();
	private final Set<Q> containsSet = new HashSet<>();

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

	/**
	 * Returns the underlying {@link List}. This method is intended for use in API's that operate on a List (such as the Java 8 stream API) and should not be used to manipulate the {@link Followers}
	 * instance.
	 * 
	 * @return
	 */
	public List<Q> asList() {
		return backingList;
	}
}
