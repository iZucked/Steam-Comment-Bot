/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common;

import java.util.HashMap;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * A two-way association from one type to another. Internally maintains two {@link HashMap}s for lookup in both directions. Null values are not permitted.
 * 
 * @author hinton
 * 
 * @param <A>
 * @param <B>
 */
public final class Association<A, B> {

	@NonNull
	private final HashMap<A, B> forwards = new HashMap<>();;

	@NonNull
	private final HashMap<B, A> reverse = new HashMap<>();

	/**
	 * Add an association between A and B
	 * 
	 * @param a
	 * @param b
	 */
	public void add(@NonNull final A a, @NonNull final B b) {
		forwards.put(a, b);
		reverse.put(b, a);
	}

	/**
	 * Lookup the association for A. Returns null is none is found.
	 * 
	 * @param a
	 * @return
	 */
	@Nullable
	public B lookup(@Nullable final A a) {
		return forwards.get(a);
	}

	/**
	 * Lookup the association for A. Throws an {@link IllegalArgumentException} if no association is found.
	 * 
	 * @param a
	 * @return
	 */
	@NonNull
	public B lookupNullChecked(@NonNull final A a) {
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
	@Nullable
	public A reverseLookup(@Nullable final B b) {
		return reverse.get(b);
	}

	/**
	 * Lookup the association for A. Throws an {@link IllegalArgumentException} if no association is found.
	 * 
	 * @param a
	 * @return
	 */
	@NonNull
	public A reverseLookupNullChecked(@NonNull final B b) {
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
}
