/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

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

	@Override
	public ISequenceElement getReturnElement(final @NonNull IResource resource, final @NonNull IPort port) {
		final Map<IResource, ISequenceElement> byResource = returnElements.get(port);
		return byResource == null ? null : byResource.get(resource);
	}

	@Override
	public void setReturnElement(final @NonNull IResource resource, final @NonNull IPort port, final @NonNull ISequenceElement element) {
		Map<IResource, ISequenceElement> byResource = returnElements.get(port);
		if (byResource == null) {
			byResource = new HashMap<IResource, ISequenceElement>();
			returnElements.put(port, byResource);
		}
		byResource.put(resource, element);
	}
}
