/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

	private final ArrayList<ISequenceElement> optionalList = new ArrayList<ISequenceElement>();
	private final ArrayList<ISequenceElement> requiredList = new ArrayList<ISequenceElement>();
	private final ArrayList<ISequenceElement> softRequiredList = new ArrayList<ISequenceElement>();
	private final IIndexBits<ISequenceElement> optionalElements = new ArrayIndexBits<ISequenceElement>();

	@Override
	public boolean isElementOptional(final ISequenceElement element) {
		return optionalElements.isSet(element);
	}

	@Override
	public boolean isElementRequired(final ISequenceElement element) {
		return !isElementOptional(element);
	}

	@Override
	public List<ISequenceElement> getOptionalElements() {
		return Collections.unmodifiableList(optionalList);
	}

	@Override
	public List<ISequenceElement> getRequiredElements() {
		return Collections.unmodifiableList(requiredList);
	}

	/**
	 * @since 2.0
	 */
	@Override
	public List<ISequenceElement> getSoftRequiredElements() {
		return Collections.unmodifiableList(softRequiredList);
	}

	@Override
	public void setOptional(final ISequenceElement element, final boolean isOptional) {
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
