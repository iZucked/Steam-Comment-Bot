/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.common.dcproviders.IOrderedSequenceElementsDataComponentProviderEditor;

/**
 * Implementation of {@link IOrderedSequenceElementsDataComponentProviderEditor}
 * using {@link HashMap} as the backing implementation.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type.
 */
public final class OrderedSequenceElementsDataComponentProvider<T> implements
		IOrderedSequenceElementsDataComponentProviderEditor<T> {

	private final String name;
	private final Map<T, T> nextElements;
	private final Map<T, T> previousElements;

	public OrderedSequenceElementsDataComponentProvider(final String name) {
		this.name = name;
		this.nextElements = new HashMap<T, T>();
		this.previousElements = new HashMap<T, T>();
	}

	@Override
	public void setElementOrder(final T previousElement, final T nextElement) {
		nextElements.put(previousElement, nextElement);
		previousElements.put(nextElement, previousElement);
	}

	@Override
	public T getNextElement(final T previousElement) {
		if (nextElements.containsKey(previousElement)) {
			return nextElements.get(previousElement);
		}
		return null;
	}

	@Override
	public T getPreviousElement(final T nextElement) {
		if (previousElements.containsKey(nextElement)) {
			return previousElements.get(nextElement);
		}
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		nextElements.clear();
		previousElements.clear();
	}
}
