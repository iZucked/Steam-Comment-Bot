/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.mmxlabs.common.indexedobjects.IIndexBits;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexBits;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProviderEditor;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Optional elements editor for indexed elements.
 * 
 * @author hinton
 * 
 */
@NonNullByDefault
public class IndexedOptionalElementsEditor implements IOptionalElementsProviderEditor {

	private @Nullable ImmutableList<ISequenceElement> optionalImmutableList;
	private final List<ISequenceElement> optionalList = new ArrayList<>();
	private final IIndexBits<ISequenceElement> optionalElements = new ArrayIndexBits<>();

	@Override
	public boolean isElementOptional(final ISequenceElement element) {
		return optionalElements.isSet(element);
	}

	@Override
	public ImmutableList<ISequenceElement> getOptionalElements() {
		ImmutableList<ISequenceElement> l = optionalImmutableList;
		if (l == null) {
			// Only expect to do this once.
			l = optionalImmutableList = ImmutableList.copyOf(optionalList);
		}
		return l;
	}

	@Override
	public void setOptional(final @NonNull ISequenceElement element, final boolean isOptional) {
		// We should not be changing data after the immutable list has been requested
		assert optionalImmutableList == null;

		if (isOptional) {
			optionalElements.set(element);
		} else {
			optionalElements.clear(element);
		}

		while (optionalList.remove(element))
			;

		if (isOptional) {
			optionalList.add(element);
		}
	}
}
