/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.providers.IShortCargoReturnElementProviderEditor;

/**
 * 
 * @since 2.0
 * 
 */
public class HashMapShortCargoReturnElementProviderEditor implements IShortCargoReturnElementProviderEditor {

	final private HashMap<ILoadOption, ISequenceElement> loadOptionMap = new HashMap<ILoadOption, ISequenceElement>();
	final private HashMap<ISequenceElement, ISequenceElement> loadElementMap = new HashMap<ISequenceElement, ISequenceElement>();

	private final String name;

	public HashMapShortCargoReturnElementProviderEditor(final String name) {
		super();
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		loadElementMap.clear();
		loadOptionMap.clear();
	}

	@Override
	public ISequenceElement getReturnElement(ILoadOption loadOption) {
		return loadOptionMap.get(loadOption);
	}

	@Override
	public ISequenceElement getReturnElement(ISequenceElement loadElement) {
		return loadElementMap.get(loadElement);
	}

	@Override
	public void setReturnElement(ISequenceElement loadElement, ILoadOption loadOption, ISequenceElement returnElement) {
		loadElementMap.put(loadElement, returnElement);
		loadOptionMap.put(loadOption, returnElement);
	}
}
