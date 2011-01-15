/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

import com.mmxlabs.common.indexedobjects.IIndexMap;
import com.mmxlabs.common.indexedobjects.IIndexedObject;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexMap;
import com.mmxlabs.optimiser.common.dcproviders.IOrderedSequenceElementsDataComponentProviderEditor;

public final class IndexedOrderedSequenceElementsEditor<T extends IIndexedObject> implements
		IOrderedSequenceElementsDataComponentProviderEditor<T> {

	private final IIndexMap<T, T> successors = new ArrayIndexMap<T,T>();
	private final IIndexMap<T, T> predecessors = new ArrayIndexMap<T,T>();
	
	private final String name;
	
	public IndexedOrderedSequenceElementsEditor(final String name) {
		super();
		this.name = name;
	}

	@Override
	public final T getNextElement(final T previousElement) {
		return successors.maybeGet(previousElement);
	}

	@Override
	public final T getPreviousElement(final T nextElement) {
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
	public void setElementOrder(final T previousElement, final T nextElement) {
		successors.set(previousElement, nextElement);
		predecessors.set(nextElement, previousElement);
	}
}
