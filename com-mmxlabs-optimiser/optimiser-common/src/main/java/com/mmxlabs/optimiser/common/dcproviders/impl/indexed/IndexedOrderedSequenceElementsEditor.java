/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
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

	private final String name;

	public IndexedOrderedSequenceElementsEditor(final String name) {
		super();
		this.name = name;
	}

	@Override
	public final ISequenceElement getNextElement(final ISequenceElement previousElement) {
		return successors.maybeGet(previousElement);
	}

	@Override
	public final ISequenceElement getPreviousElement(final ISequenceElement nextElement) {
		return predecessors.maybeGet(nextElement);
	}

	@Override
	public final String getName() {
		return name;
	}

	@Override
	public void dispose() {
		successors.clear();
		predecessors.clear();
	}

	@Override
	public void setElementOrder(final ISequenceElement previousElement, final ISequenceElement nextElement) {
		successors.set(previousElement, nextElement);
		predecessors.set(nextElement, previousElement);
	}
}
