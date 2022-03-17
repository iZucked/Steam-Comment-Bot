/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * A two-way association from one type to another. Internally maintains two
 * {@link HashMap}s for lookup in both directions. Null values are not
 * permitted.
 * 
 * @author hinton
 * 
 * @param <A>
 * @param <B>
 */
@NonNullByDefault
public final class Association<A, B> {

	private final HashMap<A, B> forwards = new HashMap<>();

	private final HashMap<B, A> reverse = new HashMap<>();

	/**
	 * Add an association between A and B
	 * 
	 * @param a
	 * @param b
	 */
	public void add(final A a, final B b) {
		forwards.put(a, b);
		reverse.put(b, a);
	}

	/**
	 * Lookup the association for A. Returns null is none is found.
	 * 
	 * @param a
	 * @return
	 */
	public @Nullable B lookup(@Nullable final A a) {
		return forwards.get(a);
	}

	/**
	 * Lookup the association for A. Throws an {@link IllegalArgumentException} if
	 * no association is found.
	 * 
	 * @param a
	 * @return
	 */

	public B lookupNullChecked(final A a) {
		final B b = forwards.get(a);
		if (b == null) {
			throw new IllegalArgumentException(String.format("Assocation not found for %s", a));
		}
		return b;
	}

	/**
	 * Lookup the reverse association for B. Returns null is none is found.
	 * 
	 * @param a
	 * @return
	 */

	public @Nullable A reverseLookup(@Nullable final B b) {
		return reverse.get(b);
	}

	/**
	 * Lookup the association for A. Throws an {@link IllegalArgumentException} if
	 * no association is found.
	 * 
	 * @param a
	 * @return
	 */

	public A reverseLookupNullChecked(final B b) {
		final A a = reverse.get(b);
		if (a == null) {
			throw new IllegalArgumentException(String.format("Assocation not found for %s", b));
		}
		return a;
	}

	/**
	 * Clear any existing associations
	 */
	public void clear() {
		forwards.clear();
		reverse.clear();
	}

	/**
	 * Returns an iterator that wraps the forwards map. Uses wrapper to prevent any
	 * modification to the original map.
	 */
	public Iterator<Pair<A, B>> iterator() {
		return new Iterator<Pair<A, B>>() {
			final Iterator<Entry<A, B>> forwardsIterator = forwards.entrySet().iterator();

			@Override
			public boolean hasNext() {
				return forwardsIterator.hasNext();
			}

			@Override
			public @NonNull Pair<A, B> next() {
				final Entry<A, B> entry = forwardsIterator.next();
				return Pair.of(entry.getKey(), entry.getValue());
			}
		};
	}
}
