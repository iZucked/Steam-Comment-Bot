/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.indexedobjects.IIndexMap;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexMap;
import com.mmxlabs.optimiser.common.dcproviders.IOrderedSequenceElementsDataComponentProviderEditor;
import com.mmxlabs.optimiser.core.ISequenceElement;

public final class IndexedOrderedSequenceElementsEditor implements IOrderedSequenceElementsDataComponentProviderEditor {

	private final IIndexMap<@NonNull ISequenceElement, @Nullable ISequenceElement> successors = new ArrayIndexMap<>();
	private final IIndexMap<@NonNull ISequenceElement, @Nullable ISequenceElement> predecessors = new ArrayIndexMap<>();

	@Override
	@Nullable
	public final ISequenceElement getNextElement(final @NonNull ISequenceElement previousElement) {
		return successors.maybeGet(previousElement);
	}

	@Override
	@Nullable
	public final ISequenceElement getPreviousElement(@NonNull final ISequenceElement nextElement) {
		return predecessors.maybeGet(nextElement);
	}

	@Override
	public void setElementOrder(final @NonNull ISequenceElement previousElement, final @NonNull ISequenceElement nextElement) {
		successors.set(previousElement, nextElement);
		predecessors.set(nextElement, previousElement);
	}
}
