/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

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
public class IndexedOptionalElementsEditor implements IOptionalElementsProviderEditor {

	private final List<@NonNull ISequenceElement> optionalList = new ArrayList<>();
	private final List<@NonNull ISequenceElement> requiredList = new ArrayList<>();
	private final List<@NonNull ISequenceElement> softRequiredList = new ArrayList<>();
	private final IIndexBits<ISequenceElement> optionalElements = new ArrayIndexBits<>();

	@Override
	public boolean isElementOptional(final ISequenceElement element) {
		return optionalElements.isSet(element);
	}

	@Override
	public boolean isElementRequired(final ISequenceElement element) {
		return !isElementOptional(element);
	}

	@Override
	public List<@NonNull ISequenceElement> getOptionalElements() {
		return Collections.unmodifiableList(optionalList);
	}

	@Override
	public List<@NonNull ISequenceElement> getRequiredElements() {
		return Collections.unmodifiableList(requiredList);
	}

	@Override
	public List<@NonNull ISequenceElement> getSoftRequiredElements() {
		return Collections.unmodifiableList(softRequiredList);
	}

	@Override
	public void setOptional(final @NonNull ISequenceElement element, final boolean isOptional) {
		if (isOptional) {
			optionalElements.set(element);
		} else {
			optionalElements.clear(element);
		}

		while (optionalList.remove(element)) {
			;
		}
		while (requiredList.remove(element)) {
			;
		}
		while (softRequiredList.remove(element)) {
			;
		}
		if (isOptional) {
			optionalList.add(element);
		} else {
			// requiredList.add(element);
		}
	}

	/**
	 * @since 2.0
	 */
	@Override
	public void setSoftRequired(final ISequenceElement element, boolean isSoftRequired) {
		setOptional(element, isSoftRequired);
		// setOptional will clear the softRequiredList entry if present
		if (isSoftRequired) {
			softRequiredList.add(element);
		}
	}
}
