/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
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
	private final String name;
	private final ArrayList<ISequenceElement> optionalList = new ArrayList<ISequenceElement>();
	private final ArrayList<ISequenceElement> requiredList = new ArrayList<ISequenceElement>();
	IIndexBits<ISequenceElement> optionalElements = new ArrayIndexBits<ISequenceElement>();

	public IndexedOptionalElementsEditor(final String name) {
		super();
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}


	@Override
	public void dispose() {
		optionalElements = null;
		optionalList.clear();
		requiredList.clear();
	}


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

	@Override
	public void setOptional(final ISequenceElement element, final boolean isOptional) {
		if (isOptional) {
			optionalElements.set(element);
		} else {
			optionalElements.clear(element);
		}

		while (optionalList.remove(element))
			;
		while (requiredList.remove(element))
			;
		if (isOptional) {
			optionalList.add(element);
		}
	}
}
