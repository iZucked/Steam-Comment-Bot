/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.IShortCargoReturnElementProviderEditor;

@NonNullByDefault
public class HashMapShortCargoReturnElementProviderEditor implements IShortCargoReturnElementProviderEditor {

	private final Map<Triple<IResource, ISequenceElement, IPort>, ISequenceElement> loadElementMap = new HashMap<>();

	@Override
	public ISequenceElement getReturnElement(IResource resource, ISequenceElement loadElement, IPort port) {
		return loadElementMap.get(Triple.of(resource, loadElement, port));
	}

	@Override
	public void setReturnElement(IResource resource, ISequenceElement loadElement, IPort port, ISequenceElement returnElement) {
		loadElementMap.put(Triple.of(resource, loadElement, port), returnElement);
	}
}
