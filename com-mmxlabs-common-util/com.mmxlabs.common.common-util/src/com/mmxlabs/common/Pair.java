/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common;

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
public class Pair<A, B> {
	private A first;
	private B second;

	@SuppressWarnings("null")
	public Pair() {
		this((A) null, (B) null);
	}

	public Pair(final A first, final B second) {
		super();
		this.first = first;
		this.second = second;
	}

	public Pair(final Pair<A, B> copy) {
		setBoth(copy.getFirst(), copy.getSecond());
	}

	public final A getFirst() {
		return first;
	}

	public final void setFirst(final A first) {
		this.first = first;
	}

	public final B getSecond() {
		return second;
	}

	public final void setSecond(final B second) {
		this.second = second;
	}

	public final void setBoth(final A i, final B j) {
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
		result = (prime * result) + ((first == null) ? 0 : first.hashCode());
		result = (prime * result) + ((second == null) ? 0 : second.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		@SuppressWarnings("rawtypes")
		final Pair other = (Pair) obj;
		if (first == null) {
			if (other.first != null) {
				return false;
			}
		} else if (!first.equals(other.first)) {
			return false;
		}
		if (second == null) {
			if (other.second != null) {
				return false;
			}
		} else if (!second.equals(other.second)) {
			return false;
		}
		return true;
	}
}
