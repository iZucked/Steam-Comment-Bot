/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProviderEditor;

public class HashMapStartEndRequirementEditor<T> implements
		IStartEndRequirementProviderEditor<T> {

	protected HashMap<IResource, IStartEndRequirement> startRequirements = new HashMap<IResource, IStartEndRequirement>();
	protected HashMap<IResource, IStartEndRequirement> endRequirements = new HashMap<IResource, IStartEndRequirement>();
	
	protected HashMap<IResource, T> startElements = new HashMap<IResource, T>();
	protected HashMap<IResource, T> endElements = new HashMap<IResource, T>();
	
	private final String name;
	
	public HashMapStartEndRequirementEditor(String name) {
		this.name = name;
	}
	
	@Override
	public IStartEndRequirement getStartRequirement(IResource resource) {
		return startRequirements.get(resource);
	}

	@Override
	public IStartEndRequirement getEndRequirement(IResource resource) {
		return endRequirements.get(resource);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		startRequirements.clear();
		endRequirements.clear();
		
		startElements.clear();
		endElements.clear();
	}

	@Override
	public void setStartEndRequirements(IResource resource,
			IStartEndRequirement startRequirement,
			IStartEndRequirement endRequirement) {
		startRequirements.put(resource, startRequirement);
		endRequirements.put(resource, endRequirement);
	}

	@Override
	public T getEndElement(IResource resource) {
		return endElements.get(resource);
	}

	@Override
	public T getStartElement(IResource resource) {
		return startElements.get(resource);
	}
	
	@Override
	public void setStartEndElements(IResource resource, T startElement, T endElement) {
		startElements.put(resource, startElement);
		endElements.put(resource, endElement);
	}
}
