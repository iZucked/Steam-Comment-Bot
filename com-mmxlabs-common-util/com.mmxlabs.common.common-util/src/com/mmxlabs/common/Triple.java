/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common;

import org.eclipse.jdt.annotation.NonNull;

/**
 * A class for holding a triple of heterogenous items
 * 
 * @author Tom Hinton, Simon Goodall
 * 
 * 
 * @param <A>
 *            the type of the first item
 * @param <B>
 *            the type of the second item
 * @param <C>
 *            the type of the third item
 * @since 2.0
 */
public class Triple<A, B, C> {
	private A first;
	private B second;
	private C third;

	@SuppressWarnings("null")
	public Triple() {
		this((A) null, (B) null, (C) null);
	}

	public Triple(final A first, final B second, final C third) {
		super();
		this.first = first;
		this.second = second;
		this.third = third;
	}

	public Triple(final @NonNull Triple<A, B, C> copy) {
		this.first = copy.getFirst();
		this.second = copy.getSecond();
		this.third = copy.getThird();
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

	public final C getThird() {
		return third;
	}

	public final void setThird(final C third) {
		this.third = third;
	}

	public final void setAll(final A i, final B j, final C k) {
		setFirst(i);
		setSecond(j);
		setThird(k);
	}

	/**
	 * Represents the pair as a string in the form (first, second).
	 */
	@Override
	public final String toString() {
		return "(" + first + ", " + second + ", " + third + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((first == null) ? 0 : first.hashCode());
		result = (prime * result) + ((second == null) ? 0 : second.hashCode());
		result = (prime * result) + ((third == null) ? 0 : third.hashCode());
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
		final Triple other = (Triple) obj;
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
		if (third == null) {
			if (other.third != null) {
				return false;
			}
		} else if (!third.equals(other.third)) {
			return false;
		}
		return true;
	}
}
