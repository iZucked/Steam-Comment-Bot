/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.scenario.common.impl;

import java.util.Collection;

import com.mmxlabs.common.indexedobjects.IIndexedObject;

public final class IndexedMultiMatrixProvider<T extends IIndexedObject, U extends Comparable<U>> extends HashMapMultiMatrixProvider<T, U> {

	private final IndexedMatrixEditor<T, U> minima = new IndexedMatrixEditor<T, U>((U) null);

	private final IndexedMatrixEditor<T, U> maxima = new IndexedMatrixEditor<T, U>((U) null);

	public IndexedMultiMatrixProvider() {

	}

	public final void cacheExtremalValues(final Collection<T> elements) {
		for (final T x : elements) {
			for (final T y : elements) {
				minima.set(x, y, super.getMinimumValue(x, y));
				maxima.set(x, y, super.getMaximumValue(x, y));
			}
		}
	}

	@Override
	public U getMinimumValue(final T x, final T y) {
		return minima.get(x, y);
	}

	@Override
	public U getMaximumValue(final T x, final T y) {
		return maxima.get(x, y);
	}
}
