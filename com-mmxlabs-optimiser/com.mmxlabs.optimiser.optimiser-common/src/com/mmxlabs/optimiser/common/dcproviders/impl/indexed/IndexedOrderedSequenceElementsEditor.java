/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

import com.mmxlabs.common.indexedobjects.IIndexMap;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexMap;
import com.mmxlabs.optimiser.common.dcproviders.IOrderedSequenceElementsDataComponentProviderEditor;
import com.mmxlabs.optimiser.core.ISequenceElement;

public final class IndexedOrderedSequenceElementsEditor implements IOrderedSequenceElementsDataComponentProviderEditor {

	private final IIndexMap<ISequenceElement, ISequenceElement> successors = new ArrayIndexMap<ISequenceElement, ISequenceElement>();
	private final IIndexMap<ISequenceElement, ISequenceElement> predecessors = new ArrayIndexMap<ISequenceElement, ISequenceElement>();

	@Override
	public final ISequenceElement getNextElement(final ISequenceElement previousElement) {
		return successors.maybeGet(previousElement);
	}

	@Override
	public final ISequenceElement getPreviousElement(final ISequenceElement nextElement) {
		return predecessors.maybeGet(nextElement);
	}

	@Override
	public void setElementOrder(final ISequenceElement previousElement, final ISequenceElement nextElement) {
		successors.set(previousElement, nextElement);
		predecessors.set(nextElement, previousElement);
	}
}
