/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common;

import org.eclipse.jdt.annotation.NonNull;

/**
 * A class for holding a pair of heterogenous items
 * 
 * @author hinton
 * 
 * @param <A>
 *            the type of the first item
 * @param <B>
 *            the type of the second item
 */
public class NonNullPair<A, B> {
	@NonNull
	private A first;

	@NonNull
	private B second;

	public NonNullPair(@NonNull final A first, @NonNull final B second) {
		this.first = first;
		this.second = second;
	}

	public NonNullPair(@NonNull final NonNullPair<A, B> copy) {
		this.first = copy.getFirst();
		this.second = copy.getSecond();
	}

	@NonNull
	public final A getFirst() {
		return first;
	}

	public final void setFirst(@NonNull final A first) {
		this.first = first;
	}

	@NonNull
	public final B getSecond() {
		return second;
	}

	public final void setSecond(@NonNull final B second) {
		this.second = second;
	}

	public final void setBoth(@NonNull final A i, @NonNull final B j) {
		setFirst(i);
		setSecond(j);
	}

	/**
	 * Represents the pair as a string in the form (first, second).
	 */
	@Override
	public final String toString() {
		return "(" + first + ", " + second + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + (first.hashCode());
		result = (prime * result) + (second.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		}

		if (obj instanceof NonNullPair<?, ?>) {
			@SuppressWarnings("rawtypes")
			final NonNullPair other = (NonNullPair) obj;

			return first.equals(other.first) && second.equals(other.second);
		}
		return false;
	}
}
