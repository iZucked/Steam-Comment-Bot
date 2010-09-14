package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;

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

	final private HashMap<IPort, T> returnElements = 
		new HashMap<IPort, T>();
	
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
	public T getReturnElement(IPort port) {
		return returnElements.get(port);
	}

	@Override
	public void setReturnElement(final IPort port, final T element) {
		returnElements.put(port, element);
	}
}
