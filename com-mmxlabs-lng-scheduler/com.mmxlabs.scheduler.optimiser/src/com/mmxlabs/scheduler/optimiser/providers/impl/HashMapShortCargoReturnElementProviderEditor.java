/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.providers.IShortCargoReturnElementProviderEditor;

/**
 * 
 * 
 */
public class HashMapShortCargoReturnElementProviderEditor implements IShortCargoReturnElementProviderEditor {

	final private HashMap<ILoadOption, ISequenceElement> loadOptionMap = new HashMap<ILoadOption, ISequenceElement>();
	final private HashMap<ISequenceElement, ISequenceElement> loadElementMap = new HashMap<ISequenceElement, ISequenceElement>();

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
