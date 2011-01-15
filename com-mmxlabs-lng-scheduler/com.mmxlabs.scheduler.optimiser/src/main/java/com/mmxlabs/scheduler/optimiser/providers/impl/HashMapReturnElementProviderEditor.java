/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.IReturnElementProviderEditor;

/**
 * Hash map implementation of {@link IReturnElementProviderEditor}
 * @author hinton
 *
 * @param <T>
 */
public class HashMapReturnElementProviderEditor<T> implements
		IReturnElementProviderEditor<T> {

	final private HashMap<IPort, HashMap<IResource, T>> returnElements = 
		new HashMap<IPort, HashMap<IResource, T>>();
	
	private final String name;
	
	public HashMapReturnElementProviderEditor(String name) {
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
	public T getReturnElement(final IResource resource, final IPort port) {
		final HashMap<IResource, T> byResource = returnElements.get(port);
		return byResource == null ? null : byResource.get(resource);
	}

	@Override
	public void setReturnElement(final IResource resource, final IPort port, final T element) {
		HashMap<IResource, T> byResource = returnElements.get(port);
		if (byResource == null) {
			byResource = new HashMap<IResource, T>();
			returnElements.put(port, byResource);
		}
		byResource.put(resource, element);
	}
}
