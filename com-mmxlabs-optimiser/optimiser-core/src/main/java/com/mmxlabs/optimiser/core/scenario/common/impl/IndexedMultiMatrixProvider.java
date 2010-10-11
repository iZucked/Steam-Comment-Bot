package com.mmxlabs.optimiser.core.scenario.common.impl;

import java.util.Collection;

import com.mmxlabs.common.indexedobjects.IIndexedObject;

public final class IndexedMultiMatrixProvider<T extends IIndexedObject, 
	U extends Comparable<U>> extends
		HashMapMultiMatrixProvider<T, U> {

	public IndexedMultiMatrixProvider(String name) {
		super(name);
	}

	private final IndexedMatrixEditor<T, U> minima = 
		new IndexedMatrixEditor<T, U>("minimum-values", null);
	public final void cacheMinimumValues(final Collection<T> elements) {
		for (final T x : elements) {
			for (final T y : elements) {
				minima.set(x, y, super.getMinimumValue(x, y));
			}
		}
	}
	@Override
	public U getMinimumValue(T x, T y) {
		return minima.get(x, y);
	}
}
