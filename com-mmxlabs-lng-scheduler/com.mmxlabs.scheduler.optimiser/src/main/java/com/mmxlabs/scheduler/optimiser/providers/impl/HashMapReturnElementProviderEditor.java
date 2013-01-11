/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.IReturnElementProviderEditor;

/**
 * Hash map implementation of {@link IReturnElementProviderEditor}
 * 
 * @author hinton
 * 
 */
public class HashMapReturnElementProviderEditor implements IReturnElementProviderEditor {

	final private HashMap<IPort, Map<IResource, ISequenceElement>> returnElements = new HashMap<IPort, Map<IResource, ISequenceElement>>();

	private final String name;

	public HashMapReturnElementProviderEditor(final String name) {
		super();
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		returnElements.clear();
	}

	@Override
	public ISequenceElement getReturnElement(final IResource resource, final IPort port) {
		final Map<IResource, ISequenceElement> byResource = returnElements.get(port);
		return byResource == null ? null : byResource.get(resource);
	}

	@Override
	public void setReturnElement(final IResource resource, final IPort port, final ISequenceElement element) {
		Map<IResource, ISequenceElement> byResource = returnElements.get(port);
		if (byResource == null) {
			byResource = new HashMap<IResource, ISequenceElement>();
			returnElements.put(port, byResource);
		}
		byResource.put(resource, element);
	}
}
