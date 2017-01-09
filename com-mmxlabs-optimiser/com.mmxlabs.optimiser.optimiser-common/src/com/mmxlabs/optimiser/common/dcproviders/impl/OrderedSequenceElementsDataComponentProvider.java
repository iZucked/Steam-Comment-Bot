/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.common.dcproviders.IOrderedSequenceElementsDataComponentProviderEditor;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Implementation of {@link IOrderedSequenceElementsDataComponentProviderEditor} using {@link HashMap} as the backing implementation.
 * 
 * @author Simon Goodall
 * 
 */
public final class OrderedSequenceElementsDataComponentProvider implements IOrderedSequenceElementsDataComponentProviderEditor {

	private final Map<ISequenceElement, ISequenceElement> nextElements;
	private final Map<ISequenceElement, ISequenceElement> previousElements;

	public OrderedSequenceElementsDataComponentProvider() {
		this.nextElements = new HashMap<ISequenceElement, ISequenceElement>();
		this.previousElements = new HashMap<ISequenceElement, ISequenceElement>();
	}

	@Override
	public void setElementOrder(final ISequenceElement previousElement, final ISequenceElement nextElement) {
		nextElements.put(previousElement, nextElement);
		previousElements.put(nextElement, previousElement);
	}

	@Override
	public ISequenceElement getNextElement(final ISequenceElement previousElement) {
		if (nextElements.containsKey(previousElement)) {
			return nextElements.get(previousElement);
		}
		return null;
	}

	@Override
	public ISequenceElement getPreviousElement(final ISequenceElement nextElement) {
		if (previousElements.containsKey(nextElement)) {
			return previousElements.get(nextElement);
		}
		return null;
	}
}
