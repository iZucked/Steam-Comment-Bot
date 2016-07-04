/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.ga.impl;

import java.util.Objects;
import java.util.TreeSet;

import com.mmxlabs.common.Equality;
import com.mmxlabs.optimiser.ga.Individual;

/**
 * Class to combined an {@link Individual} with it's fitness for use in a {@link TreeSet} ordered by fitness.
 * 
 */
public final class Tuple<I> implements Comparable<Tuple<I>> {
	public final I i;
	public final int idx;
	public final long f;

	public Tuple(final I i, final int idx, final long f) {
		this.i = i;
		this.idx = idx;
		this.f = f;
	}

	@Override
	public final int compareTo(final Tuple<I> o) {
		final long c = f - o.f;
		// Sort on fitness
		if (c < 0) {
			return -1;
		} else if (c > 0) {
			return 1;
		} else {
			// Then sort of original position.
			return idx - o.idx;
		}
	}

	@Override
	public boolean equals(final Object obj) {

		if (obj instanceof Tuple) {
			final Tuple<?> t = (Tuple<?>) obj;
			if (t.f != f) {
				return false;
			}
			if (t.i != i) {
				return false;
			}

			if (!Equality.isEqual(t.i, i)) {
				return false;
			}
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(i, idx, f);
	}
}