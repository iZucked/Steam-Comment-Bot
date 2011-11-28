/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mmxlabs.common.indexedobjects.IIndexBits;
import com.mmxlabs.common.indexedobjects.IIndexedObject;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexBits;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProviderEditor;

/**
 * Optional elements editor for indexed elements.
 * 
 * @author hinton
 * 
 */
public class IndexedOptionalElementsEditor<T extends IIndexedObject> implements IOptionalElementsProviderEditor<T> {
	private final String name;
	private final ArrayList<T> optionalList = new ArrayList<T>();
	private final ArrayList<T> requiredList = new ArrayList<T>();
	IIndexBits<T> optionalElements = new ArrayIndexBits<T>();

	public IndexedOptionalElementsEditor(String name) {
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
	public boolean isElementOptional(T element) {
		return optionalElements.isSet(element);
	}


	@Override
	public boolean isElementRequired(T element) {
		return !isElementOptional(element);
	}


	@Override
	public List<T> getOptionalElements() {
		return Collections.unmodifiableList(optionalList);
	}


	@Override
	public List<T> getRequiredElements() {
		return Collections.unmodifiableList(requiredList);
	}

	@Override
	public void setOptional(T element, boolean isOptional) {
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
